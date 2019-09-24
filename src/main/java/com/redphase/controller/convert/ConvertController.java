package com.redphase.controller.convert;

import com.Application;
import com.redphase.api.atlas.IDataAnalyzeService;
import com.redphase.api.atlas.IDataDeviceService;
import com.redphase.controller.atlas.AtlasBaseController;
import com.redphase.dto.TreeItemDto;
import com.redphase.dto.ZTreeNodeDto;
import com.redphase.dto.account.AccountDto;
import com.redphase.dto.atlas.DataAnalyzeDto;
import com.redphase.dto.SkillItemDto;
import com.redphase.framework.util.CommonConstant;
import com.redphase.framework.util.FileUtil;
import com.redphase.framework.util.ValidatorUtil;
import com.redphase.service.convert.ConvertFileTask;
import com.redphase.view.DataTableCell;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@FXMLController
@Slf4j
public class ConvertController extends AtlasBaseController implements Initializable {
    private static final SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @FXML
    @Getter
    private TreeView accountTree;
    @FXML
    @Getter
    private Button refreshButton;
    @FXML
    TextField electricBureauText;//请输入电业局名称
    @FXML
    TextField substationText;//请输入变电站名称
    @FXML
    ChoiceBox deviceTypeChoiceBox;//
    @FXML
    ChoiceBox voltageLevelChoiceBox;//
    @FXML
    TextField deviceNameText;//设备名称
    @FXML
    ChoiceBox testTechnologyChoiceBox;//
    @FXML
    DatePicker produceDateDatePicker;//点击输入出厂日期
    @FXML
    DatePicker useDateDatePicker;//点击输入投运日期

    @FXML
    Button searchButton;
    @FXML
    BorderPane dataBorderPane;
    @FXML
    CheckBox allCheck;
    @FXML
    TableView fileTable;
    @FXML
    TableColumn idTd;
    @FXML
    TableColumn typeTd;
    @FXML
    TableColumn nameTd;
    @FXML
    Tab tab1, tab2;
    @FXML
    ChoiceBox areaChoiceBox;
    @FXML
    TextField outFilePathText;
    @FXML
    Button filePathButton;
    @FXML
    Button runButton;
    @FXML
    Label progressText;
    @FXML
    Label progressValue;
    @FXML
    Label progressLine;
    @FXML
    ListView logsList;

    @Autowired
    IDataDeviceService dataDeviceService;
    @Autowired
    IDataAnalyzeService dataAnalyzeService;

    Map<String, CheckBox> dataCheckMap = new HashMap<>();
    Set<String> dataPathSet = new HashSet<>();
    Set<DataAnalyzeDto> dataAnalyzeSet = new HashSet<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize...");
        hideNodeView();
        closeSet.setOnMouseClicked(event -> hideNodeView());
        loadSearchParamData();
        Platform.runLater(() -> {
            treeSet.clear();
            loadAccTree();
        });
        areaChoiceBox.setItems(FXCollections.observableArrayList("江苏"));
        areaChoiceBox.getSelectionModel().select(0);
        mlayer.prefWidthProperty().bind(Application.getStage().widthProperty());
        mlayer.prefHeightProperty().bind(Application.getStage().heightProperty());
        mVBox.prefWidthProperty().bind(mlayer.prefWidthProperty().subtract(200));
        mVBox.prefHeightProperty().bind(mlayer.prefHeightProperty().subtract(150));
        mHBox.prefWidthProperty().bind(mlayer.prefWidthProperty());
        mHBox.prefHeightProperty().bind(mlayer.prefHeightProperty());
        accountTree.prefHeightProperty().bind(Application.getStage().heightProperty().subtract(170));
        dataBorderPane.prefWidthProperty().bind(Application.getStage().widthProperty().subtract(370));
        dataBorderPane.prefHeightProperty().bind(Application.getStage().heightProperty().subtract(85));
        accountTree.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Node node = event.getPickResult().getIntersectedNode();
            if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
                TreeItem currentTreeItem = ((TreeItem) accountTree.getSelectionModel().getSelectedItem());
                Object object = currentTreeItem.getValue();
                if (currentTreeItem.getChildren().size() > 0) {
                    currentTreeItem.setExpanded(!currentTreeItem.isExpanded());
                    if (object instanceof TreeItemDto) {
                        TreeItemDto treeItemDto = (TreeItemDto) object;
                        Object valObj = treeItemDto.getValue();
                        if (valObj instanceof ZTreeNodeDto) {
                            ZTreeNodeDto zTreeNodeDto = (ZTreeNodeDto) valObj;
                            if (currentTreeItem.isExpanded()) {
                                treeSet.add("" + zTreeNodeDto.getId());
                            } else {
                                treeSet.remove("" + zTreeNodeDto.getId());
                            }
                        } else {
                            if (currentTreeItem.isExpanded()) {
                                treeSet.add("" + treeItemDto.getValue());
                            } else {
                                treeSet.remove("" + treeItemDto.getValue());
                            }
                        }
                    } else if (object instanceof SkillItemDto) {
                        SkillItemDto skillItemDto = (SkillItemDto) object;
                        if (currentTreeItem.isExpanded()) {
                            treeSet.add("" + skillItemDto.getValue());
                        } else {
                            treeSet.remove("" + skillItemDto.getValue());
                        }
                    } else {
                        String value = (String) object;
                        if (currentTreeItem.isExpanded()) {
                            treeSet.add(value);
                        } else {
                            treeSet.remove(value);
                        }
                    }
                }
                ZTreeNodeDto treeNodeDto = null;
                if (object instanceof TreeItemDto) {
                    TreeItemDto itemDto = (TreeItemDto) object;
                    treeNodeDto = (ZTreeNodeDto) itemDto.getValue();
                } else if (object instanceof SkillItemDto) {
                    SkillItemDto itemDto = (SkillItemDto) object;
                    treeNodeDto = (ZTreeNodeDto) itemDto.getValue();
                }
                if (treeNodeDto != null) {
                    loadAtlasFiles(treeNodeDto.getFullName());
                }
            }
        });
    }

    public void loadAtlasFiles(String fullName) {
        log.debug("loadAtlasFiles..." + fullName);
        try {
            if (StringUtils.isEmpty(fullName)) {
                return;
            }
            String[] dirArr = fullName.split(CommonConstant.FEIGN_ERROR_SYMBOL_STRING);
            Map paramMap = new HashMap() {{
                for (int i = 0; i < dirArr.length; i++) {
                    switch (i) {
                        case 0: {
                            //电业局
                            put("electricBureau", dirArr[0]);
                            break;
                        }
                        case 1: {
                            //变电站
                            put("substation", dirArr[1]);
                            break;
                        }
                        case 2: {
                            //设备类型
                            put("deviceType", AccountDto.DeviceType.getValueByText(dirArr[2]).toString());
                            break;
                        }
                        case 3: {
                            //电压等级
                            put("voltageLevel", dirArr[3].replaceAll("([^\\d])", ""));
                            break;
                        }
                        case 4: {
                            //检测日期
                            put("dateDetection", dirArr[4]);
                            break;
                        }
                        case 5: {
                            //检测技术
                            put("testTechnology", dirArr[5]);
                            break;
                        }
                    }
                }
            }};
            dataPathSet.clear();
            dataCheckMap.clear();
            dataAnalyzeSet.clear();
            ObservableList<DataAnalyzeDto> dataList = FXCollections.observableArrayList();
            dataList.setAll(dataAnalyzeService.findListByDevice(paramMap));
            if (dataList != null) {
                AtomicInteger atomicInteger = new AtomicInteger(1);
                dataList.forEach(dataAnalyzeDto -> {
                    CheckBox checkBox = new CheckBox("" + (atomicInteger.get()));
//                    checkBox.setStyle("-fx-text-alignment: center;-fx-alignment: center;-fx-pref-width:75;-fx-padding: 0;-fx-content-display: center;");
                    checkBox.setId(dataAnalyzeDto.getFileUrl());
                    checkBox.selectedProperty().addListener((ov, oldVal, newVal) -> {
                        if (newVal) {
                            dataPathSet.add(checkBox.getId());
                            dataAnalyzeSet.add(dataAnalyzeDto);
                        } else {
                            allCheck.setSelected(false);
                            dataPathSet.remove(checkBox.getId());
                            dataAnalyzeSet.remove(dataAnalyzeDto);
                        }
                    });
                    if (allCheck.isSelected()) {
                        checkBox.setSelected(allCheck.isSelected());
                    } else {
                        checkBox.setSelected(dataPathSet.contains(checkBox.getId()));
                    }
                    dataCheckMap.put(checkBox.getText(), checkBox);
                    atomicInteger.getAndAdd(1);
                });
            }
            fileTable.setItems(dataList);
            idTd.setCellFactory((col) -> {
                TableCell<DataAnalyzeDto, String> cell = new TableCell<DataAnalyzeDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            Label label= new Label("");
                            label.setGraphic(dataCheckMap.get("" + (this.getIndex() + 1)));
                            this.setGraphic(label);
                            this.setStyle("-fx-pref-width: 75;-fx-text-overrun: center-ellipsis;");
                        }else{
                            this.setText(null);
                            this.setGraphic(null);
                        }
                    }
                };
                return cell;
            });
            typeTd.setCellFactory((col) -> new DataTableCell("dataFormat", new HashMap<String, Object>() {{
                put("TEV_XJZS", "TEV_巡检噪声");
                put("TEV_XJ", "TEV_巡检");
                put("TEV_TJZS", "TEV_统计噪声");
                put("TEV_TJ", "TEV_统计数据");
                put("TEV_TJLB", "TEV_统计录波");
                put("AA_XJZS", "AA_巡检噪声");
                put("AA_XJ", "AA_巡检");
                put("AA_TJZS", "AA_统计噪声");
                put("AA_TJ", "AA_统计数据");
                put("AA_TJLB", "AA_统计录波");
                put("AA_FX", "AA_飞行图谱");
                put("AA_BX", "AA_波形图谱");
                put("AE_XJZS", "AE_巡检噪声");
                put("AE_XJ", "AE_巡检");
                put("AE_TJZS", "AE_统计噪声");
                put("AE_TJ", "AE_统计数据");
                put("AE_TJLB", "AE_统计录波");
                put("AE_FX", "AE_飞行图谱");
                put("AE_BX", "AE_波形图谱");
                put("HFCT_XJZS", "HFCT_巡检噪声");
                put("HFCT_XJ", "HFCT_巡检");
                put("HFCT_TJZS", "HFCT_统计噪声");
                put("HFCT_TJ", "HFCT_统计数据");
                put("HFCT_TJLB", "HFCT_统计录波");
                put("UHF_TJZS_1", "UHF_模式1_统计噪声");
                put("UHF_TJ_1", "UHF_模式1_统计数据");
                put("UHF_TJLB_1", "UHF_模式1_统计录波");
                put("UHF_TJZS_2", "UHF_模式2_统计噪声");
                put("UHF_TJ_2", "UHF_模式2_统计数据");
                put("UHF_TJLB_2", "UHF_模式2_统计录波");
                put("LC", "负荷电流");
                put("HJ", "环境参数");
            }}));
            nameTd.setCellFactory((col) -> {
                TableCell<DataAnalyzeDto, String> cell = new TableCell<DataAnalyzeDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setStyle("-fx-text-alignment: center-left;-fx-alignment: center-left;");
                        if (!empty) {
                            DataAnalyzeDto dataAnalyzeDto = this.getTableView().getItems().get(this.getIndex());
                            this.setText(FileUtil.getFileName(dataAnalyzeDto.getFileUrl()));
                        }
                    }
                };
                return cell;
            });
        } catch (Exception e) {
            log.error("系统异常!", e);
            ialert.error("系统异常!" + e.getMessage());
        }
    }

    /**
     * 装载查询参数数据
     */
    private void loadSearchParamData() {
        try {
            deviceTypeChoiceBox.setItems(FXCollections.observableArrayList("请选择", "开关柜", "变压器", "组合电器", "电缆"));
            deviceTypeChoiceBox.getSelectionModel().select(0);
            testTechnologyChoiceBox.setItems(FXCollections.observableArrayList("请选择", "AA", "AE", "HFCT", "UHF", "TEV"));
            testTechnologyChoiceBox.getSelectionModel().select(0);
            voltageLevelChoiceBox.setItems(FXCollections.observableArrayList("请选择", "10kV", "35kV", "110kV", "220kV", "500kV"));
            voltageLevelChoiceBox.getSelectionModel().select(0);
        } catch (Exception e) {

        }
    }

    @FXML
    public void exportPath() {
        log.info("exportPath...");
        String path = directoryChooser("请选择数据导出路径", null);
        if (ValidatorUtil.notEmpty(path)) {
            outFilePathText.setText(path);
        }
    }

    @FXML
    public void exportRun() {
        log.info("exportRun...");
        String outFilePath = outFilePathText.getText();
        if (ValidatorUtil.isEmpty(outFilePath)) {
            ialert.error("请选择输出目录!");
            return;
        }
//        ehcacheUtil.setCache("atlas_path_out", outFilePath);
        log.debug("输出路径==>{}", outFilePath);

//        logsList.getItems().clear();
        runButton.setDisable(true);
        ConvertFileTask task = new ConvertFileTask(new Hashtable() {{
            put("ialert", ialert);
            put("logsList", logsList);
            put("outFilePath", outFilePath);
            put("dataAnalyzeSet", dataAnalyzeSet);

            put("progressText", progressText);
            put("progressLine", progressLine);
            put("progressValue", progressValue);

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
        }});
        task.runningProperty().addListener((ov, wasRunning, isRunning) -> {
            if (!isRunning) {
                runButton.setDisable(false);
            }
        });
        final Thread thread = new Thread(task, "ConvertFileTask");
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    public void loadAccTree() {
        refreshButton.setDisable(true);
        loadAccTree(accountTree, new HashMap() {{
            put("isAtlas", "1");
            if (ValidatorUtil.notEmpty(electricBureauText.getText())) {
                put("electricBureau", electricBureauText.getText());
            }
            if (ValidatorUtil.notEmpty(substationText.getText())) {
                put("substation", substationText.getText());
            }
            if (deviceTypeChoiceBox.getValue() != null && ValidatorUtil.notEmpty("" + deviceTypeChoiceBox.getValue())) {
                put("deviceType", getDeviceTypeValue(String.valueOf(deviceTypeChoiceBox.getValue())));
            }
            if (voltageLevelChoiceBox.getValue() != null && ValidatorUtil.notEmpty("" + voltageLevelChoiceBox.getValue())) {
                put("voltageLevel", getvoltageLevelValue(String.valueOf(voltageLevelChoiceBox.getValue())));
            }
            if (ValidatorUtil.notEmpty(deviceNameText.getText())) {
                put("deviceName", deviceNameText.getText());
            }
            if (testTechnologyChoiceBox.getValue() != null && !"请选择".equals("" + testTechnologyChoiceBox.getValue())) {
                put("testTechnology", testTechnologyChoiceBox.getValue());
            }
            if (produceDateDatePicker.getValue() != null) {
                put("produceDate", produceDateDatePicker.getValue().format(dateTimeFormatter));
            }
            if (useDateDatePicker.getValue() != null) {
                put("useDate", useDateDatePicker.getValue().format(dateTimeFormatter));
            }
        }});
        refreshButton.setDisable(false);
    }

    @FXML
    public void onReset() {
        log.info("onReset..");
        electricBureauText.setText(null);//请输入电业局名称
        substationText.setText(null);//请输入变电站名称
        deviceTypeChoiceBox.getSelectionModel().select(0);//
        voltageLevelChoiceBox.getSelectionModel().select(0);//
        deviceNameText.setText(null);//设备名称
        testTechnologyChoiceBox.getSelectionModel().select(0);//
        produceDateDatePicker.setValue(null);//点击输入出厂日期
        useDateDatePicker.setValue(null);//点击输入投运日期
    }

    @FXML
    public void setShowDone() {
        log.info("setShowDone..");
        hideNodeView();
        loadAccTree();
    }

    @FXML
    public void checkAll() {
        log.info("checkAll..");
        dataCheckMap.values().forEach(node -> {
            node.setSelected(allCheck.isSelected());
        });
    }

    @FXML
    @Getter
    Pane mlayer;//蒙层
    @FXML
    VBox mVBox;
    @FXML
    HBox mHBox;
    @FXML
    Pane closeSet;

    private void hideNodeView(Parent... nodeViews) {
        if (nodeViews == null || nodeViews.length == 0) {
            nodeViews = new Parent[]{mlayer, mVBox};
        }
        for (Parent nodeView : nodeViews) {
            nodeView.setVisible(false);
        }
    }

    @FXML
    public void searhData() {
        log.info("searhData..");
        hideNodeView();

        mlayer.setVisible(true);
        mVBox.setVisible(true);
    }

    @Override
    public void dispose() {

    }
}
