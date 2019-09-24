package com.redphase.controller.nda.data;

import com.alibaba.fastjson.JSONObject;
import com.redphase.controller.nda.NdaBaseController;
import com.redphase.dto.atlas.DataAnalyzeDto;
import com.redphase.dto.atlas.DataDeviceDto;
import com.redphase.dto.atlas.DataDeviceSiteDto;
import com.redphase.dto.atlas.DataLcDto;
import com.redphase.dto.atlas.aa.*;
import com.redphase.dto.table.DataAaTableDto;
import com.redphase.dto.table.DataTableDto;
import com.redphase.framework.util.I18nUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@FXMLController
@Slf4j
public class NdaDataAASiteInfoController extends NdaBaseController implements Initializable {
    @FXML
    Label typeL;//
    @FXML
    Label titleL;//标题
    @FXML
    TableView<DataAaTableDto> deviceSiteTableView;//检测位置列表
    @FXML
    TableColumn<DataAaTableDto, String> idTableColumn;//序号
    @FXML
    TableColumn<DataAaTableDto, String> siteTableColumn;//检测位置
    @FXML
    TableColumn<DataAaTableDto, String> xjzdzTableColumn;//AA巡检最大值
    @FXML
    TableColumn<DataAaTableDto, String> xjyxzTableColumn;//AA巡检有效值
    @FXML
    TableColumn<DataAaTableDto, String> xjf1fzTableColumn;//AA巡检f1分值
    @FXML
    TableColumn<DataAaTableDto, String> xjf2fzTableColumn;//AA巡检f2分值
    @FXML
    TableColumn<DataAaTableDto, String> xjjtTableColumn;//AA巡检截图
    @FXML
    TableColumn<DataAaTableDto, String> xjlyTableColumn;//AA巡检录音
    @FXML
    TableColumn<DataAaTableDto, String> tjTableColumn;//AA统计检测
    @FXML
    TableColumn<DataAaTableDto, String> tjjtTableColumn;//AA统计截图
    @FXML
    TableColumn<DataAaTableDto, String> tjlbTableColumn;//AA统计录波
    @FXML
    TableColumn<DataAaTableDto, String> fxjtTableColumn;//AA飞行截图
    @FXML
    TableColumn<DataAaTableDto, String> bxjtTableColumn;//AA波形截图
    @FXML
    TableColumn<DataAaTableDto, String> kjgTableColumn;//可见光照片
    @FXML
    TableColumn<DataAaTableDto, String> fhTableColumn;//负荷电流
    ObservableList<DataAaTableDto> dataList;
    List<DataDeviceSiteDto> dataDeviceSiteDtoList;
    String testTechnology = "AA";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize...");
    }

    public void loadData(DataDeviceDto dataDeviceDto) {
        try {
            String deviceType = "";
            switch (dataDeviceDto.getDeviceType()) {//设备类型(1、开关柜；2、变压器；3、组合电器；4、电缆)
                case "1": {
                    deviceType = "开关柜";
                    break;
                }
                case "2": {
                    deviceType = "变压器";
                    break;
                }
                case "3": {
                    deviceType = "组合电器";
                    break;
                }
                case "4": {
                    deviceType = "电缆";
                    break;
                }
            }
            testTechnology = dataDeviceDto.getTestTechnology();

            typeL.setText(deviceType + "名称:");//
            titleL.setText(dataDeviceDto.getDeviceName());//

            //位置信息
            dataDeviceSiteDtoList = dataDeviceSiteService.findDataIsList(new DataDeviceSiteDto() {{
                setDataDeviceId(dataDeviceDto.getId());
            }});
            dataList = FXCollections.observableArrayList();
            for (DataDeviceSiteDto dataDeviceSiteDto : dataDeviceSiteDtoList) {
                DataAaTableDto siteDataAaTableDto = new DataAaTableDto();
                siteDataAaTableDto.setDataDeviceDto(dataDeviceDto);
                siteDataAaTableDto.setDataDeviceSiteDto(dataDeviceSiteDto);
                siteDataAaTableDto.setSite(dataDeviceSiteDto.getSiteName()); //检测位置
                siteDataAaTableDto.setFddj(dataDeviceDto.getVoltageLevel() + "kV");//电压等级；
                siteDataAaTableDto.setBcsb(dataDeviceDto.getDeviceName() + ""); //被测设备；
                siteDataAaTableDto.setXjList(new ArrayList<>());//巡检文件列表
                siteDataAaTableDto.setTjList(new ArrayList<>());//统计文件列表
                siteDataAaTableDto.setTjlbList(new ArrayList<>());//录波文件列表
                siteDataAaTableDto.setFxList(new ArrayList<>());//飞行文件列表
                siteDataAaTableDto.setBxList(new ArrayList<>());//波形文件列表
                siteDataAaTableDto.setKjgzpList(new ArrayList<>());//可将光文件列表
                int seqNo = 0;
                //获取当前位置数据
                List<DataAnalyzeDto> analyzeDtos = dataAnalyzeService.findDataIsList(new DataAnalyzeDto() {{
                    setDataDeviceSiteId(dataDeviceSiteDto.getId());
                }});
                if (dataList != null) {
                    for (DataAnalyzeDto analyzeDto : analyzeDtos) {
                        switch (analyzeDto.getDataFormat()) {
                            case "LC": {
                                //负荷电流
                                DataLcDto dataLcDto = dataLcService.findDataById(new DataLcDto() {{
                                    setDataAnalyzeId(analyzeDto.getId());
                                }});
                                siteDataAaTableDto.setFh(dataLcDto.getX4());
                                break;
                            }
                            case "HJ": {
                                break;
                            }
                            case "AA_XJ": {
                                DataAaTableDto xjDataTableDto = new DataAaTableDto() {{
                                    setDataDeviceDto(siteDataAaTableDto.getDataDeviceDto());
                                    setDataDeviceSiteDto(siteDataAaTableDto.getDataDeviceSiteDto());
                                }};
                                //AA巡检检测文件
                                xjDataTableDto.setFileUrl(analyzeDto.getFileUrl());
                                //AA巡检截图
                                xjDataTableDto.setScreenshots(analyzeDto.getScreenshots());
                                //巡检录音
                                xjDataTableDto.setAudios(analyzeDto.getAudios());
                                DataAaXjDto dataAaXjDto = dataAaXjService.findDataById(new DataAaXjDto() {{
                                    setDataAnalyzeId(analyzeDto.getId());
                                }});
                                if (dataAaXjDto != null) {
//                                    if (zdxj == null || dataAaXjDto.getX5() > zdxj) {
//                                        zdxj = dataAaXjDto.getX5();
//                                    }
                                    xjDataTableDto.setCssj("" + dataAaXjDto.getX3());//测试时间；
                                    xjDataTableDto.setCkyz("" + dataAaXjDto.getX15());//参考阈值；
                                    xjDataTableDto.setFdzy("" + dataAaXjDto.getX16());//放大增益；
                                    siteDataAaTableDto.setTbpl("" + dataAaXjDto.getX20());//同步频率；
                                    siteDataAaTableDto.setXtpl("" + dataAaXjDto.getX19());//系统频率；
                                    xjDataTableDto.setFz(dataAaXjDto.getX5());//最大值
                                    xjDataTableDto.setYxz(dataAaXjDto.getX7());//有效值
                                    xjDataTableDto.setF1(dataAaXjDto.getX8());//f1分量
                                    xjDataTableDto.setF2(dataAaXjDto.getX9());//f2分量
                                    siteDataAaTableDto.getXjList().add(xjDataTableDto);
                                    //-----------
                                    if (siteDataAaTableDto.getKjgzpList().size() == 0) {
                                        xjDataTableDto.setPhotos(analyzeDto.getPhotos());
                                        siteDataAaTableDto.getKjgzpList().add(xjDataTableDto);
                                    }
                                }
                                break;
                            }
                            case "AA_XJZS": {
                                break;
                            }
                            case "AA_TJ": {
                                DataAaTableDto tjDataTableDto = new DataAaTableDto() {{
                                    setDataDeviceDto(siteDataAaTableDto.getDataDeviceDto());
                                    setDataDeviceSiteDto(siteDataAaTableDto.getDataDeviceSiteDto());
                                }};
                                //AA统计检测文件
                                tjDataTableDto.setFileUrl(analyzeDto.getFileUrl());
                                //AA统计截图
                                tjDataTableDto.setScreenshots(analyzeDto.getScreenshots());
                                DataAaTjDto dataAaTjDto = dataAaTjService.findDataById(new DataAaTjDto() {{
                                    setDataAnalyzeId(analyzeDto.getId());
                                }});
                                if (dataAaTjDto != null) {
                                    tjDataTableDto.setCssj("" + dataAaTjDto.getX14());//测试时间；
                                    tjDataTableDto.setXwwy("" + dataAaTjDto.getX19());//相位位移；
                                    tjDataTableDto.setCkyz("" + dataAaTjDto.getX18());//参考阈值；
                                    tjDataTableDto.setFdzy("" + dataAaTjDto.getX23());//放大增益；
                                    tjDataTableDto.setTbfs("" + (new Integer(dataAaTjDto.getX20()) == 1 ? "外同步" : "内同步"));//同步方式 同步类型外同步:0x01内同步:0x02默认写入内同步
                                    tjDataTableDto.setTbpl("" + dataAaTjDto.getX21());//同步频率；
                                    tjDataTableDto.setXtpl("" + dataAaTjDto.getX22());//系统频率；
                                    tjDataTableDto.setFz(dataAaTjDto.getX16());//幅值；
                                    tjDataTableDto.setMc("" + dataAaTjDto.getX17());//脉冲；
                                    tjDataTableDto.setYz("" + dataAaTjDto.getX18());//阈值；
                                    tjDataTableDto.setAtlasArr(JSONObject.parseObject(dataAaTjDto.getX11(), Float[][].class));
                                    siteDataAaTableDto.getTjList().add(tjDataTableDto);
                                    //-----------
                                    if (siteDataAaTableDto.getKjgzpList().size() == 0) {
                                        tjDataTableDto.setPhotos(analyzeDto.getPhotos());
                                        siteDataAaTableDto.getKjgzpList().add(tjDataTableDto);
                                    }
                                }
                                break;
                            }
                            case "AA_TJZS": {
                                break;
                            }
                            case "AA_TJLB": {
                                //AA统计录波
                                DataAaTableDto tjlbDataTableDto = new DataAaTableDto() {{
                                    setDataDeviceDto(siteDataAaTableDto.getDataDeviceDto());
                                    setDataDeviceSiteDto(siteDataAaTableDto.getDataDeviceSiteDto());
                                }};
                                DataAaTjlbDto dataAaTjlbDto = dataAaTjlbService.findDataById(new DataAaTjlbDto() {{
                                    setDataAnalyzeId(analyzeDto.getId());
                                }});
                                if (dataAaTjlbDto != null) {
                                    tjlbDataTableDto.setSeqNo(seqNo++);
                                    tjlbDataTableDto.setCssj("" + dataAaTjlbDto.getX3());//测试时间；
                                    tjlbDataTableDto.setXwwy("" + dataAaTjlbDto.getX13());//相位位移；
                                    tjlbDataTableDto.setCkyz("" + dataAaTjlbDto.getX12());//参考阈值；
                                    tjlbDataTableDto.setFdzy("" + dataAaTjlbDto.getX17());//放大增益；
                                    tjlbDataTableDto.setTbfs("" + (new Integer(dataAaTjlbDto.getX14()) == 1 ? "外同步" : "内同步"));//同步方式 同步类型外同步:0x01内同步:0x02默认写入内同步
                                    tjlbDataTableDto.setTbpl("" + dataAaTjlbDto.getX15());//同步频率；
                                    tjlbDataTableDto.setXtpl("" + dataAaTjlbDto.getX16());//系统频率；
                                    tjlbDataTableDto.setAtlasArr(JSONObject.parseObject(dataAaTjlbDto.getX11(), Float[][].class));
                                    siteDataAaTableDto.getTjlbList().add(tjlbDataTableDto);
                                    //-----------
                                    if (siteDataAaTableDto.getKjgzpList().size() == 0) {
                                        tjlbDataTableDto.setPhotos(analyzeDto.getPhotos());
                                        siteDataAaTableDto.getKjgzpList().add(tjlbDataTableDto);
                                    }
                                }
                                break;
                            }
                            case "AA_FX": {
                                //AA飞行
                                DataAaTableDto fxDataTableDto = new DataAaTableDto() {{
                                    setDataDeviceDto(siteDataAaTableDto.getDataDeviceDto());
                                    setDataDeviceSiteDto(siteDataAaTableDto.getDataDeviceSiteDto());
                                }};
                                //AA飞行文件
                                fxDataTableDto.setFileUrl(analyzeDto.getFileUrl());
                                //AA飞行截图
                                fxDataTableDto.setScreenshots(analyzeDto.getScreenshots());
                                DataAaFxDto dataAaFxDto = dataAaFxService.findDataById(new DataAaFxDto() {{
                                    setDataAnalyzeId(analyzeDto.getId());
                                }});
                                if (dataAaFxDto != null) {
                                    fxDataTableDto.setCssj("" + dataAaFxDto.getX3());//测试时间；
                                    fxDataTableDto.setCkyz("" + dataAaFxDto.getX12());//参考阈值；
                                    fxDataTableDto.setTbfs("" + (new Integer(dataAaFxDto.getX20()) == 1 ? "外同步" : "内同步"));//同步方式 同步类型外同步:0x01内同步:0x02默认写入内同步
                                    fxDataTableDto.setTbpl("" + dataAaFxDto.getX21());//同步频率；
                                    fxDataTableDto.setXtpl("" + dataAaFxDto.getX19());//系统频率；
                                    fxDataTableDto.setKmsj(dataAaFxDto.getX14());//开门时间
                                    fxDataTableDto.setKmsj(dataAaFxDto.getX15());//闭锁时间
                                    fxDataTableDto.setKmsj(dataAaFxDto.getX16());//等待时间
                                    fxDataTableDto.setAtlasArr(JSONObject.parseObject(dataAaFxDto.getX11(), Float[][].class));
                                    siteDataAaTableDto.getFxList().add(fxDataTableDto);
                                    //-----------
                                    if (siteDataAaTableDto.getKjgzpList().size() == 0) {
                                        fxDataTableDto.setPhotos(analyzeDto.getPhotos());
                                        siteDataAaTableDto.getKjgzpList().add(fxDataTableDto);
                                    }
                                }
                                break;
                            }
                            case "AA_BX": {
                                DataAaTableDto bxDataTableDto = new DataAaTableDto() {{
                                    setDataDeviceDto(siteDataAaTableDto.getDataDeviceDto());
                                    setDataDeviceSiteDto(siteDataAaTableDto.getDataDeviceSiteDto());
                                }};
                                //AA波形文件
                                bxDataTableDto.setFileUrl(analyzeDto.getFileUrl());
                                //AA波形截图
                                bxDataTableDto.setScreenshots(analyzeDto.getScreenshots());
                                DataAaBxDto dataAaBxDto = dataAaBxService.findDataById(new DataAaBxDto() {{
                                    setDataAnalyzeId(analyzeDto.getId());
                                }});
                                if (dataAaBxDto != null) {
                                    bxDataTableDto.setCssj("" + dataAaBxDto.getX3());//测试时间；
                                    bxDataTableDto.setTbfs("" + (new Integer(dataAaBxDto.getX13()) == 1 ? "外同步" : "内同步"));//同步方式 同步类型外同步:0x01内同步:0x02默认写入内同步
                                    bxDataTableDto.setTbpl("" + dataAaBxDto.getX14());//同步频率；
                                    bxDataTableDto.setXtpl("" + dataAaBxDto.getX15());//系统频率；
                                    siteDataAaTableDto.getBxList().add(bxDataTableDto);
                                    //-----------
                                    if (siteDataAaTableDto.getKjgzpList().size() == 0) {
                                        bxDataTableDto.setPhotos(analyzeDto.getPhotos());
                                        siteDataAaTableDto.getKjgzpList().add(bxDataTableDto);
                                    }
                                }
                            }
                        }
                    }
                }
                dataList.add(siteDataAaTableDto);
            }
            deviceSiteTableView.setItems(dataList);
            //序号
            idTableColumn.setCellFactory((col) -> {
                TableCell<DataAaTableDto, String> cell = new TableCell<DataAaTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        this.setStyle("-fx-pref-width: 60;-fx-text-overrun: center-ellipsis;");
                        if (!empty) {
                            int rowIndex = this.getIndex() + 1;
                            this.setText(String.valueOf(rowIndex));
                        }
                    }
                };
                return cell;
            });
            //site;//检测位置
            siteTableColumn.setCellFactory((col) -> {
                TableCell<DataAaTableDto, String> cell = new TableCell<DataAaTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        this.setStyle("-fx-min-width: 300;-fx-text-fill: #000;-fx-underline: false;");
                        if (!empty) {
                            DataAaTableDto dataAaTableDto = this.getTableView().getItems().get(this.getIndex());
                            this.setText(dataAaTableDto.getSite());
                        }
                    }
                };
                return cell;
            });
            //AA巡检最大值
            xjzdzTableColumn.setCellFactory((col) -> {
                TableCell<DataAaTableDto, String> cell = new TableCell<DataAaTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        this.setStyle("-fx-min-width: 300;-fx-text-fill: #000;-fx-underline: false;");
                        if (!empty) {
                            DataAaTableDto dataAaTableDto = this.getTableView().getItems().get(this.getIndex());
                            Float fz = 0f;
                            if (dataAaTableDto.getXjList() != null && dataAaTableDto.getXjList().size() > 0) {
                                for (DataTableDto dto : dataAaTableDto.getXjList()) {
                                    if (fz < dto.getFz()) {
                                        fz = dto.getFz();
                                    }
                                }
                            }
                            this.setText("" + fz);
                        }
                    }
                };
                return cell;
            });
            //xj;//AA巡检有效值
            xjyxzTableColumn.setCellFactory((col) -> {
                TableCell<DataAaTableDto, String> cell = new TableCell<DataAaTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        this.setStyle("-fx-text-fill: #000;-fx-underline: false;");
                        if (!empty) {
                            DataAaTableDto dataAaTableDto = this.getTableView().getItems().get(this.getIndex());
                            Float yxz = 0f;
                            if (dataAaTableDto.getXjList() != null && dataAaTableDto.getXjList().size() > 0) {
                                for (DataTableDto dto : dataAaTableDto.getXjList()) {
                                    if (yxz < dto.getYxz()) {
                                        yxz = dto.getYxz();
                                    }
                                }
                            }
                            this.setText("" + yxz);
                        }
                    }
                };
                return cell;
            });
            //xj;//AA巡检F1
            xjf1fzTableColumn.setCellFactory((col) -> {
                TableCell<DataAaTableDto, String> cell = new TableCell<DataAaTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        this.setStyle("-fx-text-fill: #000;-fx-underline: false;");
                        if (!empty) {
                            DataAaTableDto dataAaTableDto = this.getTableView().getItems().get(this.getIndex());
                            Float f1 = 0f;
                            if (dataAaTableDto.getXjList() != null && dataAaTableDto.getXjList().size() > 0) {
                                for (DataTableDto dto : dataAaTableDto.getXjList()) {
                                    if (f1 < dto.getF1()) {
                                        f1 = dto.getF1();
                                    }
                                }
                            }
                            this.setText("" + f1);
                        }
                    }
                };
                return cell;
            });
            //xj;//AA巡检F2
            xjf2fzTableColumn.setCellFactory((col) -> {
                TableCell<DataAaTableDto, String> cell = new TableCell<DataAaTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        this.setStyle("-fx-text-fill: #000;-fx-underline: false;");
                        if (!empty) {
                            DataAaTableDto dataAaTableDto = this.getTableView().getItems().get(this.getIndex());
                            Float f2 = 0f;
                            if (dataAaTableDto.getXjList() != null && dataAaTableDto.getXjList().size() > 0) {
                                for (DataTableDto dto : dataAaTableDto.getXjList()) {
                                    if (f2 < dto.getF2()) {
                                        f2 = dto.getF2();
                                    }
                                }
                            }
                            this.setText("" + f2);
                        }
                    }
                };
                return cell;
            });
            //xjjt;//AA巡检截图
            xjjtTableColumn.setCellFactory((col) -> {
                TableCell<DataAaTableDto, String> cell = new TableCell<DataAaTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        if (!empty) {
                            DataAaTableDto dataAaTableDto = this.getTableView().getItems().get(this.getIndex());
                            loadImageDialog(testTechnology, 1, this, "巡检截图", dataAaTableDto.getXjList());
                        }
                    }
                };
                return cell;
            });
            //tj://AA巡检录音
            xjlyTableColumn.setCellFactory((col) -> {
                TableCell<DataAaTableDto, String> cell = new TableCell<DataAaTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        if (!empty) {
                            DataAaTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                            loadSoundDialog(this, "录音", tableDto.getXjList());
                        }
                    }
                };
                return cell;
            });

            //tj;//AA统计检测文件
            tjTableColumn.setCellFactory((col) -> {
                TableCell<DataAaTableDto, String> cell = new TableCell<DataAaTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        if (!empty) {
                            DataAaTableDto dataAaTableDto = this.getTableView().getItems().get(this.getIndex());
                            loadTjDialog(testTechnology, this, dataDeviceDto.getDeviceName() + "," + dataAaTableDto.getSite(), "AA文件", dataAaTableDto.getTjList());
                        }
                    }
                };
                return cell;
            });
            //tjjt;//AA统计截图
            tjjtTableColumn.setCellFactory((col) -> {
                TableCell<DataAaTableDto, String> cell = new TableCell<DataAaTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        if (!empty) {
                            DataAaTableDto dataAaTableDto = this.getTableView().getItems().get(this.getIndex());
                            loadImageDialog(testTechnology, 1, this, "统计截图", dataAaTableDto.getTjList());
                        }
                    }
                };
                return cell;
            });
            //tjlb;//AA统计录波
            tjlbTableColumn.setCellFactory((col) -> {
                TableCell<DataAaTableDto, String> cell = new TableCell<DataAaTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        if (!empty) {
                            DataAaTableDto dataAaTableDto = this.getTableView().getItems().get(this.getIndex());
                            loadTjlbDialog(testTechnology, this, dataDeviceDto.getDeviceName() + "," + dataAaTableDto.getSite(), "统计录波", dataAaTableDto.getTjlbList());
                        }
                    }
                };
                return cell;
            });
            //fxjt;//AA飞行截图
            fxjtTableColumn.setCellFactory((col) -> {
                TableCell<DataAaTableDto, String> cell = new TableCell<DataAaTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        if (!empty) {
                            DataAaTableDto dataAaTableDto = this.getTableView().getItems().get(this.getIndex());
                            loadImageDialog(testTechnology, 1, this, "飞行截图", dataAaTableDto.getFxList());
                        }
                    }
                };
                return cell;
            });
            //bxtj;//AA波形统计截图
            bxjtTableColumn.setCellFactory((col) -> {
                TableCell<DataAaTableDto, String> cell = new TableCell<DataAaTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        if (!empty) {
                            DataAaTableDto dataAaTableDto = this.getTableView().getItems().get(this.getIndex());
                            loadImageDialog(testTechnology, 1, this, "波形截图", dataAaTableDto.getBxList());
                        }
                    }
                };
                return cell;
            });

            //kjg;//可见光照片
            kjgTableColumn.setCellFactory((col) -> {
                TableCell<DataAaTableDto, String> cell = new TableCell<DataAaTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        if (!empty) {
                            DataAaTableDto dataAaTableDto = this.getTableView().getItems().get(this.getIndex());
                            loadImageDialog(testTechnology, 2, this, "可见光照片", dataAaTableDto.getKjgzpList());
                        }
                    }
                };
                return cell;
            });
            //fh;//负荷电流
            fhTableColumn.setCellFactory((col) -> {
                TableCell<DataAaTableDto, String> cell = new TableCell<DataAaTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        if (!empty) {
                            DataAaTableDto dataAaTableDto = this.getTableView().getItems().get(this.getIndex());
                            this.setText(dataAaTableDto.getFh() != null ? "" + dataAaTableDto.getFh() : "--");
                            this.setOnMouseClicked((me) -> {
                                log.debug("负荷电流==>{}", dataAaTableDto.getFh());
                            });
                        }
                    }
                };
                return cell;
            });
        } catch (Exception e) {
            log.error("系统异常!", e);
            ialert.error(I18nUtil.get("error.sys") + e.getMessage());
        }
    }

    @Override
    public void dispose() {

    }
}