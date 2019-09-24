package com.redphase.controller.nda.data;

import com.redphase.controller.nda.NdaBaseController;
import com.redphase.dto.atlas.DataAnalyzeDto;
import com.redphase.dto.atlas.DataDeviceDto;
import com.redphase.dto.atlas.DataDeviceSiteDto;
import com.redphase.dto.atlas.tev.DataTevXjDto;
import com.redphase.dto.sys.SysVariableDto;
import com.redphase.framework.util.I18nUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
public class NdaDataTEVDetailController extends NdaBaseController implements Initializable {
    @FXML
    FlowPane dataFlowPane;
    List<DataDeviceDto> dataDeviceList;

    double multiple = 7;
    double VF = 70;
    double VFC = 0;
    String unit = "dBmV";
    double prpdX = 50;
    double prpdY = 260;
    double width = 550;
    double xStep = width / 0;
    double yStep = 250 / multiple;
    double pixelMultiple = 250.0 / VF;

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

    String[] siteNameArr = new String[]{"前中", "前下", "后上", "后中", "后下", "侧上", "侧中", "侧下"};

    Map<String, Canvas> result = new HashMap();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.debug(" initialize...");
    }

    public void init(List<DataDeviceDto> dataDeviceList) {
        try {
            this.dataDeviceList = dataDeviceList;
            //获取阈值生效类型0客户端设备1服务端软件
            thresholdTypeDto = setupService.getVariableByCode(new SysVariableDto() {{
                setCode("threshold-type");
            }});
            if ("0".equalsIgnoreCase(thresholdTypeDto.getValue())) {
                //使用设备设置
                ialert.warning("TODO设备关注值未知!");
            } else {
                //使用软件设置
                List<SysVariableDto> list = setupService.getVariableListByPCode(new SysVariableDto() {{
                    setCode("i-threshold");
                }});
                list.stream().filter(dto -> dto.getCode().startsWith("i-tev")).forEach(dto -> {
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
                    }
                });
            }
            dataFlowPane.getChildren().clear();
            dataDeviceList = dataDeviceList.stream().filter(d -> !"#".equals(d.getDeviceName())).collect(Collectors.toList());
            //设备IDs
            String dataDeviceIds = dataDeviceList.stream().map(d -> d.getId().toString()).collect(Collectors.joining(","));
//            //获取所有检测文件
            List<DataAnalyzeDto> analyzeDtos = dataAnalyzeService.findDataByDataDeviceIds(dataDeviceIds);
            //查询所有检测位置
            List<DataDeviceSiteDto> dataDeviceSiteDtos = dataDeviceSiteService.findDataByDataDeviceIds(dataDeviceIds);
//            //过滤出所有地电波巡检检测文件
            List<DataAnalyzeDto> xjAnalyzeList = analyzeDtos.stream().filter(a -> "TEV_XJ".equals(a.getDataFormat())).collect(Collectors.toList());
//            //查询所有地电波巡检检测数据
            List<DataTevXjDto> tevXjList = dataTevXjService.findDataByIds(xjAnalyzeList.stream().map(a -> a.getId().toString()).collect(Collectors.joining(",")));

            Map<String, DataAnalyzeDto> dataAnalyzeDtoMap = new Hashtable() {{
                if (xjAnalyzeList != null) {
                    xjAnalyzeList.forEach(dataAnalyzeDto -> {
                        put(dataAnalyzeDto.getDataDeviceId() + "#" + dataAnalyzeDto.getDataDeviceSiteId(), dataAnalyzeDto);
                    });
                }
            }};
            Map<String, DataTevXjDto> dataTevXjDtoMap = new Hashtable() {{
                if (tevXjList != null) {
                    tevXjList.forEach(dataTevXjDto -> {
                        put("" + dataTevXjDto.getDataAnalyzeId(), dataTevXjDto);
                    });
                }
            }};

            xStep = width / siteNameArr.length;
            Map<String, DataTevXjDto> deviceMap = new HashMap<>();
            for (DataDeviceSiteDto siteDto : dataDeviceSiteDtos) {
                //各位置图数据
                DataAnalyzeDto dataAnalyzeDto = dataAnalyzeDtoMap.get(siteDto.getDataDeviceId() + "#" + siteDto.getId());
                if (dataAnalyzeDto == null) {
                    continue;
                }
                DataTevXjDto dataTevXjDto = dataTevXjDtoMap.get("" + dataAnalyzeDto.getId());
                if (dataTevXjDto == null) {
                    continue;
                }
                DataTevXjDto oldDataTevXjDto = deviceMap.get(siteDto.getSiteName() + ":" + siteDto.getDataDeviceId());
                if (oldDataTevXjDto == null || dataTevXjDto.getX5() > oldDataTevXjDto.getX5()) {
                    deviceMap.put(siteDto.getSiteName() + ":" + siteDto.getDataDeviceId(), dataTevXjDto);
                }
            }

            if (dataDeviceList != null && dataDeviceList.size() > 0) {
                dataDeviceList.stream().filter(dataDeviceDto -> !"#".equals(dataDeviceDto.getDeviceName())).forEach(deviceDto -> {
                    dataFlowPane.getChildren().add(addAtlas(deviceDto));
                    for (int i = 0; i < siteNameArr.length; i++) {
                        DataTevXjDto dataTevXjDto = deviceMap.get(siteNameArr[i] + ":" + deviceDto.getId());
                        setFzAtlas(i, siteNameArr[i], result.get("d" + deviceDto.getId()), dataTevXjDto == null ? 0.1f : dataTevXjDto.getX5());
                    }
                });
            }
        } catch (Exception e) {
            log.error("开关柜详情出错!", e);
            ialert.error(I18nUtil.get("error.sys") + e.getMessage());
        }
    }

    private void setFzAtlas(int i, String siteName, Canvas canvas, Float fz) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        setColor(gc, fz);
        //System.out.println(fz*pixelMultiple);
        gc.fillRect(prpdX + (i * xStep) + xStep / 2, prpdY - fz * pixelMultiple, 25, fz * pixelMultiple);    //TODO 如果该位置有多个检测数据，取最大值
        gc.setFill(Color.web("#999999"));
        gc.fillText("" + siteName, prpdX + (i * xStep) + xStep / 2, prpdY + 15);
    }

    private Pane addAtlas(DataDeviceDto deviceDto) {
        Pane ndaPane = new Pane() {{
            setPrefWidth(620);
            prefHeight(450);
            getStyleClass().setAll("tt");
        }};
        FlowPane.setMargin(ndaPane, new Insets(5, 5, 5, 5));
        ndaPane.getChildren().add(new Label("地电波(dBmV)") {{
            setAlignment(Pos.CENTER);
            setContentDisplay(ContentDisplay.CENTER);
            setLayoutX(-200);
            setLayoutY(215);
            setPrefHeight(30);
            setPrefWidth(450);
            setRotate(-90);
            setTextFill(Color.web("#aaaaaa"));
            setTextOverrun(OverrunStyle.CLIP);
            setWrapText(true);
            setFont(Font.font(18));
        }});
        ndaPane.getChildren().add(new Label("" + deviceDto.getDeviceName()) {{
            setAlignment(Pos.CENTER);
            setPrefHeight(65);
            setPrefWidth(620);
            setTextFill(Color.web("#7e7dab"));
            setFont(Font.font(18));
        }});
        ndaPane.getChildren().add(new Canvas() {{
            setHeight(300);
            setWidth(550);
            setLayoutX(50);
            setLayoutY(65);
            drawBackground(this);
        }});
        ndaPane.getChildren().add(new Canvas() {{
            setId("d" + deviceDto.getId());
            setHeight(300);
            setWidth(550);
            setLayoutX(50);
            setLayoutY(65);

            result.put(getId(), this);

//            GraphicsContext gc=this.getGraphicsContext2D();
//            setColor(gc,dataTevXjDto.getX5());
//            gc.fillRect(prpdX+(i * xStep)+xStep/2,prpdY-fz*pixelMultiple,25,fz*pixelMultiple);    //TODO 如果该位置有多个检测数据，取最大值
//            gc.setFill(Color.web("#999999"));
//            gc.fillText(""+deviceName,prpdX+(i * xStep)+xStep/4,prpdY+15);
        }});
        ndaPane.getChildren().add(getScaleplateHBox());
        return ndaPane;
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

    private HBox getScaleplateHBox() {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setLayoutX(10.0);
        hBox.setLayoutY(375.0);
        hBox.setPrefWidth(620.0);
        hBox.getChildren().add(getScaleplateLable("正常", "lt1"));
        hBox.getChildren().add(getScaleplateLable("关注", "lt2"));
        hBox.getChildren().add(getScaleplateLable("预警", "lt3"));
        hBox.getChildren().add(getScaleplateLable("检修", "lt4"));
        return hBox;
    }

    private Label getScaleplateLable(String text, String className) {
        return new Label() {{
            setText(text);
            getStyleClass().setAll("d20");
            setPrefHeight(20);
            setPrefWidth(60);
            setFont(Font.font(14.0));
            setGraphic(new Label() {{
                setText(" ");
                setPrefWidth(20);
                setPrefHeight(20);
                getStyleClass().setAll(className);
            }});
        }};
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

    @Override
    public void dispose() {

    }
}