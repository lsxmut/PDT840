package com.redphase.controller.nda.data;

import com.redphase.controller.nda.NdaBaseController;
import com.redphase.dto.atlas.DataAnalyzeDto;
import com.redphase.dto.atlas.DataDeviceDto;
import com.redphase.dto.atlas.DataHjDto;
import com.redphase.framework.util.CommonConstant;
import com.redphase.framework.util.ValidatorUtil;
import com.redphase.view.nda.data.*;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@FXMLController
@Slf4j
public class NdaDataSkillController extends NdaBaseController implements Initializable {
    @FXML
    BorderPane dataBorderPane;
    @Autowired
    NdaDataAAView ndaDataAAView;
    @Autowired
    NdaDataAEView ndaDataAEView;
    @Autowired
    NdaDataHFCTView ndaDataHFCTView;
    @Autowired
    NdaDataTEVView ndaDataTEVView;
    @Autowired
    NdaDataUHFView ndaDataUHFView;
    @Autowired
    NdaDataHFCTController ndaDataHFCTController;
    @Autowired
    NdaDataAAController ndaDataAAController;
    @Autowired
    NdaDataAEController ndaDataAEController;
    @Autowired
    NdaDataTEVController ndaDataTEVController;
    @Autowired
    NdaDataUHFController ndaDataUHFController;
    @Autowired
    NdaSiteImagesController ndaSiteImagesController;
    @FXML
    Label testTechnologyL;//检测技术
    //    @FXML
//    Button reportB;//生成报告
    @FXML
    Label bdzL;//变电站名称
    @FXML
    Label dydjL;//电压等级
    @FXML
    Label jcryL;//检测人员
    @FXML
    Label jcrqL;//检测日期
    @FXML
    TableView<DataDeviceDto> deviceSiteTableView;//设备列表
    @FXML
    TableColumn<DataDeviceDto, String> codeTableColumn;
    @FXML
    TableColumn<DataDeviceDto, String> nameTableColumn;
    boolean initFlag = false;
    private Pane rightAnchorPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize...");
        if (initFlag) return;
        initFlag = true;
        dataBorderPane.prefWidthProperty().bind(ndaController.getRightAnchorPane().prefWidthProperty());
        dataBorderPane.prefHeightProperty().bind(ndaController.getRightAnchorPane().prefHeightProperty());
        rightAnchorPane = ndaController.getRightAnchorPane();
    }

//    @FXML
//    public void report() {
//        log.debug("NdaDataSkillController report..");
//
//    }


    public void loadDeviceList(String testTechnology) {
        try {
            testTechnologyL.setText(testTechnology);
//            if ("AA".equalsIgnoreCase(testTechnology)||"HFCT".equalsIgnoreCase(testTechnology)) {
//                reportB.setVisible(true);
//            }
            String fullName = (String) ehcacheUtil.getCache(CommonConstant.NDA_DATA_SKILL_FULL_NAME);
            String[] dirArr = fullName.split("_");
            List<DataDeviceDto> dataDeviceDtoList = dataDeviceService.findListByFullName(fullName);
            DataDeviceDto mainDataDeviceDto = null;//主设备目录
            DataDeviceDto subDataDeviceDto = null;//具体设备目录

            for (DataDeviceDto deviceDto : dataDeviceDtoList) {
                if ("#".equalsIgnoreCase(deviceDto.getDeviceName())) {
                    mainDataDeviceDto = deviceDto;
                } else {
                    subDataDeviceDto = deviceDto;
                }
            }
            if (subDataDeviceDto == null) {
                subDataDeviceDto = mainDataDeviceDto;
            }
            ehcacheUtil.setCache(CommonConstant.NDA_DATA_SKILL_DEVICE_MAIN, mainDataDeviceDto);
            DataDeviceDto finalMainDataDeviceDto = mainDataDeviceDto;
            DataDeviceDto finalSubDataDeviceDto = subDataDeviceDto;
            bdzL.setText(subDataDeviceDto.getSubstation());//变电站名称
            dydjL.setText(subDataDeviceDto.getVoltageLevel() + "");//电压等级
            jcrqL.setText(subDataDeviceDto.getDateDetection());//检测日期
//            String dataSource = "0";
//            if (!"开关柜".equalsIgnoreCase(dirArr[2])) {
//                dataSource = "1";
//            }
//            String finalDataSource = dataSource;
            List<DataAnalyzeDto> dataAnalyzeDtoList = dataAnalyzeService.findDataIsList(new DataAnalyzeDto() {{
                if ("开关柜".equalsIgnoreCase(dirArr[2]) && "TEV".equalsIgnoreCase(dirArr[6])) {
                    setDataDeviceId(finalMainDataDeviceDto.getId());
                } else {
                    setDataDeviceId(finalSubDataDeviceDto.getId());
                }
//                setDataSource(finalDataSource);
            }});
            for (DataAnalyzeDto dataAnalyzeDto : dataAnalyzeDtoList) {
                if ("HJ".equalsIgnoreCase(dataAnalyzeDto.getDataFormat()) && 0 == (dataAnalyzeDto.getOrderNo())) {
                    DataHjDto dataHjDto = dataHjService.findDataById(new DataHjDto() {{
                        setDataAnalyzeId(dataAnalyzeDto.getId());
                    }});
                    String jcry = dataHjDto.getX6();
                    if (ValidatorUtil.notEmpty(dataHjDto.getX7())) {
                        jcry += "," + dataHjDto.getX7();
                    } else if (ValidatorUtil.notEmpty(dataHjDto.getX8())) {
                        jcry += "," + dataHjDto.getX8();
                    } else if (ValidatorUtil.notEmpty(dataHjDto.getX9())) {
                        jcry += "," + dataHjDto.getX9();
                    } else if (ValidatorUtil.notEmpty(dataHjDto.getX10())) {
                        jcry += "," + dataHjDto.getX10();
                    }
                    jcryL.setText(jcry);//检测人员
                }
            }
            dataDeviceDtoList.remove(mainDataDeviceDto);
            ObservableList<DataDeviceDto> data = FXCollections.observableArrayList(dataDeviceDtoList);
            deviceSiteTableView.setItems(data);

            codeTableColumn.setCellFactory((col) -> {
                TableCell<DataDeviceDto, String> cell = new TableCell<DataDeviceDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        this.setStyle("-fx-pref-width: 100;-fx-text-overrun: center-ellipsis;");
                        if (!empty) {
                            int rowIndex = this.getIndex() + 1;
                            this.setText(String.valueOf(rowIndex));
                        }
                    }
                };
                return cell;
            });
            nameTableColumn.setCellFactory((col) -> {
                TableCell<DataDeviceDto, String> cell = new TableCell<DataDeviceDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        this.setStyle("-fx-text-fill: #31a1ff;-fx-underline: true;-fx-cursor: hand;");
                        if (!empty) {
                            DataDeviceDto deviceDto = this.getTableView().getItems().get(this.getIndex());
                            this.setText(deviceDto.getDeviceName());
                            this.setOnMouseClicked((me) -> {
                                ehcacheUtil.setCache(CommonConstant.NDA_DATA_SKILL_DEVICE_SUB, deviceDto);
                                switch (testTechnology) {
                                    case "TEV": {
                                        rightAnchorPane.getChildren().setAll(ndaDataTEVView.getView());
                                        ndaDataTEVController.loadTEVData(fullName);
                                        break;
                                    }
                                    case "AA": {
                                        rightAnchorPane.getChildren().setAll(ndaDataAAView.getView());
                                        ndaDataAAController.loadAAData(fullName);
                                        break;
                                    }
                                    case "HFCT": {
                                        rightAnchorPane.getChildren().setAll(ndaDataHFCTView.getView());
                                        ndaDataHFCTController.loadHFCTDetail();
                                        break;
                                    }
                                    case "AE": {
                                        rightAnchorPane.getChildren().setAll(ndaDataAEView.getView());
                                        ndaDataAEController.loadAEDetail();
                                        break;
                                    }
                                    case "UHF": {
                                        rightAnchorPane.getChildren().setAll(ndaDataUHFView.getView());
                                        ndaDataUHFController.loadUHFDetail();
                                        break;
                                    }
                                    default:
                                        ialert.error("未知检测类型!");
                                }
                            });
                        }
                    }
                };
                return cell;
            });

        } catch (Exception e) {
            log.error("系统异常!", e);
            ialert.error(e.getMessage());
        }
    }

    @Override
    public void dispose() {

    }
}