package com.redphase.controller.atlas.data;

import com.redphase.controller.atlas.AtlasBaseController;
import com.redphase.controller.atlas.AtlasController;
import com.redphase.dto.atlas.DataAnalyzeDto;
import com.redphase.dto.atlas.DataDeviceDto;
import com.redphase.dto.atlas.DataHjDto;
import com.redphase.framework.util.CommonConstant;
import com.redphase.framework.util.ValidatorUtil;
import com.redphase.view.atlas.data.*;
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
public class AtlasDataSkillController extends AtlasBaseController implements Initializable {
    @FXML
    BorderPane dataBorderPane;
    private Pane rightAnchorPane;
    @Autowired
    AtlasDataAAView atlasDataAAView;
    @Autowired
    AtlasDataAEView atlasDataAEView;
    @Autowired
    AtlasDataHFCTView atlasDataHFCTView;
    @Autowired
    AtlasDataTEVView atlasDataTEVView;
    @Autowired
    AtlasDataUHFView atlasDataUHFView;
    @Autowired
    AtlasController atlasController;
    @Autowired
    AtlasDataHFCTController atlasDataHFCTController;
    @Autowired
    AtlasDataAAController atlasDataAAController;
    @Autowired
    AtlasDataAEController atlasDataAEController;
    @Autowired
    AtlasDataTEVController atlasDataTEVController;
    @Autowired
    AtlasDataUHFController atlasDataUHFController;
    @Autowired
    AtlasSiteImagesController atlasSiteImagesController;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize...");
        if (initFlag) return;
        initFlag = true;
        dataBorderPane.prefWidthProperty().bind(atlasController.getRightAnchorPane().prefWidthProperty());
        dataBorderPane.prefHeightProperty().bind(atlasController.getRightAnchorPane().prefHeightProperty());
        rightAnchorPane = atlasController.getRightAnchorPane();
    }

//    @FXML
//    public void report() {
//        log.debug("AtlasDataSkillController report..");
//
//    }


    public void loadDeviceList(String testTechnology) {
        try {
            testTechnologyL.setText(testTechnology);
//            if ("AA".equalsIgnoreCase(testTechnology)||"HFCT".equalsIgnoreCase(testTechnology)) {
//                reportB.setVisible(true);
//            }
            String fullName = (String) ehcacheUtil.getCache(CommonConstant.ATLAS_DATA_SKILL_FULL_NAME);
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
            ehcacheUtil.setCache(CommonConstant.ATLAS_DATA_SKILL_DEVICE_MAIN, mainDataDeviceDto);
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
                                ehcacheUtil.setCache(CommonConstant.ATLAS_DATA_SKILL_DEVICE_SUB, deviceDto);
                                switch (testTechnology) {
                                    case "TEV": {
                                        rightAnchorPane.getChildren().setAll(atlasDataTEVView.getView());
                                        atlasDataTEVController.loadTEVData(fullName);
                                        break;
                                    }
                                    case "AA": {
                                        rightAnchorPane.getChildren().setAll(atlasDataAAView.getView());
                                        atlasDataAAController.loadAAData(fullName);
                                        break;
                                    }
                                    case "HFCT": {
                                        rightAnchorPane.getChildren().setAll(atlasDataHFCTView.getView());
                                        atlasDataHFCTController.loadHFCTDetail();
                                        break;
                                    }
                                    case "AE": {
                                        rightAnchorPane.getChildren().setAll(atlasDataAEView.getView());
                                        atlasDataAEController.loadAEDetail();
                                        break;
                                    }
                                    case "UHF": {
                                        rightAnchorPane.getChildren().setAll(atlasDataUHFView.getView());
                                        atlasDataUHFController.loadUHFDetail();
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