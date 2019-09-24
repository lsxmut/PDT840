package com.redphase.controller.atlas.data;

import com.redphase.controller.atlas.AtlasBaseController;
import com.redphase.dto.account.AccountDto;
import com.redphase.dto.atlas.*;
import com.redphase.dto.table.DataTableDto;
import com.redphase.dto.table.DataTevTableDto;
import com.redphase.dto.atlas.tev.DataTevTjzsDto;
import com.redphase.dto.atlas.tev.DataTevXjDto;
import com.redphase.dto.atlas.tev.DataTevXjzsDto;
import com.redphase.dto.world.tev.TevDataDto;
import com.redphase.dto.world.tev.TevListDto;
import com.redphase.dto.sys.SysVariableDto;
import com.redphase.framework.util.*;
import com.redphase.framework.util.word.TevWordUtil;
import com.redphase.view.atlas.data.AtlasDataTEVDetailView;
import com.redphase.view.atlas.data.AtlasDataTEVSiteInfoView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
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
public class AtlasDataTEVController extends AtlasBaseController implements Initializable {
    @FXML
    BorderPane dataBorderPane;
    @FXML
    Label fullNameL;
    @FXML
    Pane buttonPane;//
    @FXML
    Button analysisButton;//
    @FXML
    Button detailButton;//
    @FXML
    Button reportButton;//
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
    TableView<DataTevTableDto> deviceTableView;//检测设备列表
    @FXML
    TableColumn<DataTevTableDto, String> idTableColumn;//序号
    @FXML
    TableColumn<DataTevTableDto, String> deviceNameTableColumn;//开关柜名称
    @FXML
    TableColumn<DataTevTableDto, String> qzfdTableColumn;//前中幅度
    @FXML
    TableColumn<DataTevTableDto, String> qzcsTableColumn;//前中次数
    @FXML
    TableColumn<DataTevTableDto, String> qxfdTableColumn;//前下幅度
    @FXML
    TableColumn<DataTevTableDto, String> qxcsTableColumn;//前下次数
    @FXML
    TableColumn<DataTevTableDto, String> hsfdTableColumn;//后上幅度
    @FXML
    TableColumn<DataTevTableDto, String> hscsTableColumn;//后上次数
    @FXML
    TableColumn<DataTevTableDto, String> hzfdTableColumn;//后中幅度
    @FXML
    TableColumn<DataTevTableDto, String> hzcsTableColumn;//后中次数
    @FXML
    TableColumn<DataTevTableDto, String> hxfdTableColumn;//后下幅度
    @FXML
    TableColumn<DataTevTableDto, String> hxcsTableColumn;//后下次数
    @FXML
    TableColumn<DataTevTableDto, String> csfdTableColumn;//侧上幅度
    @FXML
    TableColumn<DataTevTableDto, String> cscsTableColumn;//侧上次数
    @FXML
    TableColumn<DataTevTableDto, String> czfdTableColumn;//侧中幅度
    @FXML
    TableColumn<DataTevTableDto, String> czcsTableColumn;//侧中次数
    @FXML
    TableColumn<DataTevTableDto, String> cxfdTableColumn;//侧下幅度
    @FXML
    TableColumn<DataTevTableDto, String> cxcsTableColumn;//侧下次数
    @FXML
    TableColumn<DataTevTableDto, String> csbjtjgTableColumn;//超声波监听结果
    @FXML
    TableColumn<DataTevTableDto, String> csbcszTableColumn;//超声波测试值
    @FXML
    TableColumn<DataTevTableDto, String> fhTableColumn;//负荷电流
    @FXML
    TableColumn<DataTevTableDto, String> kggyxztTableColumn;//开关柜运行状态

    @Autowired
    private AtlasDataTEVSiteInfoView atlasDataTEVSiteInfoView;
    @Autowired
    private AtlasDataTEVSiteInfoController atlasDataTEVSiteInfoController;
    @Autowired
    private AtlasDataTEVAnalysisController atlasHorizontalAnalysisController;
    @Autowired
    private AtlasDataTEVDetailView atlasDataTEVDetailView;
    @Autowired
    private AtlasDataTEVDetailController atlasDataTEVDetailController;

    DataHjDto dataHjDto;
    DataTevXjzsDto dataTevXjzsDto;
    Float zdxj;
    ObservableList<DataTevTableDto> dataList;

    String testTechnology = "TEV";


    boolean initFlag = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize...");
        if (initFlag) return;
        initFlag = true;
        dataBorderPane.prefWidthProperty().bind(atlasController.getRightAnchorPane().prefWidthProperty());
        dataBorderPane.prefHeightProperty().bind(atlasController.getRightAnchorPane().prefHeightProperty());
    }

    List<DataDeviceDto> dataDeviceList;

    public void loadTEVData(String fullName) {
        clearL(fullNameL, bdzL, dydjL, tqqkL, wdL, sdL, xjjszs1L, xjjszs1PicL, xjjszs2L, xjjszs2PicL, xjjszs3L, xjjszs3PicL, tjjszs1L, tjjszs1PicL, tjjszs2L, tjjszs2PicL, tjjszs3L, tjjszs3PicL, dqzsL, jcryL, jcrqL);
        xjjszs1PicList = new ArrayList();
        xjjszs2PicList = new ArrayList();
        xjjszs3PicList = new ArrayList();
        tjjszs1PicList = new ArrayList();
        tjjszs2PicList = new ArrayList();
        tjjszs3PicList = new ArrayList();
        ;
        try {
            fullNameL.setText(fullName);
            //设备列表
            dataDeviceList = dataDeviceService.findListByFullName(fullName);
            //主设备目录
            DataDeviceDto mainDataDeviceDto = dataDeviceList.stream().filter(d -> "#".equals(d.getDeviceName())).findFirst().get();
            if ("1".equals(mainDataDeviceDto.getDeviceType())) {
                buttonPane.setVisible(true);
                buttonPane.setManaged(true);
            } else {
                buttonPane.setVisible(false);
                buttonPane.setManaged(false);
            }
            bdzL.setText(mainDataDeviceDto.getSubstation());//变电站名称
            dydjL.setText(mainDataDeviceDto.getVoltageLevel() + "");//电压等级
            jcrqL.setText(mainDataDeviceDto.getDateDetection());//检测日期

            //查询检测文件
            List<DataAnalyzeDto> dataAnalyzeDtoList = dataAnalyzeService.findDataIsList(new DataAnalyzeDto() {{
                setDataDeviceId(mainDataDeviceDto.getId());
            }});

            for (DataAnalyzeDto dataAnalyzeDto : dataAnalyzeDtoList) {
                DataTableDto dataTableDto = new DataTableDto();
                dataTableDto.setDataDeviceDto(mainDataDeviceDto);
                dataTableDto.setFddj(mainDataDeviceDto.getVoltageLevel() + "kV");//电压等级；
                dataTableDto.setBcsb(mainDataDeviceDto.getDeviceName()); //被测设备；
                dataTableDto.setFileUrl(dataAnalyzeDto.getFileUrl());
                dataTableDto.setScreenshots(dataAnalyzeDto.getScreenshots());
                dataTableDto.setAudios(dataAnalyzeDto.getAudios());
                //环境参数数据
                if ("HJ".equalsIgnoreCase(dataAnalyzeDto.getDataFormat()) && 0 == dataAnalyzeDto.getOrderNo()) {
                    //查询环境参数数据
                    dataHjDto = dataHjService.findDataById(new DataHjDto() {{
                        setDataAnalyzeId(dataAnalyzeDto.getId());
                    }});
                    wdL.setText("" + dataHjDto.getX4());//温度
                    sdL.setText("" + dataHjDto.getX5());//湿度
                    tqqkL.setText(dataHjService.getWeather(dataHjDto.getX3()));//天气情况
                    String[] jcryArr = {dataHjDto.getX6(), dataHjDto.getX7(), dataHjDto.getX8(), dataHjDto.getX9(), dataHjDto.getX10()};
                    jcryL.setText(Arrays.stream(jcryArr).filter(s -> StringUtils.isNotEmpty(s)).collect(Collectors.joining(",")));//检测人员
                } else if ("TEV_XJ".equalsIgnoreCase(dataAnalyzeDto.getDataFormat())) {
                    DataTevXjDto dataTevXjDto = dataTevXjService.findDataById(new DataTevXjDto() {{
                        setDataAnalyzeId(dataAnalyzeDto.getId());
                    }});
                    if (dataTevXjDto != null) {
                        if (zdxj == null || dataTevXjDto.getX6() > zdxj) {
                            zdxj = dataTevXjDto.getX6();
                        }
                    }
                } else if ("TEV_XJZS".equalsIgnoreCase(dataAnalyzeDto.getDataFormat())) {
                    //巡检噪声
                    dataTevXjzsDto = dataTevXjzsService.findDataById(new DataTevXjzsDto() {{
                        setDataAnalyzeId(dataAnalyzeDto.getId());
                    }});

                    dataTableDto.setCssj(StrUtil.asString(dataTevXjzsDto.getX3()));//测试时间；
                    dataTableDto.setXwwy("--");//相位位移；
                    dataTableDto.setCkyz(StrUtil.asString(dataTevXjzsDto.getX24()));//参考阈值；
//                    dataTableDto.setFdzy(StrUtil.asString(dataTevXjzsDto.getX11()));//放大增益；
//                    dataTableDto.setQzsj(StrUtil.asString(dataTevXjzsDto.getX12()));//前置衰减；
                    dataTableDto.setTbfs(dataTevXjzsDto.getX17() == 1 ? "外同步" : "内同步");//同步方式 同步类型外同步:0x01内同步:0x02默认写入内同步
                    dataTableDto.setTbpl(StrUtil.asString(dataTevXjzsDto.getX18()));//同步频率；
                    dataTableDto.setXtpl(StrUtil.asString(dataTevXjzsDto.getX16()));//系统频率；
                    dataTableDto.setZdz(dataTevXjzsDto.getX6());//最大值；

                    if (dataAnalyzeDto.getOrderNo() == 0) {
                        xjjszs1L.setText(StrUtil.asString(dataTevXjzsDto.getX5()));
                        xjjszs1PicList.add(dataTableDto);
                        loadImageDialog(testTechnology, 1, xjjszs1PicL, "巡检截图", xjjszs1PicList);
                    } else if (dataAnalyzeDto.getOrderNo() == 1) {
                        xjjszs2L.setText(StrUtil.asString(dataTevXjzsDto.getX5()));
                        xjjszs2PicList.add(dataTableDto);
                        loadImageDialog(testTechnology, 1, xjjszs2PicL, "巡检截图", xjjszs2PicList);
                    } else if (dataAnalyzeDto.getOrderNo() == 2) {
                        xjjszs3L.setText(StrUtil.asString(dataTevXjzsDto.getX5()));
                        xjjszs3PicList.add(dataTableDto);
                        loadImageDialog(testTechnology, 1, xjjszs3PicL, "巡检截图", xjjszs3PicList);
                    }
                } else if ("TEV_TJZS".equalsIgnoreCase(dataAnalyzeDto.getDataFormat())) {
                    //统计噪声
                    DataTevTjzsDto dataTevTjzsDto = dataTevTjzsService.findDataById(new DataTevTjzsDto() {{
                        setDataAnalyzeId(dataAnalyzeDto.getId());
                    }});
                    dataTableDto.setCssj(StrUtil.asString(dataTevTjzsDto.getX14()));//测试时间；
                    dataTableDto.setXwwy(StrUtil.asString(dataTevTjzsDto.getX19()));//相位位移；
                    dataTableDto.setCkyz(StrUtil.asString(dataTevTjzsDto.getX18()));//参考阈值；
                    dataTableDto.setFdzy(StrUtil.asString(dataTevTjzsDto.getX23()));//放大增益；
                    dataTableDto.setQzsj(StrUtil.asString(dataTevTjzsDto.getX24()));//前置衰减；
                    dataTableDto.setTbfs(dataTevTjzsDto.getX20() == 1 ? "外同步" : "内同步");//同步方式 同步类型外同步:0x01内同步:0x02默认写入内同步
                    dataTableDto.setTbpl(StrUtil.asString(dataTevTjzsDto.getX21()));//同步频率；
                    dataTableDto.setXtpl(StrUtil.asString(dataTevTjzsDto.getX22()));//系统频率；

                    if (dataAnalyzeDto.getOrderNo() == 0) {
                        tjjszs1L.setText(StrUtil.asString(dataTevTjzsDto.getX16()));
                        tjjszs1PicList.add(dataTableDto);
                        loadImageDialog(testTechnology, 1, tjjszs1PicL, "统计截图", tjjszs1PicList);
                    } else if (dataAnalyzeDto.getOrderNo() == 1) {
                        tjjszs2L.setText(StrUtil.asString(dataTevTjzsDto.getX16()));
                        tjjszs2PicList.add(dataTableDto);
                        loadImageDialog(testTechnology, 1, tjjszs2PicL, "统计截图", tjjszs2PicList);
                    } else if (dataAnalyzeDto.getOrderNo() == 2) {
                        tjjszs3L.setText(StrUtil.asString(dataTevTjzsDto.getX16()));
                        tjjszs3PicList.add(dataTableDto);
                        loadImageDialog(testTechnology, 1, tjjszs3PicL, "统计截图", tjjszs3PicList);
                    }
                }
            }

            //检测设备列表数据
            dataList = FXCollections.observableArrayList();
            for (DataDeviceDto dataDeviceDto : dataDeviceList) {
                if ("#".equals(dataDeviceDto.getDeviceName())) {
                    continue;
                }
                DataTevTableDto tableDto = new DataTevTableDto() {{
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
                    //过滤出当前位置地电波巡检检测文件
                    List<DataAnalyzeDto> xjAnalyzeList = analyzeDtos.stream().filter(a -> "TEV_XJ".equals(a.getDataFormat())).collect(Collectors.toList());
                    //查询当前位置地电波巡检检测数据
                    List<DataTevXjDto> tevXjList = dataTevXjService.findDataByIds(xjAnalyzeList.stream().map(a -> a.getId().toString()).collect(Collectors.joining(",")));
                    //过滤出最大的幅度、放电频度、超声波测试值
                    DataTevXjDto maxFzDto = tevXjList.stream().max(Comparator.comparing(DataTevXjDto::getX5)).orElse(new DataTevXjDto());
                    DataTevXjDto maxFdDto = tevXjList.stream().max(Comparator.comparing(DataTevXjDto::getX7)).orElse(new DataTevXjDto());
                    DataTevXjDto maxCsbcszDto = tevXjList.stream().max(Comparator.comparing(DataTevXjDto::getX19)).orElse(new DataTevXjDto());
                    //根据位置名称，设置列表中检测数据的值
                    setSiteData(tableDto, dataDeviceSiteDto.getSiteName(), maxFzDto.getX5(), maxFdDto.getX7(), maxCsbcszDto.getX19());
                    //计算超声波监听结果
                    tableDto.setCsbjtjg(calculateCsbjtjg(tevXjList));
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
            idTableColumn.setCellFactory(column -> new TableCell<DataTevTableDto, String>() {
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
            deviceNameTableColumn.setCellFactory(column -> new TableCell<DataTevTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    this.setStyle("-fx-text-fill: #31a1ff;-fx-underline: true;-fx-cursor: hand;");
                    if (!empty) {
                        DataTevTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getDataDeviceDto().getDeviceName());
                        this.setOnMouseClicked(e -> {
//                            Double width = Application.getStage().getWidth() - 200;
//                            Double height = Application.getStage().getHeight() - 100;
                            idialog.openDialog(tableDto.getDataDeviceDto().getDeviceName(), atlasDataTEVSiteInfoView, 1180.0, 700.0, true);
                            ehcacheUtil.setCache(CommonConstant.ATLAS_DATA_SKILL_DEVICE_SITE, tableDto.getDataDeviceDto());
                            atlasDataTEVSiteInfoController.loadData();
                        });
                    }
                }
            });

            //前中幅度
            qzfdTableColumn.setCellFactory(column -> new TableCell<DataTevTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataTevTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getQzfd() == null ? "--" : StrUtil.subZero(tableDto.getQzfd().toString()));
                    }
                }
            });

            //前中次数
            qzcsTableColumn.setCellFactory(column -> new TableCell<DataTevTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataTevTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getQzcs() == null ? "--" : tableDto.getQzcs().toString());
                    }
                }
            });

            //前下幅度
            qxfdTableColumn.setCellFactory(column -> new TableCell<DataTevTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataTevTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getQxfd() == null ? "--" : StrUtil.subZero(tableDto.getQxfd().toString()));
                    }
                }
            });

            //前下次数
            qxcsTableColumn.setCellFactory(column -> new TableCell<DataTevTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataTevTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getQxcs() == null ? "--" : tableDto.getQxcs().toString());
                    }
                }
            });

            //后上幅度
            hsfdTableColumn.setCellFactory(column -> new TableCell<DataTevTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataTevTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getHsfd() == null ? "--" : StrUtil.subZero(tableDto.getHsfd().toString()));
                    }
                }
            });

            //后上次数
            hscsTableColumn.setCellFactory(column -> new TableCell<DataTevTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataTevTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getHscs() == null ? "--" : tableDto.getHscs().toString());
                    }
                }
            });

            //后中幅度
            hzfdTableColumn.setCellFactory(column -> new TableCell<DataTevTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataTevTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getHzfd() == null ? "--" : StrUtil.subZero(tableDto.getHzfd().toString()));
                    }
                }
            });

            //后中次数
            hzcsTableColumn.setCellFactory(column -> new TableCell<DataTevTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataTevTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getHzcs() == null ? "--" : tableDto.getHzcs().toString());
                    }
                }
            });

            //后下幅度
            hxfdTableColumn.setCellFactory(column -> new TableCell<DataTevTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataTevTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getHxfd() == null ? "--" : StrUtil.subZero(tableDto.getHxfd().toString()));
                    }
                }
            });

            //后下次数
            hxcsTableColumn.setCellFactory(column -> new TableCell<DataTevTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataTevTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getHxcs() == null ? "--" : tableDto.getHxcs().toString());
                    }
                }
            });

            //侧上幅度
            csfdTableColumn.setCellFactory(column -> new TableCell<DataTevTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataTevTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getCsfd() == null ? "--" : StrUtil.subZero(tableDto.getCsfd().toString()));
                    }
                }
            });

            //侧上次数
            cscsTableColumn.setCellFactory(column -> new TableCell<DataTevTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataTevTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getCscs() == null ? "--" : tableDto.getCscs().toString());
                    }
                }
            });

            //侧中幅度
            czfdTableColumn.setCellFactory(column -> new TableCell<DataTevTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataTevTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getCzfd() == null ? "--" : StrUtil.subZero(tableDto.getCzfd().toString()));
                    }
                }
            });

            //侧中次数
            czcsTableColumn.setCellFactory(column -> new TableCell<DataTevTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataTevTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getCzcs() == null ? "--" : tableDto.getCzcs().toString());
                    }
                }
            });

            //侧下幅度
            cxfdTableColumn.setCellFactory(column -> new TableCell<DataTevTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataTevTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getCxfd() == null ? "--" : StrUtil.subZero(tableDto.getCxfd().toString()));
                    }
                }
            });

            //侧下次数
            cxcsTableColumn.setCellFactory(column -> new TableCell<DataTevTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataTevTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getCxcs() == null ? "--" : tableDto.getCxcs().toString());
                    }
                }
            });

            //超声波监听结果
            csbjtjgTableColumn.setCellFactory(column -> new TableCell<DataTevTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataTevTableDto dataTevTableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(dataTevTableDto.getCsbjtjg());
                    }
                }
            });

            //超声波测试值
            csbcszTableColumn.setCellFactory(column -> new TableCell<DataTevTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataTevTableDto dataTevTableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(dataTevTableDto.getCsbcsz() == null ? "--" : StrUtil.subZero(dataTevTableDto.getCsbcsz().toString()));
                    }
                }
            });

            //负荷电流
            fhTableColumn.setCellFactory(column -> new TableCell<DataTevTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataTevTableDto dataTevTableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(dataTevTableDto.getFh() == null ? "--" : StrUtil.subZero(dataTevTableDto.getFh().toString()));
                    }
                }
            });

            //开关柜运行状态
            kggyxztTableColumn.setCellFactory(column -> new TableCell<DataTevTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        DataTevTableDto dataTevTableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(dataTevTableDto.getKggyxzt() == null ? "--" : dataTevTableDto.getKggyxzt());
                    }
                }
            });
        } catch (Exception e) {
            log.error("TEV加载出错！", e);
        }
    }

    /**
     * 根据位置名称，设置列表中检测数据的值
     *
     * @param tableDto
     * @param siteName
     * @param fd       幅度
     * @param cs       次数
     * @param csbcsz   超声波测试值
     */
    private void setSiteData(DataTevTableDto tableDto, String siteName, Float fd, Integer cs, Float csbcsz) {
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
        //超声波测试值取最大值
        if (tableDto.getCsbcsz() == null || (csbcsz != null && csbcsz > tableDto.getCsbcsz())) {
            tableDto.setCsbcsz(csbcsz);
        }
    }

    /**
     * 计算超声波监听结果
     *
     * @param tevXjList
     * @return
     */
    private String calculateCsbjtjg(List<DataTevXjDto> tevXjList) {
        if (!ListUtil.isNotEmpty(tevXjList)) {
            return "未检测";
        }
        //各个位置监听结果统计(key-超声波监听结果，value-出现次数)
        Map<Integer, Long> map = tevXjList.stream().collect(Collectors.groupingBy(DataTevXjDto::getX21, Collectors.counting()));
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

    /**
     * 横向分析表
     */
    public void hxfxb() {
//        Double width = Application.getStage().getWidth() - 200;
//        Double height = Application.getStage().getHeight() - 100;
        idialog.openDialog("横向分析表", atlasHorizontalAnalysisView, 1150.0, 745.0, false);
        atlasHorizontalAnalysisController.loadChartData(fullNameL.getText());
    }

    @FXML
    public void switchgearDetail() {
//        Double width = Application.getStage().getWidth() - 200;
//        Double height = Application.getStage().getHeight() - 100;
        idialog.openDialog("开关柜详情", atlasDataTEVDetailView, 1300.0, 745.0, false);
        atlasDataTEVDetailController.init(dataDeviceList);
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
                setDeviceType(AccountDto.DeviceType.getValueByText(dirArr[2]));
            }});
            TevDataDto dataDto = new TevDataDto() {{
                setBdz(accountDto.getSubstation());// 变电站
//                setWtdw("");// 委托单位
                setSydw(accountDto.getRunDept());// 试验单位
//                setYxbh("");// 运行编号
                String[] jcryArr = {dataHjDto.getX6(), dataHjDto.getX7(), dataHjDto.getX8(), dataHjDto.getX9(), dataHjDto.getX10()};
//                setSyxz("");// 试验性质
//                setSyrq("");// 试验日期
                setSyry(Arrays.stream(jcryArr).filter(s -> StringUtils.isNotEmpty(s)).collect(Collectors.joining(",")));// 试验人员
//                setSydd("");// 试验地点
                setBgrq(DateUtil.getCurDateStr("yyyy-MM-dd"));// 报告日期
//                setBzr("");// 编制人
//                setShr("");// 审核人
//                setPzr("");// 批准人
                setSytq(dataHjService.getWeather(dataHjDto.getX3()));// 试验天气
                setWd("" + dataHjDto.getX4());// 温度
                setSd("" + dataHjDto.getX5());// 湿度
                setBjzs("" + dataTevXjzsDto.getX6());// 背景噪声 巡检噪声最大值
                //二、设备铭牌
                setSbxh(accountDto.getDeviceVersion());// 设备型号
                setSccj(accountDto.getManufacturer());// 生产厂家
                setEddy(accountDto.getVoltageLevel() + "kV");// 额定电压
                setTyrq(DateUtil.getDateStr(accountDto.getUseDate()));// 投运日期
                setCcrq(DateUtil.getDateStr(accountDto.getProduceDate()));// 出厂日期
                //三、检测数据
                for (int i = 0; ListUtil.isNotEmpty(dataList) && i < dataList.size(); i++) {
                    DataTevTableDto dataTevTableDto = dataList.get(i);
                    TevListDto oldTevListDto = new TevListDto() {{
                        setIsNew(0);
                        setBh(dataTevTableDto.getDataDeviceDto().getDeviceName());//开关柜编号
                    }};
                    TevListDto newTevListDto = new TevListDto() {{
                        setIsNew(1);
                        setBh(dataTevTableDto.getDataDeviceDto().getDeviceName());//开关柜编号
                        setQz(StrUtil.subZero(StrUtil.asString(dataTevTableDto.getQzfd())));
                        setQx(StrUtil.subZero(StrUtil.asString(dataTevTableDto.getQxfd())));
                        setHs(StrUtil.subZero(StrUtil.asString(dataTevTableDto.getHsfd())));
                        setHz(StrUtil.subZero(StrUtil.asString(dataTevTableDto.getHzfd())));
                        setHx(StrUtil.subZero(StrUtil.asString(dataTevTableDto.getHxfd())));
                        setCs(StrUtil.subZero(StrUtil.asString(dataTevTableDto.getCsfd())));
                        setCz(StrUtil.subZero(StrUtil.asString(dataTevTableDto.getCzfd())));
                        setCx(StrUtil.subZero(StrUtil.asString(dataTevTableDto.getCxfd())));
                        setFh(StrUtil.subZero(StrUtil.asString(dataTevTableDto.getFh())));
//                        setKjg();//备注（可见光照片）
//                        setJl(); //结论
                    }};
                    oldTevListDto.setXh(StrUtil.asString(i + 1));
                    newTevListDto.setXh(StrUtil.asString(i + 1));
                    getOldDtoList().add(oldTevListDto);
                    getNewDtoList().add(newTevListDto);
                }
//                setTzfx("A-特征分析");// 特征分析
                setBjz(zdxj + "");//背景值    用的最大巡检值
                setYqcj(I18nUtil.get("report.yqcj"));//仪器厂家
                setYqxh(I18nUtil.get("report.yqxh"));//仪器型号
            }};
            SysVariableDto variableDto = setupService.getVariableByCode(new SysVariableDto() {{
                setCode("i-path-reports");
            }});
            new TevWordUtil().report(variableDto.getValue() + File.separator + "暂态地电压局部放电检测报告-" + (DateUtil.getCurDateStr("yyyyMMddHHmmsss")) + ".doc", dataDto);
            ialert.success("检测报告已生成!");
        } catch (Exception e) {
            log.error("暂态地电压局部放电检测报告-生成失败!", e);
            ialert.error("暂态地电压局部放电检测报告-生成失败!" + e.getMessage());
        }
    }

    @Override
    public void dispose() {

    }
}