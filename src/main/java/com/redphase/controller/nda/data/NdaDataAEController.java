package com.redphase.controller.nda.data;

import com.alibaba.fastjson.JSONObject;
import com.redphase.controller.nda.NdaBaseController;
import com.redphase.dto.account.AccountDto;
import com.redphase.dto.atlas.*;
import com.redphase.dto.atlas.ae.*;
import com.redphase.dto.table.DataAeTableDto;
import com.redphase.dto.table.DataTableDto;
import com.redphase.dto.world.ae.AeDataDto;
import com.redphase.dto.world.ae.AeListDto;
import com.redphase.dto.sys.SysVariableDto;
import com.redphase.framework.util.CommonConstant;
import com.redphase.framework.util.DateUtil;
import com.redphase.framework.util.I18nUtil;
import com.redphase.framework.util.ValidatorUtil;
import com.redphase.framework.util.word.AeWordUtil;
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
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@FXMLController
@Slf4j
public class NdaDataAEController extends NdaBaseController implements Initializable {
    @FXML
    BorderPane dataBorderPane;
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
    Label xjzs1L;//巡检金属噪声1
    @FXML
    Label xjzsjt1L;//巡检金属截图1
    List xjzsjt1List;
    @FXML
    Label xjzs2L;//巡检金属噪声2
    @FXML
    Label xjzsjt2L;//巡检金属截图2
    List xjzsjt2List;
    @FXML
    Label xjzs3L;//巡检金属噪声3
    @FXML
    Label xjzsjt3L;//巡检金属截图3
    List xjzsjt3List;

    @FXML
    Label tjzs1L;//统计金属噪声1
    @FXML
    Label tjzsjt1L;//统计金属截图1
    List tjzsjt1List;
    @FXML
    Label tjzs2L;//统计金属噪声2
    @FXML
    Label tjzsjt2L;//统计金属截图2
    List tjzsjt2List;
    @FXML
    Label tjzs3L;//统计金属噪声3
    @FXML
    Label tjzsjt3L;//统计金属截图3
    List tjzsjt3List;
    @FXML
    TableView<DataAeTableDto> deviceSiteTableView;//检测位置列表
    @FXML
    TableColumn<DataAeTableDto, String> idTableColumn;//序号
    @FXML
    TableColumn<DataAeTableDto, String> siteTableColumn;//检测位置
    @FXML
    TableColumn<DataAeTableDto, String> xjzdzTableColumn;//AE巡检最大值
    @FXML
    TableColumn<DataAeTableDto, String> xjyxzTableColumn;//AE巡检有效值
    @FXML
    TableColumn<DataAeTableDto, String> xjf1fzTableColumn;//AE巡检f1分值
    @FXML
    TableColumn<DataAeTableDto, String> xjf2fzTableColumn;//AE巡检f2分值
    @FXML
    TableColumn<DataAeTableDto, String> xjjtTableColumn;//AE巡检截图
    //    @FXML
//    TableColumn<DataAeTableDto, String> xjlyTableColumn;//AE巡检录音
    @FXML
    TableColumn<DataAeTableDto, String> tjTableColumn;//AE统计检测
    @FXML
    TableColumn<DataAeTableDto, String> tjjtTableColumn;//AE统计截图
    @FXML
    TableColumn<DataAeTableDto, String> tjlbTableColumn;//AE统计录波
    //    @FXML
//    TableColumn<DataAeTableDto, String> fxTableColumn;//AE飞行检测
    @FXML
    TableColumn<DataAeTableDto, String> fxjtTableColumn;//AE飞行截图
    //    @FXML
//    TableColumn<DataAeTableDto, String> bxTableColumn;//AE波形检测
    @FXML
    TableColumn<DataAeTableDto, String> bxjtTableColumn;//AE波形截图
    @FXML
    TableColumn<DataAeTableDto, String> kjgTableColumn;//可见光照片
    @FXML
    TableColumn<DataAeTableDto, String> fhTableColumn;//负荷电流

    boolean initFlag = false;
    ObservableList<DataAeTableDto> dataList;
    List<DataDeviceSiteDto> dataDeviceSiteDtoList;
    DataDeviceDto subDataDeviceDto;
    DataHjDto dataHjDto;
    String siteStr = "";
    //    Float zdxj;
    Float zdxjzs;
    String testTechnology = "AE";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize...");
        if (initFlag) return;
        initFlag = true;
        dataBorderPane.prefWidthProperty().bind(ndaController.getRightAnchorPane().prefWidthProperty());
        dataBorderPane.prefHeightProperty().bind(ndaController.getRightAnchorPane().prefHeightProperty());
    }

    @FXML
    public void report() {
        log.debug("report..");
        try {
            String fullName = (String) ehcacheUtil.getCache(CommonConstant.NDA_DATA_SKILL_FULL_NAME);
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
            AeDataDto dataDto = new AeDataDto() {{
                //一、基本信息
                setBdz(accountDto.getSubstation());//变电站
                //setWtdw("A-委托单位");//委托单位
                setSydw(accountDto.getRunDept());//试验单位
                //setYxbh("A-运行编号");//运行编号
                //setSyxz("A-试验性质");//试验性质
                //setSyrq("A-试验日期");//试验日期
                String[] jcryArr = {dataHjDto.getX6(), dataHjDto.getX7(), dataHjDto.getX8(), dataHjDto.getX9(), dataHjDto.getX10()};
                setSyry(Arrays.stream(jcryArr).filter(s -> StringUtils.isNotEmpty(s)).collect(Collectors.joining(",")));//试验人员
                // setSydd("A-试验地点");//试验地点
                setBgrq(DateUtil.getCurDateStr("yyyy-MM-dd"));//报告日期
                // setBzr("A-编制人");//编制人
                // setShr("A-审核人");//审核人
                // setPzr("A-批准人");//批准人
                setTq(dataHjService.getWeather(dataHjDto.getX3()));//试验天气
                setWd("" + dataHjDto.getX4());//环境温度（℃）
                setSd("" + dataHjDto.getX5());//环境相对湿度（%）
                //二、设备铭牌
                setSccj(accountDto.getManufacturer());//生产厂家
                setCcrq(DateUtil.getDateStr(accountDto.getProduceDate()));//出厂日期
                setCcbh(accountDto.getDeviceCode());//出厂编号
                setSbxh(accountDto.getDeviceVersion());//设备型号
                setEddy(accountDto.getVoltageLevel() + "");//额定电压(kV)
                //三、检测数据
                setBjzs(zdxjzs + "");//背景噪声
                //setTzfx("A-特征分析");//特征分析
                setBjz(zdxjzs + "");//背景值
                setYqcj(I18nUtil.get("report.yqcj"));//仪器厂家
                setYqxh(I18nUtil.get("report.yqxh"));//仪器型号
//                setYqbh();//仪器编号
                //setBz("A-备注");//备注
                setDtoList(new ArrayList<AeListDto>() {{
                    if (dataList != null) {
                        for (int i = 0; i < dataList.size(); i++) {
                            DataAeTableDto aeTableDto = dataList.get(i);
                            int finalI = i;
                            add(new AeListDto() {{
                                setXh("" + (finalI + 1));// 序号
                                setJcwz(aeTableDto.getSite());//检测位置
                                Float fz = 0f;
                                if (aeTableDto.getXjList() != null && aeTableDto.getXjList().size() > 0) {
                                    for (DataTableDto dto : aeTableDto.getXjList()) {
                                        if (fz < dto.getFz()) {
                                            fz = dto.getFz();
                                            if (ValidatorUtil.notEmpty(dto.getScreenshots()) && !"[]".equals(dto.getScreenshots())) {
                                                List<String> imgs = JSONObject.parseArray(dto.getScreenshots(), String.class);
                                                setTpwj(imgs.get(0));//备注（可见光照片）
                                            }
                                        }
                                    }
                                }
                                setJcsz("" + fz);//检测数值
//                                if (aeTableDto.getTjList() != null && aeTableDto.getTjList().size() > 0) {
//                                    ndaSiteCountController.init(testTechnology, aeTableDto.getTjList(), null);
//                                    String tmpPngPath = System.getProperty("java.io.tmpdir") + "/" + System.nanoTime() + ".png";
//                                    ndaSiteCountController.reportToData();
//                                    ImageIO.write(SwingFXUtils.fromFXImage(ndaSiteCountController.getPrpsPane().snapshot(new SnapshotParameters(), null), null), "png", new File(tmpPngPath));
//                                    setTpwj(tmpPngPath);// 图谱文件
//                                }
                                setFh(aeTableDto.getFh() + "");//负荷电流(A)
                                //  setJl("A-结论");//结论
//                                if(aeTableDto.getKjgzpList()!=null && aeTableDto.getKjgzpList().size()>0){
//                                    DataTableDto kjgDto=aeTableDto.getKjgzpList().get(0);
//                                    if(ValidatorUtil.notEmpty(kjgDto.getPhotos()) && !"[]".equals(kjgDto.getPhotos())){
//                                        List<String> imgs=JSONObject.parseArray(kjgDto.getPhotos(),String.class);
//                                        setKjg(imgs.get(0));//备注（可见光照片）
//                                    }
//                                }
                            }});
                        }
                    }
                }});

            }};
            SysVariableDto variableDto = setupService.getVariableByCode(new SysVariableDto() {{
                setCode("i-path-reports");
            }});
            new AeWordUtil().report(variableDto.getValue() + File.separator + "超声波（AE）局部放电检测报告-" + (DateUtil.getCurDateStr("yyyyMMddHHmmsss")) + ".doc", dataDto);
            ialert.success("检测报告已生成!");
        } catch (Exception e) {
            ialert.error("超声波（AE）局部放电检测报告-生成失败!" + e.getMessage());
        }
    }

    public void loadAEDetail() {
        clearL(tjzsjt1L, tjzs2L, tjzsjt2L, tjzs3L, tjzsjt3L, xjzs1L, xjzsjt1L, xjzs2L, xjzsjt2L, xjzs3L, xjzsjt3L);
        xjzsjt1List = new ArrayList();
        xjzsjt2List = new ArrayList();
        xjzsjt3List = new ArrayList();
        tjzsjt1List = new ArrayList();
        tjzsjt2List = new ArrayList();
        tjzsjt3List = new ArrayList();
        try {
            DataDeviceDto mainDataDeviceDto = (DataDeviceDto) ehcacheUtil.getCache(CommonConstant.NDA_DATA_SKILL_DEVICE_MAIN);
            subDataDeviceDto = (DataDeviceDto) ehcacheUtil.getCache(CommonConstant.NDA_DATA_SKILL_DEVICE_SUB);
            DataDeviceDto finalSubDataDeviceDto = subDataDeviceDto != null ? subDataDeviceDto : mainDataDeviceDto;
            String deviceType = "";
            switch (finalSubDataDeviceDto.getDeviceType()) {//设备类型(1、开关柜；2、变压器；3、组合电器；4、电缆)
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
            testTechnology = finalSubDataDeviceDto.getTestTechnology();
            typeL.setText(deviceType + "名称:");//
            titleL.setText(finalSubDataDeviceDto.getDeviceName());//
            bdzL.setText(finalSubDataDeviceDto.getSubstation());//变电站名称
            sbmcL.setText(finalSubDataDeviceDto.getDeviceName());//设备名称
            dydjL.setText(finalSubDataDeviceDto.getVoltageLevel());//电压等级
            sblxL.setText(deviceType);//设备类型
            jgmcL.setText(finalSubDataDeviceDto.getSpaceName());//间隔名称
            jcrqL.setText(finalSubDataDeviceDto.getDateDetection());//检测日期

            siteStr = subDataDeviceDto.getSubstation() + "," + subDataDeviceDto.getDeviceName();

            List<DataAnalyzeDto> dataAnalyzeDtoList = dataAnalyzeService.findDataIsList(new DataAnalyzeDto() {{
                setDataDeviceId(finalSubDataDeviceDto.getId());
            }});
            //位置信息
            String finalDeviceType = deviceType;
            dataDeviceSiteDtoList = dataDeviceSiteService.findDataIsList(new DataDeviceSiteDto() {{
                if (("开关柜".equalsIgnoreCase(finalDeviceType) && "TEV".equalsIgnoreCase(testTechnology))) {
                    setDataDeviceId(mainDataDeviceDto.getId());
                } else {
                    setDataDeviceId(finalSubDataDeviceDto.getId());
                }
            }});
            dataList = FXCollections.observableArrayList();
            for (DataDeviceSiteDto dataDeviceSiteDto : dataDeviceSiteDtoList) {
                DataAeTableDto siteDataAeTableDto = new DataAeTableDto();
                siteDataAeTableDto.setDataDeviceDto(finalSubDataDeviceDto);
                siteDataAeTableDto.setDataDeviceSiteDto(dataDeviceSiteDto);
                siteDataAeTableDto.setSite(dataDeviceSiteDto.getSiteName()); //检测位置
                siteDataAeTableDto.setFddj(finalSubDataDeviceDto.getVoltageLevel() + "kV");//电压等级；
                siteDataAeTableDto.setBcsb(finalSubDataDeviceDto.getDeviceName() + ""); //被测设备；
                siteDataAeTableDto.setXjList(new ArrayList<>());//巡检文件列表
                siteDataAeTableDto.setTjList(new ArrayList<>());//统计文件列表
                siteDataAeTableDto.setTjlbList(new ArrayList<>());//录波文件列表
                siteDataAeTableDto.setFxList(new ArrayList<>());//飞行文件列表
                siteDataAeTableDto.setBxList(new ArrayList<>());//波形文件列表
                siteDataAeTableDto.setKjgzpList(new ArrayList<>());//可将光文件列表
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
                                siteDataAeTableDto.setFh(dataLcDto.getX4());
                                break;
                            }
                            case "HJ": {
                                break;
                            }
                            case "AE_XJ": {
                                DataAeTableDto xjDataTableDto = new DataAeTableDto() {{
                                    setDataDeviceDto(siteDataAeTableDto.getDataDeviceDto());
                                    setDataDeviceSiteDto(siteDataAeTableDto.getDataDeviceSiteDto());
                                }};
                                //AE巡检检测文件
                                xjDataTableDto.setFileUrl(analyzeDto.getFileUrl());
                                //AE巡检截图
                                xjDataTableDto.setScreenshots(analyzeDto.getScreenshots());
                                //巡检录音
                                xjDataTableDto.setAudios(analyzeDto.getAudios());
                                DataAeXjDto dataAeXjDto = dataAeXjService.findDataById(new DataAeXjDto() {{
                                    setDataAnalyzeId(analyzeDto.getId());
                                }});
                                if (dataAeXjDto != null) {
//                                    if (zdxj == null || dataAeXjDto.getX5() > zdxj) {
//                                        zdxj = dataAeXjDto.getX5();
//                                    }
                                    xjDataTableDto.setCssj("" + dataAeXjDto.getX3());//测试时间；
                                    xjDataTableDto.setCkyz("" + dataAeXjDto.getX14());//参考阈值；
                                    siteDataAeTableDto.setTbpl("" + dataAeXjDto.getX19());//同步频率；
                                    siteDataAeTableDto.setXtpl("" + dataAeXjDto.getX18());//系统频率；
                                    xjDataTableDto.setYxz(dataAeXjDto.getX7());//有效值
                                    xjDataTableDto.setF1(dataAeXjDto.getX8());//f1分量
                                    xjDataTableDto.setF2(dataAeXjDto.getX9());//f2分量
                                    xjDataTableDto.setFz(dataAeXjDto.getX5());
                                    siteDataAeTableDto.getXjList().add(xjDataTableDto);
                                    //-----------
                                    if (siteDataAeTableDto.getKjgzpList().size() == 0) {
                                        xjDataTableDto.setPhotos(analyzeDto.getPhotos());
                                        siteDataAeTableDto.getKjgzpList().add(xjDataTableDto);
                                    }
                                }
                                break;
                            }
                            case "AE_XJZS": {
                                DataAeXjzsDto dataAeXjzsDto = dataAeXjzsService.findDataById(new DataAeXjzsDto() {{
                                    setDataAnalyzeId(analyzeDto.getId());
                                }});
                                if (dataAeXjzsDto != null) {
                                    if (zdxjzs == null || dataAeXjzsDto.getX5() > zdxjzs) {
                                        zdxjzs = dataAeXjzsDto.getX5();
                                    }
                                }
                                break;
                            }
                            case "AE_TJ": {
                                DataAeTableDto tjDataTableDto = new DataAeTableDto() {{
                                    setDataDeviceDto(siteDataAeTableDto.getDataDeviceDto());
                                    setDataDeviceSiteDto(siteDataAeTableDto.getDataDeviceSiteDto());
                                }};
                                //AE统计检测文件
                                tjDataTableDto.setFileUrl(analyzeDto.getFileUrl());
                                //AE统计截图
                                tjDataTableDto.setScreenshots(analyzeDto.getScreenshots());
                                DataAeTjDto dataAeTjDto = dataAeTjService.findDataById(new DataAeTjDto() {{
                                    setDataAnalyzeId(analyzeDto.getId());
                                }});
                                if (dataAeTjDto != null) {
                                    tjDataTableDto.setCssj("" + dataAeTjDto.getX14());//测试时间；
                                    tjDataTableDto.setXwwy("" + dataAeTjDto.getX19());//相位位移；
                                    tjDataTableDto.setCkyz("" + dataAeTjDto.getX18());//参考阈值；
                                    tjDataTableDto.setFdzy("" + dataAeTjDto.getX23());//放大增益；
                                    tjDataTableDto.setTbfs("" + (new Integer(dataAeTjDto.getX20()) == 1 ? "外同步" : "内同步"));//同步方式 同步类型外同步:0x01内同步:0x02默认写入内同步
                                    tjDataTableDto.setTbpl("" + dataAeTjDto.getX21());//同步频率；
                                    tjDataTableDto.setXtpl("" + dataAeTjDto.getX22());//系统频率；
                                    tjDataTableDto.setFz(dataAeTjDto.getX16());//幅值；
                                    tjDataTableDto.setMc("" + dataAeTjDto.getX17());//脉冲；
                                    tjDataTableDto.setYz("" + dataAeTjDto.getX18());//阈值；
                                    tjDataTableDto.setQzzy("" + dataAeTjDto.getX27());//前置增益；
                                    tjDataTableDto.setAtlasArr(JSONObject.parseObject(dataAeTjDto.getX11(), Float[][].class));
                                    siteDataAeTableDto.getTjList().add(tjDataTableDto);
                                    //-----------
                                    if (siteDataAeTableDto.getKjgzpList().size() == 0) {
                                        tjDataTableDto.setPhotos(analyzeDto.getPhotos());
                                        siteDataAeTableDto.getKjgzpList().add(tjDataTableDto);
                                    }
                                }
                                break;
                            }
                            case "AE_TJZS": {
                                break;
                            }
                            case "AE_TJLB": {
                                //AE统计录波
                                DataAeTableDto tjlbDataTableDto = new DataAeTableDto() {{
                                    setDataDeviceDto(siteDataAeTableDto.getDataDeviceDto());
                                    setDataDeviceSiteDto(siteDataAeTableDto.getDataDeviceSiteDto());
                                }};
                                DataAeTjlbDto dataAeTjlbDto = dataAeTjlbService.findDataById(new DataAeTjlbDto() {{
                                    setDataAnalyzeId(analyzeDto.getId());
                                }});
                                if (dataAeTjlbDto != null) {
                                    tjlbDataTableDto.setSeqNo(seqNo++);
                                    tjlbDataTableDto.setCssj("" + dataAeTjlbDto.getX3());//测试时间；
                                    tjlbDataTableDto.setXwwy("" + dataAeTjlbDto.getX13());//相位位移；
                                    tjlbDataTableDto.setCkyz("" + dataAeTjlbDto.getX12());//参考阈值；
                                    tjlbDataTableDto.setFdzy("" + dataAeTjlbDto.getX17());//放大增益；
                                    tjlbDataTableDto.setTbfs("" + (new Integer(dataAeTjlbDto.getX14()) == 1 ? "外同步" : "内同步"));//同步方式 同步类型外同步:0x01内同步:0x02默认写入内同步
                                    tjlbDataTableDto.setTbpl("" + dataAeTjlbDto.getX15());//同步频率；
                                    tjlbDataTableDto.setXtpl("" + dataAeTjlbDto.getX16());//系统频率；
                                    tjlbDataTableDto.setQzzy("" + dataAeTjlbDto.getX21());//前置增益；
                                    tjlbDataTableDto.setAtlasArr(JSONObject.parseObject(dataAeTjlbDto.getX11(), Float[][].class));
                                    siteDataAeTableDto.getTjlbList().add(tjlbDataTableDto);
                                    //-----------
                                    if (siteDataAeTableDto.getKjgzpList().size() == 0) {
                                        tjlbDataTableDto.setPhotos(analyzeDto.getPhotos());
                                        siteDataAeTableDto.getKjgzpList().add(tjlbDataTableDto);
                                    }
                                }
                                break;
                            }
                            case "AE_FX": {
                                //AE飞行
                                DataAeTableDto fxDataTableDto = new DataAeTableDto() {{
                                    setDataDeviceDto(siteDataAeTableDto.getDataDeviceDto());
                                    setDataDeviceSiteDto(siteDataAeTableDto.getDataDeviceSiteDto());
                                }};
                                //AE飞行文件
                                fxDataTableDto.setFileUrl(analyzeDto.getFileUrl());
                                //AE飞行截图
                                fxDataTableDto.setScreenshots(analyzeDto.getScreenshots());
                                DataAeFxDto dataAeFxDto = dataAeFxService.findDataById(new DataAeFxDto() {{
                                    setDataAnalyzeId(analyzeDto.getId());
                                }});
                                if (dataAeFxDto != null) {
                                    fxDataTableDto.setCssj("" + dataAeFxDto.getX3());//测试时间；
                                    fxDataTableDto.setCkyz("" + dataAeFxDto.getX12());//参考阈值；
                                    fxDataTableDto.setTbfs("" + (new Integer(dataAeFxDto.getX20()) == 1 ? "外同步" : "内同步"));//同步方式 同步类型外同步:0x01内同步:0x02默认写入内同步
                                    fxDataTableDto.setTbpl("" + dataAeFxDto.getX21());//同步频率；
                                    fxDataTableDto.setXtpl("" + dataAeFxDto.getX19());//系统频率；
                                    fxDataTableDto.setKmsj(dataAeFxDto.getX14());//开门时间；
                                    fxDataTableDto.setBssj(dataAeFxDto.getX15());//闭锁时间；
                                    fxDataTableDto.setDdsj(dataAeFxDto.getX16());//等待时间；
                                    fxDataTableDto.setAtlasArr(JSONObject.parseObject(dataAeFxDto.getX11(), Float[][].class));
                                    siteDataAeTableDto.getFxList().add(fxDataTableDto);
                                    //-----------
                                    if (siteDataAeTableDto.getKjgzpList().size() == 0) {
                                        fxDataTableDto.setPhotos(analyzeDto.getPhotos());
                                        siteDataAeTableDto.getKjgzpList().add(fxDataTableDto);
                                    }
                                }
                                break;
                            }
                            case "AE_BX": {
                                DataAeTableDto bxDataTableDto = new DataAeTableDto() {{
                                    setDataDeviceDto(siteDataAeTableDto.getDataDeviceDto());
                                    setDataDeviceSiteDto(siteDataAeTableDto.getDataDeviceSiteDto());
                                }};
                                //AE波形文件
                                bxDataTableDto.setFileUrl(analyzeDto.getFileUrl());
                                //AE波形截图
                                bxDataTableDto.setScreenshots(analyzeDto.getScreenshots());
                                DataAeBxDto dataAeBxDto = dataAeBxService.findDataById(new DataAeBxDto() {{
                                    setDataAnalyzeId(analyzeDto.getId());
                                }});
                                if (dataAeBxDto != null) {
                                    bxDataTableDto.setCssj("" + dataAeBxDto.getX3());//测试时间；
                                    bxDataTableDto.setTbfs("" + (new Integer(dataAeBxDto.getX13()) == 1 ? "外同步" : "内同步"));//同步方式 同步类型外同步:0x01内同步:0x02默认写入内同步
                                    bxDataTableDto.setTbpl("" + dataAeBxDto.getX14());//同步频率；
                                    bxDataTableDto.setXtpl("" + dataAeBxDto.getX15());//系统频率；

                                    siteDataAeTableDto.getBxList().add(bxDataTableDto);
                                    //-----------
                                    if (siteDataAeTableDto.getKjgzpList().size() == 0) {
                                        bxDataTableDto.setPhotos(analyzeDto.getPhotos());
                                        siteDataAeTableDto.getKjgzpList().add(bxDataTableDto);
                                    }
                                }
                            }
                        }
                    }
                }
                dataList.add(siteDataAeTableDto);
            }
            this.dataList = dataList;
            deviceSiteTableView.setItems(dataList);
            //序号
            idTableColumn.setCellFactory((col) -> {
                TableCell<DataAeTableDto, String> cell = new TableCell<DataAeTableDto, String>() {
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
                TableCell<DataAeTableDto, String> cell = new TableCell<DataAeTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        this.setStyle("-fx-min-width: 300;-fx-text-fill: #000;-fx-underline: false;");
                        if (!empty) {
                            DataAeTableDto dataAeTableDto = this.getTableView().getItems().get(this.getIndex());
                            this.setText(dataAeTableDto.getSite());
                        }
                    }
                };
                return cell;
            });
            //AE巡检最大值
            xjzdzTableColumn.setCellFactory((col) -> {
                TableCell<DataAeTableDto, String> cell = new TableCell<DataAeTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        this.setStyle("-fx-min-width: 300;-fx-text-fill: #000;-fx-underline: false;");
                        if (!empty) {
                            DataAeTableDto dataAeTableDto = this.getTableView().getItems().get(this.getIndex());
                            Float fz = 0f;
                            if (dataAeTableDto.getXjList() != null && dataAeTableDto.getXjList().size() > 0) {
                                for (DataTableDto dto : dataAeTableDto.getXjList()) {
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
            //xj;//AE巡检有效值
            xjyxzTableColumn.setCellFactory((col) -> {
                TableCell<DataAeTableDto, String> cell = new TableCell<DataAeTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        this.setStyle("-fx-text-fill: #000;-fx-underline: false;");
                        if (!empty) {
                            DataAeTableDto dataAeTableDto = this.getTableView().getItems().get(this.getIndex());
                            Float yxz = 0f;
                            if (dataAeTableDto.getXjList() != null && dataAeTableDto.getXjList().size() > 0) {
                                for (DataTableDto dto : dataAeTableDto.getXjList()) {
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
            //xj;//AE巡检F1
            xjf1fzTableColumn.setCellFactory((col) -> {
                TableCell<DataAeTableDto, String> cell = new TableCell<DataAeTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        this.setStyle("-fx-text-fill: #000;-fx-underline: false;");
                        if (!empty) {
                            DataAeTableDto dataAeTableDto = this.getTableView().getItems().get(this.getIndex());
                            Float f1 = 0f;
                            if (dataAeTableDto.getXjList() != null && dataAeTableDto.getXjList().size() > 0) {
                                for (DataTableDto dto : dataAeTableDto.getXjList()) {
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
            //xj;//AE巡检F2
            xjf2fzTableColumn.setCellFactory((col) -> {
                TableCell<DataAeTableDto, String> cell = new TableCell<DataAeTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        this.setStyle("-fx-text-fill: #000;-fx-underline: false;");
                        if (!empty) {
                            DataAeTableDto dataAeTableDto = this.getTableView().getItems().get(this.getIndex());
                            Float f2 = 0f;
                            if (dataAeTableDto.getXjList() != null && dataAeTableDto.getXjList().size() > 0) {
                                for (DataTableDto dto : dataAeTableDto.getXjList()) {
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
            //xjjt;//AE巡检截图
            xjjtTableColumn.setCellFactory((col) -> {
                TableCell<DataAeTableDto, String> cell = new TableCell<DataAeTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        if (!empty) {
                            DataAeTableDto dataAeTableDto = this.getTableView().getItems().get(this.getIndex());
                            loadImageDialog(testTechnology, 1, this, "巡检截图", dataAeTableDto.getXjList());
                        }
                    }
                };
                return cell;
            });
//            //tj://AE巡检录音
//            xjlyTableColumn.setCellFactory((col) -> {
//                TableCell<DataAeTableDto, String> cell = new TableCell<DataAeTableDto, String>() {
//                    @Override
//                    public void updateItem(String item, boolean empty) {
//                        super.updateItem(item, empty);
//                        this.setText(null);
//                        this.setGraphic(null);
//                        if (!empty) {
//                            DataAeTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
//                            loadSoundDialog(this,"录音",tableDto.getXjList());
//                        }
//                    }
//                };
//                return cell;
//            });

            //tj;//AE统计检测文件
            tjTableColumn.setCellFactory((col) -> {
                TableCell<DataAeTableDto, String> cell = new TableCell<DataAeTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        if (!empty) {
                            DataAeTableDto dataAeTableDto = this.getTableView().getItems().get(this.getIndex());
                            loadTjDialog(testTechnology, this, finalSubDataDeviceDto.getDeviceName() + "," + dataAeTableDto.getSite(), "AE文件", dataAeTableDto.getTjList());
                        }
                    }
                };
                return cell;
            });
            //tjjt;//AE统计截图
            tjjtTableColumn.setCellFactory((col) -> {
                TableCell<DataAeTableDto, String> cell = new TableCell<DataAeTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        if (!empty) {
                            DataAeTableDto dataAeTableDto = this.getTableView().getItems().get(this.getIndex());
                            loadImageDialog(testTechnology, 1, this, "统计截图", dataAeTableDto.getTjList());
                        }
                    }
                };
                return cell;
            });
            //tjlb;//AE统计录波
            tjlbTableColumn.setCellFactory((col) -> {
                TableCell<DataAeTableDto, String> cell = new TableCell<DataAeTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        if (!empty) {
                            DataAeTableDto dataAeTableDto = this.getTableView().getItems().get(this.getIndex());
                            loadTjlbDialog(testTechnology, this, finalSubDataDeviceDto.getDeviceName() + "," + dataAeTableDto.getSite(), "统计录波", dataAeTableDto.getTjlbList());
                        }
                    }
                };
                return cell;
            });

//            //fx;//AE飞行检测文件
//            fxTableColumn.setCellFactory((col) -> {
//                TableCell<DataAeTableDto, String> cell = new TableCell<DataAeTableDto, String>() {
//                    @Override
//                    public void updateItem(String item, boolean empty) {
//                        super.updateItem(item, empty);
//                        this.setText(null);
//                        this.setGraphic(null);
//                        if (!empty) {
//                            DataAeTableDto dataAeTableDto = this.getTableView().getItems().get(this.getIndex());
//                            if (dataAeTableDto.getFxList() != null && dataAeTableDto.getFxList().size() > 0) {
//                                this.setStyle("-fx-text-fill: #31a1ff;-fx-underline: true;-fx-cursor: hand;");
//                                this.setText("AE文件");
//                                this.setOnMouseClicked((me) -> {
//                                    log.debug("AE飞行检测文件==>{}", dataAeTableDto.getFileUrl());
//                                    Double width = Application.getStage().getWidth() - 200;
//                                    Double height = Application.getStage().getHeight() - 100;
//                                    idialog.openDialog(finalSubDataDeviceDto.getDeviceName() + "," + dataAeTableDto.getSite(), ndaDataSiteCountView, width, height);
//                                    ndaSiteCountController.init(testTechnology, dataAeTableDto.getFxList(), new HashMap() {{
////                                        put("dataTableDto",dataAeTableDto);
//                                    }});
//                                });
//                            } else {
//                                this.setText("--");
//                            }
//                        }
//                    }
//                };
//                return cell;
//            });
            //fxjt;//AE飞行截图
            fxjtTableColumn.setCellFactory((col) -> {
                TableCell<DataAeTableDto, String> cell = new TableCell<DataAeTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        if (!empty) {
                            DataAeTableDto dataAeTableDto = this.getTableView().getItems().get(this.getIndex());
                            loadImageDialog(testTechnology, 1, this, "飞行截图", dataAeTableDto.getFxList());
                        }
                    }
                };
                return cell;
            });
//            //bx;//AE波形检测文件
//            bxTableColumn.setCellFactory((col) -> {
//                TableCell<DataAeTableDto, String> cell = new TableCell<DataAeTableDto, String>() {
//                    @Override
//                    public void updateItem(String item, boolean empty) {
//                        super.updateItem(item, empty);
//                        this.setText(null);
//                        this.setGraphic(null);
//                        if (!empty) {
//                            DataAeTableDto dataAeTableDto = this.getTableView().getItems().get(this.getIndex());
//                            if (dataAeTableDto.getBxList() != null && dataAeTableDto.getBxList().size() > 0) {
//                                this.setStyle("-fx-text-fill: #31a1ff;-fx-underline: true;-fx-cursor: hand;");
//                                this.setText("AE文件");
//                                this.setOnMouseClicked((me) -> {
//                                    log.debug("AE波形检测文件==>{}", dataAeTableDto.getFileUrl());
//                                    Double width = Application.getStage().getWidth() - 200;
//                                    Double height = Application.getStage().getHeight() - 100;
//                                    idialog.openDialog(finalSubDataDeviceDto.getDeviceName() + "," + dataAeTableDto.getSite(), ndaDataSiteCountView, width, height);
//                                    ndaSiteCountController.init(testTechnology, dataAeTableDto.getBxList(), new HashMap() {{
////                                        put("dataTableDto",dataAeTableDto);
//                                    }});
//                                });
//                            } else {
//                                this.setText("--");
//                            }
//                        }
//                    }
//                };
//                return cell;
//            });
            //bxtj;//AE波形统计截图
            bxjtTableColumn.setCellFactory((col) -> {
                TableCell<DataAeTableDto, String> cell = new TableCell<DataAeTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        if (!empty) {
                            DataAeTableDto dataAeTableDto = this.getTableView().getItems().get(this.getIndex());
                            loadImageDialog(testTechnology, 1, this, "波形截图", dataAeTableDto.getBxList());
                        }
                    }
                };
                return cell;
            });

            //kjg;//可见光照片
            kjgTableColumn.setCellFactory((col) -> {
                TableCell<DataAeTableDto, String> cell = new TableCell<DataAeTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        if (!empty) {
                            DataAeTableDto dataAeTableDto = this.getTableView().getItems().get(this.getIndex());
                            loadImageDialog(testTechnology, 2, this, "可见光照片", dataAeTableDto.getKjgzpList());
                        }
                    }
                };
                return cell;
            });
            //fh;//负荷电流
            fhTableColumn.setCellFactory((col) -> {
                TableCell<DataAeTableDto, String> cell = new TableCell<DataAeTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        if (!empty) {
                            DataAeTableDto dataAeTableDto = this.getTableView().getItems().get(this.getIndex());
                            this.setText(dataAeTableDto.getFh() != null ? "" + dataAeTableDto.getFh() : "--");
                            this.setOnMouseClicked((me) -> {
                                log.debug("负荷电流==>{}", dataAeTableDto.getFh());
                            });
                        }
                    }
                };
                return cell;
            });
            for (DataAnalyzeDto dataAnalyzeDto : dataAnalyzeDtoList) {
                DataAeTableDto deviceDataAeTableDto = new DataAeTableDto();
                deviceDataAeTableDto.setDataDeviceDto(finalSubDataDeviceDto);
//                dataHfctTableDto.setDataDeviceSiteDto(dataDeviceSiteDto);
//                dataHfctTableDto.setSite(dataDeviceSiteDto.getSiteName()); //检测位置
                deviceDataAeTableDto.setFddj(finalSubDataDeviceDto.getVoltageLevel() + "kV");//电压等级；
                deviceDataAeTableDto.setBcsb(finalSubDataDeviceDto.getDeviceName() + ""); //被测设备；
                deviceDataAeTableDto.setFileUrl(dataAnalyzeDto.getFileUrl());
                deviceDataAeTableDto.setScreenshots(dataAnalyzeDto.getScreenshots());
                deviceDataAeTableDto.setAudios(dataAnalyzeDto.getAudios());

                if ("HJ".equalsIgnoreCase(dataAnalyzeDto.getDataFormat()) && 0 == (dataAnalyzeDto.getOrderNo())) {
                    dataHjDto = dataHjService.findDataById(new DataHjDto() {{
                        setDataAnalyzeId(dataAnalyzeDto.getId());
                    }});
                    wdL.setText("" + dataHjDto.getX4());//温度
                    sdL.setText("" + dataHjDto.getX5());//湿度
                    tqqkL.setText(dataHjService.getWeather(dataHjDto.getX3()));//天气情况
                    String[] jcryArr = {dataHjDto.getX6(), dataHjDto.getX7(), dataHjDto.getX8(), dataHjDto.getX9(), dataHjDto.getX10()};
                    jcryL.setText(Arrays.stream(jcryArr).filter(s -> StringUtils.isNotEmpty(s)).collect(Collectors.joining()));//检测人员
                } else if ("LC".equalsIgnoreCase(dataAnalyzeDto.getDataFormat()) && 0 == (dataAnalyzeDto.getOrderNo())) {
                    DataLcDto dataLcDto = dataLcService.findDataById(new DataLcDto() {{
                        setDataAnalyzeId(dataAnalyzeDto.getId());
                    }});
                    log.debug("dataLcDto=>{}", dataLcDto);
                } else if ("AE_TJZS".equalsIgnoreCase(dataAnalyzeDto.getDataFormat())) {
                    DataAeTjzsDto dataAeTjzsDto = dataAeTjzsService.findDataById(new DataAeTjzsDto() {{
                        setDataAnalyzeId(dataAnalyzeDto.getId());
                    }});
                    deviceDataAeTableDto.setCssj("" + dataAeTjzsDto.getX14());//测试时间；
                    deviceDataAeTableDto.setXwwy("" + dataAeTjzsDto.getX19());//相位位移；
                    deviceDataAeTableDto.setCkyz("" + dataAeTjzsDto.getX18());//参考阈值；
                    deviceDataAeTableDto.setFdzy("" + dataAeTjzsDto.getX23());//放大增益；
                    deviceDataAeTableDto.setQzzy("" + dataAeTjzsDto.getX27());//前置增益；
                    deviceDataAeTableDto.setTbfs("" + (new Integer(dataAeTjzsDto.getX20()) == 1 ? "外同步" : "内同步"));//同步方式 同步类型外同步:0x01内同步:0x02默认写入内同步
                    deviceDataAeTableDto.setTbpl("" + dataAeTjzsDto.getX21());//同步频率；
                    deviceDataAeTableDto.setXtpl("" + dataAeTjzsDto.getX22());//系统频率；
                    deviceDataAeTableDto.setMc("" + dataAeTjzsDto.getX17());//脉冲；
                    deviceDataAeTableDto.setFz(dataAeTjzsDto.getX16());//幅值；
                    switch (dataAnalyzeDto.getOrderNo()) {
                        case 0:
                            tjzs1L.setText("" + dataAeTjzsDto.getX16());
                            tjzsjt1List.add(deviceDataAeTableDto);
                            loadImageDialog(testTechnology, 1, tjzsjt1L, "统计截图", tjzsjt1List);
                            break;
                        case 1:
                            tjzs2L.setText("" + dataAeTjzsDto.getX16());
                            tjzsjt2List.add(deviceDataAeTableDto);
                            loadImageDialog(testTechnology, 1, tjzsjt2L, "统计截图", tjzsjt2List);
                            break;
                        case 2:
                            tjzs3L.setText("" + dataAeTjzsDto.getX16());
                            tjzsjt3List.add(deviceDataAeTableDto);
                            loadImageDialog(testTechnology, 1, tjzsjt3L, "统计截图", tjzsjt3List);
                            break;
                    }
                } else if ("AE_XJZS".equalsIgnoreCase(dataAnalyzeDto.getDataFormat())) {
                    DataAeXjzsDto dataAeXjzsDto = dataAeXjzsService.findDataById(new DataAeXjzsDto() {{
                        setDataAnalyzeId(dataAnalyzeDto.getId());
                    }});
                    deviceDataAeTableDto.setCssj("" + dataAeXjzsDto.getX3());//测试时间；
                    deviceDataAeTableDto.setCkyz("" + dataAeXjzsDto.getX14());//参考阈值；
                    deviceDataAeTableDto.setTbpl("" + dataAeXjzsDto.getX19());//同步频率；
                    deviceDataAeTableDto.setXtpl("" + dataAeXjzsDto.getX18());//系统频率；
                    deviceDataAeTableDto.setQzzy("" + dataAeXjzsDto.getX23());//前置增益；
                    deviceDataAeTableDto.setYxz(dataAeXjzsDto.getX7());//有效值；
                    deviceDataAeTableDto.setF1(dataAeXjzsDto.getX16());//ｆ１分量；
                    deviceDataAeTableDto.setF2(dataAeXjzsDto.getX17());//ｆ２分量；
                    switch (dataAnalyzeDto.getOrderNo()) {
                        case 0:
                            xjzs1L.setText("" + setScale(dataAeXjzsDto.getX5()));
                            xjzsjt1List.add(deviceDataAeTableDto);
                            loadImageDialog(testTechnology, 1, xjzsjt1L, "巡检截图", xjzsjt1List);
                            break;
                        case 1:
                            xjzs2L.setText("" + setScale(dataAeXjzsDto.getX5()));
                            xjzsjt2List.add(deviceDataAeTableDto);
                            loadImageDialog(testTechnology, 1, xjzsjt2L, "巡检截图", xjzsjt2List);
                            break;
                        case 2:
                            xjzs3L.setText("" + setScale(dataAeXjzsDto.getX5()));
                            xjzsjt3List.add(deviceDataAeTableDto);
                            loadImageDialog(testTechnology, 1, xjzsjt3L, "巡检截图", xjzsjt3List);
                            break;
                    }
                }
            }
        } catch (Exception e) {
            log.error("系统异常!", e);
            ialert.error(I18nUtil.get("error.sys") + e.getMessage());
        }
    }

    private Float setScale(Float ff) {
        try {
            if (ff != null) {
                return new BigDecimal(ff).setScale(1, RoundingMode.DOWN).floatValue();
            } else {
                return ff;
            }
        } catch (Exception e) {
            return ff;
        }
    }

    @Override
    public void dispose() {

    }
}