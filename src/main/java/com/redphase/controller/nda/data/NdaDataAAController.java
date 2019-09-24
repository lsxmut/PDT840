package com.redphase.controller.nda.data;

import com.redphase.controller.nda.NdaBaseController;
import com.redphase.dto.account.AccountDto;
import com.redphase.dto.atlas.*;
import com.redphase.dto.atlas.aa.DataAaTjzsDto;
import com.redphase.dto.atlas.aa.DataAaXjDto;
import com.redphase.dto.atlas.aa.DataAaXjzsDto;
import com.redphase.dto.sys.SysVariableDto;
import com.redphase.dto.table.DataAaTableDto;
import com.redphase.dto.table.DataTableDto;
import com.redphase.dto.world.aa.AaDataDto;
import com.redphase.dto.world.aa.AaListDto;
import com.redphase.framework.util.DateUtil;
import com.redphase.framework.util.I18nUtil;
import com.redphase.framework.util.ListUtil;
import com.redphase.framework.util.StrUtil;
import com.redphase.framework.util.word.AaWordUtil;
import com.redphase.view.nda.data.NdaDataAASiteInfoView;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@FXMLController
@Slf4j
public class NdaDataAAController extends NdaBaseController implements Initializable {
    @FXML
    BorderPane dataBorderPane;
    @FXML
    Label fullNameL;
//    @FXML
//    Pane buttonPane;//
//    @FXML
//    Button reportButton;//
    @FXML
    Label bdzL;//变电站名称
    @FXML
    Label dydjL;//电压等级
    @FXML
    Label tqqkL;//天气情况
    @FXML
    Label wdL;//温度
    @FXML
    Label sdL;//湿度
    @FXML
    Label xjjszs1L;//巡检金属噪声1(dB)
    @FXML
    Label xjjszs1PicL;//巡检金属噪声1(dB)截图
    List xjjszs1PicList;
    @FXML
    Label xjjszs2L;//巡检金属噪声2(dB)
    @FXML
    Label xjjszs2PicL;//巡检金属噪声2(dB)截图
    List xjjszs2PicList;
    @FXML
    Label xjjszs3L;//巡检金属噪声3(dB)
    @FXML
    Label xjjszs3PicL;//巡检金属噪声3(dB)截图
    List xjjszs3PicList;
    @FXML
    Label tjjszs1L;//统计金属噪声1(dB)
    @FXML
    Label tjjszs1PicL;//统计金属噪声1(dB)截图
    List tjjszs1PicList;
    @FXML
    Label tjjszs2L;//统计金属噪声2(dB)
    @FXML
    Label tjjszs2PicL;//统计金属噪声2(dB)截图
    List tjjszs2PicList;
    @FXML
    Label tjjszs3L;//统计金属噪声3(dB)
    @FXML
    Label tjjszs3PicL;//统计金属噪声3(dB)截图
    List tjjszs3PicList;
    @FXML
    Label dqzsL;//大气噪声
    @FXML
    Label jcryL;//检测人员
    @FXML
    Label jcrqL;//检测日期
    @FXML
    TableView<DataAaTableDto> deviceTableView;//检测设备列表
    @FXML
    TableColumn<DataAaTableDto, String> idTableColumn;//序号
    @FXML
    TableColumn<DataAaTableDto, String> deviceNameTableColumn;//开关柜名称
    @FXML
    TableColumn<DataAaTableDto, String> qzfdTableColumn;//前中幅度
    @FXML
    TableColumn<DataAaTableDto, String> qzcsTableColumn;//前中次数
    @FXML
    TableColumn<DataAaTableDto, String> qxfdTableColumn;//前下幅度
    @FXML
    TableColumn<DataAaTableDto, String> qxcsTableColumn;//前下次数
    @FXML
    TableColumn<DataAaTableDto, String> hsfdTableColumn;//后上幅度
    @FXML
    TableColumn<DataAaTableDto, String> hscsTableColumn;//后上次数
    @FXML
    TableColumn<DataAaTableDto, String> hzfdTableColumn;//后中幅度
    @FXML
    TableColumn<DataAaTableDto, String> hzcsTableColumn;//后中次数
    @FXML
    TableColumn<DataAaTableDto, String> hxfdTableColumn;//后下幅度
    @FXML
    TableColumn<DataAaTableDto, String> hxcsTableColumn;//后下次数
    @FXML
    TableColumn<DataAaTableDto, String> csfdTableColumn;//侧上幅度
    @FXML
    TableColumn<DataAaTableDto, String> cscsTableColumn;//侧上次数
    @FXML
    TableColumn<DataAaTableDto, String> czfdTableColumn;//侧中幅度
    @FXML
    TableColumn<DataAaTableDto, String> czcsTableColumn;//侧中次数
    @FXML
    TableColumn<DataAaTableDto, String> cxfdTableColumn;//侧下幅度
    @FXML
    TableColumn<DataAaTableDto, String> cxcsTableColumn;//侧下次数
    @FXML
    TableColumn<DataAaTableDto, String> csbjtjgTableColumn;//超声波监听结果
    @FXML
    TableColumn<DataAaTableDto, String> fhTableColumn;//负荷电流
    @FXML
    TableColumn<DataAaTableDto, String> kggyxztTableColumn;//开关柜运行状态
    ObservableList<DataAaTableDto> dataList;
    String testTechnology = "AA";
    boolean initFlag = false;
    List<DataDeviceDto> dataDeviceList;
    DataHjDto wdMaxDto;
    DataHjDto sdMaxDto;
    Float zdxjzs;//最大巡检噪声幅值
    //    Float zdxj;//最大巡检幅值
    Set<String> jcrySet = new HashSet<>();
    @Autowired
    private NdaDataAASiteInfoView ndaDataAASiteInfoView;
    @Autowired
    private NdaDataAASiteInfoController ndaDataAASiteInfoController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize...");
        if (initFlag) return;
        initFlag = true;
        dataBorderPane.prefWidthProperty().bind(ndaController.getRightAnchorPane().prefWidthProperty());
        dataBorderPane.prefHeightProperty().bind(ndaController.getRightAnchorPane().prefHeightProperty());
    }

    public void loadAAData(String fullName) {
        clearL(fullNameL, bdzL, dydjL, tqqkL, wdL, sdL, xjjszs1L, xjjszs1PicL, xjjszs2L, xjjszs2PicL, xjjszs3L, xjjszs3PicL, tjjszs1L, tjjszs1PicL, tjjszs2L, tjjszs2PicL, tjjszs3L, tjjszs3PicL, dqzsL, jcryL, jcrqL);
        xjjszs1PicList = new ArrayList();
        xjjszs2PicList = new ArrayList();
        xjjszs3PicList = new ArrayList();
        tjjszs1PicList = new ArrayList();
        tjjszs2PicList = new ArrayList();
        tjjszs3PicList = new ArrayList();
        try {
            fullNameL.setText(fullName);
            //设备列表
            dataDeviceList = dataDeviceService.findListByFullName(fullName);
            //主设备目录
            DataDeviceDto mainDataDeviceDto = dataDeviceList.stream().filter(d -> "#".equals(d.getDeviceName())).findFirst().get();
//            if ("1".equals(mainDataDeviceDto.getDeviceType())) {
//                buttonPane.setVisible(true);
//                buttonPane.setManaged(true);
//            } else {
//                buttonPane.setVisible(false);
//                buttonPane.setManaged(false);
//            }
            bdzL.setText(mainDataDeviceDto.getSubstation());//变电站名称
            dydjL.setText(mainDataDeviceDto.getVoltageLevel() + "");//电压等级
            jcrqL.setText(mainDataDeviceDto.getDateDetection());//检测日期

            //查询环境参数数据
            List<DataHjDto> dataHjDtoList = dataHjService.findListByDevice(new HashMap() {{
                put("dateDetection", mainDataDeviceDto.getDateDetection());
                put("testTechnology", mainDataDeviceDto.getTestTechnology());
                put("electricBureau", mainDataDeviceDto.getElectricBureau());
                put("substation", mainDataDeviceDto.getSubstation());
                put("voltageLevel", mainDataDeviceDto.getVoltageLevel());
                put("deviceType", mainDataDeviceDto.getDeviceType());
//                put("deviceName", mainDataDeviceDto.getDeviceName());
            }});
            if (dataHjDtoList != null) {
                //温度
                wdMaxDto = dataHjDtoList.stream().max(Comparator.comparing(DataHjDto::getX4)).orElse(new DataHjDto());
                wdL.setText("" + wdMaxDto.getX4());
                //湿度
                sdMaxDto = dataHjDtoList.stream().max(Comparator.comparing(DataHjDto::getX5)).orElse(new DataHjDto());
                sdL.setText("" + sdMaxDto.getX5());
                //天气情况
                tqqkL.setText(dataHjService.getWeather(wdMaxDto.getX3()));
                jcrySet.clear();
                jcrySet.add(wdMaxDto.getX6());
                jcrySet.add(wdMaxDto.getX7());
                jcrySet.add(wdMaxDto.getX8());
                jcrySet.add(wdMaxDto.getX9());
                jcrySet.add(wdMaxDto.getX10());
                jcryL.setText(jcrySet.stream().filter(s -> StringUtils.isNotEmpty(s)).collect(Collectors.joining(",")));//检测人员
            }


            List<DataAnalyzeDto> dataAnalyzeDtoXjzsList = dataAnalyzeService.findListByDevice(new HashMap() {{
                put("dateDetection", mainDataDeviceDto.getDateDetection());
                put("testTechnology", mainDataDeviceDto.getTestTechnology());
                put("electricBureau", mainDataDeviceDto.getElectricBureau());
                put("substation", mainDataDeviceDto.getSubstation());
                put("voltageLevel", mainDataDeviceDto.getVoltageLevel());
                put("deviceType", mainDataDeviceDto.getDeviceType());
                put("dataFormat", "AA_XJZS");
            }});
            if (dataAnalyzeDtoXjzsList != null) {
                List<DataAaXjzsDto> dataAaXjzsDtoList = dataAaXjzsService.findDataByIds(dataAnalyzeDtoXjzsList.stream().map(a -> a.getId().toString()).collect(Collectors.joining(",")));
                if (dataAaXjzsDtoList != null) {
                    //巡检噪声
                    DataAaXjzsDto dataAaXjzsDtoMax = dataAaXjzsDtoList.stream().max(Comparator.comparing(DataAaXjzsDto::getX5)).orElse(null);
                    if (dataAaXjzsDtoMax != null) {
                        zdxjzs = dataAaXjzsDtoMax.getX5();
                        DataAnalyzeDto dataAnalyzeDtoMax = dataAnalyzeDtoXjzsList.stream().filter(analyzeDto -> analyzeDto.getId().equals(dataAaXjzsDtoMax.getDataAnalyzeId())).findFirst().orElse(new DataAnalyzeDto());
                        if (dataAnalyzeDtoMax != null) {
                            DataTableDto dataTableDto = new DataTableDto();
                            dataTableDto.setDataDeviceDto(mainDataDeviceDto);
                            dataTableDto.setFddj(mainDataDeviceDto.getVoltageLevel() + "kV");//电压等级；
                            dataTableDto.setBcsb(mainDataDeviceDto.getDeviceName()); //被测设备；
                            dataTableDto.setFileUrl(dataAnalyzeDtoMax.getFileUrl());
                            dataTableDto.setScreenshots(dataAnalyzeDtoMax.getScreenshots());
                            dataTableDto.setAudios(dataAnalyzeDtoMax.getAudios());

                            dataTableDto.setCssj(StrUtil.asString(dataAaXjzsDtoMax.getX3()));//测试时间；
                            dataTableDto.setXwwy("--");//相位位移；
                            dataTableDto.setCkyz(StrUtil.asString(dataAaXjzsDtoMax.getX15()));//参考阈值；
                            dataTableDto.setTbpl(StrUtil.asString(dataAaXjzsDtoMax.getX20()));//同步频率；
                            dataTableDto.setXtpl(StrUtil.asString(dataAaXjzsDtoMax.getX19()));//系统频率；
                            dataTableDto.setZdz(dataAaXjzsDtoMax.getX5());//最大值；
                            if (dataAnalyzeDtoMax.getOrderNo() == 0) {
                                xjjszs1L.setText(StrUtil.asString(dataAaXjzsDtoMax.getX5()));
                                xjjszs1PicList.add(dataTableDto);
                                loadImageDialog(testTechnology, 1, xjjszs1PicL, "巡检截图", xjjszs1PicList);
                            } else if (dataAnalyzeDtoMax.getOrderNo() == 1) {
                                xjjszs2L.setText(StrUtil.asString(dataAaXjzsDtoMax.getX5()));
                                xjjszs2PicList.add(dataTableDto);
                                loadImageDialog(testTechnology, 1, xjjszs2PicL, "巡检截图", xjjszs2PicList);
                            } else if (dataAnalyzeDtoMax.getOrderNo() == 2) {
                                xjjszs3L.setText(StrUtil.asString(dataAaXjzsDtoMax.getX5()));
                                xjjszs3PicList.add(dataTableDto);
                                loadImageDialog(testTechnology, 1, xjjszs3PicL, "巡检截图", xjjszs3PicList);
                            }
                        }
                    }
                }
            }

//            List<DataAnalyzeDto> dataAnalyzeDtoXjList = dataAnalyzeService.findListByDevice(new HashMap() {{
//                put("dateDetection", mainDataDeviceDto.getDateDetection());
//                put("testTechnology", mainDataDeviceDto.getTestTechnology());
//                put("electricBureau", mainDataDeviceDto.getElectricBureau());
//                put("substation", mainDataDeviceDto.getSubstation());
//                put("voltageLevel", mainDataDeviceDto.getVoltageLevel());
//                put("deviceType", mainDataDeviceDto.getDeviceType());
//                put("dataFormat", "AA_XJ");
//            }});
//            if (dataAnalyzeDtoXjList != null) {
//                List<DataAaXjDto> dataAaXjDtoList = dataAaXjService.findDataByIds(dataAnalyzeDtoXjList.stream().map(a -> a.getId().toString()).collect(Collectors.joining(",")));
//                if (dataAaXjDtoList != null) {
//                    //巡检
//                    DataAaXjDto dataAaXjDtoMax = dataAaXjDtoList.stream().max(Comparator.comparing(DataAaXjDto::getX5)).orElse(null);
//                    if (dataAaXjDtoMax != null) {
//                        zdxj = dataAaXjDtoMax.getX5();
//                    }
//                }
//            }

            List<DataAnalyzeDto> dataAnalyzeDtoTjzsList = dataAnalyzeService.findListByDevice(new HashMap() {{
                put("dateDetection", mainDataDeviceDto.getDateDetection());
                put("testTechnology", mainDataDeviceDto.getTestTechnology());
                put("electricBureau", mainDataDeviceDto.getElectricBureau());
                put("substation", mainDataDeviceDto.getSubstation());
                put("voltageLevel", mainDataDeviceDto.getVoltageLevel());
                put("deviceType", mainDataDeviceDto.getDeviceType());
                put("dataFormat", "AA_TJZS");
            }});
            if (dataAnalyzeDtoTjzsList != null) {
                List<DataAaTjzsDto> dataAaTjzsDtoList = dataAaTjzsService.findDataByIds(dataAnalyzeDtoTjzsList.stream().map(a -> a.getId().toString()).collect(Collectors.joining(",")));
                if (dataAaTjzsDtoList != null) {
                    //统计噪声
                    DataAaTjzsDto dataAaTjzsDtoMax = dataAaTjzsDtoList.stream().max(Comparator.comparing(DataAaTjzsDto::getX5)).orElse(null);
                    if (dataAaTjzsDtoMax != null) {
                        DataAnalyzeDto dataAnalyzeDtoMax = dataAnalyzeDtoTjzsList.stream().filter(analyzeDto -> analyzeDto.getId().equals(dataAaTjzsDtoMax.getDataAnalyzeId())).findFirst().orElse(null);
                        if (dataAnalyzeDtoMax != null) {
                            DataTableDto dataTableDto = new DataTableDto();
                            dataTableDto.setDataDeviceDto(mainDataDeviceDto);
                            dataTableDto.setFddj(mainDataDeviceDto.getVoltageLevel() + "kV");//电压等级；
                            dataTableDto.setBcsb(mainDataDeviceDto.getDeviceName()); //被测设备；
                            dataTableDto.setFileUrl(dataAnalyzeDtoMax.getFileUrl());
                            dataTableDto.setScreenshots(dataAnalyzeDtoMax.getScreenshots());
                            dataTableDto.setAudios(dataAnalyzeDtoMax.getAudios());
                            dataTableDto.setCssj(StrUtil.asString(dataAaTjzsDtoMax.getX14()));//测试时间；
                            dataTableDto.setXwwy(StrUtil.asString(dataAaTjzsDtoMax.getX19()));//相位位移；
                            dataTableDto.setCkyz(StrUtil.asString(dataAaTjzsDtoMax.getX18()));//参考阈值；
                            dataTableDto.setFdzy(StrUtil.asString(dataAaTjzsDtoMax.getX23()));//放大增益；
                            dataTableDto.setTbfs(dataAaTjzsDtoMax.getX20() == 1 ? "外同步" : "内同步");//同步方式 同步类型外同步:0x01内同步:0x02默认写入内同步
                            dataTableDto.setTbpl(StrUtil.asString(dataAaTjzsDtoMax.getX21()));//同步频率；
                            dataTableDto.setXtpl(StrUtil.asString(dataAaTjzsDtoMax.getX22()));//系统频率；
                            if (dataAnalyzeDtoMax.getOrderNo() == 0) {
                                tjjszs1L.setText(StrUtil.asString(dataAaTjzsDtoMax.getX16()));
                                tjjszs1PicList.add(dataTableDto);
                                loadImageDialog(testTechnology, 1, tjjszs1PicL, "统计截图", tjjszs1PicList);
                            } else if (dataAnalyzeDtoMax.getOrderNo() == 1) {
                                tjjszs2L.setText(StrUtil.asString(dataAaTjzsDtoMax.getX16()));
                                tjjszs2PicList.add(dataTableDto);
                                loadImageDialog(testTechnology, 1, tjjszs2PicL, "统计截图", tjjszs2PicList);
                            } else if (dataAnalyzeDtoMax.getOrderNo() == 2) {
                                tjjszs3L.setText(StrUtil.asString(dataAaTjzsDtoMax.getX16()));
                                tjjszs3PicList.add(dataTableDto);
                                loadImageDialog(testTechnology, 1, tjjszs3PicL, "统计截图", tjjszs3PicList);
                            }
                        }
                    }
                }
            }

            //检测设备列表数据
            dataList = FXCollections.observableArrayList();
            for (DataDeviceDto dataDeviceDto : dataDeviceList) {
                if ("#".equals(dataDeviceDto.getDeviceName())) {
                    continue;
                }
                DataAaTableDto tableDto = new DataAaTableDto() {{
                    setDataDeviceDto(dataDeviceDto);
                }};
                //位置信息
                List<DataDeviceSiteDto> dataDeviceSiteList = dataDeviceSiteService.findDataIsList(new DataDeviceSiteDto() {{
                    setDataDeviceId(dataDeviceDto.getId());
                }});
                for (DataDeviceSiteDto dataDeviceSiteDto : dataDeviceSiteList) {
                    //获取当前位置所有检测文件
                    List<DataAnalyzeDto> analyzeDtos = dataAnalyzeService.findDataIsList(new DataAnalyzeDto() {{
                        setDataDeviceSiteId(dataDeviceSiteDto.getId());
                    }});
                    //过滤出当前位置AA巡检检测文件
                    List<DataAnalyzeDto> xjAnalyzeList = analyzeDtos.stream().filter(a -> "AA_XJ".equals(a.getDataFormat())).collect(Collectors.toList());
                    //查询当前位置AA巡检检测数据
                    List<DataAaXjDto> aaXjList = dataAaXjService.findDataByIds(xjAnalyzeList.stream().map(a -> a.getId().toString()).collect(Collectors.joining(",")));
                    //过滤出最大的幅度、放电频度
                    DataAaXjDto maxFzDto = aaXjList.stream().max(Comparator.comparing(DataAaXjDto::getX5)).orElse(new DataAaXjDto());
                    DataAaXjDto maxFdDto = aaXjList.stream().max(Comparator.comparing(DataAaXjDto::getX6)).orElse(new DataAaXjDto());
                    //根据位置名称，设置列表中检测数据的值
                    setSiteData(tableDto, dataDeviceSiteDto.getSiteName(), maxFzDto.getX5(), maxFdDto.getX6());
                    //计算超声波监听结果
                    tableDto.setCsbjtjg(calculateCsbjtjg(aaXjList));
                    //当前位置负荷电流数据检测文件
                    DataAnalyzeDto lcDataAnalyzeDto = analyzeDtos.stream().filter(a -> "LC".equals(a.getDataFormat())).findFirst().orElse(null);
                    if (lcDataAnalyzeDto != null) {
                        //负荷电流
                        DataLcDto dataLcDto = dataLcService.findDataById(new DataLcDto() {{
                            setDataAnalyzeId(lcDataAnalyzeDto.getId());
                        }});
                        tableDto.setFh(dataLcDto.getX4());
                    }
                    //查询台帐，获取开关柜运行状态
                    List<AccountDto> accountDtoList = accountService.queryAccountList(new HashMap() {{
                        put("electricBureau", dataDeviceDto.getElectricBureau());
                        put("substation", dataDeviceDto.getSubstation());
                        put("deviceType", dataDeviceDto.getDeviceType());
                        put("voltageLevel", dataDeviceDto.getVoltageLevel());
                        put("deviceName", dataDeviceDto.getDeviceName());
                    }});
                    if (ListUtil.isNotEmpty(accountDtoList)) {
                        tableDto.setKggyxzt(accountDtoList.get(0).getRunState());
                    }
                }
                dataList.add(tableDto);
            }
            deviceTableView.setItems(dataList);

            //序号
            idTableColumn.setCellFactory(column -> new TableCell<DataAaTableDto, String>() {
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
            });

            //开关柜名称
            deviceNameTableColumn.setCellFactory(column -> new TableCell<DataAaTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    this.setStyle("-fx-text-fill: #31a1ff;-fx-underline: true;-fx-cursor: hand;");
                    if (!empty) {
                        DataAaTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getDataDeviceDto().getDeviceName());
                        this.setOnMouseClicked(e -> {
//                            Double width = Application.getStage().getWidth() - 200;
//                            Double height = Application.getStage().getHeight() - 100;
                            idialog.openDialog(tableDto.getDataDeviceDto().getDeviceName(), ndaDataAASiteInfoView, 1180.0, 700.0, true);
                            ndaDataAASiteInfoController.loadData(tableDto.getDataDeviceDto());
                        });
                    }
                }
            });

            //前中幅度
            qzfdTableColumn.setCellFactory(column -> new TableCell<DataAaTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataAaTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getQzfd() == null ? "--" : StrUtil.subZero(tableDto.getQzfd().toString()));
                    }
                }
            });

            //前中次数
            qzcsTableColumn.setCellFactory(column -> new TableCell<DataAaTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataAaTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getQzcs() == null ? "--" : tableDto.getQzcs().toString());
                    }
                }
            });

            //前下幅度
            qxfdTableColumn.setCellFactory(column -> new TableCell<DataAaTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataAaTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getQxfd() == null ? "--" : StrUtil.subZero(tableDto.getQxfd().toString()));
                    }
                }
            });

            //前下次数
            qxcsTableColumn.setCellFactory(column -> new TableCell<DataAaTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataAaTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getQxcs() == null ? "--" : tableDto.getQxcs().toString());
                    }
                }
            });

            //后上幅度
            hsfdTableColumn.setCellFactory(column -> new TableCell<DataAaTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataAaTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getHsfd() == null ? "--" : StrUtil.subZero(tableDto.getHsfd().toString()));
                    }
                }
            });

            //后上次数
            hscsTableColumn.setCellFactory(column -> new TableCell<DataAaTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataAaTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getHscs() == null ? "--" : tableDto.getHscs().toString());
                    }
                }
            });

            //后中幅度
            hzfdTableColumn.setCellFactory(column -> new TableCell<DataAaTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataAaTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getHzfd() == null ? "--" : StrUtil.subZero(tableDto.getHzfd().toString()));
                    }
                }
            });

            //后中次数
            hzcsTableColumn.setCellFactory(column -> new TableCell<DataAaTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataAaTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getHzcs() == null ? "--" : tableDto.getHzcs().toString());
                    }
                }
            });

            //后下幅度
            hxfdTableColumn.setCellFactory(column -> new TableCell<DataAaTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataAaTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getHxfd() == null ? "--" : StrUtil.subZero(tableDto.getHxfd().toString()));
                    }
                }
            });

            //后下次数
            hxcsTableColumn.setCellFactory(column -> new TableCell<DataAaTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataAaTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getHxcs() == null ? "--" : tableDto.getHxcs().toString());
                    }
                }
            });

            //侧上幅度
            csfdTableColumn.setCellFactory(column -> new TableCell<DataAaTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataAaTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getCsfd() == null ? "--" : StrUtil.subZero(tableDto.getCsfd().toString()));
                    }
                }
            });

            //侧上次数
            cscsTableColumn.setCellFactory(column -> new TableCell<DataAaTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataAaTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getCscs() == null ? "--" : tableDto.getCscs().toString());
                    }
                }
            });

            //侧中幅度
            czfdTableColumn.setCellFactory(column -> new TableCell<DataAaTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataAaTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getCzfd() == null ? "--" : StrUtil.subZero(tableDto.getCzfd().toString()));
                    }
                }
            });

            //侧中次数
            czcsTableColumn.setCellFactory(column -> new TableCell<DataAaTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataAaTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getCzcs() == null ? "--" : tableDto.getCzcs().toString());
                    }
                }
            });

            //侧下幅度
            cxfdTableColumn.setCellFactory(column -> new TableCell<DataAaTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataAaTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getCxfd() == null ? "--" : StrUtil.subZero(tableDto.getCxfd().toString()));
                    }
                }
            });

            //侧下次数
            cxcsTableColumn.setCellFactory(column -> new TableCell<DataAaTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataAaTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getCxcs() == null ? "--" : tableDto.getCxcs().toString());
                    }
                }
            });

            //超声波监听结果
            csbjtjgTableColumn.setCellFactory(column -> new TableCell<DataAaTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataAaTableDto dataAaTableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(dataAaTableDto.getCsbjtjg());
                    }
                }
            });

            //负荷电流
            fhTableColumn.setCellFactory(column -> new TableCell<DataAaTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataAaTableDto dataAaTableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(dataAaTableDto.getFh() == null ? "--" : StrUtil.subZero(dataAaTableDto.getFh().toString()));
                    }
                }
            });

            //开关柜运行状态
            kggyxztTableColumn.setCellFactory(column -> new TableCell<DataAaTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataAaTableDto dataAaTableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(dataAaTableDto.getKggyxzt() == null ? "--" : dataAaTableDto.getKggyxzt());
                    }
                }
            });
        } catch (Exception e) {
            log.error("AA加载出错！", e);
        }
    }

    /**
     * 根据位置名称，设置列表中检测数据的值
     *
     * @param tableDto
     * @param siteName
     * @param fd       幅度
     * @param cs       次数
     */
    private void setSiteData(DataAaTableDto tableDto, String siteName, Float fd, Integer cs) {
        switch (siteName) {
            case "前中":
                tableDto.setQzfd(fd);
                tableDto.setQzcs(cs);
                break;
            case "前下":
                tableDto.setQxfd(fd);
                tableDto.setQxcs(cs);
                break;
            case "后上":
                tableDto.setHsfd(fd);
                tableDto.setHscs(cs);
                break;
            case "后中":
                tableDto.setHzfd(fd);
                tableDto.setHzcs(cs);
                break;
            case "后下":
                tableDto.setHxfd(fd);
                tableDto.setHxcs(cs);
                break;
            case "侧上":
                tableDto.setCsfd(fd);
                tableDto.setCscs(cs);
                break;
            case "侧中":
                tableDto.setCzfd(fd);
                tableDto.setCzcs(cs);
                break;
            case "侧下":
                tableDto.setCxfd(fd);
                tableDto.setCxcs(cs);
                break;
            default:
                break;
        }
    }

    /**
     * 计算超声波监听结果
     *
     * @param aaXjList
     * @return
     */
    private String calculateCsbjtjg(List<DataAaXjDto> aaXjList) {
        if (!ListUtil.isNotEmpty(aaXjList)) {
            return "未检测";
        }
        //各个位置监听结果统计(key-超声波监听结果，value-出现次数)
        Map<Integer, Long> map = aaXjList.stream().collect(Collectors.groupingBy(DataAaXjDto::getX11, Collectors.counting()));
        if (MapUtils.getLongValue(map, 2) > 0) {
            return "异常";
        } else if (MapUtils.getLongValue(map, 1) > 0) {
            return "正常";
        } else if (MapUtils.getLongValue(map, 3) > 0) {
            return "未监听";
        } else {
            return "未检测";
        }
    }

    @FXML
    public void report() {
        log.debug("report..");
        try {
            AccountDto accountDto = accountService.findDataByFullName(new AccountDto() {{
                String[] dirArr = fullNameL.getText().split("_");
                //电业局
                setElectricBureau(dirArr[0]);
                //变电站
                setSubstation(dirArr[1]);
                //电压等级
                setVoltageLevel(Integer.parseInt(dirArr[3].replaceAll("([^\\d])", "")));
                //设备类型
                setDeviceType(DeviceType.getValueByText(dirArr[2]));
            }});
            AaDataDto dataDto = new AaDataDto() {{
                setBdz(accountDto.getSubstation());// 变电站
//                setWtdw("");// 委托单位
                setJcdw(accountDto.getRunDept());// 试验单位
//                setYxbh("");// 运行编号
//                setSyxz("");// 试验性质
//                setSyrq("");// 试验日期
                setJcry(jcrySet.stream().filter(s -> StringUtils.isNotEmpty(s)).collect(Collectors.joining(",")));// 试验人员
//                setSydd("");// 试验地点
                setBgrq(DateUtil.getCurDateStr("yyyy-MM-dd"));// 报告日期
//                setBzr("");// 编制人
//                setShr("");// 审核人
//                setPzr("");// 批准人
                setTq(dataHjService.getWeather(wdMaxDto.getX3()));// 试验天气
                setWd("" + wdMaxDto.getX4());// 温度
                setSd("" + sdMaxDto.getX5());// 湿度
                //二、设备铭牌
                setSccj(accountDto.getManufacturer());// 生产厂家
                setCcrq(DateUtil.getDateStr(accountDto.getProduceDate()));// 出厂日期
                setCcbh(accountDto.getDeviceCode());// 出厂编号
                setSbxh(accountDto.getDeviceVersion());// 设备型号
                setEddy(accountDto.getVoltageLevel() + "");// 额定电压
//                //二、设备铭牌
                setSccj(accountDto.getManufacturer());// 生产厂家
                setCcrq(DateUtil.getDateStr(accountDto.getProduceDate()));// 出厂日期
                setCcbh(accountDto.getDeviceCode());// 出厂编号
                setSbxh(accountDto.getDeviceVersion());// 设备型号
                setEddy(accountDto.getVoltageLevel() + "kV");// 额定电压
                //三、检测数据
                setBjzs(zdxjzs + "");//背景噪声
                setDtoList(new ArrayList<AaListDto>() {{
                    if (dataList != null) {
                        for (int i = 0; ListUtil.isNotEmpty(dataList) && i < dataList.size(); i++) {
                            DataAaTableDto dataAaTableDto = dataList.get(i);
                            int finalI = i;
                            DataDeviceDto dataDeviceDto = dataAaTableDto.getDataDeviceDto();
                            add(new AaListDto() {{
                                setXh(String.valueOf(finalI));//序号
                                setJcwz(dataDeviceDto.getDeviceName());// 检测位置
                                DataAaXjDto maxDataAaXjDto = dataAaXjService.findDataMaxByMap(new HashMap() {{
                                    put("dataDeviceId", dataDeviceDto.getId());
                                }});
                                if (maxDataAaXjDto != null) {
                                    setJcsz(maxDataAaXjDto.getX5() + "");// 检测数值-最大巡检幅值
                                }
//                              tpwj;// 图谱文件
                                DataLcDto maxDataLcDto = dataLcService.findDataMaxByMap(new HashMap() {{
                                    put("dataDeviceId", dataDeviceDto.getId());
                                }});
                                if (maxDataLcDto != null) {
                                    setFh(maxDataLcDto.getX4() + "");// 负荷电流(A)-最大负荷电流
                                }
//                              jl;// 结论
//                              kjg;// 备注（可见光照片）
                            }});
                        }
                    }
                }});
//                setTzfx("A-特征分析");// 特征分析
                setBjz(zdxjzs + "");//背景值
                setYqcj(I18nUtil.get("report.yqcj"));//仪器厂家
                setYqxh(I18nUtil.get("report.yqxh"));//仪器型号
            }};
            SysVariableDto variableDto = setupService.getVariableByCode(new SysVariableDto() {{
                setCode("i-path-reports");
            }});
            new AaWordUtil().report(variableDto.getValue() + File.separator + "超声波(AA)局部放电检测报告-" + (DateUtil.getCurDateStr("yyyyMMddHHmmsss")) + ".doc", dataDto);
            ialert.success("检测报告已生成!");
        } catch (Exception e) {
            log.error("超声波(AA)局部放电检测报告-生成失败!", e);
            ialert.error("超声波(AA)局部放电检测报告-生成失败!" + e.getMessage());
        }
    }

    @Override
    public void dispose() {

    }
}