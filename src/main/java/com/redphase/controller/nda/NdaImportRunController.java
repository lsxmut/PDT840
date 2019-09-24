package com.redphase.controller.nda;

import com.Application;
import com.redphase.framework.util.ValidatorUtil;
import com.redphase.service.nda.NdaFileTask;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

@FXMLController
@Slf4j
public class NdaImportRunController extends NdaBaseController implements Initializable {
    private static final SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String[] deviceTypeArr = new String[]{"开关柜", "变压器", "组合电器", "电缆"};
    @FXML
    private ListView successList;
    @FXML
    private ListView errorList;
    @FXML
    private TextField importPath;
    @FXML
    private Button importRunButton;
    @FXML
    private ImageView progressIcon;
    @FXML
    private Label progressText;
    @FXML
    private ProgressBar progressLine;
    @FXML
    private Label progressValue;
    @FXML
    private BorderPane dataBorderPane, errorPane;
    private String importPathText;
    private AtomicInteger fileCount = new AtomicInteger(0);//待解析文件总数

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize...");
        try {
            if (dataBorderPane != null) {
                dataBorderPane.prefWidthProperty().bind(Application.getStage().widthProperty().subtract(395));
                dataBorderPane.prefHeightProperty().bind(Application.getStage().heightProperty().subtract(100));
                importPath.prefWidthProperty().bind(dataBorderPane.prefWidthProperty().subtract(130));
                successList.prefWidthProperty().bind(dataBorderPane.prefWidthProperty().divide(2.0).subtract(15));
                errorPane.prefWidthProperty().bind(successList.prefWidthProperty());
                progressLine.prefWidthProperty().bind(dataBorderPane.prefWidthProperty().subtract(30));
                progressValue.prefWidthProperty().bind(progressLine.prefWidthProperty());
            }
            //--最近导入地址!..
            importPathText = (String) ehcacheUtil.getCache("nda_path_import");
            if (ValidatorUtil.notEmpty(importPathText)) {
                importPath.setText(importPathText);
            }
            progressIcon.setImage(new Image("/com/redphase/images/ic_cloud.png"));
            progressText.setText("准备就绪");
            progressLine.setProgress(0 / fileCount.doubleValue());
            progressValue.setText(0 + "/" + fileCount);
        } catch (Exception e) {
            log.error("获取数据存储地址异常!", e);
            ialert.error("获取数据存储地址异常!" + e.getMessage());
        }
    }

    private boolean isImportPath(String testTechnology) {
        return importPathText.indexOf(File.separator + testTechnology + File.separator) != -1 || importPathText.indexOf("_" + testTechnology + "_") != -1;
    }

    public void copyDir(String oldPath){
        File file = new File(oldPath);
        String[] filePath = file.list();
        for (int i = 0; i < filePath.length; i++) {
            if ((new File(oldPath + file.separator + filePath[i])).isDirectory()) {
                copyDir(oldPath + file.separator + filePath[i]);
            }
            if (new File(oldPath + file.separator + filePath[i]).isFile()) {
                copyFile(oldPath + file.separator + filePath[i]);
            }
        }
    }
    public void copyFile(String oldPath){
        File oldFile = new File(oldPath);
        if (!("jpg".equalsIgnoreCase(getFileExt(oldFile.getName())) || "png".equalsIgnoreCase(getFileExt(oldFile.getName())) || "pcm".equalsIgnoreCase(getFileExt(oldFile.getName())) || "wav".equalsIgnoreCase(getFileExt(oldFile.getName())))) {
            fileCount.getAndAdd(1);
        }
    }

    @FXML
    public void analyzeFile() {
        importPathText = importPath.getText();
        if (ValidatorUtil.isEmpty(importPathText)) {
            ialert.error("请选择数据导入路径!");
            return;
        }
        boolean isImportSwitchDervice = false;
        for (String deviceType : deviceTypeArr) {
            if (importPathText.indexOf(deviceType) != -1) {
                isImportSwitchDervice = true;
            }
        }
        if (!isImportSwitchDervice) {
            ialert.error("请选择检测技术目录!");
            return;
        }
        //判断导入数据文件夹是否正确
        boolean isImportPathErrorFlag = true;//文件夹错误标记
        // 出现次数
        int num = 0;
        // 循环遍历每个字符，判断是否是字符 a ，如果是，累加次数
        for (int i = 0; i < importPathText.length(); i++) {
            // 获取每个字符，判断是否是字符a
            if (importPathText.charAt(i) == '_') {
                // 累加统计次数
                num++;
            }
        }
        if (num == 3 && (isImportPath("TEV")
                || isImportPath("AA")
                || isImportPath("AE")
                || isImportPath("HFCT")
                || isImportPath("UHF")
        )) {
            isImportPathErrorFlag = false;
        }
        if (isImportPathErrorFlag) {
            ialert.error("检测原始数据文件目录结构不符!");
            Label errorLab = new Label(sdfTime.format(new Date()) + "    " + "导入失败...检测原始数据文件目录结构不符!");
            errorLab.setStyle("-fx-text-fill:red;");
            errorList.getItems().add(errorLab);
            return;
        }

        ehcacheUtil.setCache("nda_path_import", importPathText);
        log.debug("导入路径==>{}", importPathText);

        Platform.runLater(() -> {
            successList.getItems().add(sdfTime.format(new Date()) + "    " + "导入开始...");
        });
        fileCount = new AtomicInteger(0);
        copyDir(importPathText);
        successList.getItems().clear();
        errorList.getItems().clear();
        importRunButton.setDisable(true);
        NdaFileTask task = new NdaFileTask(new Hashtable() {{
            put("ndaController", ndaController);
            put("ialert", ialert);
            put("successList", successList);
            put("errorList", errorList);
            put("importPathText", importPathText);
            put("accountService", accountService);
            put("accountSiteInfoService", accountSiteInfoService);
            put("dataDeviceService", dataDeviceService);
            put("dataDeviceSiteService", dataDeviceSiteService);
            put("dataAnalyzeService", dataAnalyzeService);
            put("dataHjService", dataHjService);
            put("dataLcService", dataLcService);
            put("dataTevTjService", dataTevTjService);
            put("dataTevTjlbService", dataTevTjlbService);
            put("dataTevTjzsService", dataTevTjzsService);
            put("dataTevXjService", dataTevXjService);
            put("dataTevXjlhService", dataTevXjlhService);
            put("dataTevXjzsService", dataTevXjzsService);
            put("dataAaBxService", dataAaBxService);
            put("dataAaFxService", dataAaFxService);
            put("dataAaTjService", dataAaTjService);
            put("dataAaTjlbService", dataAaTjlbService);
            put("dataAaTjzsService", dataAaTjzsService);
            put("dataAaXjService", dataAaXjService);
            put("dataAaXjzsService", dataAaXjzsService);
            put("dataAeBxService", dataAeBxService);
            put("dataAeFxService", dataAeFxService);
            put("dataAeTjService", dataAeTjService);
            put("dataAeTjlbService", dataAeTjlbService);
            put("dataAeTjzsService", dataAeTjzsService);
            put("dataAeXjService", dataAeXjService);
            put("dataAeXjzsService", dataAeXjzsService);
            put("dataHfctTjService", dataHfctTjService);
            put("dataHfctTjlbService", dataHfctTjlbService);
            put("dataHfctTjzsService", dataHfctTjzsService);
            put("dataHfctXjService", dataHfctXjService);
            put("dataHfctXjzsService", dataHfctXjzsService);
            put("dataUhfTj1Service", dataUhfTj1Service);
            put("dataUhfTj2Service", dataUhfTj2Service);
            put("dataUhfTjlb1Service", dataUhfTjlb1Service);
            put("dataUhfTjlb2Service", dataUhfTjlb2Service);
            put("dataUhfTjzs1Service", dataUhfTjzs1Service);
            put("dataUhfTjzs2Service", dataUhfTjzs2Service);
            put("fileCount", fileCount);
            put("progressIcon", progressIcon);
            put("progressText", progressText);
            put("progressLine", progressLine);
            put("progressValue", progressValue);
        }});
        task.runningProperty().addListener((ov, wasRunning, isRunning) -> {
            if (!isRunning) {
                importRunButton.setDisable(false);
            }
        });
        final Thread thread = new Thread(task, "NdaFileTask");
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    public void onImportPath() {
        String path = directoryChooser("请选择数据导入路径", importPathText);
        if (ValidatorUtil.notEmpty(path)) {
            importPath.setText(path);
            importPathText = path;
        }
    }

    @Override
    public void dispose() {

    }

}