package com.redphase.controller.atlas.data;

import com.redphase.controller.atlas.AtlasBaseController;
import com.redphase.dto.atlas.DataDeviceDto;
import com.redphase.dto.atlas.DataDeviceSiteDto;
import com.redphase.dto.table.DataTableDto;
import com.redphase.framework.util.I18nUtil;
import com.redphase.framework.util.ValidatorUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

@FXMLController
@Slf4j
public class AtlasDataTJLBController extends AtlasBaseController implements Initializable {
    @FXML
    @Getter
    Pane mlayer;//蒙层
    @FXML
    @Getter
    Pane setPane;//设置窗容器

    @FXML
    Pane cutFloatPane;
    @FXML
    Pane statisticsFloatPane;
    @FXML
    Pane leftFloatPane;
    @FXML
    Pane rightFloatPane;

    @FXML
    Label setTitle;
    @FXML
    Pane closeSet;
    @FXML
    Pane openDataPane;
    //    @FXML
//    Pane setShowPane;
    @FXML
    Pane setThresholdPane;
    @FXML
    TextField setThresholdText;

    @FXML
    Pane data1Pane;
    @FXML
    Canvas prpsCanvasBack;
    @FXML
    Canvas prpsCanvasAtlas;
    @FXML
    Canvas prpdCanvasBack;
    @FXML
    Canvas prpdCanvasAtlas;
    @FXML
    Canvas qφCanvasBack;
    @FXML
    Canvas qφCanvasAtlas;
    @FXML
    Canvas feature1CanvasBack;
    @FXML
    Canvas feature1CanvasAtlas;
    @FXML
    Canvas feature1CanvasLayer;
    @FXML
    Canvas feature1CanvasLine;
    @FXML
    Canvas data1CanvasBack;
    @FXML
    Canvas data1CanvasAtlas;
    GraphicsContext prpsBackGraphicsContext;
    GraphicsContext prpsGraphicsContext;
    GraphicsContext prpdBackGraphicsContext;
    GraphicsContext prpdGraphicsContext;
    GraphicsContext qφBackGraphicsContext;
    GraphicsContext qφGraphicsContext;
    GraphicsContext feature1BackGraphicsContext;
    GraphicsContext feature1GraphicsContext;
    GraphicsContext feature1LayerGraphicsContext;
    GraphicsContext feature1LineGraphicsContext;
    GraphicsContext data1BackGraphicsContext;
    GraphicsContext data1GraphicsContext;
    //----图谱---
    @FXML
    Label fdlx2L;//放点类型
    @FXML
    Label mcL;//脉冲
    @FXML
    Label ldL;//烈度
    @FXML
    Label zdzL;//最大值
    @FXML
    Label yzL;//阈值
    @FXML
    Label xwL;//相位
    @FXML
    Label yzL1;//阈值
    @FXML
    Label xwL1;//相位
    @FXML
    Label feature1Unit;
    @FXML
    Label prpdUnit;
    @FXML
    Label qφUnit;

    DataTableDto dataTableDto;
    DataDeviceDto dataDeviceDto;
    DataDeviceSiteDto dataDeviceSiteDto;
    List<DataTableDto> dataTableDtos;
    Integer current;

    double atlasPhaseLenght = 60;//单组数据相位个数
    double atlasOneLenghtF = 50;//每秒数据组数
    int atlasOneLenghtD = (int) atlasOneLenghtF;//每秒数据组数

    String testTechnology;
    Float[][] atlasArr;//图谱数据
    double[] maxSecondCountArr;//多秒统计最大值
    double[][] maxPhaseAtlasArr;//单秒相位最大值

    double rangeValue = 0.0;//量程倍数
    int changeRangeValue = 5;//放大缩写值
    int changeRangeCount = 5;//放大缩写总次数
    double changeRangeMultiple = 0.2;//放大缩写倍数
    int secondCount = 0;//统计总秒数
    int secondCurrent = 0;//统计当前秒数
    double hPixel = 0;
    double multiple = 3;
    double VF = 0;//范围
    double VFC = 0;//范围修正
    int defaultPhase;//初始相位位移量
    double thresholdValue = 0;//阈值
    int statisticsValue = 1;//统计时长
    int statisticsBeginSecond = 0;//统计开始-秒
    int statisticsLastSecond = 0;//统计最后-秒
    int degreeValue = 0;//相位值  0至360
    //数据单位==================
    String unit = "";


    double prpdWidth = 300;
    double prpdHeight = 180.0;
    double prpdStepX = (prpdWidth) / 4.0;
    double prpdStepY = prpdHeight / multiple;
    double prpdDataStep = prpdWidth / atlasPhaseLenght;//300像素等比{atlasPhaseLenght}份
    double prpdDataX = 35.0;//初始x轴坐标
    double prpdDataY = 195.0;//初始Y轴坐标

    double hxStep = (210 - 50) / atlasPhaseLenght;
    double hyStep = (200 - 160) / atlasPhaseLenght;
    double vxStep = (210 - 50) / atlasOneLenghtF;
    double vyStep = (200 - 160) / atlasOneLenghtF;

    double qφWidth = 300;//x轴总长
    double qφHeight = 180.0;//y轴总长
    double qφStepX = (qφWidth) / 4.0;
    double qφStepY = qφHeight / multiple;
    double qφxDataStep = qφWidth / atlasPhaseLenght;//xLength像素等比{atlasPhaseLenght}份
    double qφDataX = 35;//初始x轴坐标
    double qφDataY = 195;//初始y轴坐标
    double feature1Width = 800;
    double feature1Height = 200;
    double feature1StepX = (feature1Width) / 12.0;
    double feature1StepY = feature1Height / multiple;
    double feature1DataStep = feature1Width / 120.0;
    double feature1DataX = 40;
    double feature1DataY = 230;
    double data1Width = 250;
    double data1Height = 180;
    double data1StepX = (data1Width) / 4.0;
    double data1StepY = data1Width / 4.0;
    double data1DataX = 35;
    double data1DataY = 25;

    private Timeline timeline;
    final Duration oneFrameAmt = Duration.millis(1000 / atlasOneLenghtF);
    KeyFrame oneFrame;
    int[] currentX;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize...");
        closeSet.setOnMouseClicked(event -> hideNodeView());
    }

    public void init(String testTechnology, List<DataTableDto> dataTableDtos) {
        log.info("init...");
        this.testTechnology = testTechnology;
        this.dataTableDtos = dataTableDtos;
        hideNodeView();
        current = 0;
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        oneFrame = null;
        loadAtlas(true);
    }

    private void setMenuFloat(Pane menuPane, Pane floatPane) {
        mlayer.setVisible(false);
        setPane.setVisible(false);
        if (!floatPane.equals(cutFloatPane)) cutFloatPane.setVisible(false);
        if (!floatPane.equals(statisticsFloatPane)) statisticsFloatPane.setVisible(false);
        if (!floatPane.equals(leftFloatPane)) leftFloatPane.setVisible(false);
        if (!floatPane.equals(rightFloatPane)) rightFloatPane.setVisible(false);

        if (floatPane.isVisible()) {
            floatPane.setVisible(false);
        } else {
            floatPane.setVisible(true);
        }
    }

    int currentPhase;
    String tempTestTechnology = "";

    /**
     * flag初始化标记
     *
     * @param flag
     */
    private void loadAtlas(boolean flag) {
        try {
            if (flag) {
                data1Pane.setVisible(false);
                prpsBackGraphicsContext = prpsCanvasBack.getGraphicsContext2D();
                prpsGraphicsContext = prpsCanvasAtlas.getGraphicsContext2D();
                prpdBackGraphicsContext = prpdCanvasBack.getGraphicsContext2D();
                prpdGraphicsContext = prpdCanvasAtlas.getGraphicsContext2D();
                qφBackGraphicsContext = qφCanvasBack.getGraphicsContext2D();
                qφGraphicsContext = qφCanvasAtlas.getGraphicsContext2D();
                feature1BackGraphicsContext = feature1CanvasBack.getGraphicsContext2D();
                feature1GraphicsContext = feature1CanvasAtlas.getGraphicsContext2D();
                feature1LayerGraphicsContext = feature1CanvasLayer.getGraphicsContext2D();
                feature1LineGraphicsContext = feature1CanvasLine.getGraphicsContext2D();
                data1BackGraphicsContext = data1CanvasBack.getGraphicsContext2D();
                data1GraphicsContext = data1CanvasAtlas.getGraphicsContext2D();
                switch (testTechnology) {
                    case "TEV": {
                        VF = 70;
                        VFC = 0;
                        changeRangeCount = 3;
                        changeRangeValue = 3;
                        changeRangeMultiple = 0.2;
                        unit = "dBmV";
                        break;
                    }
                    case "AA": {
                        VF = 80;
                        VFC = 10;
                        changeRangeCount = 3;
                        changeRangeValue = 3;
                        changeRangeMultiple = 0.2;
                        unit = "dBuV";
                        //AA巡检和AA统计数据是dBuV
                        //AA飞行和AA波形数据是uV
                        data1Pane.setVisible(true);
                        break;
                    }
                    case "AE": {
                        VF = 300;
                        VFC = 0;
                        changeRangeCount = 4;
                        changeRangeValue = 4;
                        changeRangeMultiple = 0.2;
                        unit = "mV";
                        data1Pane.setVisible(true);
                        break;
                    }
                    case "HFCT": {
                        VF = 300;
                        VFC = 0;
                        changeRangeCount = 4;
                        changeRangeValue = 4;
                        changeRangeMultiple = 0.2;
                        unit = "mV";
                        break;
                    }
                    case "UHF": {
                        VF = 70;
                        VFC = 80;
                        changeRangeCount = 3;
                        changeRangeValue = 3;
                        changeRangeMultiple = 0.2;
                        unit = "dBm";
                        break;
                    }
                }
                feature1Unit.setText("幅值（" + unit + "）");
                prpdUnit.setText("幅值（" + unit + "）");
                qφUnit.setText("幅值（" + unit + "）");
                dataTableDto = dataTableDtos.get(current);

                dataDeviceDto = dataTableDto.getDataDeviceDto();
                //--图谱
                fdlx2L.setText("--");//放点类型
                mcL.setText((ValidatorUtil.notEmpty(dataTableDto.getMc()) ? dataTableDto.getMc() : "--") + "个/秒");//脉冲
                ldL.setText((ValidatorUtil.notEmpty(dataTableDto.getLd()) ? dataTableDto.getLd() : "--") + "");//烈度
                zdzL.setText((ValidatorUtil.notEmpty("" + dataTableDto.getFz()) ? "" + dataTableDto.getFz() : "--") + unit);//最大值/幅值

                if (!tempTestTechnology.equalsIgnoreCase(testTechnology)) {
                    setThresholdText.setText(dataTableDto.getCkyz());
                    thresholdValue = new BigDecimal(setThresholdText.getText()).doubleValue();
                }
                tempTestTechnology = testTechnology;

                yzL.setText(thresholdValue + unit);//阈值
                yzL1.setText(thresholdValue + unit);//阈值
                thresholdL.setText("阈值(" + thresholdValue + unit + ")");
                degreeValue = new BigDecimal(dataTableDto.getXwwy()).intValue();
                xwL.setText(degreeValue + "°");//相位
                xwL1.setText(degreeValue + "°");//相位
                //获取初始相位位移
                defaultPhase = new BigDecimal(dataTableDto.getXwwy()).intValue();
                dataDeviceSiteDto = dataTableDto.getDataDeviceSiteDto();

                rangeValue = 0;
                idialog.setTitle(atlasDataTJLBView, dataDeviceDto.getSubstation() + "," + dataDeviceDto.getDeviceName() + "," + dataDeviceSiteDto.getSiteName() + "," + dataTableDto.getCssj());

                thresholdSetL.setText("( " + (0 - VFC) + " ~ " + (VF - VFC) + unit + " )");

                atlasArr = dataTableDto.getAtlasArr();
//                //test
//                atlasArr = new Float[200][60];
//                for (int x = 0; x < atlasArr.length; x++) {
//                    for (int y = 0; y < 60; y++) {
//                        atlasArr[x][y] = x + y +30f;
//                    }
//                }
                secondCount = atlasArr.length / atlasOneLenghtD;
                maxSecondCountArr = new double[secondCount];//相位数据  由图谱数据转换而来
                secondCurrent = 0;
                currentX = new int[secondCount];
                maxPhaseAtlasArr = new double[secondCount][(int) atlasPhaseLenght];
                statisticsLastSecond = secondCount;
                statisticsBeginSecond = secondCount - 1;
                statisticsValue = 1;
                //按钮--量程-放大--
                enlargeMenu.setDisable(true);
                enlargeImg.setImage(new javafx.scene.image.Image("/com/redphase/images/8x.png"));
                //按钮--量程-缩小--
                narrowMenu.setDisable(false);
                narrowImg.setImage(new javafx.scene.image.Image("/com/redphase/images/9.png"));
                //量程-值-
                changeRangeValue = changeRangeCount;
                //按钮--统计-时间--
                if (secondCount > 10) {
                    statistics2Button.setDisable(false);
                } else {
                    statistics2Button.setDisable(true);
                }
                if (secondCount > 30) {
                    statistics3Button.setDisable(false);
                } else {
                    statistics3Button.setDisable(true);
                }
                currentPhaseX = (int) atlasOneLenghtD;

                feature1GraphicsContext.setFill(Color.web("#6cb7f7", 1.0));
                feature1GraphicsContext.setLineWidth(0);
                prpsGraphicsContext.setLineWidth(2);
                prpdGraphicsContext.setLineWidth(4);
                qφGraphicsContext.setStroke(Color.web("#5dbef5", 1.0));
                qφGraphicsContext.setLineWidth(3);
                feature1LayerGraphicsContext.setStroke(Color.web("#3cf7e3", 1.0));
                feature1LayerGraphicsContext.setFill(Color.web("#3cf7e3"));
            }
            reDrawVFBackground();

            //----------------------特征值展示区-所有秒的最大值等---------------------------------------
            int secondX = 0;
            for (int x = 0; x < secondCount * atlasOneLenghtD; x++) {
                for (int y = 0; y < atlasPhaseLenght; y++) {
                    double pv = (atlasArr[x][y] + VFC) / VF;
                    pv = pv * 100;
                    if (maxSecondCountArr[secondX] < pv) {
                        maxSecondCountArr[secondX] = pv;
                    }
                    if (maxPhaseAtlasArr[secondX][y] < pv) {
                        maxPhaseAtlasArr[secondX][y] = pv;
                    }
                }
                if ((x + 1) % atlasOneLenghtD == 0) {
                    double maxpv = changeNumRangeMultiple(maxSecondCountArr[secondX]);//, feature1Height
                    if ((thresholdValue + VFC) / VF * 100 <= maxpv) {
                        double maxpvheight = (maxpv > 100 ? 100 : maxpv) * 2;
                        feature1GraphicsContext.fillRect(feature1DataX + (secondX * feature1DataStep) + 1, 230 - maxpvheight, feature1DataStep - 2, maxpvheight);
                    }
                    if (secondX >= statisticsBeginSecond && secondX < statisticsLastSecond) {
                        feature1LayerGraphicsContext.fillRect(feature1DataX + (secondX * feature1DataStep), 30, feature1DataStep, feature1Height);
                        feature1LayerGraphicsContext.strokeRect(feature1DataX + (secondX * feature1DataStep), 30, feature1DataStep, feature1Height);
                    }
                    secondX++;
                }
            }
            //相位调整---
            int tempPhase = degreeValue - defaultPhase;
            if (tempPhase < 0) {
                tempPhase = 360 + tempPhase;
            }
            currentPhase = 60 - tempPhase / 6;//真实数组相位 24=360/6

            //-----------------------展示statistics的数据-------------------------------------------------
            //实际秒
            int realStatisticsSecond = statisticsLastSecond - statisticsBeginSecond;
            Float[][] statisticsAtlasArr = new Float[realStatisticsSecond * atlasOneLenghtD][60];//图谱数据--统计数据集合
            System.arraycopy(atlasArr, statisticsBeginSecond * atlasOneLenghtD, statisticsAtlasArr, 0, (realStatisticsSecond) * atlasOneLenghtD);
            //总统计秒
            for (int s = 0; s < realStatisticsSecond; s++) {
                //每秒数据集
                Float[][] currentAtlasArr = new Float[50][(int) atlasPhaseLenght];
                System.arraycopy(statisticsAtlasArr, s * 50, currentAtlasArr, 0, 50);
                prpsGraphicsContext.clearRect(0, 0, prpsCanvasAtlas.getWidth(), prpsCanvasAtlas.getHeight());
                qφGraphicsContext.clearRect(0, 0, qφCanvasAtlas.getWidth(), qφCanvasAtlas.getHeight());
                double[] lastQφArr = new double[(int) atlasPhaseLenght];//图谱数据
                //倒叙数组
                Collections.reverse(Arrays.asList(currentAtlasArr));
                //单个数据
                for (int x = 0; x < currentPhaseX; x++) {
                    //调整相位后的单组数据
                    Float[] currentPhaseAtlasArr = new Float[(int) atlasPhaseLenght];
                    //根据相位位移获取前半段数据
                    if (currentPhase < atlasPhaseLenght) {
                        System.arraycopy(currentAtlasArr[x], currentPhase, currentPhaseAtlasArr, 0, (int) atlasPhaseLenght - currentPhase);
                    }
                    //根据相位位移获取后半段数据
                    System.arraycopy(currentAtlasArr[x], 0, currentPhaseAtlasArr, (int) atlasPhaseLenght - currentPhase, currentPhase);

                    for (int y = 0; y < atlasPhaseLenght; y++) {
                        double pv = getSetPvColor((currentPhaseAtlasArr[y] + VFC) / VF);
                        if (thresholdValue > currentPhaseAtlasArr[y]) continue;
                        if (pv > 0) {
                            if (lastQφArr[y] < pv) {//&& s == (realStatisticsSecond - 1)
                                lastQφArr[y] = pv;
                                //qφ
                                double qφpv = changeNumRangeMultiple(pv);//, feature1Height
                                if (qφpv > 0) {
                                    qφGraphicsContext.strokeLine(qφDataX + 3 + (y * qφxDataStep), qφDataY, qφDataX + 3 + (y * qφxDataStep), qφDataY - (qφHeight / 100.0 * (qφpv > 100 ? 100 : qφpv)));
                                }
                            }
                            double numRangeMultiple = changeNumRangeMultiple(pv);
                            double numRangeMultiplePrps = 160 + (x * vyStep) + (y * -hyStep) - (1.2 * (numRangeMultiple > 100 ? 100 : numRangeMultiple));
                            prpsGraphicsContext.strokeLine(50 + (x * vxStep) + (y * hxStep), 160 + (x * vyStep) + (y * -hyStep), 50 + (x * vxStep) + (y * hxStep), numRangeMultiplePrps);//1.2=120/100
                            if (numRangeMultiple <= 100) {
                                double numRangeMultiplePrpd = 195 - (1.8 * (numRangeMultiple > 100 ? 100 : numRangeMultiple));
                                prpdGraphicsContext.strokeLine(prpdDataX + (y * prpdDataStep), numRangeMultiplePrpd, prpdDataX + (y * prpdDataStep), numRangeMultiplePrpd);//1.8=180/100 180=高像素 100=等份数
                            }
                        }
                    }
                }
            }
            //数据区
            if ("AA".equalsIgnoreCase(testTechnology) || "AE".equalsIgnoreCase(testTechnology)) {
                data1GraphicsContext.clearRect(0, 0, data1CanvasAtlas.getWidth(), data1CanvasAtlas.getHeight());
                drawData1Value(data1GraphicsContext);
            }
        } catch (Exception e) {
            log.error("统计图谱异常!", e);
            ialert.error("统计图谱异常!" + e.getMessage());
        }
    }

    private double changeNumRangeMultiple(double srcpv) {//, double height
        return srcpv + 100 * changeRangeMultiple * (changeRangeCount - changeRangeValue);
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

    /**
     * 重新构画各图参考线
     */
    private void reDrawVFBackground() {
        hPixel = ((VF + rangeValue)) / multiple;
        prpsGraphicsContext.clearRect(0, 0, prpsCanvasAtlas.getWidth(), prpsCanvasAtlas.getHeight());
        drawPRPSBackground(prpsBackGraphicsContext);
        prpdGraphicsContext.clearRect(0, 0, prpdCanvasAtlas.getWidth(), prpdCanvasAtlas.getHeight());
        drawPRPDBackground(prpdBackGraphicsContext);
        qφGraphicsContext.clearRect(0, 0, qφCanvasAtlas.getWidth(), qφCanvasAtlas.getHeight());
        drawQφBackground(qφBackGraphicsContext);
        feature1GraphicsContext.clearRect(0, 0, feature1CanvasAtlas.getWidth(), feature1CanvasAtlas.getHeight());
        feature1LayerGraphicsContext.clearRect(0, 0, feature1CanvasLayer.getWidth(), feature1CanvasLayer.getHeight());
        feature1LineGraphicsContext.clearRect(0, 0, feature1CanvasLine.getWidth(), feature1CanvasLine.getHeight());
        drawFeature1Background(feature1BackGraphicsContext);
        if ("AA".equalsIgnoreCase(testTechnology) || "AE".equalsIgnoreCase(testTechnology)) {
            drawData1Background(data1BackGraphicsContext);
        }
    }

    //PRPS背景坐标
    private void drawPRPSBackground(GraphicsContext gc) {
        //清屏
        gc.clearRect(0, 0, prpsCanvasBack.getWidth(), prpsCanvasBack.getHeight());
        gc.setStroke(Color.web("#cccccc", 1.0));
        gc.setLineWidth(1);
        gc.setFont(Font.font(10));
        gc.setFill(Color.web("#999999"));
        double pixelNum = (120 / multiple);
        //纵面图--横线  +1
        for (int i = 0; i < multiple + 1; i++) {
            gc.strokeLine(50, 160 - (i * pixelNum), 210, 120 - (i * pixelNum));
            gc.fillText("" + new BigDecimal(i * hPixel - VFC).setScale(2, RoundingMode.UP), 10, 160 - (i * pixelNum) + 5);
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

    //PRPD背景坐标
    private void drawPRPDBackground(GraphicsContext gc) {
        //清屏
        gc.clearRect(0, 0, prpdCanvasBack.getWidth(), prpdCanvasBack.getHeight());
        gc.setStroke(Color.web("#cccccc", 1.0));
        gc.setLineWidth(1);
        gc.setFont(Font.font(10));
        gc.setFill(Color.web("#999999"));

        //纵面图--横线  +1
        for (int i = 0; i < multiple + 1; i++) {
            gc.strokeLine(prpdDataX, prpdDataY - (i * prpdStepY), prpdDataX + prpdWidth, prpdDataY - (i * prpdStepY));
            gc.fillText("" + new BigDecimal(i * hPixel - VFC).setScale(2, RoundingMode.UP), 0, prpdDataY - (i * prpdStepY) + 5);
        }
        //纵面图--竖线
        for (int i = 0; i < 5; i++) {
            //160像素分为4等份
            gc.strokeLine(prpdDataX + (i * prpdStepX), prpdDataY, prpdDataX + (i * prpdStepX), prpdDataY - prpdHeight);
            gc.fillText("" + (90 * i), prpdDataX + (i * prpdStepX) - 5, prpdDataY + 15);
        }
        //=============正弦线===begin==========================
        //中线
        gc.setStroke(Color.web("#999999"));
        gc.setLineDashes(2);
        gc.strokeLine(prpdDataX, prpdDataY - (prpdHeight * 0.5), prpdDataX + prpdWidth, prpdDataY - (prpdHeight * 0.5));
        //曲线
        double sineWidth = prpdWidth, sineHeight = prpdHeight;
        gc.setStroke(Color.web("#69ddf0"));
        gc.setLineWidth(1.5);
        gc.setLineDashes(0);
        gc.beginPath();
        //-------------------------
        gc.moveTo(prpdDataX, prpdDataY - (sineHeight * 0.5));
        gc.bezierCurveTo(prpdDataX + (sineWidth) / 4, prpdDataY - sineHeight, prpdDataX + (sineWidth) / 4, prpdDataY - sineHeight, prpdDataX + (sineWidth) / 2, prpdDataY - (sineHeight * 0.5));
        //--------------------------
        gc.moveTo(prpdDataX + (sineWidth) / 2, prpdDataY - (sineHeight * 0.5));
        gc.bezierCurveTo(prpdDataX + sineWidth - (sineWidth) / 4, prpdDataY, prpdDataX + sineWidth - (sineWidth) / 4, prpdDataY, prpdDataX + (sineWidth), prpdDataY - (sineHeight * 0.5));
        //-------------------------
        gc.stroke();
        gc.closePath();
        //=============正弦线===end==========================
    }

    int currentPhaseX = 0;
    AtomicBoolean isFlag = new AtomicBoolean(true);

    private void drawPRPSAtlas() {
        if (oneFrame == null) {
            oneFrame = new KeyFrame(oneFrameAmt, event -> {
                try {
                    if (!isFlag.get()) {
                        log.debug("还未完成..");
                        return;
                    }
                    try {
                        isFlag.getAndSet(false);
                        int srcPos = secondCurrent * atlasOneLenghtD + currentX[secondCurrent];
                        if (secondCurrent >= secondCount || secondCurrent > 0 && srcPos - 1 == atlasArr.length - atlasOneLenghtD) {
                            bStop();
                        } else {
                            if (currentX[secondCurrent] == 0) {
                                //----------------------特征值展示区-秒的最大值等---------------------------------------
                                int destNum = (int) atlasPhaseLenght - currentPhase;
                                //调整相位后的单组数据-最大值
                                double[] currentMaxPhaseAtlasArr = new double[(int) atlasPhaseLenght];
                                //根据相位位移获取前半段数据
                                if (currentPhase < atlasPhaseLenght) {
                                    System.arraycopy(maxPhaseAtlasArr[secondCurrent], currentPhase, currentMaxPhaseAtlasArr, 0, destNum);
                                }
                                //根据相位位移获取后半段数据
                                System.arraycopy(maxPhaseAtlasArr[secondCurrent], 0, currentMaxPhaseAtlasArr, destNum, currentPhase);
                                qφGraphicsContext.clearRect(0, 0, prpdCanvasAtlas.getWidth(), prpdCanvasAtlas.getHeight());
                                data1GraphicsContext.clearRect(0, 0, data1CanvasAtlas.getWidth(), data1CanvasAtlas.getHeight());
                                for (int y = 0; y < atlasPhaseLenght; y++) {
                                    if (((thresholdValue + VFC) / VF) * 100 > currentMaxPhaseAtlasArr[y]) continue;
                                    double qφpv = changeNumRangeMultiple(currentMaxPhaseAtlasArr[y]);//, qφHeight
                                    if (qφpv > 0) {
                                        qφGraphicsContext.strokeLine(qφDataX + 3 + (y * qφxDataStep), qφDataY, qφDataX + 3 + (y * qφxDataStep), qφDataY - (qφHeight / 100.0 * (qφpv > 100 ? 100 : qφpv)));
                                    }
                                }
                                //数据区
                                if ("AA".equalsIgnoreCase(testTechnology) || "AE".equalsIgnoreCase(testTechnology)) {
                                    data1GraphicsContext.clearRect(0, 0, data1CanvasAtlas.getWidth(), data1CanvasAtlas.getHeight());
                                    drawData1Value(data1GraphicsContext);
                                }

                                reDrawfeature1Graphics();
                                feature1LayerGraphicsContext.clearRect(0, 0, feature1CanvasLayer.getWidth(), feature1CanvasLayer.getHeight());
                                //#4169E1"
                                feature1LayerGraphicsContext.setStroke(Color.web("#3cf7e3", 1.0));
                                feature1LayerGraphicsContext.setFill(Color.web("#3cf7e3"));
                                feature1LayerGraphicsContext.fillRect(feature1DataX, 30, feature1DataStep + (secondCurrent * feature1DataStep), feature1Height);
                                feature1LayerGraphicsContext.strokeRect(feature1DataX, 30, feature1DataStep + (secondCurrent * feature1DataStep), feature1Height);
                            }
                            currentPhaseX = currentX[secondCurrent];

                            //递推--begin----
                            prpsGraphicsContext.clearRect(0, 0, prpsCanvasAtlas.getWidth(), prpsCanvasAtlas.getHeight());
                            //获取当前组数
                            Float[][] currentAtlasArr;
                            if (secondCurrent > 0) {
                                currentAtlasArr = new Float[50][(int) atlasPhaseLenght];
                                System.arraycopy(atlasArr, srcPos, currentAtlasArr, 0, currentAtlasArr.length);
                            } else {
                                currentAtlasArr = new Float[currentX[secondCurrent] + 1][(int) atlasPhaseLenght];
                                System.arraycopy(atlasArr, 0, currentAtlasArr, 0, currentAtlasArr.length);
                            }
                            //倒叙数组
                            Collections.reverse(Arrays.asList(currentAtlasArr));
                            for (int x = 0; x < currentAtlasArr.length; x++) {
                                int destNum = (int) atlasPhaseLenght - currentPhase;
                                //调整相位后的单组数据
                                Float[] currentPhaseAtlasArr = new Float[(int) atlasPhaseLenght];
                                //根据相位位移获取前半段数据
                                if (currentPhase < atlasPhaseLenght) {
                                    System.arraycopy(currentAtlasArr[x], currentPhase, currentPhaseAtlasArr, 0, destNum);
                                }
                                //根据相位位移获取后半段数据
                                System.arraycopy(currentAtlasArr[x], 0, currentPhaseAtlasArr, destNum, currentPhase);
                                for (int y = 0; y < atlasPhaseLenght; y++) {
                                    double pv = getSetPvColor((currentPhaseAtlasArr[y] + VFC) / VF);
                                    if (thresholdValue > currentPhaseAtlasArr[y]) continue;
                                    if (pv > 0) {
                                        double numRangeMultiple = changeNumRangeMultiple(pv);
                                        double numRangeMultiplePrps = 160 + (x * vyStep) + (y * -hyStep) - (1.2 * (numRangeMultiple > 100 ? 100 : numRangeMultiple));
                                        prpsGraphicsContext.strokeLine(50 + (x * vxStep) + (y * hxStep), 160 + (x * vyStep) + (y * -hyStep), 50 + (x * vxStep) + (y * hxStep), numRangeMultiplePrps);//1.2=120/100
                                        if (numRangeMultiple <= 100) {
                                            double numRangeMultiplePrpd = 195 - (1.8 * (numRangeMultiple > 100 ? 100 : numRangeMultiple));
                                            prpdGraphicsContext.strokeLine(prpdDataX + (y * prpdDataStep), numRangeMultiplePrpd, prpdDataX + (y * prpdDataStep), numRangeMultiplePrpd);//1.8=180/100 180=高像素 100=等份数
                                        }
                                    }
                                }
                            }
                            //递推--end----
                            if ((currentX[secondCurrent] + 1) % 50 == 0) {
                                currentX[secondCurrent] = 0;
                                secondCurrent++;
                            } else {
                                currentX[secondCurrent]++;
                            }
                        }
                    } finally {
                        isFlag.getAndSet(true);
                    }
                } catch (Exception e) {
                    bStop();
                    log.error("系统异常!", e);
                    Platform.runLater(() -> {
                        ialert.error(I18nUtil.get("error.sys") + e.getMessage());
                    });
                }
            });
            timeline.getKeyFrames().addAll(oneFrame);
        }
    }


    /**
     * 重画特征值
     */
    private void reDrawfeature1Graphics() {
        //特征值数据区
        feature1GraphicsContext.clearRect(0, 0, feature1CanvasAtlas.getWidth(), feature1CanvasAtlas.getHeight());
        for (int secondX = 0; secondX <= secondCurrent; secondX++) {
            double feature1pv = changeNumRangeMultiple(maxSecondCountArr[secondX]);//, feature1Height
            if ((thresholdValue + VFC) / VF * 100 > feature1pv) continue;
            if (feature1pv > 100) {
                feature1pv = 100;
            }
            double feature1pvheight = (feature1pv > 100 ? 100 : feature1pv) * 2;
            feature1GraphicsContext.fillRect(feature1DataX + (secondX * feature1DataStep) + 1, 230 - feature1pvheight, feature1DataStep - 2, feature1pvheight);

        }
    }

    //Qφ背景坐标
    private void drawQφBackground(GraphicsContext gc) {
        //清屏
        gc.clearRect(0, 0, qφCanvasBack.getWidth(), qφCanvasBack.getHeight());
        gc.setStroke(Color.web("#cccccc", 1.0));
        gc.setLineWidth(1);
        gc.setFont(Font.font(10));
        gc.setFill(Color.web("#999999"));

        //纵面图--横线  +1
        for (int i = 0; i < multiple + 1; i++) {
            gc.strokeLine(qφDataX, qφDataY - (i * qφStepY), qφDataX + qφWidth, qφDataY - (i * qφStepY));
            gc.fillText("" + new BigDecimal(i * hPixel - VFC).setScale(2, RoundingMode.UP), 0, qφDataY - (i * qφStepY) + 5);
        }
        //纵面图--竖线
        for (int i = 0; i < 5; i++) {
            //160像素分为4等份
            gc.strokeLine(qφDataX + (i * qφStepX), qφDataY, qφDataX + (i * qφStepX), qφDataY - qφHeight);
            gc.fillText("" + (90 * i), qφDataX + (i * qφStepX) - 5, qφDataY + 15);
        }
    }

    //	特征图 背景坐标
    private void drawFeature1Background(GraphicsContext gc) {
        //清屏
        gc.clearRect(0, 0, feature1CanvasBack.getWidth(), feature1CanvasBack.getHeight());
        gc.setStroke(Color.web("#cccccc", 1.0));
        gc.setLineWidth(1);
        gc.setFont(Font.font(10));
        gc.setFill(Color.web("#999999"));

        //纵面图--横线  +1
        for (int i = 0; i < multiple + 1; i++) {
            gc.strokeLine(feature1DataX, feature1DataY - (i * feature1StepY), feature1DataX + feature1Width, feature1DataY - (i * feature1StepY));
            gc.fillText("" + new BigDecimal(i * hPixel - VFC).setScale(2, RoundingMode.UP), 5, feature1DataY - (i * feature1StepY) + 5);
        }
        //纵面图--竖线
        for (int i = 0; i < 13; i++) {
            //160像素分为4等份
            gc.strokeLine(feature1DataX + (i * feature1StepX), feature1DataY, feature1DataX + (i * feature1StepX), feature1DataY - feature1Height);
            gc.fillText("" + (10 * i) + "S", feature1DataX + (i * feature1StepX) - (i > 9 ? 10 : 5), feature1DataY + 15);
        }
        //阈值参考线 参考阈值包括两个，一个是数据文件中的，为设备端的参考阈值，一个是用户在软件中设置的，软件中的默认为幅值下限，且这两个参考阈值采用不同的颜色在特征图中给予指示
        //清屏
        feature1LineGraphicsContext.clearRect(0, 0, feature1CanvasLine.getWidth(), feature1CanvasLine.getHeight());
        //设备端的参考阈值--
        //中线
        feature1LineGraphicsContext.setStroke(Color.web("#3cf7e3"));
        feature1LineGraphicsContext.setLineDashes(2);
        double pv = (new BigDecimal(dataTableDto.getCkyz()).doubleValue() + VFC) / (VF - VF * changeRangeMultiple * (changeRangeCount - changeRangeValue));
        pv = pv * 100;
        feature1LineGraphicsContext.strokeLine(feature1DataX, feature1DataY - (pv * 2), feature1DataX + feature1Width, feature1DataY - (pv * 2));
        //软件中设置的，软件中的默认为幅值下限--
        feature1LineGraphicsContext.setStroke(Color.rgb(255, 0, 0));
        feature1LineGraphicsContext.setLineDashes(2);
        double ckfz = (thresholdValue + VFC) / (VF - VF * changeRangeMultiple * (changeRangeCount - changeRangeValue));
        ckfz = ckfz * 100;
        if (ckfz <= 100) {
            feature1LineGraphicsContext.strokeLine(feature1DataX, feature1DataY - (ckfz * 2), feature1DataX + feature1Width, feature1DataY - (ckfz * 2));
        }
    }

    //数据显示区 背景坐标
    private void drawData1Background(GraphicsContext gc) {
        //清屏
        gc.clearRect(0, 0, data1CanvasBack.getWidth(), data1CanvasBack.getHeight());
//        gc.setLineWidth(15);
        double height = 15;
        for (int i = 0; i < 4; i++) {
            gc.setFill(Color.web("#cccccc", 1.0));
            gc.fillRect(data1DataX + 5, data1DataY + (i * data1StepY), data1Width, height);

            gc.setFont(Font.font(18));
            gc.setFill(Color.web("#2266a0"));
            String text = "";
            int type = 0;//0幅值 1,百分比
            switch (i) {
                //横线  Qp
                case 0:
                    text = "Qp";
                    type = 0;
                    break;
                //横线  Qm
                case 1:
                    text = "Qm";
                    type = 0;
                    break;
                //横线  F1
                case 2:
                    text = "F1";
                    type = 1;
                    break;
                //横线  F2
                case 3:
                    text = "F2";
                    type = 1;
                    break;
            }
            gc.fillText(text, data1DataX - 30, data1DataY + (i * data1StepY) + 15);

            gc.setFont(Font.font(10));
            gc.setFill(Color.web("#999999"));
            for (int n = 0; n < 5; n++) {
                gc.fillText("" + new BigDecimal((type == 0 ? (VF + rangeValue) : 100) / 4 * n).setScale(2, RoundingMode.UP), data1DataX + (n * data1StepX) - 5, data1DataY + (i * data1StepY) + 35);
            }
        }
    }

    //数据显示区 显值
    private void drawData1Value(GraphicsContext gc) {
        //AA  AE
        //每一秒的幅值信息，Qp--最大值，Qm--有效值，F1--F1分量，F2--F2分量
        //清屏
        gc.clearRect(0, 0, data1CanvasAtlas.getWidth(), data1CanvasAtlas.getHeight());
        gc.setLineWidth(0);
        gc.setStroke(Color.web("#5dbef5", 1.0));
        gc.setFont(Font.font(18));
        gc.setFill(Color.web("#2266a0"));

        for (int i = 0; i < 4; i++) {
            switch (i) {
                //横线  Qp 最大值
                case 0: {
                    double qp = maxSecondCountArr[secondCurrent];
                    double value = qp > 0 ? (qp + VFC) / (VF - VF * changeRangeMultiple * (changeRangeCount - changeRangeValue)) : 0;
                    value = value * 100;
                    setData1ValueByType(i, gc, qp + unit, value);
                    break;
                }
                //横线  Qm 有效值
                case 1: {
                    double qm = dataTableDto.getYxz() == null ? 0 : dataTableDto.getYxz();
                    double value = qm > 0 ? (qm + VFC) / (VF - VF * changeRangeMultiple * (changeRangeCount - changeRangeValue)) : 0;
                    value = value * 100;
                    setData1ValueByType(i, gc, qm + unit, value);
                    break;
                }
                //横线  F1 F1分量
                case 2: {
                    double f1 = dataTableDto.getF1() == null ? 0 : dataTableDto.getF1();
                    double value = f1;
                    setData1ValueByType(i, gc, f1 + "%", value);
                    break;
                }
                //横线  F2 F2分量
                case 3: {
                    double f2 = dataTableDto.getF2() == null ? 0 : dataTableDto.getF2();
                    double value = f2;
                    setData1ValueByType(i, gc, f2 + "%", value);
                    break;
                }
            }
        }
    }

    double data1DataStepRatio = 2.5;

    private void setData1ValueByType(int type, GraphicsContext gc, String value, double width) {
        double height = 15;
        if (width > 0) {
            gc.fillRect(data1DataX + 5, data1DataY + (type * data1StepY), (width > 100 ? 100 : width) * data1DataStepRatio, height);
        }
        gc.fillText(value, data1DataX + data1Width + 10, data1DataY + (type * data1StepY) + 15);
    }

    @FXML
    public void doSmall() {
        log.info("doSmall..");
        doChange(0);
    }

    @FXML
    public void doBig() {
        log.info("doBig..");
        doChange(1);
    }

    private void doChange(int type) {
        String value = setThresholdText.getText();
        if (ValidatorUtil.isEmpty(value)) {
            ialert.error("阈值不能为空!");
        }
        BigDecimal num = new BigDecimal(value);

        if (type == 0) {
            num = num.subtract(new BigDecimal(1));
        } else {
            num = num.add(new BigDecimal(1));
        }
        if (num.doubleValue() < (0 - VFC)) {
            ialert.error("阈值不能小于" + (0 - VFC));
            return;
        }
        if (num.doubleValue() > (VF - VFC)) {
            ialert.error("阈值不能大于" + (VF - VFC));
            return;
        }
        setThresholdText.setText(String.valueOf(num));
    }

    private void hideNodeView(Parent... nodeViews) {
        if (nodeViews == null || nodeViews.length == 0) {
            nodeViews = new Parent[]{mlayer, setPane, openDataPane, setThresholdPane, cutFloatPane, statisticsFloatPane, leftFloatPane, rightFloatPane};
        }
        for (Parent nodeView : nodeViews) {
            nodeView.setVisible(false);
        }
    }

    @FXML
    VBox openMenu;//打开
    @FXML
    TableView filesTableView;//表格
    @FXML
    TableColumn idTc;//序号
    @FXML
    TableColumn fileTc;//文件名

    @FXML
    public void menuOpen() {
        log.info("menuOpen..");
        hideNodeView();

        ObservableList<DataTableDto> data = FXCollections.observableArrayList(dataTableDtos);
        filesTableView.setItems(data);
        idTc.setCellFactory((col) -> {
            TableCell<DataTableDto, String> cell = new TableCell<DataTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    this.setStyle("-fx-pref-width: 80;-fx-text-overrun: center-ellipsis;");
                    if (!empty) {
                        int rowIndex = this.getIndex() + 1;
                        this.setText(String.valueOf(rowIndex));
                    }
                }
            };
            return cell;
        });
        fileTc.setCellFactory((col) -> {
            TableCell<DataTableDto, String> cell = new TableCell<DataTableDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    this.setStyle("-fx-text-fill: #31a1ff;-fx-underline: true;-fx-cursor: hand;");
                    if (!empty) {
                        DataTableDto tableDto = this.getTableView().getItems().get(this.getIndex());
                        this.setText(tableDto.getCssj());
                        this.setOnMouseClicked((me) -> {
                            current = tableDto.getSeqNo();
                            hideNodeView();
                            bStop();
                            loadAtlas(true);
                        });
                    }
                }
            };
            return cell;
        });

        mlayer.setVisible(true);
        setPane.setVisible(true);
        setTitle.setText("打开文件");
        openDataPane.setVisible(true);
    }

    //@FXML
    //Pane saveMenu;//保存
    @FXML
    public void menuSave() {
        log.info("menuSave..");
    }

    //@FXML
    //Pane reportMenu;//报告
    @FXML
    public void menuReport() {
        log.info("menuReport..");
        ialert.warning("待完成");
    }

    @FXML
    VBox cutMenu;//剪切板

    @FXML
    public void menuCut() {
        log.info("menuCut..");
        setMenuFloat(cutMenu, cutFloatPane);
    }

    @FXML
    VBox thresholdMenu;//阈值

    @FXML
    public void menuThreshold() {
        log.info("searhData..");
        hideNodeView();
        mlayer.setVisible(true);
        setPane.setVisible(true);
        setTitle.setText("阈值设置");
        setThresholdPane.setVisible(true);
    }

    @FXML
    Pane leftSecondMenu;//统计时间-左移
    @FXML
    ImageView leftSecondImg;

    @FXML
    public void menuLeftSecond() {
        log.info("menuLeftSecond..");

        changeSecond(0);
    }

    @FXML
    Pane statisticsMenu;//统计时间
    @FXML
    ImageView statisticsImg;
    @FXML
    Label statisticsL;

    @FXML
    public void menuStatistics() {
        log.info("menuStatistics..");
        setMenuFloat(statisticsMenu, statisticsFloatPane);
    }

    @FXML
    Pane rightSecondMenu;//统计时间-右移
    @FXML
    ImageView rightSecondImg;

    @FXML
    public void menuRightSecond() {
        log.info("menuRightSecond..");
        changeSecond(1);
    }

    /**
     * 变更统计时间值
     *
     * @param type 0减 1加
     */
    private void changeSecond(int type) {
        switch (type) {
            case 0: {
                statisticsLastSecond -= 1;
                break;
            }
            case 1: {
                statisticsLastSecond += 1;
                break;
            }
        }
        statisticsBeginSecond = statisticsLastSecond - statisticsValue;
        if (statisticsLastSecond > secondCount) {
            statisticsLastSecond = statisticsValue;
        }
        if (statisticsBeginSecond < 0) {
            statisticsLastSecond = secondCount;
        }
        statisticsBeginSecond = statisticsLastSecond - statisticsValue;
        loadAtlas(false);
    }

    int leftDegreeStep = 15;//相位左移变量
    int rightDegreeStep = 15;//相位右移变量

    @FXML
    public void leftDegree() {
        log.info("leftDegree..");
        changeDegreeValue(leftDegreeStep, 0);
    }

    @FXML
    public void rightDegree() {
        log.info("rightDegree..");
        changeDegreeValue(rightDegreeStep, 1);
    }

    @FXML
    Pane leftDegreeMenu;//相位-左移

    @FXML
    public void menuLeftDegree() {
        log.info("menuLeftDegree..");
        setMenuFloat(leftDegreeMenu, leftFloatPane);
    }

    @FXML
    Pane rightDegreeMenu;//相位-右移

    @FXML
    public void menuRightDegree() {
        log.info("menuRightDegree..");
        setMenuFloat(rightDegreeMenu, rightFloatPane);
    }

    @FXML
    Pane enlargeMenu;//放大
    @FXML
    ImageView enlargeImg;

    @FXML
    public void menuEnlarge() {
        log.info("menuEnlarge..");
        //录波数据展示 全部动态坐标(纵坐标 放大/缩小) step 20 最多调整3次
        changeRangeValue++;
        if (changeRangeValue >= changeRangeCount) {
            enlargeMenu.setDisable(true);
            enlargeImg.setImage(new javafx.scene.image.Image("/com/redphase/images/8x.png"));
            narrowMenu.setDisable(false);
            narrowImg.setImage(new javafx.scene.image.Image("/com/redphase/images/9.png"));
            changeRangeValue = changeRangeCount;
        } else {
            enlargeMenu.setDisable(false);
            enlargeImg.setImage(new javafx.scene.image.Image("/com/redphase/images/8.png"));
            narrowMenu.setDisable(false);
            narrowImg.setImage(new javafx.scene.image.Image("/com/redphase/images/9.png"));
        }
        rangeValue = -VF * changeRangeMultiple * (changeRangeCount - changeRangeValue);
        if (payMenu.isDisable()) {
            reDrawVFBackground();
        } else {
            loadAtlas(false);
        }
    }

    @FXML
    Pane narrowMenu;//缩小
    @FXML
    ImageView narrowImg;

    @FXML
    public void menuNarrow() {
        log.info("menuNarrow..");
        changeRangeValue--;
        if (changeRangeValue <= 0) {
            enlargeMenu.setDisable(false);
            enlargeImg.setImage(new javafx.scene.image.Image("/com/redphase/images/8.png"));
            narrowMenu.setDisable(true);
            narrowImg.setImage(new javafx.scene.image.Image("/com/redphase/images/9x.png"));
            changeRangeValue = 0;
        } else {
            enlargeMenu.setDisable(false);
            enlargeImg.setImage(new javafx.scene.image.Image("/com/redphase/images/8.png"));
            narrowMenu.setDisable(false);
            narrowImg.setImage(new javafx.scene.image.Image("/com/redphase/images/9.png"));
        }
        rangeValue = -VF * changeRangeMultiple * (changeRangeCount - changeRangeValue);
        if (payMenu.isDisable()) {
            reDrawVFBackground();
        } else {
            loadAtlas(false);
        }
    }

    @FXML
    Pane payMenu;//播放
    @FXML
    ImageView playImg;//播放图
    @FXML
    ImageView pauseImg;//暂停图
    @FXML
    ImageView stopImg;//停止图

    private void setStatisticsButteon(boolean flag) {
        leftSecondImg.setImage(new javafx.scene.image.Image("/com/redphase/images/5" + (flag ? "x" : "") + ".png"));//false
        leftSecondMenu.setDisable(flag);
        statisticsImg.setImage(new javafx.scene.image.Image("/com/redphase/images/6" + (flag ? "x" : "") + ".png"));//false
        statisticsMenu.setDisable(flag);
        rightSecondImg.setImage(new javafx.scene.image.Image("/com/redphase/images/7" + (flag ? "x" : "") + ".png"));//false
        rightSecondMenu.setDisable(flag);
    }

    @FXML
    public void menuPay() {
        log.info("menuPay..");
        if (stopMenu.isDisable()) {
            reDrawVFBackground();
        }
        if (oneFrame == null) {
            drawPRPSAtlas();
        }

        setStatisticsButteon(true);

        timeline.play();

        playImg.setImage(new javafx.scene.image.Image("/com/redphase/images/7x.png"));//false
        payMenu.setDisable(true);

        pauseImg.setImage(new javafx.scene.image.Image("/com/redphase/images/10.png"));//true
        pauseMenu.setDisable(false);

        stopImg.setImage(new javafx.scene.image.Image("/com/redphase/images/11.png"));//true
        stopMenu.setDisable(false);
    }

    @FXML
    Pane pauseMenu;//暂停

    @FXML
    public void menuPause() {
        log.info("menuPause..");
        timeline.pause();

        playImg.setImage(new javafx.scene.image.Image("/com/redphase/images/7.png"));//true
        payMenu.setDisable(false);

        pauseImg.setImage(new javafx.scene.image.Image("/com/redphase/images/10x.png"));//false
        pauseMenu.setDisable(true);

        stopImg.setImage(new javafx.scene.image.Image("/com/redphase/images/11.png"));//true
        stopMenu.setDisable(false);
    }

    @FXML
    Pane stopMenu;//停止

    @FXML
    public void menuStop() {
        log.info("menuStop..");
        bStop();
        loadAtlas(false);
    }

    private void bStop() {
        timeline.stop();
        isFlag.getAndSet(true);
        setStatisticsButteon(false);
        //播放复位
        secondCurrent = 0;
        for (int s = 0; s < secondCount; s++) {
            currentX[s] = 0;
        }
        currentPhaseX = atlasOneLenghtD;

        playImg.setImage(new javafx.scene.image.Image("/com/redphase/images/7.png"));//true
        payMenu.setDisable(false);

        pauseImg.setImage(new javafx.scene.image.Image("/com/redphase/images/10x.png"));//false
        pauseMenu.setDisable(true);

        stopImg.setImage(new javafx.scene.image.Image("/com/redphase/images/11x.png"));//false
        stopMenu.setDisable(true);
    }

    @FXML
    public void setCancel() {
        log.info("setCancel..");
        hideNodeView();
    }

    @FXML
    Label thresholdL;
    @FXML
    Label thresholdSetL;

    @FXML
    public void setThresholdDone() {
        log.info("setThresholdDone..");
        String thresholdTextValue = setThresholdText.getText();
        if (ValidatorUtil.isEmpty(thresholdTextValue)) {
            ialert.error("阈值不能为空!");
            return;
        }
        hideNodeView();
        thresholdValue = new BigDecimal(thresholdTextValue).doubleValue();

        if (thresholdValue < (0 - VFC)) {
            ialert.error("阈值不能小于" + (0 - VFC));
            return;
        }
        if (thresholdValue > (VF - VFC)) {
            ialert.error("阈值不能大于" + (VF - VFC));
            return;
        }

        yzL.setText(thresholdValue + unit);//阈值
        yzL1.setText(thresholdValue + unit);//阈值
        thresholdL.setText("阈值(" + thresholdValue + unit + ")");

        loadAtlas(false);
    }

    //cutFloatList.getItems().add("谱图：PRPD");
    private void cut(Parent nodeView) {
        hideNodeView();
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new ImageSelection(SwingFXUtils.fromFXImage(nodeView.snapshot(new SnapshotParameters(), null), null)), null);
    }

    @FXML
    Pane prpdPane;

    @FXML
    public void cutPRPD() {
        log.info("cutPRPD..");
        cut(prpdPane);
    }

    //cutFloatList.getItems().add("谱图：PRPS");
    @FXML
    Pane prpsPane;

    @FXML
    public void cutPRPS() {
        log.info("cutPRPS..");
        cut(prpsPane);
    }

    //cutFloatList.getItems().add("谱图：TOF");
    @FXML
    public void cutTOF() {
        log.info("cutTOF..");
        hideNodeView();
    }

    //cutFloatList.getItems().add("谱图：Q（φ）");
    @FXML
    Pane qφPane;

    @FXML
    public void cutQφ() {
        log.info("cutQφ..");
        cut(qφPane);
    }

    //cutFloatList.getItems().add("谱图：特征值");
    @FXML
    Pane feature1Pane;

    @FXML
    public void cutFeature() {
        log.info("cutFeature..");
        cut(feature1Pane);
    }

    //cutFloatList.getItems().add("波形：超声波");
    @FXML
    public void cutUltrasonic() {
        log.info("cutUltrasonic..");
        hideNodeView();
    }

    //cutFloatList.getItems().add("截屏");
    @FXML
    AnchorPane dataPane;

    @FXML
    public void cutScreenshot() {
        log.info("cutScreenshot..");
        cut(dataPane);
    }

    //statisticsFloatList.getItems().add("统计：1秒");
    @FXML
    Button statistics1Button;
    @FXML
    Button statistics2Button;
    @FXML
    Button statistics3Button;
    @FXML
    Button statisticsAllButton;

    @FXML
    public void statistics1() {
        log.info("statistics1..");
        reStatistics(1);
    }

    //statisticsFloatList.getItems().add("统计：10秒");
    @FXML
    public void statistics2() {
        log.info("statistics2..");
        reStatistics(10);
    }

    //statisticsFloatList.getItems().add("统计：30秒");
    @FXML
    public void statistics3() {
        log.info("statistics3..");
        reStatistics(30);
    }

    //statisticsFloatList.getItems().add("统计：全部");
    @FXML
    public void statisticsAll() {
        log.info("statisticsAll..");
        reStatistics(secondCount);
    }

    private void reStatistics(int value) {
        hideNodeView();
        statisticsValue = value;
        statisticsL.setText("统计:" + (statisticsValue == secondCount ? "全部" : statisticsValue + "秒"));
        changeSecond(3);
    }

    //leftFloatList.getItems().add("15°");
    @FXML
    public void left1() {
        log.info("left1..");
        leftDegreeStep = 15;
        setDegreeStep(15, 0);
    }

    //leftFloatList.getItems().add("30°");
    @FXML
    public void left2() {
        log.info("left2..");
        setDegreeStep(30, 0);
    }

    //leftFloatList.getItems().add("45°");
    @FXML
    public void left3() {
        log.info("left3..");
        setDegreeStep(45, 0);
    }

    //leftFloatList.getItems().add("60°");
    @FXML
    public void left4() {
        log.info("left4..");
        setDegreeStep(60, 0);
    }

    //leftFloatList.getItems().add("90°");
    @FXML
    public void left5() {
        log.info("left5..");
        setDegreeStep(90, 0);
    }

    //rightFloatList.getItems().add("15°");
    @FXML
    public void right1() {
        log.info("right1..");
        setDegreeStep(15, 1);
    }

    //rightFloatList.getItems().add("30°");
    @FXML
    public void right2() {
        log.info("right2..");
        setDegreeStep(30, 1);
    }

    //rightFloatList.getItems().add("45°");
    @FXML
    public void right3() {
        log.info("right3..");
        setDegreeStep(45, 1);
    }

    //rightFloatList.getItems().add("60°");
    @FXML
    public void right4() {
        log.info("right4..");
        setDegreeStep(60, 1);
    }

    //rightFloatList.getItems().add("90°");
    @FXML
    public void right5() {
        log.info("right5..");
        setDegreeStep(90, 1);
    }

    /**
     * 变更相位值
     *
     * @param value 变更值
     * @param type  0减 1加
     */
    private void changeDegreeValue(int value, int type) {
        hideNodeView();
        if (type == 1) {
            degreeValue += value;
        } else {
            degreeValue -= value;
        }
        if (degreeValue > 360) {
            degreeValue = degreeValue - 360;
        }
        if (degreeValue < 0) {
            degreeValue = 360 + degreeValue;
        }
        log.debug("changeDegreeValue degreeValue={}", degreeValue);
        xwL.setText(degreeValue + "°");//相位
        xwL1.setText(degreeValue + "°");//相位

        //相位调整---
        int tempPhase = degreeValue - defaultPhase;
        if (tempPhase < 0) {
            tempPhase = 360 + tempPhase;
        }
        currentPhase = 60 - tempPhase / 6;//真实数组相位 24=360/6

        if (payMenu.isDisable()) {
            reDrawVFBackground();
        } else {
            loadAtlas(false);
        }
    }

    @FXML
    Label leftDegreeL;
    @FXML
    Label rightDegreeL;

    /**
     * 设置相位转换步长值
     *
     * @param value 值
     * @param type  类型 0左移 1右移
     */
    private void setDegreeStep(int value, int type) {
        hideNodeView();
        if (type == 0) {
            leftDegreeStep = value;
            leftDegreeL.setText("左移:" + leftDegreeStep + "`");
        } else {
            rightDegreeStep = value;
            rightDegreeL.setText("右移:" + rightDegreeStep + "`");
        }
    }

    static class ImageSelection implements Transferable {
        private Image image;

        public ImageSelection(Image image) {
            this.image = image;
        }

        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{DataFlavor.imageFlavor};
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return DataFlavor.imageFlavor.equals(flavor);
        }

        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            if (!DataFlavor.imageFlavor.equals(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return image;
        }
    }

    @Override
    public void dispose() {
        bStop();
    }
}