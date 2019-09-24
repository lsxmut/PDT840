package com.redphase.service.convert;

import com.redphase.api.account.IAccountService;
import com.redphase.api.atlas.*;
import com.redphase.api.atlas.aa.*;
import com.redphase.api.atlas.ae.*;
import com.redphase.api.atlas.hfct.*;
import com.redphase.api.atlas.tev.*;
import com.redphase.api.atlas.uhf.*;
import com.redphase.api.task.IAccountSiteInfoService;
import com.redphase.dto.atlas.DataAnalyzeDto;
import com.redphase.entity.atlas.DataDevice;
import com.redphase.service.analyze.AnalyzeData;
import com.redphase.service.convert.tofile.*;
import com.redphase.view.AlertView;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ConvertFileTask extends Task {
    static final SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    AnalyzeData analyzeData;
    AlertView ialert;
    ListView logsList;
    String outFilePath;
    Set<DataAnalyzeDto> dataAnalyzeSet;

    Label progressText;
    Label progressLine;
    Label progressValue;
    AtomicInteger fileCount;
    AtomicInteger currentfileCount;

    IAccountService accountService;
    IAccountSiteInfoService accountSiteInfoService;
    IDataDeviceService dataDeviceService;
    IDataDeviceSiteService dataDeviceSiteService;
    IDataAnalyzeService dataAnalyzeService;
    IDataHjService dataHjService;
    IDataLcService dataLcService;
    IDataAaBxService dataAaBxService;
    IDataAaFxService dataAaFxService;
    IDataAaTjlbService dataAaTjlbService;
    IDataAaTjService dataAaTjService;
    IDataAaTjzsService dataAaTjzsService;
    IDataAaXjService dataAaXjService;
    IDataAaXjzsService dataAaXjzsService;
    IDataAeBxService dataAeBxService;
    IDataAeFxService dataAeFxService;
    IDataAeTjlbService dataAeTjlbService;
    IDataAeTjService dataAeTjService;
    IDataAeTjzsService dataAeTjzsService;
    IDataAeXjService dataAeXjService;
    IDataAeXjzsService dataAeXjzsService;
    IDataHfctTjlbService dataHfctTjlbService;
    IDataHfctTjService dataHfctTjService;
    IDataHfctTjzsService dataHfctTjzsService;
    IDataHfctXjService dataHfctXjService;
    IDataHfctXjzsService dataHfctXjzsService;
    IDataTevTjlbService dataTevTjlbService;
    IDataTevTjService dataTevTjService;
    IDataTevTjzsService dataTevTjzsService;
    IDataTevXjService dataTevXjService;
    IDataTevXjlhService dataTevXjlhService;
    IDataTevXjzsService dataTevXjzsService;
    IDataUhfTj1Service dataUhfTj1Service;
    IDataUhfTj2Service dataUhfTj2Service;
    IDataUhfTjlb1Service dataUhfTjlb1Service;
    IDataUhfTjlb2Service dataUhfTjlb2Service;
    IDataUhfTjzs1Service dataUhfTjzs1Service;
    IDataUhfTjzs2Service dataUhfTjzs2Service;
    float progressLineWidth = 680.0f;

    public ConvertFileTask(Map map) {
        this.analyzeData = new AnalyzeData();
        this.ialert = (AlertView) map.get("ialert");
        this.logsList = (ListView) map.get("logsList");
        this.outFilePath = (String) map.get("outFilePath");
        this.dataAnalyzeSet = (Set) map.get("dataAnalyzeSet");
        this.progressText = (Label) map.get("progressText");
        this.progressLine = (Label) map.get("progressLine");
        this.progressValue = (Label) map.get("progressValue");
        this.fileCount = new AtomicInteger(this.dataAnalyzeSet.size());
        this.currentfileCount = new AtomicInteger(0);

        this.accountService = (IAccountService) map.get("accountService");
        this.accountSiteInfoService = (IAccountSiteInfoService) map.get("accountSiteInfoService");
        this.dataDeviceService = (IDataDeviceService) map.get("dataDeviceService");
        this.dataDeviceSiteService = (IDataDeviceSiteService) map.get("dataDeviceSiteService");
        this.dataAnalyzeService = (IDataAnalyzeService) map.get("dataAnalyzeService");
        this.dataHjService = (IDataHjService) map.get("dataHjService");
        this.dataLcService = (IDataLcService) map.get("dataLcService");
        this.dataTevTjService = (IDataTevTjService) map.get("dataTevTjService");
        this.dataTevTjlbService = (IDataTevTjlbService) map.get("dataTevTjlbService");
        this.dataTevTjzsService = (IDataTevTjzsService) map.get("dataTevTjzsService");
        this.dataTevXjService = (IDataTevXjService) map.get("dataTevXjService");
        this.dataTevXjlhService = (IDataTevXjlhService) map.get("dataTevXjlhService");
        this.dataTevXjzsService = (IDataTevXjzsService) map.get("dataTevXjzsService");
        this.dataAaBxService = (IDataAaBxService) map.get("dataAaBxService");
        this.dataAaFxService = (IDataAaFxService) map.get("dataAaFxService");
        this.dataAaTjService = (IDataAaTjService) map.get("dataAaTjService");
        this.dataAaTjlbService = (IDataAaTjlbService) map.get("dataAaTjlbService");
        this.dataAaTjzsService = (IDataAaTjzsService) map.get("dataAaTjzsService");
        this.dataAaXjService = (IDataAaXjService) map.get("dataAaXjService");
        this.dataAaXjzsService = (IDataAaXjzsService) map.get("dataAaXjzsService");
        this.dataAeBxService = (IDataAeBxService) map.get("dataAeBxService");
        this.dataAeFxService = (IDataAeFxService) map.get("dataAeFxService");
        this.dataAeTjService = (IDataAeTjService) map.get("dataAeTjService");
        this.dataAeTjlbService = (IDataAeTjlbService) map.get("dataAeTjlbService");
        this.dataAeTjzsService = (IDataAeTjzsService) map.get("dataAeTjzsService");
        this.dataAeXjService = (IDataAeXjService) map.get("dataAeXjService");
        this.dataAeXjzsService = (IDataAeXjzsService) map.get("dataAeXjzsService");
        this.dataHfctTjService = (IDataHfctTjService) map.get("dataHfctTjService");
        this.dataHfctTjlbService = (IDataHfctTjlbService) map.get("dataHfctTjlbService");
        this.dataHfctTjzsService = (IDataHfctTjzsService) map.get("dataHfctTjzsService");
        this.dataHfctXjService = (IDataHfctXjService) map.get("dataHfctXjService");
        this.dataHfctXjzsService = (IDataHfctXjzsService) map.get("dataHfctXjzsService");
        this.dataUhfTj1Service = (IDataUhfTj1Service) map.get("dataUhfTj1Service");
        this.dataUhfTj2Service = (IDataUhfTj2Service) map.get("dataUhfTj2Service");
        this.dataUhfTjlb1Service = (IDataUhfTjlb1Service) map.get("dataUhfTjlb1Service");
        this.dataUhfTjlb2Service = (IDataUhfTjlb2Service) map.get("dataUhfTjlb2Service");
        this.dataUhfTjzs1Service = (IDataUhfTjzs1Service) map.get("dataUhfTjzs1Service");
        this.dataUhfTjzs2Service = (IDataUhfTjzs2Service) map.get("dataUhfTjzs2Service");
    }

    @Override
    protected Object call() {
        try {
            Platform.runLater(() -> {
                progressText.setText("正在执行地区数据导出程序,请等待---");
                progressLine.setPrefWidth(progressLineWidth * (currentfileCount.doubleValue() / fileCount.doubleValue()));
                progressValue.setText(currentfileCount + "/" + fileCount);
                logsList.getItems().add(sdfTime.format(new Date()) + "\t正在导出文件,文件总数:" + fileCount + " ");
            });

            //准备导出检测文件
            dataAnalyzeSet.forEach(dataAnalyzeDto -> {
                log.debug("dataPath==>{}", dataAnalyzeDto.getFileUrl());
                try {
                    currentfileCount.getAndAdd(1);
                    File dataFile = new File(dataAnalyzeDto.getFileUrl());
                    Platform.runLater(() -> {
                        progressLine.setPrefWidth(progressLineWidth * (currentfileCount.doubleValue() / fileCount.doubleValue()));
                        progressValue.setText(currentfileCount + "/" + fileCount);
                        logsList.getItems().add(sdfTime.format(new Date()) + "\t正在导出:" + dataFile.getName());
                    });
//                    if (!dataFile.exists()) {
//                        Platform.runLater(() -> {
//                            Label errorLab = new Label(sdfTime.format(new Date()) + "\t导出异常:文件不存在," + dataAnalyzeDto.getFileUrl());
//                            errorLab.setStyle("-fx-text-fill:red;");
//                            logsList.getItems().add(errorLab);
//                        });
//                        return;
//                    }
                    DataDevice dataDevice = dataAnalyzeDto.getDataDevice();
                    Map<String, Object> dataMap = new HashMap() {{
                        put("file", dataFile);
                        put("outFilePath", outFilePath);
                        put("dataAnalyzeDto", dataAnalyzeDto);
                        put("dataHjDto", dataHjService.findDataByDevice(new HashMap() {{
                            put("dateDetection", dataDevice.getDateDetection());
                            put("testTechnology", dataDevice.getTestTechnology());
                            put("electricBureau", dataDevice.getElectricBureau());
                            put("substation", dataDevice.getSubstation());
                            put("voltageLevel", dataDevice.getVoltageLevel());
                            put("deviceType", dataDevice.getDeviceType());
                            put("deviceName", dataDevice.getDeviceName());
                        }}));
                    }};
                    switch (dataAnalyzeDto.getDataFormat()) {
                        case "HFCT_TJ": {
                            //"HFCT_统计数据"
                            dataMap.put("dataHfctTjService", dataHfctTjService);
                            JSDataHfctTJ2File.toFile(dataMap);
                            break;
                        }
                        case "HFCT_TJZS": {
                            //"HFCT_统计噪声数据"
                            dataMap.put("dataHfctTjzsService", dataHfctTjzsService);
                            JSDataHfctTJZS2File.toFile(dataMap);
                            break;
                        }
                        case "UHF_TJ_1": {
                            //"UHF_模式1_统计数据"
                            dataMap.put("dataUhfTj1Service", dataUhfTj1Service);
                            JSDataUhfTJ2File.toFile(dataMap);
                            break;
                        }
                        case "UHF_TJZS_1": {
                            //"UHF_模式1_统计噪声数据"
                            dataMap.put("dataUhfTjzs1Service", dataUhfTjzs1Service);
                            JSDataUhfTJZS2File.toFile(dataMap);
                            break;
                        }
                        case "TEV_XJ": {
                            //"TEV_巡检数据"
                            dataMap.put("dataTevXjService", dataTevXjService);
                            new JSDataTevXJ2File().toFile(dataMap);
                            break;
                        }
                        case "TEV_XJZS": {
                            //"TEV_巡检噪声数据"
                            dataMap.put("dataTevXjzsService", dataTevXjzsService);
                            new JSDataTevXJZS2File().toFile(dataMap);
                            break;
                        }
                        case "AA_XJ": {
                            //"AA_巡检数据"
                            dataMap.put("dataAaXjService", dataAaXjService);
                            JSDataAaXJ2File.toFile(dataMap);
                            break;
                        }
                        case "AA_XJZS": {
                            //"AA_巡检噪声数据"
                            dataMap.put("dataAaXjzsService", dataAaXjzsService);
                            JSDataAaXJZS2File.toFile(dataMap);
                            break;
                        }
                        case "AA_TJ": {
                            //"AA_统计数据"
                            dataMap.put("dataAaTjService", dataAaTjService);
                            JSDataAaTJ2File.toFile(dataMap);
                            break;
                        }
                        case "AA_TJZS": {
                            //"AA_统计噪声数据"
                            dataMap.put("dataAaTjzsService", dataAaTjzsService);
                            JSDataAaTJZS2File.toFile(dataMap);
                            break;
                        }
                        case "AA_FX": {
                            //"AA_飞行(脉冲)数据"
                            dataMap.put("dataAaFxService", dataAaFxService);
                            JSDataAaFX2File.toFile(dataMap);
                            break;
                        }
                        case "AA_BX": {
                            //"AA_波形数据"
                            dataMap.put("dataAaBxService", dataAaBxService);
                            JSDataAaBX2File.toFile(dataMap);
                            break;
                        }
                        case "AE_XJ": {
                            //"AE_巡检数据"
                            dataMap.put("dataAeXjService", dataAeXjService);
                            JSDataAeXJ2File.toFile(dataMap);
                            break;
                        }
                        case "AE_XJZS": {
                            //"AE_巡检噪声数据"
                            dataMap.put("dataAeXjzsService", dataAeXjzsService);
                            JSDataAeXJZS2File.toFile(dataMap);
                            break;
                        }
                        case "AE_TJ": {
                            //"AE_统计数据"
                            dataMap.put("dataAeTjService", dataAeTjService);
                            JSDataAeTJ2File.toFile(dataMap);
                            break;
                        }
                        case "AE_TJZS": {
                            //"AE_统计噪声数据"
                            dataMap.put("dataAeTjzsService", dataAeTjzsService);
                            JSDataAeTJZS2File.toFile(dataMap);
                            break;
                        }
                        case "AE_FX": {
                            //"AE_飞行(脉冲)数据"
                            dataMap.put("dataAeFxService", dataAeFxService);
                            JSDataAeFX2File.toFile(dataMap);
                            break;
                        }
                        case "AE_BX": {
                            //"AE_波形数据"
                            dataMap.put("dataAeBxService", dataAeBxService);
                            JSDataAeBX2File.toFile(dataMap);
                            break;
                        }
                    }
                } catch (Exception e) {
                    log.error("数据导出异常!", e);
                    Platform.runLater(() -> {
                        Label errorLab = new Label(sdfTime.format(new Date()) + "\t导出异常:" + e.getMessage());
                        errorLab.setStyle("-fx-text-fill:red;");
                        logsList.getItems().add(errorLab);
                    });
                }
            });
        } finally {
            Platform.runLater(() -> {
                progressText.setText("地区数据导出程序,已完成---");
                progressLine.setPrefWidth(progressLineWidth * (currentfileCount.doubleValue() / fileCount.doubleValue()));
                progressValue.setText(currentfileCount + "/" + fileCount);
                logsList.getItems().add(sdfTime.format(new Date()) + "\t导出完成.....");
            });
        }
        return null;
    }
}
