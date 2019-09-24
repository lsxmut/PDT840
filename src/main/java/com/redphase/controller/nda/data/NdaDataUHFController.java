package com.redphase.controller.nda.data;

import com.alibaba.fastjson.JSONObject;
import com.redphase.controller.nda.NdaBaseController;
import com.redphase.dto.account.AccountDto;
import com.redphase.dto.atlas.*;
import com.redphase.dto.table.DataTableDto;
import com.redphase.dto.table.DataUhfTableDto;
import com.redphase.dto.atlas.uhf.*;
import com.redphase.dto.world.uhf.UhfDataDto;
import com.redphase.dto.world.uhf.UhfListDto;
import com.redphase.dto.sys.SysVariableDto;
import com.redphase.framework.util.CommonConstant;
import com.redphase.framework.util.DateUtil;
import com.redphase.framework.util.I18nUtil;
import com.redphase.framework.util.word.UhfWordUtil;
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
public class NdaDataUHFController extends NdaBaseController implements Initializable {
    @Autowired
    protected NdaSiteImagesController ndaSiteImagesController;
    @Autowired
    protected NdaSiteCountController ndaSiteCountController;
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
    Label tjms1zs1L;//统计模式1噪声1
    @FXML
    Label tjms1jt1L;//统计模式1截图1
    List tjms1jt1List;
    @FXML
    Label tjms1zs2L;//统计模式1噪声2
    @FXML
    Label tjms1jt2L;//统计模式1截图2
    List tjms1jt2List;
    @FXML
    Label tjms1zs3L;//统计模式1噪声3
    @FXML
    Label tjms1jt3L;//统计模式1截图3
    List tjms1jt3List;
    @FXML
    Label tjms2zs1L;//统计模式2噪声1
    @FXML
    Label tjms2jt1L;//统计模式2截图1
    List tjms2jt1List;
    @FXML
    Label tjms2zs2L;//统计模式2噪声2
    @FXML
    Label tjms2jt2L;//统计模式2截图2
    List tjms2jt2List;
    @FXML
    Label tjms2zs3L;//统计模式2噪声3
    @FXML
    Label tjms2jt3L;//统计模式2截图3
    List tjms2jt3List;

    @FXML
    TableView<DataUhfTableDto> deviceSiteTableView;//检测位置列表
    @FXML
    TableColumn<DataUhfTableDto, String> idTableColumn;//序号
    @FXML
    TableColumn<DataUhfTableDto, String> siteTableColumn;//检测位置
    @FXML
    TableColumn<DataUhfTableDto, String> kjgTableColumn;//可见光照片
    @FXML
    TableColumn<DataUhfTableDto, String> tj1wjTableColumn;//UHF统计模式1检测文件
    @FXML
    TableColumn<DataUhfTableDto, String> tj1jtTableColumn;//UHF统计模式1检测截图
    @FXML
    TableColumn<DataUhfTableDto, String> tj1lbTableColumn;//UHF统计模式1检测录波

    @FXML
    TableColumn<DataUhfTableDto, String> tj2wjTableColumn;//UHF统计模式2检测文件
    @FXML
    TableColumn<DataUhfTableDto, String> tj2jtTableColumn;//UHF统计模式2检测截图
    @FXML
    TableColumn<DataUhfTableDto, String> tj2lbTableColumn;//UHF统计模式2录波
    @FXML
    TableColumn<DataUhfTableDto, String> fhTableColumn;//负荷电流


    boolean initFlag = false;
    ObservableList<DataUhfTableDto> dataList;
    List<DataDeviceSiteDto> dataDeviceSiteDtoList;
    DataDeviceDto subDataDeviceDto;
    DataHjDto dataHjDto;
    String siteStr = "";
    String testTechnology = "UHF";

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
                setDeviceType(DeviceType.getValueByText(dirArr[2]));
            }});
            UhfDataDto dataDto = new UhfDataDto() {{
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
                setTyrq(DateUtil.getDateStr(accountDto.getUseDate()));//投运日期
                //三、检测数据
                setDtoList(new ArrayList<UhfListDto>() {{
                    if (dataList != null) {
                        for (int i = 0; i < dataList.size(); i++) {
                            DataUhfTableDto uhfTableDto = dataList.get(i);
                            int finalI = i;
                            add(new UhfListDto() {{
                                DataTableDto tableDto = uhfTableDto.getTj1List().get(0);
                                setXh("" + (finalI + 1));// 序号
                                setJcwz(uhfTableDto.getSite());//检测位置
                                setFh(uhfTableDto.getFh() + "");
                                if (uhfTableDto.getTj1List() != null && uhfTableDto.getTj1List().size() > 0) {
                                    ndaSiteCountController.init(testTechnology, uhfTableDto.getTj1List(), null);
                                    String tmpPngPath = System.getProperty("java.io.tmpdir") + "/" + System.nanoTime() + ".png";
                                    ndaSiteCountController.reportToData();
                                    ImageIO.write(SwingFXUtils.fromFXImage(ndaSiteCountController.getPrpsPane().snapshot(new SnapshotParameters(), null), null), "png", new File(tmpPngPath));
                                    setTpwj(tmpPngPath);// 图谱文件
//                                setFd("");// 是否存在放电信号（打勾）
                                }
                            }});
                        }
                    }
                }});
//                setTzfx("A-特征分析");// 特征分析
                setYqxh(I18nUtil.get("report.yqxh"));// 检测仪器
//                setJl("A-结论");// 结论
//                setBz("A-备注");// 备注
            }};
            SysVariableDto variableDto = setupService.getVariableByCode(new SysVariableDto() {{
                setCode("i-path-reports");
            }});
            new UhfWordUtil().report(variableDto.getValue() + File.separator + "特高频局部放电检测报告-" + (DateUtil.getCurDateStr("yyyyMMddHHmmsss")) + ".doc", dataDto);
            ialert.success("检测报告已生成!");
        } catch (Exception e) {
            ialert.error("特高频局部放电检测报告-生成失败!" + e.getMessage());
        }
    }

    public void loadUHFDetail() {
        try {
            clearL(typeL, titleL, bdzL, sbmcL, tqqkL, dydjL, sblxL, jgmcL, jcryL, jcrqL, wdL, sdL, dqzsL, tjms1zs1L, tjms1jt1L, tjms1zs2L, tjms1jt2L, tjms1zs3L, tjms1jt3L, tjms2zs1L, tjms2jt1L, tjms2zs2L, tjms2jt2L, tjms2zs3L, tjms2jt3L);
            tjms1jt1List = new ArrayList();
            tjms1jt2List = new ArrayList();
            tjms1jt3List = new ArrayList();
            tjms2jt1List = new ArrayList();
            tjms2jt2List = new ArrayList();
            tjms2jt3List = new ArrayList();
            DataDeviceDto mainDataDeviceDto = (DataDeviceDto) ehcacheUtil.getCache(CommonConstant.NDA_DATA_SKILL_DEVICE_MAIN);
            subDataDeviceDto = (DataDeviceDto) ehcacheUtil.getCache(CommonConstant.NDA_DATA_SKILL_DEVICE_SUB);
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
            dydjL.setText(subDataDeviceDto.getVoltageLevel() + "");//电压等级
            sblxL.setText(deviceType);//设备类型
            jgmcL.setText(subDataDeviceDto.getSpaceName());//间隔名称
            jcrqL.setText(subDataDeviceDto.getDateDetection());//检测日期

            siteStr = subDataDeviceDto.getSubstation() + "," + subDataDeviceDto.getDeviceName();
            List<DataAnalyzeDto> dataAnalyzeDtoList = dataAnalyzeService.findDataIsList(new DataAnalyzeDto() {{
                setDataDeviceId(finalSubDataDeviceDto.getId());
            }});
            //位置信息seqNo
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
                DataUhfTableDto dataUhfTableDto = new DataUhfTableDto();
                dataUhfTableDto.setDataDeviceDto(subDataDeviceDto);
                dataUhfTableDto.setDataDeviceSiteDto(dataDeviceSiteDto);
                dataUhfTableDto.setSite(dataDeviceSiteDto.getSiteName()); //检测位置
                dataUhfTableDto.setFddj(subDataDeviceDto.getVoltageLevel() + "kV");//电压等级；
                dataUhfTableDto.setBcsb(subDataDeviceDto.getDeviceName() + ""); //被测设备；
                dataUhfTableDto.setXjList(new ArrayList<>());//巡检文件列表
                dataUhfTableDto.setTj1List(new ArrayList<>());//统计文件1列表
                dataUhfTableDto.setTj2List(new ArrayList<>());//统计文件2列表
                dataUhfTableDto.setTjlb1List(new ArrayList<>());//录波1文件列表
                dataUhfTableDto.setTjlb2List(new ArrayList<>());//录波2文件列表
                dataUhfTableDto.setKjgzpList(new ArrayList<>());//可将光文件列表
                int seqNo = 0;
                //获取当前位置巡检信息
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
                                dataUhfTableDto.setFh(dataLcDto.getX4());
                                break;
                            }
                            case "HJ": {
                                break;
                            }
                            case "UHF_TJ_1": {
                                DataUhfTableDto tj1DataTableDto = new DataUhfTableDto() {{
                                    setDataDeviceDto(dataUhfTableDto.getDataDeviceDto());
                                    setDataDeviceSiteDto(dataUhfTableDto.getDataDeviceSiteDto());
                                }};
                                //UHF统计检测文件
                                tj1DataTableDto.setFileUrl(analyzeDto.getFileUrl());
                                //HFCT巡检截图
                                tj1DataTableDto.setScreenshots(analyzeDto.getScreenshots());
                                DataUhfTj1Dto dataUhfTj1Dto = dataUhfTj1Service.findDataById(new DataUhfTj1Dto() {{
                                    setDataAnalyzeId(analyzeDto.getId());
                                }});
                                if (dataUhfTj1Dto != null) {
                                    tj1DataTableDto.setDataUhfTj1Dto(dataUhfTj1Dto);
                                    tj1DataTableDto.setCssj("" + dataUhfTj1Dto.getX14());//测试时间；
                                    tj1DataTableDto.setXwwy("" + dataUhfTj1Dto.getX19());//相位位移；
                                    tj1DataTableDto.setCkyz("" + dataUhfTj1Dto.getX18());//参考阈值；
                                    tj1DataTableDto.setTbfs("" + (dataUhfTj1Dto.getX20() == 1 ? "外同步" : "内同步"));//同步方式 同步类型外同步:0x01内同步:0x02默认写入内同步
                                    tj1DataTableDto.setTbpl("" + dataUhfTj1Dto.getX21());//同步频率；
                                    tj1DataTableDto.setXtpl("" + dataUhfTj1Dto.getX22());//系统频率；
                                    tj1DataTableDto.setFz(dataUhfTj1Dto.getX16());//幅值；
                                    tj1DataTableDto.setMc("" + dataUhfTj1Dto.getX17());//脉冲；
                                    tj1DataTableDto.setYz("" + dataUhfTj1Dto.getX18());//阈值；
                                    tj1DataTableDto.setAtlasArr(JSONObject.parseObject(dataUhfTj1Dto.getX11(), Float[][].class));
                                    dataUhfTableDto.getTj1List().add(tj1DataTableDto);
                                    //-----------
                                    if (dataUhfTableDto.getKjgzpList().size() == 0) {
                                        tj1DataTableDto.setPhotos(analyzeDto.getPhotos());
                                        dataUhfTableDto.getKjgzpList().add(tj1DataTableDto);
                                    }
                                }
                                break;
                            }
                            case "UHF_TJZS_1": {
                                break;
                            }
                            case "UHF_TJ_2": {
                                DataUhfTableDto tj2DataTableDto = new DataUhfTableDto() {{
                                    setDataDeviceDto(dataUhfTableDto.getDataDeviceDto());
                                    setDataDeviceSiteDto(dataUhfTableDto.getDataDeviceSiteDto());
                                }};
                                //UHF统计检测文件
                                tj2DataTableDto.setFileUrl(analyzeDto.getFileUrl());
                                //HFCT巡检截图
                                tj2DataTableDto.setScreenshots(analyzeDto.getScreenshots());
                                DataUhfTj2Dto dataUhfTj2Dto = dataUhfTj2Service.findDataById(new DataUhfTj2Dto() {{
                                    setDataAnalyzeId(analyzeDto.getId());
                                }});
                                if (dataUhfTj2Dto != null) {
                                    tj2DataTableDto.setCssj("" + dataUhfTj2Dto.getX14());//测试时间；
                                    tj2DataTableDto.setXwwy("" + dataUhfTj2Dto.getX19());//相位位移；
                                    tj2DataTableDto.setCkyz("" + dataUhfTj2Dto.getX18());//参考阈值；
                                    tj2DataTableDto.setTbfs("" + (new Integer(dataUhfTj2Dto.getX20()) == 1 ? "外同步" : "内同步"));//同步方式 同步类型外同步:0x01内同步:0x02默认写入内同步
                                    tj2DataTableDto.setTbpl("" + dataUhfTj2Dto.getX21());//同步频率；
                                    tj2DataTableDto.setXtpl("" + dataUhfTj2Dto.getX22());//系统频率；
                                    tj2DataTableDto.setFz(dataUhfTj2Dto.getX16());//幅值；
                                    tj2DataTableDto.setMc("" + dataUhfTj2Dto.getX17());//脉冲；
                                    tj2DataTableDto.setYz("" + dataUhfTj2Dto.getX18());//阈值；
                                    tj2DataTableDto.setAtlasArr(JSONObject.parseObject(dataUhfTj2Dto.getX11(), Float[][].class));
                                    dataUhfTableDto.getTj2List().add(tj2DataTableDto);
                                    //-----------
                                    if (dataUhfTableDto.getKjgzpList().size() == 0) {
                                        tj2DataTableDto.setPhotos(analyzeDto.getPhotos());
                                        dataUhfTableDto.getKjgzpList().add(tj2DataTableDto);
                                    }
                                }
                                break;
                            }
                            case "UHF_TJZS_2": {
                                break;
                            }
                            case "UHF_TJLB_1": {
                                //UHF统计录波
                                DataUhfTableDto tjlb1DataTableDto = new DataUhfTableDto() {{
                                    setDataDeviceDto(dataUhfTableDto.getDataDeviceDto());
                                    setDataDeviceSiteDto(dataUhfTableDto.getDataDeviceSiteDto());
                                }};
                                DataUhfTjlb1Dto dataUhfTjlb1Dto = dataUhfTjlb1Service.findDataById(new DataUhfTjlb1Dto() {{
                                    setDataAnalyzeId(analyzeDto.getId());
                                }});
                                if (dataUhfTjlb1Dto != null) {
                                    tjlb1DataTableDto.setSeqNo(seqNo);
                                    tjlb1DataTableDto.setCssj("" + dataUhfTjlb1Dto.getX3());//测试时间；
                                    tjlb1DataTableDto.setXwwy("" + dataUhfTjlb1Dto.getX13());//相位位移；
                                    tjlb1DataTableDto.setCkyz("" + dataUhfTjlb1Dto.getX12());//参考阈值；
                                    tjlb1DataTableDto.setTbfs("" + (dataUhfTjlb1Dto.getX14() == 1 ? "外同步" : "内同步"));//同步方式 同步类型外同步:0x01内同步:0x02默认写入内同步
                                    tjlb1DataTableDto.setTbpl("" + dataUhfTjlb1Dto.getX15());//同步频率；
                                    tjlb1DataTableDto.setXtpl("" + dataUhfTjlb1Dto.getX16());//系统频率；
                                    tjlb1DataTableDto.setAtlasArr(JSONObject.parseObject(dataUhfTjlb1Dto.getX11(), Float[][].class));
                                    dataUhfTableDto.getTjlb1List().add(tjlb1DataTableDto);
                                    //-----------
                                    if (dataUhfTableDto.getKjgzpList().size() == 0) {
                                        tjlb1DataTableDto.setPhotos(analyzeDto.getPhotos());
                                        dataUhfTableDto.getKjgzpList().add(tjlb1DataTableDto);
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
                dataList.add(dataUhfTableDto);
            }
            this.dataList = dataList;

            deviceSiteTableView.setItems(dataList);
            //序号
            idTableColumn.setCellFactory((col) -> {
                TableCell<DataUhfTableDto, String> cell = new TableCell<DataUhfTableDto, String>() {
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
                TableCell<DataUhfTableDto, String> cell = new TableCell<DataUhfTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setGraphic(null);
                        this.setStyle("-fx-min-width: 300;-fx-text-fill: #000;-fx-underline: false;");
                        if (!empty) {
                            DataUhfTableDto UhfTableData = this.getTableView().getItems().get(this.getIndex());
                            this.setText(UhfTableData.getSite());
                        }
                    }
                };
                return cell;
            });
            //tj1wj;//UHF统计模式1检测文件
            tj1wjTableColumn.setCellFactory((col) -> {
                TableCell<DataUhfTableDto, String> cell = new TableCell<DataUhfTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        if (!empty) {
                            DataUhfTableDto dataUhfTableDto = this.getTableView().getItems().get(this.getIndex());
                            loadTjDialog(testTechnology, this, subDataDeviceDto.getDeviceName() + "," + dataUhfTableDto.getSite(), "UHF统计1文件", dataUhfTableDto.getTj1List());
                        }
                    }
                };
                return cell;
            });
            //tj1jt;//UHF统计模式1检测截图
            tj1jtTableColumn.setCellFactory((col) -> {
                TableCell<DataUhfTableDto, String> cell = new TableCell<DataUhfTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        if (!empty) {
                            DataUhfTableDto dataUhfTableDto = this.getTableView().getItems().get(this.getIndex());
                            loadImageDialog(testTechnology, 1, this, "检测截图", dataUhfTableDto.getTj1List());
                        }
                    }
                };
                return cell;
            });
            //tjlb;//UHF统计模式1录波
            tj1lbTableColumn.setCellFactory((col) -> {
                TableCell<DataUhfTableDto, String> cell = new TableCell<DataUhfTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        if (!empty) {
                            DataUhfTableDto dataUhfTableDto = this.getTableView().getItems().get(this.getIndex());
                            loadTjlbDialog(testTechnology, this, subDataDeviceDto.getDeviceName() + "," + dataUhfTableDto.getSite(), "统计录波", dataUhfTableDto.getTjlb1List());
                        }
                    }
                };
                return cell;
            });
            //tj2wj;//UHF统计模式2检测文件
            tj2wjTableColumn.setCellFactory((col) -> {
                TableCell<DataUhfTableDto, String> cell = new TableCell<DataUhfTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        if (!empty) {
                            DataUhfTableDto dataUhfTableDto = this.getTableView().getItems().get(this.getIndex());
                            loadTjDialog(testTechnology, this, subDataDeviceDto.getDeviceName() + "," + dataUhfTableDto.getSite(), "UHF统计2文件", dataUhfTableDto.getTj2List());
                        }
                    }
                };
                return cell;
            });
            //tj2jt;//UHF统计模式2检测截图
            tj2jtTableColumn.setCellFactory((col) -> {
                TableCell<DataUhfTableDto, String> cell = new TableCell<DataUhfTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        if (!empty) {
                            DataUhfTableDto dataUhfTableDto = this.getTableView().getItems().get(this.getIndex());
                            loadImageDialog(testTechnology, 1, this, "统计截图", dataUhfTableDto.getTj2List());
                        }
                    }
                };
                return cell;
            });
            //kjg;//可见光照片
            kjgTableColumn.setCellFactory((col) -> {
                TableCell<DataUhfTableDto, String> cell = new TableCell<DataUhfTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        if (!empty) {
                            DataUhfTableDto dataUhfTableDto = this.getTableView().getItems().get(this.getIndex());
                            loadImageDialog(testTechnology, 2, this, "可见光照片", dataUhfTableDto.getKjgzpList());
                        }
                    }
                };
                return cell;
            });
            //fh;//负荷电流
            fhTableColumn.setCellFactory((col) -> {
                TableCell<DataUhfTableDto, String> cell = new TableCell<DataUhfTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        this.setStyle("-fx-text-fill: #000;-fx-underline: false;");
                        if (!empty) {
                            DataUhfTableDto dataUhfTableDto = this.getTableView().getItems().get(this.getIndex());
                            this.setText(dataUhfTableDto.getFh() != null ? "" + dataUhfTableDto.getFh() : "--");
                            this.setOnMouseClicked((me) -> {
                                log.debug("负荷电流==>{}", dataUhfTableDto.getFh());
                            });
                        }
                    }
                };
                return cell;
            });
            for (DataAnalyzeDto dataAnalyzeDto : dataAnalyzeDtoList) {
                DataUhfTableDto deviceDataUhfTableDto = new DataUhfTableDto();
                deviceDataUhfTableDto.setDataDeviceDto(subDataDeviceDto);
//                dataHfctTableDto.setDataDeviceSiteDto(dataDeviceSiteDto);
//                dataHfctTableDto.setSite(dataDeviceSiteDto.getSiteName()); //检测位置
                deviceDataUhfTableDto.setFddj(subDataDeviceDto.getVoltageLevel() + "kV");//电压等级；
                deviceDataUhfTableDto.setBcsb(subDataDeviceDto.getDeviceName() + ""); //被测设备；
                deviceDataUhfTableDto.setFileUrl(dataAnalyzeDto.getFileUrl());
                deviceDataUhfTableDto.setScreenshots(dataAnalyzeDto.getScreenshots());
                deviceDataUhfTableDto.setAudios(dataAnalyzeDto.getAudios());
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
                } else if ("UHF_TJZS_1".equalsIgnoreCase(dataAnalyzeDto.getDataFormat())) {
                    DataUhfTjzs1Dto dataUhfTjzs1Dto = dataUhfTjzs1Service.findDataById(new DataUhfTjzs1Dto() {{
                        setDataAnalyzeId(dataAnalyzeDto.getId());
                    }});
                    if (dataUhfTjzs1Dto != null) {
                        deviceDataUhfTableDto.setCssj("" + dataUhfTjzs1Dto.getX14());//测试时间；
                        deviceDataUhfTableDto.setXwwy("" + dataUhfTjzs1Dto.getX19());//相位位移；
                        deviceDataUhfTableDto.setCkyz("" + dataUhfTjzs1Dto.getX18());//参考阈值；
                        deviceDataUhfTableDto.setTbfs("" + (dataUhfTjzs1Dto.getX20() == 1 ? "外同步" : "内同步"));//同步方式 同步类型外同步:0x01内同步:0x02默认写入内同步
                        deviceDataUhfTableDto.setTbpl("" + dataUhfTjzs1Dto.getX21());//同步频率；
                        deviceDataUhfTableDto.setXtpl("" + dataUhfTjzs1Dto.getX22());//系统频率；
                        switch (dataAnalyzeDto.getOrderNo()) {
                            case 0:
                                tjms1zs1L.setText("" + dataUhfTjzs1Dto.getX16());
                                tjms1jt1List.add(deviceDataUhfTableDto);
                                loadImageDialog(testTechnology, 1, tjms1jt1L, "统计截图", tjms1jt1List);
                                break;
                            case 1:
                                tjms1zs2L.setText("" + dataUhfTjzs1Dto.getX16());
                                tjms1jt2List.add(deviceDataUhfTableDto);
                                loadImageDialog(testTechnology, 1, tjms1zs2L, "统计截图", tjms1jt2List);
                                break;
                            case 2:
                                tjms1zs3L.setText("" + dataUhfTjzs1Dto.getX16());
                                tjms1jt3List.add(deviceDataUhfTableDto);
                                loadImageDialog(testTechnology, 1, tjms1jt3L, "统计截图", tjms1jt3List);
                                break;
                        }
                    }
                } else if ("UHF_TJZS_2".equalsIgnoreCase(dataAnalyzeDto.getDataFormat())) {
                    DataUhfTjzs2Dto dataUhfTjzs2Dto = dataUhfTjzs2Service.findDataById(new DataUhfTjzs2Dto() {{
                        setDataAnalyzeId(dataAnalyzeDto.getId());
                    }});
                    if (dataUhfTjzs2Dto != null) {
                        deviceDataUhfTableDto.setCssj("" + dataUhfTjzs2Dto.getX14());//测试时间；
                        deviceDataUhfTableDto.setXwwy("" + dataUhfTjzs2Dto.getX19());//相位位移；
                        deviceDataUhfTableDto.setCkyz("" + dataUhfTjzs2Dto.getX18());//参考阈值；
                        deviceDataUhfTableDto.setTbfs("" + (dataUhfTjzs2Dto.getX20() == 1 ? "外同步" : "内同步"));//同步方式 同步类型外同步:0x01内同步:0x02默认写入内同步
                        deviceDataUhfTableDto.setTbpl("" + dataUhfTjzs2Dto.getX21());//同步频率；
                        deviceDataUhfTableDto.setXtpl("" + dataUhfTjzs2Dto.getX22());//系统频率；

                        switch (dataAnalyzeDto.getOrderNo()) {
                            case 0:
                                tjms2zs1L.setText("" + dataUhfTjzs2Dto.getX16());
                                tjms2jt1List.add(deviceDataUhfTableDto);
                                loadImageDialog(testTechnology, 1, tjms2jt1L, "统计截图", tjms2jt1List);
                                break;
                            case 1:
                                tjms2zs2L.setText("" + dataUhfTjzs2Dto.getX16());
                                tjms2jt2List.add(deviceDataUhfTableDto);
                                loadImageDialog(testTechnology, 1, tjms2jt2L, "统计截图", tjms2jt2List);
                                break;
                            case 2:
                                tjms2zs3L.setText("" + dataUhfTjzs2Dto.getX16());
                                tjms2jt3List.add(deviceDataUhfTableDto);
                                loadImageDialog(testTechnology, 1, tjms2jt3L, "统计截图", tjms2jt3List);
                                break;
                        }

                    }
                }
            }
        } catch (Exception e) {
            log.error("系统异常!", e);
            ialert.error(I18nUtil.get("error.sys") + e.getMessage());
        }
    }

    @Override
    public void dispose() {

    }
}