package com.redphase.controller.atlas.data;

import com.alibaba.fastjson.JSONObject;
import com.redphase.controller.atlas.AtlasBaseController;
import com.redphase.dto.atlas.DataAnalyzeDto;
import com.redphase.dto.atlas.DataDeviceDto;
import com.redphase.dto.atlas.DataDeviceSiteDto;
import com.redphase.dto.atlas.DataLcDto;
import com.redphase.dto.table.DataTevSiteTableDto;
import com.redphase.dto.atlas.tev.DataTevTjDto;
import com.redphase.dto.atlas.tev.DataTevTjlbDto;
import com.redphase.dto.atlas.tev.DataTevXjDto;
import com.redphase.dto.atlas.tev.DataTevXjlhDto;
import com.redphase.framework.util.CommonConstant;
import com.redphase.framework.util.StrUtil;
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
public class AtlasDataTEVSiteInfoController extends AtlasBaseController implements Initializable {

    @FXML
    Label deviceNameL;//设备名称

    @FXML
    TableView<DataTevSiteTableDto> tableView;//检测位置列表

    @FXML
    TableColumn<DataTevSiteTableDto, String> idTableColumn;//序号
    @FXML
    TableColumn<DataTevSiteTableDto, String> jcwzTableColumn;//检测位置
    @FXML
    TableColumn<DataTevSiteTableDto, String> tevxjfzTableColumn;//TEV巡检幅值
    @FXML
    TableColumn<DataTevSiteTableDto, String> tevxjjcjtTableColumn;//TEV巡检截图
    @FXML
    TableColumn<DataTevSiteTableDto, String> lhxjtevTableColumn;//联合巡检TEV幅值
    @FXML
    TableColumn<DataTevSiteTableDto, String> lhxjaaTableColumn;//联合巡检AA幅值
    @FXML
    TableColumn<DataTevSiteTableDto, String> lhxjjcjtTableColumn;//联合巡检截图
    @FXML
    TableColumn<DataTevSiteTableDto, String> lhxjlyTableColumn;//联合巡检录音
    @FXML
    TableColumn<DataTevSiteTableDto, String> tevtjjcTableColumn;//TEV统计检测
    @FXML
    TableColumn<DataTevSiteTableDto, String> tevtjjcjtTableColumn;//TEV统计截图
    @FXML
    TableColumn<DataTevSiteTableDto, String> tevtjlbTableColumn;//录波
    @FXML
    TableColumn<DataTevSiteTableDto, String> zpTableColumn;//照片

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize...");
    }

    String testTechnology = "TEV";

    /**
     * 加载位置检测数据
     */
    public void loadData() {
        try {
            DataDeviceDto subDataDeviceDto = (DataDeviceDto) ehcacheUtil.getCache(CommonConstant.ATLAS_DATA_SKILL_DEVICE_SITE);
            deviceNameL.setText(subDataDeviceDto.getDeviceName());

            //位置信息
            List<DataDeviceSiteDto> dataDeviceSiteList = dataDeviceSiteService.findDataIsList(new DataDeviceSiteDto() {{
                setDataDeviceId(subDataDeviceDto.getId());
            }});

            //检测位置列表数据
            ObservableList<DataTevSiteTableDto> dataList = FXCollections.observableArrayList();
            for (DataDeviceSiteDto dataDeviceSiteDto : dataDeviceSiteList) {
                DataTevSiteTableDto siteDataTevSiteTableDto = new DataTevSiteTableDto();
                siteDataTevSiteTableDto.setDataDeviceDto(subDataDeviceDto);
                siteDataTevSiteTableDto.setDataDeviceSiteDto(dataDeviceSiteDto);
                siteDataTevSiteTableDto.setSite(dataDeviceSiteDto.getSiteName()); //检测位置
                siteDataTevSiteTableDto.setFddj(subDataDeviceDto.getVoltageLevel() + "kV");//电压等级；
                siteDataTevSiteTableDto.setBcsb(subDataDeviceDto.getDeviceName() + ""); //被测设备；
                siteDataTevSiteTableDto.setXjList(new ArrayList<>());//巡检文件列表
                siteDataTevSiteTableDto.setLhxjList(new ArrayList<>());//联合巡检文件列表
                siteDataTevSiteTableDto.setTjList(new ArrayList<>());//统计文件列表
                siteDataTevSiteTableDto.setTjlbList(new ArrayList<>());//录波文件列表
                siteDataTevSiteTableDto.setKjgzpList(new ArrayList<>());//可将光文件列表
                int seqNo = 0;
                //获取当前位置所有检测文件
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
                                siteDataTevSiteTableDto.setFh(dataLcDto.getX4());
                                break;
                            }
                            case "HJ": {
                                break;
                            }
                            case "TEV_XJ": {
                                DataTevSiteTableDto xjDataTableDto = new DataTevSiteTableDto() {{
                                    setDataDeviceDto(siteDataTevSiteTableDto.getDataDeviceDto());
                                    setDataDeviceSiteDto(siteDataTevSiteTableDto.getDataDeviceSiteDto());
                                }};
                                //TEV巡检检测文件
                                xjDataTableDto.setFileUrl(analyzeDto.getFileUrl());
                                //TEV巡检截图
                                xjDataTableDto.setScreenshots(analyzeDto.getScreenshots());
                                //巡检录音
                                xjDataTableDto.setAudios(analyzeDto.getAudios());
                                DataTevXjDto dataTevXjDto = dataTevXjService.findDataById(new DataTevXjDto() {{
                                    setDataAnalyzeId(analyzeDto.getId());
                                }});
                                if (dataTevXjDto != null) {
                                    xjDataTableDto.setCssj("" + dataTevXjDto.getX3());//测试时间；
                                    xjDataTableDto.setCkyz("" + dataTevXjDto.getX12());//参考阈值；
                                    xjDataTableDto.setTbfs("" + (dataTevXjDto.getX17() == 1 ? "外同步" : "内同步"));//同步方式 同步类型外同步:0x01内同步:0x02默认写入内同步
                                    xjDataTableDto.setTbpl("" + dataTevXjDto.getX18());//同步频率；
                                    xjDataTableDto.setXtpl("" + dataTevXjDto.getX16());//系统频率；
                                    xjDataTableDto.setFz(dataTevXjDto.getX5());//幅值
                                    xjDataTableDto.setZdz(dataTevXjDto.getX6());//最大值
                                    if (siteDataTevSiteTableDto.getFz() == null || siteDataTevSiteTableDto.getFz() < dataTevXjDto.getX5()) {
                                        siteDataTevSiteTableDto.setFz(dataTevXjDto.getX5());//幅值
                                    }
                                    siteDataTevSiteTableDto.getXjList().add(xjDataTableDto);
                                    //-----------
                                    if (siteDataTevSiteTableDto.getKjgzpList().size() == 0) {
                                        xjDataTableDto.setPhotos(analyzeDto.getPhotos());
                                        siteDataTevSiteTableDto.getKjgzpList().add(xjDataTableDto);
                                    }
                                }
                                break;
                            }
                            case "TEV_XJL": {
                                DataTevSiteTableDto lhxjDataTableDto = new DataTevSiteTableDto() {{
                                    setDataDeviceDto(siteDataTevSiteTableDto.getDataDeviceDto());
                                    setDataDeviceSiteDto(siteDataTevSiteTableDto.getDataDeviceSiteDto());
                                }};
                                //TEV巡检检测文件
                                lhxjDataTableDto.setFileUrl(analyzeDto.getFileUrl());
                                //TEV巡检截图
                                lhxjDataTableDto.setScreenshots(analyzeDto.getScreenshots());
                                //巡检录音
                                lhxjDataTableDto.setAudios(analyzeDto.getAudios());
                                DataTevXjlhDto dataTevXjlhDto = dataTevXjlhService.findDataById(new DataTevXjlhDto() {{
                                    setDataAnalyzeId(analyzeDto.getId());
                                }});
                                if (dataTevXjlhDto != null) {
                                    lhxjDataTableDto.setCssj("" + dataTevXjlhDto.getX3());//测试时间；
                                    lhxjDataTableDto.setCkyz("" + dataTevXjlhDto.getX12());//参考阈值；
                                    lhxjDataTableDto.setTbfs("" + (dataTevXjlhDto.getX17() == 1 ? "外同步" : "内同步"));//同步方式 同步类型外同步:0x01内同步:0x02默认写入内同步
                                    lhxjDataTableDto.setTbpl("" + dataTevXjlhDto.getX18());//同步频率；
                                    lhxjDataTableDto.setXtpl("" + dataTevXjlhDto.getX16());//系统频率；
                                    lhxjDataTableDto.setFz(dataTevXjlhDto.getX5());
                                    siteDataTevSiteTableDto.setLhxjTevfz(dataTevXjlhDto.getX5());
                                    siteDataTevSiteTableDto.setLhxjAafz(dataTevXjlhDto.getX19());
                                    siteDataTevSiteTableDto.getLhxjList().add(lhxjDataTableDto);
                                    //-----------
                                    if (siteDataTevSiteTableDto.getKjgzpList().size() == 0) {
                                        lhxjDataTableDto.setPhotos(analyzeDto.getPhotos());
                                        siteDataTevSiteTableDto.getKjgzpList().add(lhxjDataTableDto);
                                    }
                                }
                                break;
                            }
                            case "TEV_XJZS": {
                                break;
                            }
                            case "TEV_TJ": {
                                DataTevSiteTableDto tjDataTableDto = new DataTevSiteTableDto() {{
                                    setDataDeviceDto(siteDataTevSiteTableDto.getDataDeviceDto());
                                    setDataDeviceSiteDto(siteDataTevSiteTableDto.getDataDeviceSiteDto());
                                }};
                                //TEV统计检测文件
                                tjDataTableDto.setFileUrl(analyzeDto.getFileUrl());
                                //TEV统计截图
                                tjDataTableDto.setScreenshots(analyzeDto.getScreenshots());
                                DataTevTjDto dataTevTjDto = dataTevTjService.findDataById(new DataTevTjDto() {{
                                    setDataAnalyzeId(analyzeDto.getId());
                                }});
                                if (dataTevTjDto != null) {
                                    tjDataTableDto.setCssj("" + dataTevTjDto.getX14());//测试时间；
                                    tjDataTableDto.setXwwy("" + dataTevTjDto.getX19());//相位位移；
                                    tjDataTableDto.setCkyz("" + dataTevTjDto.getX18());//参考阈值；
                                    tjDataTableDto.setTbfs("" + (new Integer(dataTevTjDto.getX20()) == 1 ? "外同步" : "内同步"));//同步方式 同步类型外同步:0x01内同步:0x02默认写入内同步
                                    tjDataTableDto.setTbpl("" + dataTevTjDto.getX21());//同步频率；
                                    tjDataTableDto.setXtpl("" + dataTevTjDto.getX22());//系统频率；
                                    tjDataTableDto.setFz(dataTevTjDto.getX16());//幅值；
                                    tjDataTableDto.setMc("" + dataTevTjDto.getX17());//脉冲；
                                    tjDataTableDto.setYz("" + dataTevTjDto.getX18());//阈值；
                                    tjDataTableDto.setAtlasArr(JSONObject.parseObject(dataTevTjDto.getX11(), Float[][].class));
                                    siteDataTevSiteTableDto.getTjList().add(tjDataTableDto);
                                    //-----------
                                    if (siteDataTevSiteTableDto.getKjgzpList().size() == 0) {
                                        tjDataTableDto.setPhotos(analyzeDto.getPhotos());
                                        siteDataTevSiteTableDto.getKjgzpList().add(tjDataTableDto);
                                    }
                                }
                                break;
                            }
                            case "TEV_TJZS": {
                                break;
                            }
                            case "TEV_TJLB": {
                                //TEV统计录波
                                DataTevSiteTableDto tjlbDataTableDto = new DataTevSiteTableDto() {{
                                    setDataDeviceDto(siteDataTevSiteTableDto.getDataDeviceDto());
                                    setDataDeviceSiteDto(siteDataTevSiteTableDto.getDataDeviceSiteDto());
                                }};
                                DataTevTjlbDto dataTevTjlbDto = dataTevTjlbService.findDataById(new DataTevTjlbDto() {{
                                    setDataAnalyzeId(analyzeDto.getId());
                                }});
                                if (dataTevTjlbDto != null) {
                                    tjlbDataTableDto.setSeqNo(seqNo++);
                                    tjlbDataTableDto.setCssj("" + dataTevTjlbDto.getX3());//测试时间；
                                    tjlbDataTableDto.setXwwy("" + dataTevTjlbDto.getX13());//相位位移；
                                    tjlbDataTableDto.setCkyz("" + dataTevTjlbDto.getX12());//参考阈值；
                                    tjlbDataTableDto.setTbfs("" + (new Integer(dataTevTjlbDto.getX14()) == 1 ? "外同步" : "内同步"));//同步方式 同步类型外同步:0x01内同步:0x02默认写入内同步
                                    tjlbDataTableDto.setTbpl("" + dataTevTjlbDto.getX15());//同步频率；
                                    tjlbDataTableDto.setXtpl("" + dataTevTjlbDto.getX16());//系统频率；
                                    tjlbDataTableDto.setAtlasArr(JSONObject.parseObject(dataTevTjlbDto.getX11(), Float[][].class));
                                    siteDataTevSiteTableDto.getTjlbList().add(tjlbDataTableDto);
                                    //-----------
                                    if (siteDataTevSiteTableDto.getKjgzpList().size() == 0) {
                                        tjlbDataTableDto.setPhotos(analyzeDto.getPhotos());
                                        siteDataTevSiteTableDto.getKjgzpList().add(tjlbDataTableDto);
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
                dataList.add(siteDataTevSiteTableDto);
            }
            tableView.setItems(dataList);

            //序号
            idTableColumn.setCellFactory(column -> new TableCell<DataTevSiteTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        int rowIndex = this.getIndex() + 1;
                        this.setText(String.valueOf(rowIndex));
                    }
                }
            });

            //检测位置
            jcwzTableColumn.setCellFactory(column -> new TableCell<DataTevSiteTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataTevSiteTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getSite());
                    }
                }
            });

            //TEV巡检幅值
            tevxjfzTableColumn.setCellFactory(column -> new TableCell<DataTevSiteTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataTevSiteTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getFz() == null ? "--" : StrUtil.subZero(tableDto.getFz().toString()));
                    }
                }
            });

            //TEV巡检截图
            tevxjjcjtTableColumn.setCellFactory(column -> new TableCell<DataTevSiteTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataTevSiteTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        loadImageDialog(testTechnology, 1, this, "巡检截图", tableDto.getXjList());
                    }
                }
            });

            //联合巡检TEV幅值
            lhxjtevTableColumn.setCellFactory(column -> new TableCell<DataTevSiteTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataTevSiteTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getLhxjTevfz() == null ? "--" : StrUtil.subZero(tableDto.getLhxjTevfz().toString()));
                    }
                }
            });

            //联合巡检AA幅值
            lhxjaaTableColumn.setCellFactory(column -> new TableCell<DataTevSiteTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataTevSiteTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getLhxjAafz() == null ? "--" : StrUtil.subZero(tableDto.getLhxjAafz().toString()));
                    }
                }
            });

            //联合巡检截图
            lhxjjcjtTableColumn.setCellFactory(column -> new TableCell<DataTevSiteTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataTevSiteTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        loadImageDialog(testTechnology, 1, this, "巡检截图", tableDto.getLhxjList());
                    }
                }
            });

            //联合巡检录音
            lhxjlyTableColumn.setCellFactory(column -> new TableCell<DataTevSiteTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataTevSiteTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        loadSoundDialog(this, "录音", tableDto.getLhxjList());
                    }
                }
            });

            //TEV统计检测文件
            tevtjjcTableColumn.setCellFactory(column -> new TableCell<DataTevSiteTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataTevSiteTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        loadTjDialog(testTechnology, this, subDataDeviceDto.getDeviceName() + "," + tableDto.getSite(), "TEV文件", tableDto.getTjList());
                    }
                }
            });

            //TEV统计截图
            tevtjjcjtTableColumn.setCellFactory(column -> new TableCell<DataTevSiteTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataTevSiteTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        loadImageDialog(testTechnology, 1, this, "统计截图", tableDto.getTjList());
                    }
                }
            });

            //录波
            tevtjlbTableColumn.setCellFactory(column -> new TableCell<DataTevSiteTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataTevSiteTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        loadTjlbDialog(testTechnology, this, subDataDeviceDto.getDeviceName() + "," + tableDto.getSite(), "统计录波", tableDto.getTjlbList());
                    }
                }
            });

            //照片
            zpTableColumn.setCellFactory(column -> new TableCell<DataTevSiteTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataTevSiteTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        loadImageDialog(testTechnology, 2, this, "可见光照片", tableDto.getKjgzpList());
                    }
                }
            });
        } catch (Exception e) {
            log.error("加载TEV检测位置信息出错！", e);
        }
    }


    @Override
    public void dispose() {

    }

}