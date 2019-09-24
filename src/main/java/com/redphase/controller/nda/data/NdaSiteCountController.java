package com.redphase.controller.nda.data;

import com.alibaba.fastjson.JSONObject;
import com.redphase.controller.nda.NdaBaseController;
import com.redphase.dto.atlas.DataDeviceDto;
import com.redphase.dto.atlas.DataDeviceSiteDto;
import com.redphase.dto.atlas.hfct.DataHfctTjDto;
import com.redphase.dto.table.DataTableDto;
import com.redphase.dto.atlas.uhf.DataUhfTj1Dto;
import com.redphase.dto.sys.SysVariableDto;
import com.redphase.framework.util.ByteConverterUtil;
import com.redphase.framework.util.I18nUtil;
import com.redphase.framework.util.ValidatorUtil;
import com.redphase.service.atlas.diag.DiagAe;
import com.redphase.service.atlas.diag.DiagHfct;
import com.redphase.service.atlas.diag.DiagUhf;
import com.redphase.view.nda.data.NdaDataPhaseView;
import com.redphase.view.nda.data.NdaDataSiteCountView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.*;
import java.util.zip.CRC32;

@FXMLController
@Slf4j
public class NdaSiteCountController extends NdaBaseController implements Initializable {
    final static int bigDataLength = 50 * 60 * 4;
    final Duration oneFrameAmt = Duration.millis(1000 / 50);
    @Autowired
    NdaDataPhaseView ndaDataPhaseView;
    @Autowired
    NdaDataSiteCountView ndaDataSiteCountView;
    @Autowired
    NdaDataPhaseController ndaDataPhaseController;
    @Autowired
    DiagAe diagAe;
    @Autowired
    DiagUhf diagUhf;
    @Autowired
    DiagHfct diagHfct;
    @FXML
    @Getter
    Pane prpsPane;
    @FXML
    Canvas prpsCanvasBack;
    @FXML
    Canvas prpsCanvasAtlas;
    @FXML
    Canvas prpdCanvasBack;
    @FXML
    Canvas prpdCanvasAtlas;
    @FXML
    Button prevButton;
    @FXML
    Button nextButton;
    @FXML
    Label pageNumberL;
    @FXML
    Label bdzL;//变电站名称
    @FXML
    Label dydjL;//电压等级
    @FXML
    Label sbL;//设备名称
    @FXML
    Label wzL;//被测部位和测试点
    @FXML
    Label cssjL;//测试时间
    @FXML
    Label xwwyL;//相位位移
    @FXML
    Label ckyzL;//参考阈值
    @FXML
    Label tbfsL;//同步方式
    @FXML
    Label tbplL;//同步频率
    @FXML
    Label xtplL;//系统频率
    @FXML
    Label fdlxL;//放电类型（先隐藏）
    @FXML
    Label mcL;//脉冲
    @FXML
    Label ldL;//烈度
    @FXML
    Label zdzL;//最大值
    @FXML
    Label fdzyL;//放大增益
    @FXML
    Label qzsjL;//前置衰减
    @FXML
    Label qzzyL;//前置增益
    @FXML
    Label kmsjL;//开门时间
    @FXML
    Label bssjL;//闭锁时间
    @FXML
    Label ddsjL;//等待时间
    @FXML
    Label yxzL;//有效值
    @FXML
    Label f1flL;//ｆ１分量
    @FXML
    Label f2flL;//ｆ２分量
    //----图谱---
    @FXML
    HBox fdlxHBox;//放点类型容器
    @FXML
    Label fdlx2L;//放点类型
    @FXML
    Label yzLUtil;//阈值单位
    @FXML
    TextField yzL;//阈值
    @FXML
    TextField xwL;//相位
    @FXML
    Button cycleAtlasButton;//周期统计图谱
    @FXML
    Button toStateGridButton;//国网格式数据
    DataDeviceDto dataDeviceDto;
    DataDeviceSiteDto dataDeviceSiteDto;
    List<DataTableDto> dataTableDtos;
    Integer current;
    double atlasLenght = 60;
    String testTechnology;
    Float[][] atlasArr = new Float[50][60];//图谱数据
    Double[][] phaseArr = new Double[(int) atlasLenght][50];//相位数据  由图谱数据转换而来
    Double[] maxAtlasArr = new Double[(int) atlasLenght];//最大值
    Double[] minAtlasArr = new Double[(int) atlasLenght];//最小值
    Double[] avgAtlasArr = new Double[(int) atlasLenght];//平均值
    GraphicsContext prpsBackGraphicsContext;
    GraphicsContext prpsGraphicsContext;
    GraphicsContext prpdBackGraphicsContext;
    GraphicsContext prpdGraphicsContext;
    double hPixel = 0;
    double ratio = 120 / 100.0;//120个像素分为100份
    double multiple = 3;
    double VF = 0;//范围
    double VFC = 0;//范围修正
    String unit = "";
    double currentThreshold = 0;
    int currentPhase = 0;
    double prpdX = 35;//初始x轴坐标
    double prpdStep = 300 / atlasLenght;//300像素等比{atlasPhaseLenght}份
    double hxStep = (210 - 50) / atlasLenght;
    double hyStep = (200 - 160) / atlasLenght;
    double vxStep = (210 - 50) / 50.0;
    double vyStep = (200 - 160) / 50.0;
    KeyFrame oneFrame;
    Integer[] currentX = {0};
    double prpdWidth = 300;
    double prpdDataStep = prpdWidth / 60;//300像素等比{atlasPhaseLenght}份
    double prpdDataX = 35.0;//初始x轴坐标
    private Timeline timeline = new Timeline();

    public static void main(String[] args) {
//        System.out.println(DateUtil.get);
    }

    private void fdlxHBoxToggle(boolean flag) {
        fdlxHBox.setVisible(flag);
        fdlxHBox.setManaged(flag);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize...");
    }

    public void init(String testTechnology, List<DataTableDto> dataTableDtos, Map map) {
        log.info("init...");
        this.testTechnology = testTechnology;
        this.dataTableDtos = dataTableDtos;
        Parent nodeView = ndaDataSiteCountView.getView();
        fdlxHBoxToggle(false);
        hide(cssjL, xwwyL, ckyzL, tbfsL, tbplL, xtplL, fdlxL, mcL, ldL, zdzL, fdzyL, qzsjL, qzzyL, kmsjL, bssjL, ddsjL, yxzL, f1flL, f2flL);//隐藏
        //----图谱---
        if (fdlx2L == null) this.fdlx2L = (Label) nodeView.lookup("#fdlx2L");//放点类型
        if (yzLUtil == null) this.yzLUtil = (Label) nodeView.lookup("#yzLUtil");//yzLUtil
        if (yzL == null) this.yzL = (TextField) nodeView.lookup("#yzL");//阈值
        if (xwL == null) this.xwL = (TextField) nodeView.lookup("#xwL");//相位
        if (cycleAtlasButton == null) this.cycleAtlasButton = (Button) nodeView.lookup("#cycleAtlasButton");//周期统计图谱

        current = 0;
        boolean isShowToStateGridButton = false;
        switch (testTechnology) {
            case "TEV": {
                VF = 70;
                VFC = 0;
                unit = "dBmV";
                show(bdzL, dydjL, sbL, wzL, cssjL, xwwyL, ckyzL, tbfsL, tbplL, xtplL, zdzL, mcL);
                isShowToStateGridButton = false;
                break;
            }
            case "AA": {
                VF = 80;
                VFC = 10;
                unit = "dBuV";
                //AA巡检和AA统计数据是dBuV
                //AA飞行和AA波形数据是uV 不显示
                show(bdzL, dydjL, sbL, wzL, cssjL, xwwyL, ckyzL, fdzyL, tbfsL, tbplL, xtplL, kmsjL, bssjL, ddsjL, zdzL, mcL, yxzL, f1flL, f2flL);
                isShowToStateGridButton = false;
                break;
            }
            case "AE": {
                VF = 300;
                VFC = 0;
                unit = "mV";
                fdlxHBoxToggle(true);
                show(bdzL, dydjL, sbL, wzL, cssjL, xwwyL, ckyzL, fdzyL, qzzyL, tbfsL, tbplL, xtplL, kmsjL, bssjL, ddsjL, fdlxL, zdzL, mcL, yxzL, f1flL, f2flL);
                isShowToStateGridButton = false;
                break;
            }
            case "HFCT": {
                VF = 300;
                VFC = 0;
                unit = "mV";
                fdlxHBoxToggle(true);
                show(bdzL, dydjL, sbL, wzL, cssjL, xwwyL, ckyzL, fdzyL, qzzyL, tbfsL, tbplL, xtplL, fdlxL, zdzL, mcL);
                isShowToStateGridButton = true;
                break;
            }
            case "UHF": {
                VF = 70;
                VFC = 80;
                unit = "dBm";
                fdlxHBoxToggle(true);
                show(bdzL, dydjL, sbL, wzL, cssjL, xwwyL, ckyzL, tbfsL, tbplL, xtplL, fdlxL);
                isShowToStateGridButton = true;
                break;
            }
        }
        toStateGridButton.setVisible(isShowToStateGridButton);
        toStateGridButton.setManaged(isShowToStateGridButton);
        prpsBackGraphicsContext = prpsCanvasBack.getGraphicsContext2D();
        prpsGraphicsContext = prpsCanvasAtlas.getGraphicsContext2D();
        prpdBackGraphicsContext = prpdCanvasBack.getGraphicsContext2D();
        prpdGraphicsContext = prpdCanvasAtlas.getGraphicsContext2D();

        prpsGraphicsContext.clearRect(0, 0, prpsCanvasBack.getWidth(), prpsCanvasBack.getHeight());
        prpdGraphicsContext.clearRect(0, 0, prpdCanvasAtlas.getWidth(), prpdCanvasAtlas.getHeight());

        loadAtlas();
    }

    private double[] getPhaseMaxArr(Float[][] atlasArr, double threshold) {
        double[] phaseMaxArr = new double[60];
        for (int x = 0; x < 50; x++) {
            for (int y = 0; y < 60; y++) {
                if (atlasArr[x][y] < threshold) {
                    continue;
                }
                if (phaseMaxArr[y] < atlasArr[x][y]) {
                    phaseMaxArr[y] = atlasArr[x][y];
                }
            }
        }
        return phaseMaxArr;
    }

    private void loadAtlas() {
        log.info("loadAtlas..");
        try {
            if (cycleAtlasButton != null) {
                cycleAtlasButton.setDisable(true);
            }
            if (dataTableDtos.size() > 0) {
                if (current > 0) {
                    prevButton.setDisable(false);
                } else {
                    prevButton.setDisable(true);
                }
                if (current < dataTableDtos.size() - 1) {
                    nextButton.setDisable(false);
                } else {
                    nextButton.setDisable(true);
                }
            } else {
                prevButton.setDisable(true);
                nextButton.setDisable(true);
            }
            DataTableDto dataTableDto = dataTableDtos.get(current);
            pageNumberL.setText((current + 1) + "/" + dataTableDtos.size());

            clearL(bdzL, dydjL, sbL, wzL, cssjL, xwwyL, ckyzL, tbfsL, tbplL, xtplL, fdlxL, cssjL, xwwyL, ckyzL, tbfsL, tbplL, xtplL, fdlxL, mcL, ldL, zdzL, fdzyL, qzsjL, qzzyL, kmsjL, bssjL, ddsjL, yxzL, f1flL, f2flL);
            yzL.setText("0");
            xwL.setText("0");
            dataDeviceDto = dataTableDto.getDataDeviceDto();
            //属性
            setLabelText(bdzL, dataDeviceDto.getSubstation(), "");//变电站名称
            setLabelText(dydjL, dataDeviceDto.getVoltageLevel(), "");//电压等级
            setLabelText(sbL, dataDeviceDto.getDeviceName(), "");//设备名称
            setLabelText(cssjL, dataTableDto.getCssj(), "");//测试时间
            setLabelText(xwwyL, dataTableDto.getXwwy(), "");//相位位移
            setLabelText(ckyzL, dataTableDto.getCkyz(), "");//参考阈值
            setLabelText(tbfsL, dataTableDto.getTbfs(), "");//同步方式
            setLabelText(tbplL, dataTableDto.getTbpl(), "");//同步频率
            setLabelText(xtplL, dataTableDto.getXtpl(), "");//系统频率
            setLabelText(mcL, dataTableDto.getMc(), "个/秒");//脉冲
            setLabelText(ldL, dataTableDto.getLd(), "");//烈度
            setLabelText(zdzL, (dataTableDto.getZdz() != null ? dataTableDto.getZdz() : dataTableDto.getFz()), unit);//最大值/幅值
            setLabelText(fdzyL, dataTableDto.getFdzy(), "");//放大增益
            setLabelText(qzsjL, dataTableDto.getQzsj(), "");//前置衰减
            setLabelText(qzzyL, dataTableDto.getQzzy(), "");//前置增益
            setLabelText(kmsjL, dataTableDto.getKmsj(), "");//开门时间
            setLabelText(bssjL, dataTableDto.getBssj(), "");//闭锁时间
            setLabelText(ddsjL, dataTableDto.getDdsj(), "");//等待时间
            setLabelText(yxzL, dataTableDto.getYxz(), "");//有效值
            setLabelText(f1flL, dataTableDto.getF1(), "");//ｆ１分量
            setLabelText(f2flL, dataTableDto.getF2(), "");//ｆ２分量
            try {
                yzL.setText(new BigDecimal(dataTableDto.getYz()).intValue() + "");//阈值
            } catch (Exception e) {
            }
            yzLUtil.setText(unit);//阈值单位
            try {
                currentPhase = new BigDecimal(dataTableDto.getXwwy()).intValue();
                xwL.setText(currentPhase + "");//相位
            } catch (Exception e) {
            }

//            if (currentThreshold == 0) {
            currentThreshold = new BigDecimal(dataTableDto.getCkyz()).doubleValue();
//            }
            dataDeviceSiteDto = dataTableDto.getDataDeviceSiteDto();
//            if(dataDeviceSiteDto!=null){
            wzL.setText(dataDeviceSiteDto.getSiteName());//被测部位和测试点
//            }
            idialog.setTitle(ndaDataSiteCountView, dataDeviceDto.getSubstation() + dataDeviceDto.getDeviceName() + dataDeviceSiteDto.getSiteName());

            hPixel = VF / multiple;

            drawPRPSBackground(prpsBackGraphicsContext);
            drawPRPDBackground(prpdBackGraphicsContext);
            atlasArr = dataTableDto.getAtlasArr();
//            //test
//            atlasArr = new Float[50][60];
//            for (int x = 0; x < atlasArr.length; x++) {
//                for (int y = 0; y < 60; y++) {
//                    atlasArr[x][y] = x + y + 30f;
//                }
//            }
            String result = "--";
            switch (testTechnology) {
                case "AE": {
                    result = diagAe.getResult(getPhaseMaxArr(atlasArr, new BigDecimal(dataTableDto.getCkyz()).doubleValue()));
                    fdlxL.setText(result);//原始-放电类型
                    //--图谱
                    fdlx2L.setText(result);//重绘-放点类型
                    break;
                }
                case "HFCT": {
                    result = diagHfct.getResult(getPhaseMaxArr(atlasArr, new BigDecimal(dataTableDto.getCkyz()).doubleValue()));
                    fdlxL.setText(result);//原始-放电类型
                    //--图谱
                    fdlx2L.setText(result);//重绘-放点类型
                    break;
                }
                case "UHF": {
                    result = diagUhf.getResult(getPhaseMaxArr(atlasArr, new BigDecimal(dataTableDto.getCkyz()).doubleValue()));
                    fdlxL.setText(result);//原始-放电类型
                    //--图谱
                    fdlx2L.setText(result);//重绘-放点类型
                    break;
                }
            }
            currentX[0] = 0;
            drawPRPSAtlas();
            timeline.play();
        } catch (Exception e) {
            log.error("统计图谱异常!", e);
            ialert.error("统计图谱异常!" + e.getMessage());
        }
    }

    //背景坐标
    private void drawPRPSBackground(GraphicsContext gc) {
        log.info("drawPRPSBackground..");
        //清屏
        gc.clearRect(0, 0, prpsCanvasBack.getWidth(), prpsCanvasBack.getHeight());
        gc.setStroke(Color.web("#cccccc", 1.0));
        gc.setLineWidth(1);
        gc.setFont(Font.font(10));
        gc.setFill(Color.web("#999999"));
        double pixelNum = ratio * (100 / multiple);//(120/multiple);
        //纵面图--横线  +1
        for (int i = 0; i < multiple + 1; i++) {
            gc.strokeLine(50, 160 - (i * pixelNum), 210, 120 - (i * pixelNum));
            gc.fillText("" + new BigDecimal(i * hPixel - VFC).setScale(2, RoundingMode.UP), 20, 160 - (i * pixelNum) + 5);
        }
        //纵面图--竖线
        for (int i = 0; i < 5; i++) {
            //160像素分为4等份
            gc.strokeLine(50 + (i * 40), 160 + (i * -10), 50 + (i * 40), 40 + (i * -10));
        }
        //横面图--横线
        gc.setLineWidth(0.5);
        double xStep = (210 - 50) / 5.0;
        double yStep = (200 - 160) / 5.0;
        for (int i = 0; i < 6; i++) {
            gc.strokeLine(50 + (i * xStep), 160 + (i * yStep), 210 + (i * xStep), 120 + (i * yStep));
            if (i > 0) {
                gc.fillText("" + (10 * i), 50 + (i * xStep) - 30, 160 + (i * yStep) + 15);
            }
        }
        //横面图--竖线
        gc.setLineWidth(1);
        for (int i = 0; i < 5; i++) {
            //50 160 210 200
            //90 150 250 190
            gc.strokeLine(50 + (i * 40), 160 + (i * -10), 210 + (i * 40), 200 + (i * -10));
            gc.fillText("" + (90 * i), 210 + (i * 40) + 10, 200 + (i * -10) + 15);
        }
    }

    private void drawPRPDBackground(GraphicsContext gc) {
        log.info("drawPRPDBackground..");
        //清屏
        gc.clearRect(0, 0, prpdCanvasBack.getWidth(), prpdCanvasBack.getHeight());
        gc.setStroke(Color.web("#cccccc", 1.0));
        gc.setLineWidth(1);
        gc.setFont(Font.font(10));
        gc.setFill(Color.web("#999999"));

        double xStep = (300) / 4.0;
        double yStep = 180.0 / multiple;


        //纵面图--横线  +1
        for (int i = 0; i < multiple + 1; i++) {
            gc.strokeLine(prpdX, 195 - (i * yStep), prpdX + 300, 195 - (i * yStep));
            gc.fillText("" + new BigDecimal(i * hPixel - VFC).setScale(2, RoundingMode.UP), 0, 195 - (i * yStep) + 5);
        }
        //纵面图--竖线
        for (int i = 0; i < 5; i++) {
            //160像素分为4等份
            gc.strokeLine(prpdX + (i * xStep), 195, prpdX + (i * xStep), 195 - 180);
            gc.fillText("" + (90 * i), prpdX + (i * xStep) - 5, 195 + 15);
        }
        //=============正弦线===begin==========================
        //中线
        gc.setStroke(Color.web("#999999"));
        gc.setLineDashes(2);
        gc.strokeLine(prpdX, 195 - (180 * 0.5), prpdX + 300, 195 - (180 * 0.5));
        //曲线
        double sineWidth = 300, sineHeight = 180;
        gc.setStroke(Color.web("#69ddf0"));
        gc.setLineWidth(1.5);
        gc.setLineDashes(0);
        gc.beginPath();
        //-------------------------
        gc.moveTo(prpdX, 195 - (sineHeight * 0.5));
        gc.bezierCurveTo(prpdX + (sineWidth) / 4, 195 - sineHeight, prpdX + (sineWidth) / 4, 195 - sineHeight, prpdX + (sineWidth) / 2, 195 - (sineHeight * 0.5));
        //--------------------------
        gc.moveTo(prpdX + (sineWidth) / 2, 195 - (sineHeight * 0.5));
        gc.bezierCurveTo(prpdX + sineWidth - (sineWidth) / 4, 195, prpdX + sineWidth - (sineWidth) / 4, 195, prpdX + (sineWidth), 195 - (sineHeight * 0.5));
        //-------------------------
        gc.stroke();
        gc.closePath();
        //=============正弦线===end==========================
    }

    public void reportToData() {
        log.info("reportToData..");
        timeline.stop();
        //清屏
        prpsGraphicsContext.clearRect(0, 0, prpsCanvasAtlas.getWidth(), prpsCanvasAtlas.getHeight());
        for (int i = 0; i < 50; i++) {
            runDrawPrps();
        }
    }

    private void drawPRPSAtlas() {
        log.info("drawPRPSAtlas..");
        //	三维图中，线段的颜色按照以下规则定义：
        //（1）按照线段的幅值与幅值动态范围的比例来确定。
        //（2）幅值的动态范围为每种检测技术的幅值上限减去幅值的下限，如对于TEV而言70-0 = 70；
        //幅值范围：
        //	TEV——0到70——70
        //	AA——-10到70——70-（-10）=80
        //	AE——0到300——300
        //	HFCT——0到300——300
        //	UHF——-75到-10——-10-（-75）=65
        //（3）分为5个，幅值为V，幅值动态范围为VF，计算幅值占比PV = V / VF。其中，对于AA，PV = （V+10）/ VF；对于UHF，PV = （V+75）/ VF
        //	PV≤20%——线段为蓝色，RGB(0,0,255)
        //	20%＜PV≤20%——线段为青，RGB(0,255,255)
        //	40%＜PV≤60%——线段为绿，RGB(0,255,0)
        //	60%＜PV≤80%——线段为黄，RGB(255,255,0)
        //	80%＜PV——线段为红，RGB(255,0,0)
        //清屏
        prpsGraphicsContext.clearRect(0, 0, prpsCanvasAtlas.getWidth(), prpsCanvasAtlas.getHeight());
        prpdGraphicsContext.clearRect(0, 0, prpdCanvasAtlas.getWidth(), prpdCanvasAtlas.getHeight());
        if (oneFrame == null) {
            oneFrame = new KeyFrame(oneFrameAmt, event -> runDrawPrps());
            timeline.getKeyFrames().setAll(oneFrame);
            timeline.setCycleCount(Animation.INDEFINITE);
        }
    }

    private void runDrawPrps() {
        try {
            if (currentX[0] >= 50) {
                timeline.stop();
                cycleAtlasButton.setDisable(false);
            } else {
                prpsGraphicsContext.setLineWidth(2);
                prpdGraphicsContext.setLineWidth(4);
                //递推--begin----
                prpsGraphicsContext.clearRect(0, 0, prpsCanvasAtlas.getWidth(), prpsCanvasAtlas.getHeight());
                //获取当前组数
                Float[][] currentAtlasArr = new Float[currentX[0] + 1][60];
                System.arraycopy(atlasArr, 0, currentAtlasArr, 0, currentAtlasArr.length);
                //倒叙数组
                Collections.reverse(Arrays.asList(currentAtlasArr));
                for (int x = 0; x < currentAtlasArr.length; x++) {
                    //调整相位后的单组数据
                    Float[] currentPhaseAtlasArr = new Float[(int) atlasLenght];
                    //根据相位位移获取前半段数据
                    if (currentPhase < atlasLenght) {
                        System.arraycopy(currentAtlasArr[x], currentPhase, currentPhaseAtlasArr, 0, (int) atlasLenght - currentPhase);
                    }
                    //根据相位位移获取后半段数据
                    System.arraycopy(currentAtlasArr[x], 0, currentPhaseAtlasArr, (int) atlasLenght - currentPhase, currentPhase);

                    for (int y = 0; y < atlasLenght; y++) {
                        double pv = (currentPhaseAtlasArr[y] + VFC) / VF;
                        pv = getSetPvColor(pv);
                        phaseArr[y][x] = pv;
                        if (currentThreshold > currentPhaseAtlasArr[y]) continue;
                        if (pv > 0) {
                            if (pv <= 100) {
                                //prpd
                                prpdGraphicsContext.strokeLine(prpdDataX + (y * prpdDataStep), 195 - (1.8 * pv), prpdDataX + (y * prpdDataStep), 195 - (1.8 * pv));//1.8=180/100 180=高像素 100=等份数
                            }
                            if (pv > 100) {
                                pv = 100;
                            }
                            prpsGraphicsContext.strokeLine(50 + (x * vxStep) + (y * hxStep), 160 + (x * vyStep) + (y * -hyStep), 50 + (x * vxStep) + (y * hxStep), 160 + (x * vyStep) + (y * -hyStep) - (1.2 * pv));//1.2=120/100
                        }
                    }
                }
                //递推--end----
                currentX[0]++;
            }
        } catch (Exception e) {
            timeline.stop();
            log.error("统计数据重绘异常!", e);
            Platform.runLater(() -> {
                ialert.error(I18nUtil.get("error.sys") + e.getMessage());
            });
        }
    }

    private double getSetPvColor(double pv) {
        pv = pv * 100;
        if (pv <= 20.0) {
            prpsGraphicsContext.setStroke(Color.rgb(0, 0, 255));
            prpdGraphicsContext.setStroke(Color.rgb(0, 0, 255));
        } else if (20.0 < pv && pv <= 40.0) {
            prpsGraphicsContext.setStroke(Color.rgb(0, 255, 255));
            prpdGraphicsContext.setStroke(Color.rgb(0, 255, 255));
        } else if (40.0 < pv && pv <= 60.0) {
            prpsGraphicsContext.setStroke(Color.rgb(0, 255, 0));
            prpdGraphicsContext.setStroke(Color.rgb(0, 255, 0));
        } else if (60.0 < pv && pv <= 80) {
            prpsGraphicsContext.setStroke(Color.web("#f8cb19"));//Color.rgb(255,255,0)
            prpdGraphicsContext.setStroke(Color.web("#f8cb19"));//Color.rgb(255,255,0)
        } else if (pv > 0) {
            prpsGraphicsContext.setStroke(Color.rgb(255, 0, 0));
            prpdGraphicsContext.setStroke(Color.rgb(255, 0, 0));
        }
        return pv;
    }

    @FXML
    public void prev() {
        log.info("prev...");
        if (current > 0) {
            current--;
        }
        loadAtlas();
    }

    @FXML
    public void next() {
        log.info("next...");
        if (current < dataTableDtos.size()) {
            current++;
        }
        loadAtlas();
    }

    @FXML
    public void repaint() {
        log.info("repaint...");
        String yzValue = yzL.getText();
        if (ValidatorUtil.isEmpty(yzValue) || !ValidatorUtil.isInteger(yzValue)) {
            ialert.warning("请输入正确的阈值!");
            return;
        }
        Double tmpThreshold = new BigDecimal(yzValue).doubleValue();
        if (tmpThreshold < (0 - VFC)) {
            ialert.error("阈值不能小于" + (0 - VFC));
            return;
        }
        if (tmpThreshold > (VF - VFC)) {
            ialert.error("阈值不能大于" + (VF - VFC));
            return;
        }
        String xwValue = xwL.getText();
        if (ValidatorUtil.isEmpty(xwValue) || !ValidatorUtil.isNum(xwValue)) {
            ialert.warning("请输入正确的相位数值!");
            return;
        }
        int tmpPhase = new BigDecimal(xwValue).intValue();
        if (tmpPhase < 0 || tmpPhase > 360) {
            ialert.warning("请输入正确的相位(0~360)");
            return;
        }
        currentThreshold = new BigDecimal(yzValue).doubleValue();
        currentPhase = new BigDecimal(xwValue).intValue();
        currentPhase = 60 - currentPhase / 6;//真实数组相位 24=360/6
        cycleAtlasButton.setDisable(true);
        prpsGraphicsContext.clearRect(0, 0, prpsCanvasAtlas.getWidth(), prpsCanvasAtlas.getHeight());
        prpdGraphicsContext.clearRect(0, 0, prpdCanvasAtlas.getWidth(), prpdCanvasAtlas.getHeight());
        DataTableDto dataTableDto = dataTableDtos.get(current);
        currentX[0] = 0;
        atlasArr = dataTableDto.getAtlasArr();

        String result = "--";
        switch (testTechnology) {
            case "AE": {
                result = diagAe.getResult(getPhaseMaxArr(atlasArr, currentThreshold));
                fdlx2L.setText(result);//重绘-放点类型
                break;
            }
            case "HFCT": {
                result = diagHfct.getResult(getPhaseMaxArr(atlasArr, currentThreshold));
                fdlx2L.setText(result);//重绘-放点类型
                break;
            }
            case "UHF": {
                result = diagUhf.getResult(getPhaseMaxArr(atlasArr, currentThreshold));
                fdlx2L.setText(result);//重绘-放点类型
                break;
            }
        }
        timeline.play();
    }

    @FXML
    public void cycleAtlas() {
        log.info("cycleAtlas...");
        for (int x = 0; x < phaseArr.length; x++) {
            double max = 0;
            double min = 1000;
            double sum = 0;
            for (int y = 0; y < phaseArr[x].length; y++) {
                double pv = phaseArr[x][y];
                if (pv > max) {
                    max = pv;
                }
                if (pv < min) {
                    min = pv;
                }
                sum += pv;
            }
            maxAtlasArr[x] = max;
            minAtlasArr[x] = min;
            avgAtlasArr[x] = sum / atlasLenght;
        }
//        Double width = Application.getStage().getWidth() - 200;
//        Double height = Application.getStage().getHeight() - 100;
        idialog.openDialog(dataDeviceDto.getSubstation() + dataDeviceDto.getDeviceName() + dataDeviceSiteDto.getSiteName(), ndaDataPhaseView, 1300.0, 768.0, false);
        ndaDataPhaseController.init(testTechnology, atlasArr, new HashMap() {{
            put("title", dataDeviceDto.getSubstation() + dataDeviceDto.getDeviceName() + dataDeviceSiteDto.getSiteName());
            put("maxPhaseAtlasArr", maxAtlasArr);
            put("minAtlasArr", minAtlasArr);
            put("avgAtlasArr", avgAtlasArr);
            put("VF", VF);
            put("VFC", VFC);
        }});
    }

    private String getFileDateFormat(long dateValue) {
        return ("" + dateValue).substring(0, 14);
    }

    @FXML
    public void toStateGrid() {
        log.info("toStateGrid...");
        toStateGridButton.setDisable(true);
        try {
            SysVariableDto variable = setupService.getVariableByCode(new SysVariableDto() {{
                setCode("i-path-data");
            }});
            String parentPath = new File(variable.getValue()).getParent() + "/gwgs/";
            switch (testTechnology) {
                case "UHF": {
                    DataTableDto dataTableDto = dataTableDtos.get(current);
                    DataUhfTj1Dto dataUhfTj1Dto = dataTableDto.getDataUhfTj1Dto();
                    File uhfFileDir = new File(parentPath + "uhf/");
                    if (!uhfFileDir.exists()) {
                        uhfFileDir.mkdirs();
                    }
                    int dataRowLength = 0;
                    int[] dataRowBytes = DataType.UHF.dataRowBytes;
                    for (int i = 0; i < dataRowBytes.length; i++) {
                        dataRowLength += dataRowBytes[i];
                    }
                    byte[] bytes = new byte[dataRowLength];
                    String fileName = "/RedPhasePDT840_1_" + getFileDateFormat(dataUhfTj1Dto.getX14()) + ".dat";
                    File file = new File(uhfFileDir.getPath() + fileName);
                    FileOutputStream out = new FileOutputStream(file);
                    //数据长度	     int	 4字节	数据包长度，含CRC校验。
                    System.arraycopy(ByteConverterUtil.int2byte(dataRowLength), 0, bytes, 0, 4);
                    //规范版本号	     float	 4字节	所使用的数据格式规范版本号，例如1.0。
                    System.arraycopy(ByteConverterUtil.float2byte(1.0f), 0, bytes, 4, 4);
                    //图谱类型编码	 char	 1字节	标识该文件是PRPD图谱还是PRPS图谱。PRPD:0x00 PRPS: 0x01
                    bytes[8] = new Byte("1").byteValue();
                    //System.arraycopy(new Byte("1").byteValue(), 0, bytes, 8, 1);
                    //相位窗数m	     int	 4字节	工频周期被等分成m个相位窗，每个相位窗跨360/m度。
                    System.arraycopy(ByteConverterUtil.int2byte(60), 0, bytes, 9, 4);
                    //量化幅值n	     int	 4字节	幅值范围的等分区间数。如果该文件是PRPS图谱，则该4个字节清零，赋值为：0x00000000。
                    System.arraycopy(ByteConverterUtil.int2byte(0), 0, bytes, 13, 4);
                    //幅值单位	     char	 1字节	表示幅值的单位 dBm: 0x01 mV: 0x02 %: 0x03
                    bytes[17] = ValidatorUtil.notEmpty("" + dataUhfTj1Dto.getX6()) ? new Byte("" + dataUhfTj1Dto.getX6()).byteValue() : new Byte("0").byteValue();
                    //System.arraycopy(ValidatorUtil.notEmpty(dataUhfTj1Dto.getX6()) ? new Byte(dataUhfTj1Dto.getX6() + "").byteValue() : new Byte("0").byteValue(), 0, bytes, 17, 1);
                    //幅值下限	     float	 4字节	仪器所能检测到的放电信号幅值的下限。
                    System.arraycopy(ByteConverterUtil.float2byte(dataUhfTj1Dto.getX7()), 0, bytes, 18, 4);
                    //幅值上限	     float	 4字节	仪器所能检测到的放电信号幅值的上限。
                    System.arraycopy(ByteConverterUtil.float2byte(dataUhfTj1Dto.getX8()), 0, bytes, 22, 4);
                    //工频周期数p	     int	 4字节	图谱工频周期的个数。如果该文件是PRPD图谱，则该4个字节清零，赋值为：0x00000000。
                    System.arraycopy(ByteConverterUtil.int2byte(50), 0, bytes, 26, 4);
                    //放电类型概率	 char[8] 8字节	表示仪器诊断结果的放电类型概率。数组元素[0]至[7]分别存储正常、尖端放电、悬浮放电、沿面放电、内部放电、颗粒放电、外部干扰和其它共8种情况的概率，每个元素可以是整数0至100中的某数。实例：数组元素[3]为69，表示沿面放电的概率为69%。诊断结果为正常，则为数组元素[0]赋值100，数组元素[1]至[7]赋值0。如果仪器不具备放电类型诊断功能，则为数组元素[0]至[7]赋值0x00。
                    Float[][] x11Arr = JSONObject.parseObject(dataUhfTj1Dto.getX11(), Float[][].class);
                    diagUhf.getResult(getPhaseMaxArr(x11Arr, new BigDecimal(dataTableDto.getCkyz()).doubleValue()));
                    for (int i = 0; i < diagUhf.getResultIntArr().length; i++) {
                        bytes[30 + i] = new Byte("" + diagUhf.getResultIntArr()[i]).byteValue();
                        //System.arraycopy(new Byte("" + diagHfct.getResultIntArr()[i]).byteValue(), 0, bytes, 30 + i, 1);
                    }
                    //局部放电图谱数据 int[m][n] 或 float[p][m]	 4*m*n字节或4*p*m 字节	如果该文件是PRPD图谱，则为int[m][n]，m为相位窗数，n为量化幅值，数组元素[x][y]的值表示在对应第x相位窗和第y幅值处发生的放电次数。如果该文件是PRPS图谱，则为float[p][m]，p为工频周期数，m为相位窗数，数组元素[x][y]的值表示在对应第x个周期的第y相位窗处发生的放电的幅值。
                    for (int x = 0; x < x11Arr.length; x++) {
                        for (int y = 0; y < 60; y++) {
                            int destPos = 38 + (x * 60 * 4) + (y * 4);
                            System.arraycopy(ByteConverterUtil.float2byte(x11Arr[x][y]), 0, bytes, destPos, 4);
                        }
                    }
                    //CRC32	         int	 4字节	数据校验，使用CRC32算法。
                    CRC32 crc32 = new CRC32();
                    crc32.update(bytes);
                    System.arraycopy(ByteConverterUtil.int2byte((int) crc32.getValue()), 0, bytes, dataRowLength - 4, 4);
                    out.write(bytes);
                    out.close();
                    break;
                }
                case "HFCT": {
                    DataTableDto dataTableDto = dataTableDtos.get(current);
                    DataHfctTjDto dataHfctTjDto = dataTableDto.getDataHfctTjDto();
                    File hfctFileDir = new File(parentPath + "hfct/");
                    if (!hfctFileDir.exists()) {
                        hfctFileDir.mkdirs();
                    }
                    int dataRowLength = 0;
                    int[] dataRowBytes = DataType.UHF.dataRowBytes;
                    for (int i = 0; i < dataRowBytes.length; i++) {
                        dataRowLength += dataRowBytes[i];
                    }
                    byte[] bytes = new byte[dataRowLength];
                    String fileName = "/RedPhasePDT840_1_" + getFileDateFormat(dataHfctTjDto.getX14()) + ".HF";
                    File file = new File(hfctFileDir.getPath() + fileName);
                    FileOutputStream out = new FileOutputStream(file);
                    //数据长度         Int      4字节  数据包长度，含CRC校验
                    System.arraycopy(ByteConverterUtil.int2byte(dataRowLength), 0, bytes, 0, 4);
                    //规范版本号       float    4字节  所使用的数据格式规范版本号，例如1.0。
                    System.arraycopy(ByteConverterUtil.float2byte(1.0f), 0, bytes, 4, 4);
                    //图谱类型编码      char    1字节  标识该文件是PRPD图谱还是PRPS图谱。 PRPD:0x00 PRPS: 0x01
                    bytes[8] = new Byte("1").byteValue();
                    //System.arraycopy(new Byte("1").byteValue(), 0, bytes, 8, 1);
                    //相位窗数m        int      4字节  工频周期被等分成m个相位窗，每个相位窗跨360/m度。
                    System.arraycopy(ByteConverterUtil.int2byte(60), 0, bytes, 9, 4);
                    //量化幅值n        int      4字节  幅值范围的等分区间数。如果该文件是PRPS图谱，则该4个字节清零，赋值为：0x00000000。
                    System.arraycopy(ByteConverterUtil.int2byte(0), 0, bytes, 13, 4);
                    //幅值单位         char     1字节  表示幅值的单位 dB: 0x01 mV: 0x02
                    bytes[17] = ValidatorUtil.notEmpty("" + dataHfctTjDto.getX6()) ? new Byte("" + dataHfctTjDto.getX6()).byteValue() : new Byte("2").byteValue();
                    //System.arraycopy(ValidatorUtil.notEmpty(dataHfctTjDto.getX6()) ? new Byte("" + dataHfctTjDto.getX6()).byteValue() : new Byte("2").byteValue(), 0, bytes, 17, 1);
                    //幅值下限         float    4字节  仪器所能检测到的放电信号幅值的下限
                    System.arraycopy(ByteConverterUtil.float2byte(dataHfctTjDto.getX7()), 0, bytes, 18, 4);
                    //幅值上限         float    4字节  仪器所能检测到的放电信号幅值的上
                    System.arraycopy(ByteConverterUtil.float2byte(dataHfctTjDto.getX8()), 0, bytes, 22, 4);
                    //工频周期数p      int      4字节   图谱工频周期的个数。如果该文件是PRPD图谱，则该4个字节清零，赋值为：0x00000000
                    System.arraycopy(ByteConverterUtil.int2byte(50), 0, bytes, 26, 4);
                    Float[][] x11Arr = JSONObject.parseObject(dataHfctTjDto.getX11(), Float[][].class);
                    diagHfct.getResult(getPhaseMaxArr(x11Arr, new BigDecimal(dataTableDto.getCkyz()).doubleValue()));
                    for (int i = 0; i < diagHfct.getResultIntArr().length; i++) {
                        bytes[30 + i] = new Byte("" + diagHfct.getResultIntArr()[i]).byteValue();
                        //System.arraycopy(new Byte("" + diagHfct.getResultIntArr()[i]).byteValue(), 0, bytes, 30 + i, 1);
                    }
                    //局部放电图谱数据  int[m][n] 或 float[p][m] 4*m*n字节或4*p*m 字节 如果该文件是PRPD图谱，则为int[m][n]，m为相位窗数，n为量化幅值，数组元素[x][y]的值表示在对应第x相位窗和第y幅值处发生的放电次数。如果该文件是PRPS图谱，则为float[p][m]，p为工频周期数，m为相位窗数，数组元素[x][y]的值表示在对应第x个周期的第y相位窗处发生的放电的幅值。
                    for (int x = 0; x < x11Arr.length; x++) {
                        for (int y = 0; y < 60; y++) {
                            int destPos = 38 + (x * 60 * 4) + (y * 4);
                            System.arraycopy(ByteConverterUtil.float2byte(x11Arr[x][y]), 0, bytes, destPos, 4);
                        }
                    }
                    //CRC32	         int	 4字节	数据校验，使用CRC32算法。
                    CRC32 crc32 = new CRC32();
                    crc32.update(bytes);
                    System.arraycopy(ByteConverterUtil.int2byte((int) crc32.getValue()), 0, bytes, dataRowLength - 4, 4);
                    out.write(bytes);
                    out.close();
                    break;
                }
            }
        } catch (Exception e) {
            log.error("国网格式数据异常!", e);
            toStateGridButton.setDisable(false);
            ialert.error("国网格式数据异常!" + e.getMessage());
            return;
        }
        toStateGridButton.setDisable(false);
        ialert.success(I18nUtil.get("ialert.success"));
    }


    @Override
    public void dispose() {
        timeline.stop();
    }

    public enum DataType {
        UHF(new int[]{4, 4, 1, 4, 4, 1, 4, 4, 4, 8, bigDataLength, 4}),
        HFCT(new int[]{4, 4, 1, 4, 4, 1, 4, 4, 4, 8, bigDataLength, 4});
        private int[] dataRowBytes;

        DataType(int[] dataRowBytes) {
            this.dataRowBytes = dataRowBytes;
        }
    }
}