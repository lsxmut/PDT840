package com.redphase.controller.atlas.data;

import com.alibaba.fastjson.JSONObject;
import com.redphase.controller.atlas.AtlasBaseController;
import com.redphase.dto.account.AccountDto;
import com.redphase.dto.atlas.*;
import com.redphase.dto.atlas.hfct.*;
import com.redphase.dto.table.DataHfctTableDto;
import com.redphase.dto.table.DataTableDto;
import com.redphase.dto.world.hfct.HfctDataDto;
import com.redphase.dto.world.hfct.HfctListDto;
import com.redphase.dto.sys.SysVariableDto;
import com.redphase.framework.util.CommonConstant;
import com.redphase.framework.util.DateUtil;
import com.redphase.framework.util.I18nUtil;
import com.redphase.framework.util.word.HfctWordUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@FXMLController
@Slf4j
public class AtlasDataHFCTController extends AtlasBaseController implements Initializable {
    @FXML
    BorderPane dataBorderPane;
    @Autowired
    AtlasSiteCountController atlasSiteCountController;
    @FXML
    Label typeL;//
    @FXML
    Label titleL;//标题
    @FXML
    Label bdzL;//变电站名称
    @FXML
    Label sbmcL;//设备名称
    @FXML
    Label tqqkL;//天气情况
    @FXML
    Label dydjL;//电压等级
    @FXML
    Label sblxL;//设备类型
    @FXML
    Label jgmcL;//间隔名称
    @FXML
    Label jcryL;//检测人员
    @FXML
    Label jcrqL;//检测日期
    @FXML
    Label wdL;//温度
    @FXML
    Label sdL;//湿度
    @FXML
    Label dqzsL;//大气噪声
    @FXML
    Label xjzs1L;//巡检噪声1
    @FXML
    Label xjjt1L;//巡检截图1
    List xjjt1List;
    @FXML
    Label xjzs2L;//巡检噪声2
    @FXML
    Label xjjt2L;//巡检截图2
    List xjjt2List;
    @FXML
    Label xjzs3L;//巡检噪声3
    @FXML
    Label xjjt3L;//巡检截图3
    List xjjt3List;

    @FXML
    Label tjzs1L;//统计噪声1
    @FXML
    Label tjjt1L;//统计截图1
    List tjjt1List;
    @FXML
    Label tjzs2L;//统计噪声2
    @FXML
    Label tjjt2L;//统计截图2
    List tjjt2List;
    @FXML
    Label tjzs3L;//统计噪声3
    @FXML
    Label tjjt3L;//统计截图3
    List tjjt3List;
    @FXML
    TableView<DataHfctTableDto> deviceSiteTableView;//检测位置列表
    @FXML
    TableColumn<DataHfctTableDto, String> idTableColumn;//序号
    @FXML
    TableColumn<DataHfctTableDto, String> siteTableColumn;//检测位置
    @FXML
    TableColumn<DataHfctTableDto, String> kjgTableColumn;//可见光照片
    @FXML
    TableColumn<DataHfctTableDto, String> xjTableColumn;//HFCT巡检检测文件
    @FXML
    TableColumn<DataHfctTableDto, String> xjjtTableColumn;//HFCT巡检截图
    @FXML
    TableColumn<DataHfctTableDto, String> tjTableColumn;//HFCT统计检测文件
    @FXML
    TableColumn<DataHfctTableDto, String> tjjtTableColumn;//HFCT统计截图
    @FXML
    TableColumn<DataHfctTableDto, String> tjlbTableColumn;//HFCT统计录波
    @FXML
    TableColumn<DataHfctTableDto, String> fhTableColumn;//负荷电流


    boolean initFlag = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize...");
        if (initFlag) return;
        initFlag = true;
        dataBorderPane.prefWidthProperty().bind(atlasController.getRightAnchorPane().prefWidthProperty());
        dataBorderPane.prefHeightProperty().bind(atlasController.getRightAnchorPane().prefHeightProperty());
    }

    //    @Autowired
//    AccountService accountService;
    @FXML
    public void report() {
        log.debug("report..");
        try {
            String fullName = (String) ehcacheUtil.getCache(CommonConstant.ATLAS_DATA_SKILL_FULL_NAME);
            AccountDto accountDto = accountService.findDataByFullName(new AccountDto() {{
                String[] dirArr = fullName.split("_");
                //电业局
                setElectricBureau(dirArr[0]);
                //变电站
                setSubstation(dirArr[1]);
                //电压等级
                setVoltageLevel(Integer.parseInt(dirArr[3].replaceAll("([^\\d])", "")));
                //设备类型
                switch (dirArr[2]) {
                    case "开关柜":
                        setDeviceType(1);
                        break;
                    case "变压器":
                        setDeviceType(2);
                        break;
                    case "组合电器":
                        setDeviceType(3);
                        break;
                    case "电缆":
                        setDeviceType(4);
                        break;
                }
            }});
            HfctDataDto dataDto = new HfctDataDto() {{
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
                //二、设备铭牌
                setSccj(accountDto.getManufacturer());// 生产厂家
                setCcrq(DateUtil.getDateStr(accountDto.getProduceDate()));// 出厂日期
                setCcbh(accountDto.getDeviceCode());// 出厂编号
                setSbxh(accountDto.getDeviceVersion());// 设备型号
                setEddy(accountDto.getVoltageLevel() + "");// 额定电压
                //三、检测数据
                setDtoList(new ArrayList<HfctListDto>() {{
                    if (dataList != null) {
                        for (int i = 0; i < dataList.size(); i++) {
                            DataHfctTableDto hfctTableDto = dataList.get(i);
                            int finalI = i;
                            add(new HfctListDto() {{
                                setXh("" + (finalI + 1));// 序号
                                setJgmc(hfctTableDto.getSite());// 间隔名称
                                setSbmc(hfctTableDto.getDataDeviceDto().getDeviceName());// 设备名称
                                if (hfctTableDto.getTjList() != null && hfctTableDto.getTjList().size() > 0) {
                                    DataTableDto tableDto = hfctTableDto.getTjList().get(0);
//                                    setXw("");// 相位
                                    atlasSiteCountController.init(testTechnology, hfctTableDto.getTjList(), null);
                                    String tmpPngPath = System.getProperty("java.io.tmpdir") + "/" + System.nanoTime() + ".png";
                                    atlasSiteCountController.reportToData();
                                    ImageIO.write(SwingFXUtils.fromFXImage(atlasSiteCountController.getPrpsPane().snapshot(new SnapshotParameters(), null), null), "png", new File(tmpPngPath));
                                    setTpwj(tmpPngPath);// 图谱文件
//                                setFd("");// 是否存在放电信号（打勾）
                                    setFz("" + tableDto.getFz());// 测试值（峰值）（mV）
                                }
                            }});
                        }
                    }
                }});
//                setTzfx("A-特征分析");// 特征分析
                setJcyq(I18nUtil.get("report.yqxh"));// 检测仪器
//                setJl("A-结论");// 结论
//                setBz("A-备注");// 备注
            }};
            SysVariableDto variableDto = setupService.getVariableByCode(new SysVariableDto() {{
                setCode("i-path-reports");
            }});
            new HfctWordUtil().report(variableDto.getValue() + File.separator + "高频局部放电检测报告-" + (DateUtil.getCurDateStr("yyyyMMddHHmmsss")) + ".doc", dataDto);
            ialert.success("检测报告已生成!");
        } catch (Exception e) {
            log.error("高频局部放电检测报告-生成失败!", e);
            ialert.error("高频局部放电检测报告-生成失败!" + e.getMessage());
        }
    }

    ObservableList<DataHfctTableDto> dataList;
    List<DataDeviceSiteDto> dataDeviceSiteDtoList;
    DataDeviceDto subDataDeviceDto;
    DataHjDto dataHjDto;
    String siteStr = "";
    String testTechnology = "HFCT";

    public void loadHFCTDetail() {
        clearL(typeL, titleL, bdzL, sbmcL, tqqkL, dydjL, sblxL, jgmcL, jcryL, jcrqL, wdL, sdL, dqzsL, xjzs1L, xjjt1L, xjzs2L, xjjt2L, xjzs3L, xjjt3L, tjzs1L, tjjt1L, tjzs2L, tjjt2L, tjzs3L, tjjt3L);
        xjjt1List = new ArrayList();
        xjjt2List = new ArrayList();
        xjjt3List = new ArrayList();
        tjjt1List = new ArrayList();
        tjjt2List = new ArrayList();
        tjjt3List = new ArrayList();
        ;
        try {
            DataDeviceDto mainDataDeviceDto = (DataDeviceDto) ehcacheUtil.getCache(CommonConstant.ATLAS_DATA_SKILL_DEVICE_MAIN);
            subDataDeviceDto = (DataDeviceDto) ehcacheUtil.getCache(CommonConstant.ATLAS_DATA_SKILL_DEVICE_SUB);
            DataDeviceDto finalSubDataDeviceDto = subDataDeviceDto;
            String deviceType = "";
            switch (subDataDeviceDto.getDeviceType()) {//设备类型(1、开关柜；2、变压器；3、组合电器；4、电缆)
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
            testTechnology = subDataDeviceDto.getTestTechnology();

            typeL.setText(deviceType + "名称:");//
            titleL.setText(subDataDeviceDto.getDeviceName());//
            bdzL.setText(subDataDeviceDto.getSubstation());//变电站名称
            sbmcL.setText(subDataDeviceDto.getDeviceName());//设备名称
            dydjL.setText(subDataDeviceDto.getVoltageLevel());//电压等级
            sblxL.setText(deviceType);//设备类型
            jgmcL.setText(subDataDeviceDto.getSpaceName());//间隔名称
            jcrqL.setText(subDataDeviceDto.getDateDetection());//检测日期

            siteStr = subDataDeviceDto.getSubstation() + "," + subDataDeviceDto.getDeviceName();

            List<DataAnalyzeDto> dataAnalyzeDtoList = dataAnalyzeService.findDataIsList(new DataAnalyzeDto() {{
                setDataDeviceId(finalSubDataDeviceDto.getId());
            }});

            for (DataAnalyzeDto dataAnalyzeDto : dataAnalyzeDtoList) {
                DataHfctTableDto deviceDataHfctTableDto = new DataHfctTableDto();
                deviceDataHfctTableDto.setDataDeviceDto(subDataDeviceDto);
//                dataHfctTableDto.setDataDeviceSiteDto(dataDeviceSiteDto);
//                dataHfctTableDto.setSite(dataDeviceSiteDto.getSiteName()); //检测位置
                deviceDataHfctTableDto.setFddj(subDataDeviceDto.getVoltageLevel() + "kV");//电压等级；
                deviceDataHfctTableDto.setBcsb(subDataDeviceDto.getDeviceName() + ""); //被测设备；
                deviceDataHfctTableDto.setFileUrl(dataAnalyzeDto.getFileUrl());
                deviceDataHfctTableDto.setScreenshots(dataAnalyzeDto.getScreenshots());
                deviceDataHfctTableDto.setAudios(dataAnalyzeDto.getAudios());

                if ("HJ".equalsIgnoreCase(dataAnalyzeDto.getDataFormat()) && 0 == (dataAnalyzeDto.getOrderNo())) {
                    dataHjDto = dataHjService.findDataById(new DataHjDto() {{
                        setDataAnalyzeId(dataAnalyzeDto.getId());
                    }});
                    wdL.setText("" + dataHjDto.getX4());//温度
                    sdL.setText("" + dataHjDto.getX5());//湿度
                    tqqkL.setText(dataHjService.getWeather(dataHjDto.getX3()));//天气情况
                    String[] jcryArr = {dataHjDto.getX6(), dataHjDto.getX7(), dataHjDto.getX8(), dataHjDto.getX9(), dataHjDto.getX10()};
                    jcryL.setText(Arrays.stream(jcryArr).filter(s -> StringUtils.isNotEmpty(s)).collect(Collectors.joining(",")));//检测人员
                } else if ("LC".equalsIgnoreCase(dataAnalyzeDto.getDataFormat()) && 0 == (dataAnalyzeDto.getOrderNo())) {
                    DataLcDto dataLcDto = dataLcService.findDataById(new DataLcDto() {{
                        setDataAnalyzeId(dataAnalyzeDto.getId());
                    }});
                    log.debug("dataLcDto=>{}", dataLcDto);
                } else if ("HFCT_TJZS".equalsIgnoreCase(dataAnalyzeDto.getDataFormat())) {
                    DataHfctTjzsDto dataHfctTjzsDto = dataHfctTjzsService.findDataById(new DataHfctTjzsDto() {{
                        setDataAnalyzeId(dataAnalyzeDto.getId());
                    }});

                    deviceDataHfctTableDto.setCssj("" + dataHfctTjzsDto.getX14());//测试时间；
                    deviceDataHfctTableDto.setXwwy("" + dataHfctTjzsDto.getX19());//相位位移；
                    deviceDataHfctTableDto.setCkyz("" + dataHfctTjzsDto.getX18());//参考阈值；
                    deviceDataHfctTableDto.setFdzy("" + dataHfctTjzsDto.getX23());//放大增益；
                    deviceDataHfctTableDto.setQzsj("" + dataHfctTjzsDto.getX24());//前置衰减；
                    deviceDataHfctTableDto.setTbfs("" + (dataHfctTjzsDto.getX20() == 1 ? "外同步" : "内同步"));//同步方式 同步类型外同步:0x01内同步:0x02默认写入内同步
                    deviceDataHfctTableDto.setTbpl("" + dataHfctTjzsDto.getX21());//同步频率；
                    deviceDataHfctTableDto.setXtpl("" + dataHfctTjzsDto.getX22());//系统频率；
                    deviceDataHfctTableDto.setMc("" + dataHfctTjzsDto.getX17());//脉冲；
                    switch (dataAnalyzeDto.getOrderNo()) {
                        case 0:
                            tjzs1L.setText("" + dataHfctTjzsDto.getX16());
                            xjjt1List.add(deviceDataHfctTableDto);
                            loadImageDialog(testTechnology, 1, tjjt1L, "统计截图", xjjt1List);
                            break;
                        case 1:
                            tjzs2L.setText("" + dataHfctTjzsDto.getX16());
                            tjjt2List.add(deviceDataHfctTableDto);
                            loadImageDialog(testTechnology, 1, tjjt2L, "统计截图", tjjt2List);
                            break;
                        case 2:
                            tjzs3L.setText("" + dataHfctTjzsDto.getX16());
                            tjjt3List.add(deviceDataHfctTableDto);
                            loadImageDialog(testTechnology, 1, tjjt3L, "统计截图", tjjt3List);
                            break;
                    }
                } else if ("HFCT_XJZS".equalsIgnoreCase(dataAnalyzeDto.getDataFormat())) {
                    DataHfctXjzsDto dataHfctXjzsDto = dataHfctXjzsService.findDataById(new DataHfctXjzsDto() {{
                        setDataAnalyzeId(dataAnalyzeDto.getId());
                    }});

                    deviceDataHfctTableDto.setCssj("" + dataHfctXjzsDto.getX3());//测试时间；
                    deviceDataHfctTableDto.setCkyz("" + dataHfctXjzsDto.getX10());//参考阈值；
                    deviceDataHfctTableDto.setFdzy("" + dataHfctXjzsDto.getX11());//放大增益；
                    deviceDataHfctTableDto.setQzsj("" + dataHfctXjzsDto.getX12());//前置衰减；
                    deviceDataHfctTableDto.setTbfs("" + (dataHfctXjzsDto.getX20() == 1 ? "外同步" : "内同步"));//同步方式 同步类型外同步:0x01内同步:0x02默认写入内同步
                    deviceDataHfctTableDto.setTbpl("" + dataHfctXjzsDto.getX21());//同步频率；
                    deviceDataHfctTableDto.setXtpl("" + dataHfctXjzsDto.getX19());//系统频率；

                    switch (dataAnalyzeDto.getOrderNo()) {
                        case 0:
                            xjzs1L.setText("" + dataHfctXjzsDto.getX5());
                            xjjt1List.add(deviceDataHfctTableDto);
                            loadImageDialog(testTechnology, 1, xjjt1L, "巡检截图", xjjt1List);
                            break;
                        case 1:
                            xjzs2L.setText("" + dataHfctXjzsDto.getX5());
                            xjjt2List.add(deviceDataHfctTableDto);
                            loadImageDialog(testTechnology, 1, xjjt2L, "巡检截图", xjjt2List);
                            break;
                        case 2:
                            xjzs3L.setText("" + dataHfctXjzsDto.getX5());
                            xjjt3List.add(deviceDataHfctTableDto);
                            loadImageDialog(testTechnology, 1, xjjt3L, "巡检截图", xjjt3List);
                            break;
                    }
                }
            }
            //位置信息
            String finalDeviceType = deviceType;
            dataDeviceSiteDtoList = dataDeviceSiteService.findDataIsList(new DataDeviceSiteDto() {{
                if (("开关柜".equalsIgnoreCase(finalDeviceType) && "TEV".equalsIgnoreCase(testTechnology))) {
                    setDataDeviceId(mainDataDeviceDto.getId());
                } else {
                    setDataDeviceId(subDataDeviceDto.getId());
                }
            }});
            dataList = FXCollections.observableArrayList();
            for (DataDeviceSiteDto dataDeviceSiteDto : dataDeviceSiteDtoList) {
                DataHfctTableDto siteDataHfctTableDto = new DataHfctTableDto();
                siteDataHfctTableDto.setDataDeviceDto(subDataDeviceDto);
                siteDataHfctTableDto.setDataDeviceSiteDto(dataDeviceSiteDto);
                siteDataHfctTableDto.setSite(dataDeviceSiteDto.getSiteName()); //检测位置
                siteDataHfctTableDto.setFddj(subDataDeviceDto.getVoltageLevel() + "kV");//电压等级；
                siteDataHfctTableDto.setBcsb(subDataDeviceDto.getDeviceName() + ""); //被测设备；
                siteDataHfctTableDto.setXjList(new ArrayList<>());//巡检文件列表
                siteDataHfctTableDto.setTjList(new ArrayList<>());//统计文件列表
                siteDataHfctTableDto.setTjlbList(new ArrayList<>());//录波文件列表
                siteDataHfctTableDto.setKjgzpList(new ArrayList<>());//可将光文件列表
                int seqNo = 0;
                //获取当前位置数据
                List<DataAnalyzeDto> analyzeDtos = dataAnalyzeService.findDataIsList(new DataAnalyzeDto() {{
                    setDataDeviceSiteId(dataDeviceSiteDto.getId());
                }});
                if (dataList != null) {
                    for (DataAnalyzeDto analyzeDto : analyzeDtos) {
                        //可见光照片
                        siteDataHfctTableDto.setPhotos(analyzeDto.getPhotos());
                        switch (analyzeDto.getDataFormat()) {
                            case "LC": {
                                //负荷电流
                                DataLcDto dataLcDto = dataLcService.findDataById(new DataLcDto() {{
                                    setDataAnalyzeId(analyzeDto.getId());
                                }});
                                siteDataHfctTableDto.setFh(dataLcDto.getX4());
                                break;
                            }
                            case "HJ": {
                                break;
                            }
                            case "HFCT_XJ": {
                                DataTableDto xjDataTableDto = new DataTableDto() {{
                                    setDataDeviceDto(siteDataHfctTableDto.getDataDeviceDto());
                                    setDataDeviceSiteDto(siteDataHfctTableDto.getDataDeviceSiteDto());
                                }};
                                //HFCT巡检检测文件
                                xjDataTableDto.setFileUrl(analyzeDto.getFileUrl());
                                //HFCT巡检截图
                                xjDataTableDto.setScreenshots(analyzeDto.getScreenshots());
                                //HFCT巡检照片
                                xjDataTableDto.setPhotos(analyzeDto.getPhotos());
                                DataHfctXjDto dataHfctXjDto = dataHfctXjService.findDataById(new DataHfctXjDto() {{
                                    setDataAnalyzeId(analyzeDto.getId());
                                }});
                                if (dataHfctXjDto != null) {
                                    xjDataTableDto.setCssj("" + dataHfctXjDto.getX3());//测试时间；
                                    xjDataTableDto.setCkyz("" + dataHfctXjDto.getX10());//参考阈值；
                                    xjDataTableDto.setFdzy("" + dataHfctXjDto.getX11());//放大增益；
                                    xjDataTableDto.setQzsj("" + dataHfctXjDto.getX12());//前置衰减；
                                    xjDataTableDto.setTbfs("" + (dataHfctXjDto.getX20() == 1 ? "外同步" : "内同步"));//同步方式 同步类型外同步:0x01内同步:0x02默认写入内同步
                                    xjDataTableDto.setTbpl("" + dataHfctXjDto.getX21());//同步频率；
                                    xjDataTableDto.setXtpl("" + dataHfctXjDto.getX19());//系统频率；
                                    xjDataTableDto.setFz(dataHfctXjDto.getX5());
                                    siteDataHfctTableDto.getXjList().add(xjDataTableDto);
                                    //-----------
                                    if (siteDataHfctTableDto.getKjgzpList().size() == 0) {
                                        xjDataTableDto.setPhotos(analyzeDto.getPhotos());
                                        siteDataHfctTableDto.getKjgzpList().add(xjDataTableDto);
                                    }
                                }

                                break;
                            }
                            case "HFCT_XJZS": {
                                break;
                            }
                            case "HFCT_TJ": {
                                DataTableDto tjDataTableDto = new DataTableDto() {{
                                    setDataDeviceDto(siteDataHfctTableDto.getDataDeviceDto());
                                    setDataDeviceSiteDto(siteDataHfctTableDto.getDataDeviceSiteDto());
                                }};
                                //HFCT统计检测文件
                                tjDataTableDto.setFileUrl(analyzeDto.getFileUrl());
                                //HFCT统计截图
                                tjDataTableDto.setScreenshots(analyzeDto.getScreenshots());
                                //HFCT巡检照片
                                siteDataHfctTableDto.setPhotos(analyzeDto.getPhotos());
                                DataHfctTjDto dataHfctTjDto = dataHfctTjService.findDataById(new DataHfctTjDto() {{
                                    setDataAnalyzeId(analyzeDto.getId());
                                }});
                                tjDataTableDto.setDataHfctTjDto(dataHfctTjDto);
                                tjDataTableDto.setCssj("" + dataHfctTjDto.getX14());//测试时间；
                                tjDataTableDto.setXwwy("" + dataHfctTjDto.getX19());//相位位移；
                                tjDataTableDto.setCkyz("" + dataHfctTjDto.getX18());//参考阈值；
                                tjDataTableDto.setFdzy("" + dataHfctTjDto.getX23());//放大增益；
                                tjDataTableDto.setQzsj("" + dataHfctTjDto.getX24());//前置衰减；
                                tjDataTableDto.setTbfs("" + (dataHfctTjDto.getX20() == 1 ? "外同步" : "内同步"));//同步方式 同步类型外同步:0x01内同步:0x02默认写入内同步
                                tjDataTableDto.setTbpl("" + dataHfctTjDto.getX21());//同步频率；
                                tjDataTableDto.setXtpl("" + dataHfctTjDto.getX22());//系统频率；
                                tjDataTableDto.setFz(dataHfctTjDto.getX16());//幅值；
                                tjDataTableDto.setMc("" + dataHfctTjDto.getX17());//脉冲；
                                tjDataTableDto.setYz("" + dataHfctTjDto.getX18());//阈值；
                                tjDataTableDto.setAtlasArr(JSONObject.parseObject(dataHfctTjDto.getX11(), Float[][].class));
                                siteDataHfctTableDto.getTjList().add(tjDataTableDto);
                                //-----------
                                if (siteDataHfctTableDto.getKjgzpList().size() == 0) {
                                    tjDataTableDto.setPhotos(analyzeDto.getPhotos());
                                    siteDataHfctTableDto.getKjgzpList().add(tjDataTableDto);
                                }
                                break;
                            }
                            case "HFCT_TJZS": {
                                break;
                            }
                            case "HFCT_TJLB": {
                                //HFCT统计录波
                                DataTableDto tjlbDataTableDto = new DataTableDto() {{
                                    setDataDeviceDto(siteDataHfctTableDto.getDataDeviceDto());
                                    setDataDeviceSiteDto(siteDataHfctTableDto.getDataDeviceSiteDto());
                                }};
                                DataHfctTjlbDto dataHfctTjlbDto = dataHfctTjlbService.findDataById(new DataHfctTjlbDto() {{
                                    setDataAnalyzeId(analyzeDto.getId());
                                }});
                                tjlbDataTableDto.setSeqNo(seqNo++);
                                tjlbDataTableDto.setCssj("" + dataHfctTjlbDto.getX3());//测试时间；
                                tjlbDataTableDto.setXwwy("" + dataHfctTjlbDto.getX13());//相位位移；
                                tjlbDataTableDto.setCkyz("" + dataHfctTjlbDto.getX12());//参考阈值；
                                tjlbDataTableDto.setFdzy("" + dataHfctTjlbDto.getX17());//放大增益；
                                tjlbDataTableDto.setQzsj("" + dataHfctTjlbDto.getX18());//前置衰减；
                                tjlbDataTableDto.setTbfs("" + (dataHfctTjlbDto.getX14() == 1 ? "外同步" : "内同步"));//同步方式 同步类型外同步:0x01内同步:0x02默认写入内同步
                                tjlbDataTableDto.setTbpl("" + dataHfctTjlbDto.getX15());//同步频率；
                                tjlbDataTableDto.setXtpl("" + dataHfctTjlbDto.getX16());//系统频率；
                                tjlbDataTableDto.setYz("" + dataHfctTjlbDto.getX12());//阈值；

                                tjlbDataTableDto.setAtlasArr(JSONObject.parseObject(dataHfctTjlbDto.getX11(), Float[][].class));
                                siteDataHfctTableDto.getTjlbList().add(tjlbDataTableDto);
                                //-----------
                                if (siteDataHfctTableDto.getKjgzpList().size() == 0) {
                                    tjlbDataTableDto.setPhotos(analyzeDto.getPhotos());
                                    siteDataHfctTableDto.getKjgzpList().add(tjlbDataTableDto);
                                }
                                break;
                            }
                        }
                    }
                }
                dataList.add(siteDataHfctTableDto);
            }
            deviceSiteTableView.setItems(dataList);
            //序号
            idTableColumn.setCellFactory((col) -> {
                TableCell<DataHfctTableDto, String> cell = new TableCell<DataHfctTableDto, String>() {
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
                TableCell<DataHfctTableDto, String> cell = new TableCell<DataHfctTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        this.setStyle("-fx-min-width: 300;-fx-text-fill: #000;-fx-underline: false;");
                        if (!empty) {
                            DataHfctTableDto dataHfctTableDto = this.getTableView().getItems().get(this.getIndex());
                            this.setText(dataHfctTableDto.getSite());
                        }
                    }
                };
                return cell;
            });
            //xj;//HFCT巡检幅值
            xjTableColumn.setCellFactory((col) -> {
                TableCell<DataHfctTableDto, String> cell = new TableCell<DataHfctTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        this.setStyle("-fx-text-fill: #000;-fx-underline: false;");
                        if (!empty) {
                            DataHfctTableDto dataHfctTableDto = this.getTableView().getItems().get(this.getIndex());
                            Float fz = 0f;
                            if (dataHfctTableDto.getXjList() != null && dataHfctTableDto.getXjList().size() > 0) {
                                for (DataTableDto dto : dataHfctTableDto.getXjList()) {
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
            //xjjt;//HFCT巡检截图
            xjjtTableColumn.setCellFactory((col) -> {
                TableCell<DataHfctTableDto, String> cell = new TableCell<DataHfctTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        if (!empty) {
                            DataHfctTableDto dataHfctTableDto = this.getTableView().getItems().get(this.getIndex());
                            loadImageDialog(testTechnology, 1, this, "巡检截图", dataHfctTableDto.getXjList());
                        }
                    }
                };
                return cell;
            });
            //kjg;//可见光照片
            kjgTableColumn.setCellFactory((col) -> {
                TableCell<DataHfctTableDto, String> cell = new TableCell<DataHfctTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        if (!empty) {
                            DataHfctTableDto dataHfctTableDto = this.getTableView().getItems().get(this.getIndex());
                            loadImageDialog(testTechnology, 2, this, "可见光照片", dataHfctTableDto.getKjgzpList());
                        }
                    }
                };
                return cell;
            });
            //tj;//HFCT统计检测文件
            tjTableColumn.setCellFactory((col) -> {
                TableCell<DataHfctTableDto, String> cell = new TableCell<DataHfctTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        if (!empty) {
                            DataHfctTableDto dataHfctTableDto = this.getTableView().getItems().get(this.getIndex());
                            loadTjDialog(testTechnology, this, finalSubDataDeviceDto.getDeviceName() + "," + dataHfctTableDto.getSite(), "HFCT文件", dataHfctTableDto.getTjList());
                        }
                    }
                };
                return cell;
            });
            //tjjt;//HFCT统计截图
            tjjtTableColumn.setCellFactory((col) -> {
                TableCell<DataHfctTableDto, String> cell = new TableCell<DataHfctTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        if (!empty) {
                            DataHfctTableDto dataHfctTableDto = this.getTableView().getItems().get(this.getIndex());
                            loadImageDialog(testTechnology, 1, this, "统计截图", dataHfctTableDto.getTjList());
                        }
                    }
                };
                return cell;
            });
            //tjlb;//HFCT统计录波
            tjlbTableColumn.setCellFactory((col) -> {
                TableCell<DataHfctTableDto, String> cell = new TableCell<DataHfctTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        if (!empty) {
                            DataHfctTableDto dataHfctTableDto = this.getTableView().getItems().get(this.getIndex());
                            loadTjlbDialog(testTechnology, this, finalSubDataDeviceDto.getDeviceName() + "," + dataHfctTableDto.getSite(), "统计录波", dataHfctTableDto.getTjlbList());
                        }
                    }
                };
                return cell;
            });
            //fh;//负荷电流
            fhTableColumn.setCellFactory((col) -> {
                TableCell<DataHfctTableDto, String> cell = new TableCell<DataHfctTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        if (!empty) {
                            DataHfctTableDto dataHfctTableDto = this.getTableView().getItems().get(this.getIndex());
                            this.setText(dataHfctTableDto.getFh() != null ? "" + dataHfctTableDto.getFh() : "--");
                            this.setOnMouseClicked((me) -> {
                                log.debug("负荷电流==>{}", dataHfctTableDto.getFh());
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