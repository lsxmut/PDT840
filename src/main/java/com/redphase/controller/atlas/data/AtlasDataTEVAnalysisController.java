package com.redphase.controller.atlas.data;

import com.redphase.controller.atlas.AtlasBaseController;
import com.redphase.dto.atlas.DataAnalyzeDto;
import com.redphase.dto.atlas.DataDeviceDto;
import com.redphase.dto.atlas.DataDeviceSiteDto;
import com.redphase.dto.atlas.tev.DataTevXjDto;
import com.redphase.dto.sys.SysVariableDto;
import com.redphase.framework.util.I18nUtil;
import com.redphase.framework.util.StrUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@FXMLController
@Slf4j
public class AtlasDataTEVAnalysisController extends AtlasBaseController implements Initializable {
    @FXML
    CheckBox qzCheckBox;
    @FXML
    CheckBox qxCheckBox;
    @FXML
    CheckBox hsCheckBox;
    @FXML
    CheckBox hzCheckBox;
    @FXML
    CheckBox hxCheckBox;
    @FXML
    CheckBox csCheckBox;
    @FXML
    CheckBox czCheckBox;
    @FXML
    CheckBox cxCheckBox;
    @FXML
    VBox qzVBox;
    @FXML
    Label qzTitleLab;
    @FXML
    Canvas qzBack;
    @FXML
    Canvas qzAtlas;
    @FXML
    VBox qxVBox;
    @FXML
    Label qxTitleLab;
    @FXML
    Canvas qxBack;
    @FXML
    Canvas qxAtlas;
    @FXML
    VBox hsVBox;
    @FXML
    Label hsTitleLab;
    @FXML
    Canvas hsBack;
    @FXML
    Canvas hsAtlas;
    @FXML
    VBox hzVBox;
    @FXML
    Label hzTitleLab;
    @FXML
    Canvas hzBack;
    @FXML
    Canvas hzAtlas;
    @FXML
    VBox hxVBox;
    @FXML
    Label hxTitleLab;
    @FXML
    Canvas hxBack;
    @FXML
    Canvas hxAtlas;
    @FXML
    VBox csVBox;
    @FXML
    Label csTitleLab;
    @FXML
    Canvas csBack;
    @FXML
    Canvas csAtlas;
    @FXML
    VBox czVBox;
    @FXML
    Label czTitleLab;
    @FXML
    Canvas czBack;
    @FXML
    Canvas czAtlas;
    @FXML
    VBox cxVBox;
    @FXML
    Label cxTitleLab;
    @FXML
    Canvas cxBack;
    @FXML
    Canvas cxAtlas;
    @FXML
    VBox qVBox;
    @FXML
    Label qTitleLab;
    @FXML
    Canvas qBack;
    @FXML
    Canvas qAtlas;
    @FXML
    VBox hVBox;
    @FXML
    Label hTitleLab;
    @FXML
    Canvas hBack;
    @FXML
    Canvas hAtlas;
    @FXML
    VBox zhVBox;
    @FXML
    Label zhTitleLab;
    @FXML
    Canvas zhBack;
    @FXML
    Canvas zhAtlas;

    double multiple = 7;
    double VF = 70;
    double VFC = 0;
    String unit = "dBmV";
    double prpdX = 50;
    double prpdY = 310;
    double width = 1000;
    double xStep = width / 1;
    double yStep = 300 / multiple;
    double pixelMultiple = 300.0 / VF;

    SysVariableDto thresholdTypeDto = null;

    String thresholdType = "1";
    double tevNormalSoftware = 0;//地电波正常-软件
    double tevEarlyWarningSoftware = 0;//地电波关注-软件
    double tevAbnormalSoftware = 0;//地电波异常-软件
    double tevNormalApp = 0;//地电波正常-设备
    double tevEarlyWarningApp = 0;//地电波关注-设备
    double tevAbnormalApp = 0;//地电波异常-设备
    double aaNormalSoftware = 0;//超声波正常-软件
    double aaEarlyWarningSoftware = 0;//超声波关注-软件
    double aaAbnormalSoftware = 0;//超声波异常-软件
    double aaNormalApp = 0;//超声波正常-设备
    double aaEarlyWarningApp = 0;//超声波关注-设备
    double aaAbnormalApp = 0;//超声波异常-设备

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize...");
    }

    /**
     * 加载各种横向分析表数据
     *
     * @param fullName
     */
    public void loadChartData(String fullName) {
        try {
            //获取阈值生效类型0客户端设备1服务端软件
            thresholdTypeDto = setupService.getVariableByCode(new SysVariableDto() {{
                setCode("threshold-type");
            }});
            thresholdType = thresholdTypeDto.getValue();
            if ("1".equalsIgnoreCase(thresholdTypeDto.getValue())) {
                //使用软件设置
                List<SysVariableDto> list = setupService.getVariableListByPCode(new SysVariableDto() {{
                    setCode("i-threshold");
                }});
                list.stream().filter(dto -> dto.getCode().startsWith("i-tev") || dto.getCode().startsWith("i-aa")).forEach(dto -> {
                    switch (dto.getCode()) {
                        case "i-tevNormal": {
                            tevNormalSoftware = Double.parseDouble(dto.getValue());
                            break;
                        }
                        case "i-tevEarlyWarning": {
                            tevEarlyWarningSoftware = Double.parseDouble(dto.getValue());
                            break;
                        }
                        case "i-tevAbnormal": {
                            tevAbnormalSoftware = Double.parseDouble(dto.getValue());
                            break;
                        }
                        case "i-aaNormal": {
                            aaNormalSoftware = Double.parseDouble(dto.getValue());
                            break;
                        }
                        case "i-aaEarlyWarning": {
                            aaEarlyWarningSoftware = Double.parseDouble(dto.getValue());
                            break;
                        }
                        case "i-aaAbnormal": {
                            aaAbnormalSoftware = Double.parseDouble(dto.getValue());
                            break;
                        }
                    }
                });
            }
            //设备列表（过滤掉主目录）
            List<DataDeviceDto> dataDeviceList = dataDeviceService.findListByFullName(fullName);
            dataDeviceList = dataDeviceList.stream().filter(d -> !"#".equals(d.getDeviceName())).collect(Collectors.toList());
            //设备IDs
            String dataDeviceIds = dataDeviceList.stream().map(d -> d.getId().toString()).collect(Collectors.joining(","));
            //获取所有检测文件
            List<DataAnalyzeDto> analyzeDtos = dataAnalyzeService.findDataByDataDeviceIds(dataDeviceIds);
            //查询所有检测位置
            List<DataDeviceSiteDto> dataDeviceSiteDtos = dataDeviceSiteService.findDataByDataDeviceIds(dataDeviceIds);

            //过滤出所有地电波巡检检测文件
            List<DataAnalyzeDto> xjAnalyzeList = analyzeDtos.stream().filter(a -> "TEV_XJ".equals(a.getDataFormat())).collect(Collectors.toList());
            //查询所有地电波巡检检测数据
            List<DataTevXjDto> tevXjList = dataTevXjService.findDataByIds(xjAnalyzeList.stream().map(a -> a.getId().toString()).collect(Collectors.joining(",")));

            //最终数据结构（key-检测位置，value-该位置对应的柱状图数据）
            Map<String, Canvas> result = new HashMap() {{
                put("前中", qzAtlas);
                put("前下", qxAtlas);
                put("后上", hsAtlas);
                put("后中", hzAtlas);
                put("后下", hxAtlas);
                put("侧上", csAtlas);
                put("侧中", czAtlas);
                put("侧下", cxAtlas);
                put("前", qAtlas);
                put("后", hAtlas);
                put("综合", zhAtlas);
            }};
            drawBackground(qzBack, qxBack, hsBack, hzBack, hxBack, csBack, czBack, cxBack, qBack, hBack, zhBack);
            clearCanvas(qzAtlas, qxAtlas, hsAtlas, hzAtlas, hxAtlas, csAtlas, czAtlas, cxAtlas, qAtlas, hAtlas, zhAtlas);
            drawZHBackground(zhBack);
            //各设备前后面地电波最大数据（key-设备序号，value-该设备最大的地电波检测数据）
            Map<String, DataTevXjDto> maxQHTevData = new HashMap<>();
            //各设备超声波最大数据（key-设备序号，value-该设备最大的超声波检测数据）
            Map<String, DataTevXjDto> maxAAData = new HashMap<>();
            xStep = width / dataDeviceList.size();

            //基
            for (Canvas canvas : result.values()) {
                for (int i = 0; i < dataDeviceList.size(); i++) {
                    DataDeviceDto dataDeviceDto = dataDeviceList.get(i);
                    setFzAtlas(i, "" + dataDeviceDto.getDeviceName(), canvas, 0.1f);
                }
            }

            for (int i = 0; i < dataDeviceList.size(); i++) {
                DataDeviceDto dataDeviceDto = dataDeviceList.get(i);
                //设备序号，即X轴的值
                String index = StrUtil.asString(i + 1);
                //过滤出当前设备的地电波巡检检测文件
                List<DataAnalyzeDto> xjAnalyze = xjAnalyzeList.stream().filter(a -> a.getDataDeviceId().equals(dataDeviceDto.getId())).collect(Collectors.toList());
                List<Long> xjAnalyzeIds = xjAnalyze.stream().map(a -> a.getId()).collect(Collectors.toList());
                //过滤出当前设备的地电波巡检检测数据
                List<DataTevXjDto> dataTevXjDtos = tevXjList.stream().filter(xj -> xjAnalyzeIds.contains(xj.getDataAnalyzeId())).collect(Collectors.toList());
                //取出超声波检测值最大的数据
                maxAAData.put(index, dataTevXjDtos.stream().filter(t -> t.getX4() == 6).max(Comparator.comparing(DataTevXjDto::getX19)).orElse(null));
                for (DataTevXjDto dataTevXjDto : dataTevXjDtos) {
                    tevNormalApp = dataTevXjDto.getX13();//地电波正常-设备
                    tevEarlyWarningApp = dataTevXjDto.getX14();//地电波关注-设备
                    tevAbnormalApp = dataTevXjDto.getX15();//地电波异常-设备
                    //检测文件
                    DataAnalyzeDto dataAnalyzeDto = xjAnalyze.stream().filter(a -> dataTevXjDto.getDataAnalyzeId().equals(a.getId())).findFirst().orElse(null);
                    if (dataAnalyzeDto != null) {
                        //检测位置
                        DataDeviceSiteDto dataDeviceSiteDto = dataDeviceSiteDtos.stream().filter(s -> dataAnalyzeDto.getDataDeviceSiteId().equals(s.getId())).findFirst().orElse(null);
                        if (dataDeviceSiteDto != null) {
                            //各位置图数据
                            setFzAtlas(i, "" + dataDeviceDto.getDeviceName(), result.get(dataDeviceSiteDto.getSiteName()), dataTevXjDto.getX5());
                            //方向
                            String direction = dataDeviceSiteDto.getSiteName().substring(0, 1);
                            if ("前".equals(direction) || "后".equals(direction)) {
                                //前后面数据
                                setFzAtlas(i, "" + dataDeviceDto.getDeviceName(), result.get(direction), dataTevXjDto.getX5());
                                if (maxQHTevData.get(index) == null || maxQHTevData.get(index).getX5() < dataTevXjDto.getX5()) {
                                    maxQHTevData.put(index, dataTevXjDto);
                                }
                            }
                        }
                    }
                }
            }

            //计算综合分析表数据
            for (int i = 0; i < dataDeviceList.size(); i++) {
                DataDeviceDto dataDeviceDto = dataDeviceList.get(i);
                //设备序号，即X轴的值
                String index = StrUtil.asString(i + 1);

                DataTevXjDto tevXjDto = maxQHTevData.get(index);
                int tevAttentionDegree = getTevAttentionDegree(tevXjDto);
                DataTevXjDto aaXjDto = maxAAData.get(index);
                int aaAttentionDegree = getAAAttentionDegree(aaXjDto);
                Canvas canvas = result.get("综合");
                Float fz = new BigDecimal((tevAttentionDegree > aaAttentionDegree ? tevAttentionDegree : aaAttentionDegree) * (300.0f / 5f / pixelMultiple)).floatValue();
                setFzAtlas(i, "" + dataDeviceDto.getDeviceName(), canvas, fz);

                Pane pane = (Pane) canvas.getParent();
                int finalI = i;
                pane.getChildren().add(new Label("") {{
                    setPrefWidth(25);
                    setPrefHeight(fz * pixelMultiple);
                    setTextFill(Color.RED);
                    setLayoutX(prpdX + (finalI * xStep) + xStep / 2 + 50);
                    setLayoutY(prpdY - fz * pixelMultiple + 10);
//                    setStyle("-fx-background-color: red");
                    Label tlab = this;
                    setTooltip(new Tooltip() {{
                        //综合分析结果图，用户可以使用鼠标放置在柱状图上，显示该设备的分析结果，如下；
                        //	柜子名称和编号；
                        //	地电波总分析结果（前、后综合获得）——地电波分析：正常；关注；异常；严重；
                        //	超声波总分析结果——超声波分析：正常；关注；异常；严重；未监听；未检测；
                        //	地电波和超声波综合分析结果——地电波和超声波综合分析：正常；关注；异常；严重；
                        setText("" + dataDeviceDto.getDeviceName() + "；\n" +
                                "地电波总分析结果（前、后综合获得）—" + getAtlasResult(tlab, 0, tevXjDto == null ? -1 : tevXjDto.getX21(), tevAttentionDegree) + "；\n" +
                                "超声波总分析结果—" + getAtlasResult(tlab, 1, aaXjDto == null ? -1 : aaXjDto.getX21(), aaAttentionDegree) + "；\n" +
                                "地电波和超声波综合分析结果—" + getAtlasResult(tlab, 2, -1, (tevAttentionDegree > aaAttentionDegree ? tevAttentionDegree : aaAttentionDegree)) + "；");
                    }});
                }});
            }

//            fillChart(result);
        } catch (Exception e) {
            log.error("加载横向分析表出错!", e);
            ialert.error(I18nUtil.get("error.sys") + e.getMessage());
        }
    }

    private String getAtlasResult(Label tlab, int type, int source, int degree) {
        String result = "";
        if (source == 3) {//未监听:0x03,未检测:0x04
            result = "未监听";
        } else if (source == 4) {
            result = "未检测";
        }
        switch (degree) {
            case 1: {
                result = "正常";
                tlab.setStyle("-fx-background-color: #33d2b6;");
                break;
            }
            case 2: {
                result = "关注";
                tlab.setStyle("-fx-background-color: #f9c419;");
                break;
            }
            case 3: {
                result = "异常";
                tlab.setStyle("-fx-background-color: #fb9531;");
                break;
            }
            case 4: {
                result = "严重";
                tlab.setStyle("-fx-background-color: #f57070;");
                break;
            }
        }
        return result;
    }

    private int strLen(String value) {
        int len = 0;
        String chinese = "[\u4e00-\u9fa5]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                len += 2;
            } else {
                len += 1;
            }
        }
        return len;
    }

    private void setFzAtlas(int i, String deviceName, Canvas canvas, Float fz) {
        if (fz <= 0) fz = 0.1f;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        setColor(gc, fz);
        gc.fillRect(prpdX + (i * xStep) + xStep / 2, prpdY - fz * pixelMultiple, 25, fz * pixelMultiple);
        gc.setFill(Color.web("#999999"));
        gc.fillText("" + deviceName, prpdX + (i * xStep) + xStep / 2 - (strLen(deviceName) * 2.5), prpdY + 15);
    }

    private void setColor(GraphicsContext gc, Float fz) {
        double tevNormal = 0;//地电波正常
        double tevEarlyWarning = 0;//地电波关注
        double tevAbnormal = 0;//地电波异常
        if ("0".equals(thresholdType)) {
            tevNormal = tevNormalApp;
            tevEarlyWarning = tevEarlyWarningApp;
            tevAbnormal = tevAbnormalApp;
        } else {
            tevNormal = tevNormalSoftware;
            tevEarlyWarning = tevEarlyWarningSoftware;
            tevAbnormal = tevAbnormalSoftware;
        }
        if (fz <= tevNormal) {
            //正常
            gc.setFill(Color.web("#33d2b6"));
        } else if (fz <= tevEarlyWarning) {
            //关注
            gc.setFill(Color.web("#f9c419"));
        } else if (fz <= tevAbnormal) {
            //异常
            gc.setFill(Color.web("#fb9531"));
        } else {
            //检修
            gc.setFill(Color.web("#f57070"));
        }
    }

    /**
     * 动态显示/隐藏各位置图
     */
    public void toggleSiteChart() {
        qzVBox.setVisible(qzCheckBox.isSelected());
        qzVBox.setManaged(qzCheckBox.isSelected());
        qxVBox.setVisible(qxCheckBox.isSelected());
        qxVBox.setManaged(qxCheckBox.isSelected());
        hsVBox.setVisible(hsCheckBox.isSelected());
        hsVBox.setManaged(hsCheckBox.isSelected());
        hzVBox.setVisible(hzCheckBox.isSelected());
        hzVBox.setManaged(hzCheckBox.isSelected());
        hxVBox.setVisible(hxCheckBox.isSelected());
        hxVBox.setManaged(hxCheckBox.isSelected());
        csVBox.setVisible(csCheckBox.isSelected());
        csVBox.setManaged(csCheckBox.isSelected());
        czVBox.setVisible(czCheckBox.isSelected());
        czVBox.setManaged(czCheckBox.isSelected());
        cxVBox.setVisible(cxCheckBox.isSelected());
        cxVBox.setManaged(cxCheckBox.isSelected());
    }

    /**
     * 计算地电波关注度
     *
     * @param tevXjDto
     * @return
     * @throws Exception
     */
    public int getTevAttentionDegree(DataTevXjDto tevXjDto) throws Exception {
        if (tevXjDto == null || tevXjDto.getX5() == null) {
            return 0;
        }
        double tevNormal = 0;//地电波正常
        double tevEarlyWarning = 0;//地电波关注
        double tevAbnormal = 0;//地电波异常
        if ("0".equals(thresholdType)) {
            tevNormal = tevXjDto.getX13();
            tevEarlyWarning = tevXjDto.getX14();
            tevAbnormal = tevXjDto.getX15();
        } else {
            tevNormal = tevNormalSoftware;
            tevEarlyWarning = tevEarlyWarningSoftware;
            tevAbnormal = tevAbnormalSoftware;
        }
        if (tevXjDto.getX5() <= tevNormal) {
            return 1;
        } else if (tevXjDto.getX5() <= tevEarlyWarning) {
            return 2;
        } else if (tevXjDto.getX5() <= tevAbnormal) {
            return 3;
        } else {
            return 4;
        }
    }

    /**
     * 计算超声波关注度
     *
     * @param tevXjDto
     * @return
     * @throws Exception
     */
    public int getAAAttentionDegree(DataTevXjDto tevXjDto) throws Exception {
        if (tevXjDto == null || tevXjDto.getX19() == null || tevXjDto.getX21() == 3 || tevXjDto.getX21() == 4) {
            return 0;
        }
        if (tevXjDto.getX21() == 1) {
            return 1;
        } else if (tevXjDto.getX21() == 2) {
            return 3;
        }
        double aaNormal = 0;//超声波正常
        double aaEarlyWarning = 0;//超声波关注
        double aaAbnormal = 0;//超声波异常
        if ("0".equals(thresholdType)) {
            aaNormal = tevXjDto.getX27();
            aaEarlyWarning = tevXjDto.getX28();
            aaAbnormal = tevXjDto.getX29();
        } else {
            aaNormal = aaNormalSoftware;
            aaEarlyWarning = aaEarlyWarningSoftware;
            aaAbnormal = aaAbnormalSoftware;
        }
        if (tevXjDto.getX19() <= aaNormal) {
            return 1;
        } else if (tevXjDto.getX19() <= aaEarlyWarning) {
            return 2;
        } else if (tevXjDto.getX19() <= aaAbnormal) {
            return 3;
        } else {
            return 4;
        }
    }

    private void drawBackground(Canvas... canvasArr) {
        for (Canvas canvas : canvasArr) {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            double hPixel = VF / multiple;
            //清屏
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc.setStroke(Color.web("#cccccc", 1.0));
            gc.setLineWidth(1);
            gc.setFont(Font.font(10));
            gc.setFill(Color.web("#999999"));
            //纵面图--横线  +1
            for (int i = 0; i < multiple + 1; i++) {
                gc.strokeLine(prpdX, prpdY - (i * yStep), prpdX + width, prpdY - (i * yStep));
                gc.fillText("" + new BigDecimal(i * hPixel - VFC).setScale(2, RoundingMode.UP), 15, prpdY - (i * yStep) + 5);
            }
            //纵面图--竖线
            gc.strokeLine(prpdX, 0, prpdX, prpdY);
            gc.strokeLine(prpdX + width, 0, prpdX + width, prpdY);
            //警戒线
            gc.setStroke(Color.web("#f57070"));
            gc.setFill(Color.web("#f57070"));
            gc.setLineDashes(2);
            gc.strokeLine(prpdX, prpdY - (tevAbnormalSoftware * pixelMultiple), prpdX + width, prpdY - (tevAbnormalSoftware * pixelMultiple));
            gc.fillText("警戒线", prpdX + width - 30, prpdY - (tevAbnormalSoftware * pixelMultiple) - 10);
        }
    }

    private void clearCanvas(Canvas... canvasArr) {
        for (Canvas canvas : canvasArr) {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            //清屏
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        }
    }

    private void drawZHBackground(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double yStep = 300.0 / 5;
        //清屏
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.web("#cccccc", 1.0));
        gc.setLineWidth(1);
        gc.setFont(Font.font(10));
        gc.setFill(Color.web("#999999"));
        //纵面图--横线  +1
        for (int i = 0; i < 5 + 1; i++) {
            gc.strokeLine(prpdX, prpdY - (i * yStep), prpdX + width, prpdY - (i * yStep));
            gc.fillText("" + new BigDecimal(i).setScale(2, RoundingMode.UP), 15, prpdY - (i * yStep) + 5);
        }
        //纵面图--竖线
        gc.strokeLine(prpdX, 0, prpdX, prpdY);
        gc.strokeLine(prpdX + width, 0, prpdX + width, prpdY);
//            //警戒线
//            gc.setStroke(Color.web("#f57070"));
//            gc.setFill(Color.web("#f57070"));
//            gc.setLineDashes(2);
//            gc.strokeLine(prpdX, prpdY - (tevAbnormalSoftware * pixelMultiple), prpdX + width, prpdY - (tevAbnormalSoftware * pixelMultiple));
//            gc.fillText("警戒线",prpdX + width-30,prpdY - (tevAbnormalSoftware * pixelMultiple)-10);
    }

    @Override
    public void dispose() {

    }
}