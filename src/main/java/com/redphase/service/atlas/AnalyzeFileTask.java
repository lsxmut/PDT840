package com.redphase.service.atlas;

import com.alibaba.fastjson.JSON;
import com.redphase.api.account.IAccountService;
import com.redphase.api.atlas.*;
import com.redphase.api.atlas.aa.*;
import com.redphase.api.atlas.ae.*;
import com.redphase.api.atlas.hfct.*;
import com.redphase.api.atlas.tev.*;
import com.redphase.api.atlas.uhf.*;
import com.redphase.api.task.IAccountSiteInfoService;
import com.redphase.controller.atlas.AtlasController;
import com.redphase.dto.account.AccountDto;
import com.redphase.dto.atlas.*;
import com.redphase.dto.atlas.aa.*;
import com.redphase.dto.atlas.ae.*;
import com.redphase.dto.atlas.hfct.*;
import com.redphase.dto.atlas.tev.*;
import com.redphase.dto.atlas.uhf.*;
import com.redphase.dto.task.AccountSiteInfoDto;
import com.redphase.framework.ObjectCopy;
import com.redphase.framework.util.ByteConverterUtil;
import com.redphase.framework.util.ConvertAudio2WavUtil;
import com.redphase.service.analyze.AnalyzeData;
import com.redphase.view.AlertView;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class AnalyzeFileTask extends Task {
    private static final SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private AlertView ialert;
    private ListView successList;
    private ListView errorList;
    private String savePathText;
    private String importPathText;
    //    //获取目录路径-->电力局目录
//    private Set<String> electricBureauPathSet = new HashSet<>();
    //获取目录路径--变电站_检测对象_检测技术_检测日期
    private Set<String> taskDataPathSet = null;
    private AnalyzeData analyzeData;
    private AtlasController atlasController;
    private IAccountService accountService;
    private IAccountSiteInfoService accountSiteInfoService;
    private IDataDeviceService dataDeviceService;
    private IDataDeviceSiteService dataDeviceSiteService;
    private IDataAnalyzeService dataAnalyzeService;
    private IDataHjService dataHjService;
    private IDataLcService dataLcService;
    private IDataAaBxService dataAaBxService;
    private IDataAaFxService dataAaFxService;
    private IDataAaTjlbService dataAaTjlbService;
    private IDataAaTjService dataAaTjService;
    private IDataAaTjzsService dataAaTjzsService;
    private IDataAaXjService dataAaXjService;
    private IDataAaXjzsService dataAaXjzsService;
    private IDataAeBxService dataAeBxService;
    private IDataAeFxService dataAeFxService;
    private IDataAeTjlbService dataAeTjlbService;
    private IDataAeTjService dataAeTjService;
    private IDataAeTjzsService dataAeTjzsService;
    private IDataAeXjService dataAeXjService;
    private IDataAeXjzsService dataAeXjzsService;
    private IDataHfctTjlbService dataHfctTjlbService;
    private IDataHfctTjService dataHfctTjService;
    private IDataHfctTjzsService dataHfctTjzsService;
    private IDataHfctXjService dataHfctXjService;
    private IDataHfctXjzsService dataHfctXjzsService;
    private IDataTevTjlbService dataTevTjlbService;
    private IDataTevTjService dataTevTjService;
    private IDataTevTjzsService dataTevTjzsService;
    private IDataTevXjService dataTevXjService;
    private IDataTevXjlhService dataTevXjlhService;
    private IDataTevXjzsService dataTevXjzsService;
    private IDataUhfTj1Service dataUhfTj1Service;
    private IDataUhfTj2Service dataUhfTj2Service;
    private IDataUhfTjlb1Service dataUhfTjlb1Service;
    private IDataUhfTjlb2Service dataUhfTjlb2Service;
    private IDataUhfTjzs1Service dataUhfTjzs1Service;
    private IDataUhfTjzs2Service dataUhfTjzs2Service;

    private ImageView progressIcon;
    private Label progressText;
    private ProgressBar progressLine;
    private Label progressValue;
    private AtomicInteger fileCount;
    private AtomicInteger currentfileCount;

    public AnalyzeFileTask(Map map) {
        this.ialert = (AlertView) map.get("ialert");
        this.successList = (ListView) map.get("successList");
        this.errorList = (ListView) map.get("errorList");
        this.savePathText = (String) map.get("savePathText");
        this.importPathText = (String) map.get("importPathText");
        this.accountService = (IAccountService) map.get("accountService");
        this.accountSiteInfoService = (IAccountSiteInfoService) map.get("accountSiteInfoService");
        this.atlasController = (AtlasController) map.get("atlasController");
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
        this.progressIcon = (ImageView) map.get("progressIcon");
        this.progressText = (Label) map.get("progressText");
        this.progressLine = (ProgressBar) map.get("progressLine");
        this.progressValue = (Label) map.get("progressValue");
        this.fileCount = (AtomicInteger) map.get("fileCount");
        this.currentfileCount = new AtomicInteger(0);
        analyzeData = new AnalyzeData();
    }

    private Map<String, Integer> deviceSortMap = new HashMap<>();
    private Map<String, Integer> deviceSiteSortMap = new HashMap<>();

    @Override
    protected Object call() throws Exception {
        try {
            taskDataPathSet = new HashSet<>();
            deviceSortMap = new HashMap<>();
            deviceSiteSortMap = new HashMap<>();
            try {
                Platform.runLater(() -> {
                    progressIcon.setImage(new Image("/com/redphase/images/ic_cloud.png"));
                    progressText.setText("正在导入...");
                    progressLine.setProgress(currentfileCount.doubleValue() / fileCount.doubleValue());
                    progressValue.setText(currentfileCount + "/" + fileCount);
                });
                String dataFilePath = savePathText + File.separator + new File(importPathText).getName();
//                loadDirectoryFile(dataFilePath);
                taskDataPathSet.add(dataFilePath);
            } catch (Exception e) {
                log.error("数据分析文件读取异常!", e);
                Platform.runLater(() -> {
                    Label errorLab = new Label(sdfTime.format(new Date()) + "\t文件读取异常:" + "    " + e.getMessage());
                    errorLab.setStyle("-fx-text-fill:red;");
                    errorList.getItems().add(errorLab);
                });
            }
            List<AccountDto> accountResults = accountService.findDataIsList(null);
            List<AccountSiteInfoDto> accountSiteResults = accountSiteInfoService.findDataIsList(null);
            Map<String, Map<String, Integer>> tempDeviceSiteSortMap = new HashMap<>();
            if (accountSiteResults != null) {
                for (AccountSiteInfoDto accountSiteInfoDto : accountSiteResults) {
                    if (tempDeviceSiteSortMap.get(accountSiteInfoDto.getAccountId()) == null) {
                        tempDeviceSiteSortMap.put("" + accountSiteInfoDto.getAccountId(), new HashMap<>());
                    }
                    tempDeviceSiteSortMap.get("" + accountSiteInfoDto.getAccountId()).put("" + accountSiteInfoDto.getSiteName(), accountSiteInfoDto.getSortNo());
                }
            }
            if (accountResults != null) {
                for (AccountDto accountDto : accountResults) {
                    String aName = accountDto.getElectricBureau() + "#" + accountDto.getSubstation() + "#" + accountDto.getDeviceName() + "#" + accountDto.getDeviceType();
                    deviceSortMap.put(aName, accountDto.getSortNum());
                    Map<String, Integer> tempDeviceSiteMap = tempDeviceSiteSortMap.get("" + accountDto.getId());
                    if (tempDeviceSiteSortMap.get("" + accountDto.getId()) != null) {
                        for (Map.Entry entry : tempDeviceSiteMap.entrySet()) {
                            deviceSiteSortMap.put(aName + "#" + entry.getKey(), (Integer) entry.getValue());
                        }
                    }
                }
            }
            //正在获取电力局
            //  正在获取变电站
            //    正在解析检测数据
            //    正在处理数据截图
            //    正在解析开关柜测试记录表
            //    正在解析变压器测试记录表
            //    正在解析GIS特高频测试记录表
            //    正在解析GIS超声波测试记录表

            AtomicReference<DataDeviceDto> mainDataDevice = new AtomicReference<>();
            //准备解析检测文件
            taskDataPathSet.forEach(taskDataPath -> {
                try {
//                    File taskDataFile = new File(taskDataPath);
                    log.debug("taskDataPath==>{}", taskDataPath);
                    String directoryName = taskDataPath.substring(taskDataPath.lastIndexOf(File.separator) + 1, taskDataPath.length());
                    log.debug("directoryName==>{}", directoryName);//红相高压实验室_110kV组合电器_UHF_20180820-->[变电站]_[电压]kV[设备类型]_[检测技术]_[检测日期]
                    String[] deviceArr = directoryName.split("_");
//                    String electricBureau;//电业局
                    String substation = deviceArr[0];//变电站
                    String[] voltArr = deviceArr[1].split("kV");
                    Integer voltageLevel = Integer.parseInt(voltArr[0]);//电压
                    String deviceTypeName = voltArr[1];//设备类型
                    String testTechnology = deviceArr[2];//检测技术
                    String dateDetection = deviceArr[3];//检测日期

                    List<AccountDto> accountDtoList = null;
                    //根据变电站获取电力局
                    try {
                        accountDtoList = accountService.findDataListBySubstation(new AccountDto() {{
                            setSubstation(substation);
                        }});
                    } catch (Exception e) {
                        log.error("根据名称获取变电站对象!", e);
                    }
                    Integer deviceType = 0;
                    switch (deviceTypeName) {
                        case "开关柜":
                            deviceType = 1;
                            break;
                        case "变压器":
                            deviceType = 2;
                            break;
                        case "组合电器":
                            deviceType = 3;
                            break;
                        case "电缆":
                            deviceType = 4;
                            break;
                    }
                    AccountDto accountDevice = null;
                    if (accountDtoList != null && accountDtoList.size() > 0) {
                        for (AccountDto accountDto : accountDtoList) {
                            //electricBureau.equalsIgnoreCase(accountDto.getElectricBureau())
                            if (substation.equalsIgnoreCase(accountDto.getSubstation())
                                    && voltageLevel.intValue() == accountDto.getVoltageLevel().intValue()
                                    && deviceType.intValue() == accountDto.getDeviceType().intValue()) {
                                accountDevice = accountDto;
                            }
                        }
                    }
                    if (accountDevice == null) {
                        Platform.runLater(() -> {
                            Label errorLab = new Label(sdfTime.format(new Date()) + "\t不存在的设备!" + deviceArr[0] + "_" + deviceArr[1]);
                            errorLab.setStyle("-fx-text-fill:red;");
                            errorList.getItems().add(errorLab);
                        });
//                        return;
                        accountDevice = new AccountDto();
                        accountDevice.setElectricBureau("其它");//电业局
                        accountDevice.setSubstation(substation);//变电站
                        accountDevice.setVoltageLevel(voltageLevel);//电压
                        accountDevice.setDeviceType(deviceType);//设备类型
                    }
                    //[电站]_[电压][设备类型]_[检测技术]_[检测日期]
                    //220kV夏河变_110kV组合电器_HFCT_20180305
                    String rootDataPath = accountDevice.getSubstation() + "_" + accountDevice.getVoltageLevel() + "kV";

                    log.debug("[设备检测文件所在目录]==>{}", taskDataPath + deviceTypeName);
                    rootDataPath += "_" + testTechnology + "_";
                    log.debug("[电站]_[电压][设备类型]_[检测技术]_==>{}", rootDataPath);
                    //log.debug("[电站]_[电压][设备类型]_[检测技术]_[检测日期]==>{}",rootDataPath+[检测日期]);

                    //获取检测根据文件夹获取检测日期
                    DataDeviceDto dataDeviceParameterDto = new DataDeviceDto();
                    dataDeviceParameterDto.setDateDetection(dateDetection);//任务单日期
                    dataDeviceParameterDto.setTestTechnology(testTechnology);//检测技术
                    dataDeviceParameterDto.setElectricBureau(accountDevice.getElectricBureau());//电力局
                    dataDeviceParameterDto.setSubstation(accountDevice.getSubstation());//变电站
                    dataDeviceParameterDto.setDeviceType("" + accountDevice.getDeviceType());//设备类型
                    dataDeviceParameterDto.setDeviceName("#");//设备名称
                    dataDeviceParameterDto.setVoltageLevel("" + accountDevice.getVoltageLevel());//电压
                    dataDeviceParameterDto.setSpaceName(accountDevice.getSpaceName());//间隔名称
                    dataDeviceParameterDto.setFolderPath(taskDataPath);//文件夹路径
                    //保存电力设备
                    final DataDeviceDto[] dataDeviceDto = {dataDeviceService.getSaveDataDeviceDto(dataDeviceParameterDto)};
                    //主目录
                    if ((mainDataDevice.get() == null || mainDataDevice.get().getId() == null) && "#".equalsIgnoreCase(dataDeviceDto[0].getDeviceName())) {
                        mainDataDevice.set(copyTo(dataDeviceDto[0], DataDeviceDto.class));
                    }
                    //打开 [检测数据] 目录  一级目录
                    File deviceTypeFolder = new File(taskDataPath);
                    if (deviceTypeFolder.isDirectory()) {
                        AccountDto finalAccountDevice = accountDevice;
                        String finalDeviceType = deviceTypeName;
                        String finalTestTechnology = testTechnology;
//                    new Thread(() -> {
                        try {
                            File[] deviceTypeFiles = deviceTypeFolder.listFiles();
                            Platform.runLater(() -> {
                                successList.getItems().add(sdfTime.format(new Date()) + "\t正在获取:" + finalAccountDevice.getSubstation() + "." + finalDeviceType + "  文件数量:" + deviceTypeFiles.length);
                            });
                            for (int i = 0; i < deviceTypeFiles.length; i++) {
                                File deviceTypeFile = deviceTypeFiles[i];
                                if (deviceTypeFile.isFile()) {
                                    //-一级目录
                                    //文件解析
                                    parseFile("\t", finalTestTechnology, deviceTypeFile, mainDataDevice.get(), null);
                                } else {
                                    //打开 被测设备  二级目录
                                    //+其它检测数据的噪声文件
                                    Platform.runLater(() -> {
                                        successList.getItems().add(sdfTime.format(new Date()) + "\t\t正在获取:" + deviceTypeFile.getName() + "  文件数量:" + deviceTypeFile.listFiles().length);
                                    });
                                    dataDeviceParameterDto.setDeviceName(deviceTypeFile.getName());//设备名称
                                    dataDeviceParameterDto.setFolderPath(deviceTypeFile.getPath());//文件夹路径
                                    dataDeviceParameterDto.setSortNo(deviceSortMap.get(dataDeviceParameterDto.getElectricBureau() + "#" + dataDeviceParameterDto.getSubstation() + "#" + dataDeviceParameterDto.getDeviceName() + "#" + accountDevice.getDeviceType()));
                                    dataDeviceDto[0] = dataDeviceService.getSaveDataDeviceDto(dataDeviceParameterDto);

                                    File[] deviceInfoFiles = deviceTypeFile.listFiles();
                                    for (int y = 0; y < deviceInfoFiles.length; y++) {
                                        File deviceInfoFile = deviceInfoFiles[y];
                                        if (deviceInfoFile.isFile()) {
                                            //文件解析
                                            parseFile("\t\t", finalTestTechnology, deviceInfoFile, dataDeviceDto[0], null);
                                        } else {
                                            deviceSiteLoadFolder(finalTestTechnology, deviceInfoFile, dataDeviceDto[0]);
                                        }
                                    }
                                }
                            }

                        } catch (Exception e) {
                            log.error("文件解析异常!" + taskDataPath, e);
                            Platform.runLater(() -> {
                                Label errorLab = new Label(sdfTime.format(new Date()) + "\t解析异常:" + taskDataPath + "    " + e.getMessage());
                                errorLab.setStyle("-fx-text-fill:red;");
                                errorList.getItems().add(errorLab);
                            });
                        }
//                    }).start();
                    }
                } catch (Exception e) {
                    log.error("数据解析异常!", e);
                    Platform.runLater(() -> {
                        Label errorLab = new Label(sdfTime.format(new Date()) + "\t解析异常:   " + e.getMessage());
                        errorLab.setStyle("-fx-text-fill:red;");
                        errorList.getItems().add(errorLab);
                    });
                }
            });
        } finally {
            Platform.runLater(() -> {
                progressIcon.setImage(new Image("/com/redphase/images/ic_cloud_done.png"));
                progressText.setText("导入完成");
                progressLine.setProgress(currentfileCount.doubleValue() / fileCount.doubleValue());
                progressValue.setText(currentfileCount + "/" + fileCount);
                successList.getItems().add(sdfTime.format(new Date()) + "\t解析完成....." + "    ");
                //台账树刷新
                atlasController.loadAccTree();
            });
        }
        return null;
    }

    private void loadDirectoryFile(String path) {
        File[] fileArr = new File(path).listFiles();
        if (fileArr != null) {
//            successList.getItems().add(sdfTime.format(new Date()) + "    正在解析文件,文件总数:" + fileArr.length + " ");
            for (int i = 0; i < fileArr.length; i++) {
                File file = fileArr[i];
                String filePath = file.getPath();
                if (file.isDirectory()) {
                    if (file.getName().indexOf("_AA_") != -1
                            || file.getName().indexOf("_AE_") != -1
                            || file.getName().indexOf("_TEV_") != -1
                            || file.getName().indexOf("_UHF_") != -1
                            || file.getName().indexOf("_HFCT_") != -1) {
                        log.debug("taskDataPath==>{}", filePath);
                        taskDataPathSet.add(filePath);
                    }
//                    if (filePath.indexOf(File.separator + CommonConstant.ATLAS_DATA_DIR_NAME + File.separator) != -1) {
//                        successList.getItems().add(sdfTime.format(new Date()) + "    正在获取:" + file.getName() + " 数据");
//                    }
                    if (!taskDataPathSet.contains(filePath)) {
                        loadDirectoryFile(filePath);
                    }
                }
            }
        }
    }

    private void deviceSiteLoadFolder(String testTechnology, File deviceSiteFolder, DataDeviceDto dataDeviceDto) throws Exception {
        Platform.runLater(() -> {
            //打开被测部位  三级目录
            successList.getItems().add(sdfTime.format(new Date()) + "\t\t\t正在获取:" + deviceSiteFolder.getName() + "  文件数量:" + deviceSiteFolder.listFiles().length);
        });
        DataDeviceSiteDto dataDeviceSiteParameterDto = new DataDeviceSiteDto();//设备检测位置
        dataDeviceSiteParameterDto.setDataDeviceId(dataDeviceDto.getId());
        dataDeviceSiteParameterDto.setSiteName(deviceSiteFolder.getName());
        dataDeviceSiteParameterDto.setSpaceName(dataDeviceDto.getSpaceName());
        dataDeviceSiteParameterDto.setFolderPath(deviceSiteFolder.getPath());
        dataDeviceSiteParameterDto.setSortNo(deviceSiteSortMap.get(dataDeviceDto.getElectricBureau() + "#" + dataDeviceDto.getSubstation() + "#" + dataDeviceDto.getDeviceName() + "#" + dataDeviceDto.getDeviceType() + "#" + dataDeviceSiteParameterDto.getSiteName()));
        DataDeviceSiteDto dataDeviceSiteDto = dataDeviceSiteService.getSaveDataDeviceSiteDto(dataDeviceSiteParameterDto);

        if (deviceSiteFolder.isDirectory()) {
            File[] deviceSiteFiles = deviceSiteFolder.listFiles();
            for (int i = 0; i < deviceSiteFiles.length; i++) {
                File deviceSiteFile = deviceSiteFiles[i];
                try {
                    log.debug("位置==>{}", deviceSiteFile.getPath());
                    if (deviceSiteFile.isFile()) {
                        //文件解析
                        parseFile("\t\t\t\t", testTechnology, deviceSiteFile, dataDeviceDto, dataDeviceSiteDto);
                    } else {
                        log.error("未知目录," + deviceSiteFile.getPath());
                        Platform.runLater(() -> {
                            Label errorLab = new Label(sdfTime.format(new Date()) + "\t未知目录:" + deviceSiteFile.getPath());
                            errorLab.setStyle("-fx-text-fill:red;");
                            errorList.getItems().add(errorLab);
                        });
                    }
                } catch (Exception e) {
                    log.error("文件解析异常!" + deviceSiteFile.getPath(), e);
                    Platform.runLater(() -> {
                        Label errorLab = new Label(sdfTime.format(new Date()) + "\t解析异常:" + deviceSiteFile.getName() + "    " + e.getMessage());
                        errorLab.setStyle("-fx-text-fill:red;");
                        errorList.getItems().add(errorLab);
                    });
                }
            }
        }

    }

    /**
     * 获取设备可见光照片
     * path:要删除的文件的文件夹的路径
     */
    public List<String> getPhotosListByPath(String path) {
        List<String> photosList = new ArrayList<>();//照片-路径
        File file = new File(path);
        for (File tempFile : file.listFiles()) {
            if (tempFile.getPath().indexOf("_P_") != -1 && "jpg".equalsIgnoreCase(getFileExt(tempFile.getName()))) {
                photosList.add(tempFile.getPath());
            }
        }
        return photosList;
    }

    /**
     * 获取设备录音文件
     * path:要删除的文件的文件夹的路径
     */
    public List<String> getAudiosListByPath(String path) {
        List<String> audiosList = new ArrayList<>();//录音-路径
        File file = new File(path);
        for (File tempFile : file.listFiles()) {
            if ("pcm".equalsIgnoreCase(getFileExt(tempFile.getName()))) {
                audiosList.add(tempFile.getPath());
                File wavFile = new File(tempFile.getParentFile().getPath() + File.separator + tempFile.getName().substring(0, tempFile.getName().length() - 3) + "wav");
                if (!wavFile.exists()) {
                    ConvertAudio2WavUtil.convertAudio(tempFile.getPath(), wavFile.getPath());
                }
            }
        }
        return audiosList;
    }

    private void parseFile(String space, String testTechnology, File file, DataDeviceDto dataDeviceDto, DataDeviceSiteDto dataDeviceSiteDto) {
        try {
            //#DAT#AA#AE#HF#TEV#TJLB# getFileExt(file.getName())
            Platform.runLater(() -> {
                progressLine.setProgress(currentfileCount.doubleValue() / fileCount.doubleValue());
                progressValue.setText(currentfileCount + "/" + fileCount);
            });
            if ("jpg".equalsIgnoreCase(getFileExt(file.getName())) || "png".equalsIgnoreCase(getFileExt(file.getName())) || "pcm".equalsIgnoreCase(getFileExt(file.getName())) || "wav".equalsIgnoreCase(getFileExt(file.getName()))) {
                return;
            }
            currentfileCount.getAndAdd(1);

            DataAnalyzeDto dataAnalyzeParameterDto = new DataAnalyzeDto();
            dataAnalyzeParameterDto.setDataDeviceId(dataDeviceDto.getId());
            if (dataDeviceSiteDto != null) {
                dataAnalyzeParameterDto.setDataSource("2");//2具体位置
                dataAnalyzeParameterDto.setDataDeviceSiteId(dataDeviceSiteDto.getId());
            } else if ("#".equals(dataDeviceDto.getDeviceName())) {
                dataAnalyzeParameterDto.setDataSource("0");//0设备类型目录
            } else {
                dataAnalyzeParameterDto.setDataSource("1");//1具体设备
            }
            Object data = null;
            Map<String, Object> dataMap = new HashMap() {{
                put("space", space);
                put("file", file);
                put("dataDeviceDto", dataDeviceDto);
                put("dataDeviceSiteDto", dataDeviceSiteDto);
                put("dataFormat", "");
                put("imageAmount", 0);
                put("orderNo", 0);
            }};
            if (file.getPath().indexOf("_LC_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
                data = parseLC(dataMap);
            } else if (file.getPath().indexOf("_HJ_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
                data = parseHJ(dataMap);
            } else {
                switch (testTechnology) {
                    case "TEV": {
                        data = parseTEV(dataMap);
                        break;
                    }
                    case "AA": {
                        data = parseAA(dataMap);
                        break;
                    }
                    case "HFCT": {
                        data = parseHFCT(dataMap);
                        break;
                    }
                    case "AE": {
                        data = parseAE(dataMap);
                        break;
                    }
                    case "UHF": {
                        data = parseUHF(dataMap);
                        break;
                    }
                    default:
                        log.error("未知检测类型!");
                }
            }
            if (data == null) {
                throw new RuntimeException(file.getPath());
            }
            List<String> screenshotsList = new ArrayList<>();//截图-路径
            List<String> photosList = getPhotosListByPath(file.getPath().substring(0, file.getPath().lastIndexOf(File.separator)));//照片-路径
            List<String> audiosList = getAudiosListByPath(file.getPath().substring(0, file.getPath().lastIndexOf(File.separator)));//录音-路径
            Integer imageCount = (Integer) dataMap.get("imageAmount");
            if (imageCount > 0) {
                File imageFile = new File(file.getPath().substring(0, file.getPath().lastIndexOf(".")) + ".png");
                if (imageFile.exists()) {
                    screenshotsList.add(imageFile.getPath());
                } else {
                    log.error("配套截图文件缺失!" + imageFile.getPath());
                    Platform.runLater(() -> {
                        Label errorLab = new Label(sdfTime.format(new Date()) + "\t截图文件缺失==>" + file.getName());
                        errorLab.setStyle("-fx-text-fill:red;");
                        errorList.getItems().add(errorLab);
                    });
                }
            }
            String dataFormat = (String) dataMap.get("dataFormat");
            dataAnalyzeParameterDto.setOrderNo((Integer) dataMap.get("orderNo"));
            dataAnalyzeParameterDto.setDataFormat(dataFormat);
            dataAnalyzeParameterDto.setScreenshots(JSON.toJSONString(screenshotsList));
            dataAnalyzeParameterDto.setPhotos(JSON.toJSONString(photosList));
            dataAnalyzeParameterDto.setAudios(JSON.toJSONString(audiosList));
            dataAnalyzeParameterDto.setFileUrl(file.getPath());
            try {
                DataAnalyzeDto dataAnalyzeDto = dataAnalyzeService.getSaveDataAnalyzeDto(dataAnalyzeParameterDto, dataDeviceDto, dataDeviceSiteDto);
                //数据保存
                saveData(data, dataAnalyzeDto);
            } catch (Exception e) {
                log.error("文件数据入库异常!", e);
            }

        } catch (Exception e) {
            log.error("文件解析异常!" + file.getPath(), e);
            Platform.runLater(() -> {
                Label errorLab = new Label(sdfTime.format(new Date()) + "\t文件解析异常==>" + file.getName());
                errorLab.setStyle("-fx-text-fill:red;");
                errorList.getItems().add(errorLab);
            });
        }
    }

    private void saveData(Object data, DataAnalyzeDto dataAnalyzeDto) {
        if (dataAnalyzeDto == null || data == null) {
            return;
        }
        String clazzName = data.getClass().getName().substring(data.getClass().getName().lastIndexOf(".") + 1, data.getClass().getName().length());
        log.debug("data class==>{}", clazzName);
        try {
            synchronized (DataAnalyzeDto.class) {
                switch (clazzName) {
                    case "DataHjDto": {
                        DataHjDto dataDto = copyTo(data, DataHjDto.class);
                        dataDto.setDataAnalyzeId(dataAnalyzeDto.getId());
                        dataHjService.saveOrUpdateData(dataDto);
                        break;
                    }
                    case "DataLcDto": {
                        DataLcDto dataDto = copyTo(data, DataLcDto.class);
                        dataDto.setDataAnalyzeId(dataAnalyzeDto.getId());
                        dataLcService.saveOrUpdateData(dataDto);
                        break;
                    }
                    case "DataTevTjDto": {
                        DataTevTjDto dataDto = copyTo(data, DataTevTjDto.class);
                        dataDto.setDataAnalyzeId(dataAnalyzeDto.getId());
                        dataTevTjService.saveOrUpdateData(dataDto);
                        break;
                    }
                    case "DataTevTjlbDto": {
                        DataTevTjlbDto dataDto = copyTo(data, DataTevTjlbDto.class);
                        dataDto.setDataAnalyzeId(dataAnalyzeDto.getId());
                        dataTevTjlbService.saveOrUpdateData(dataDto);
                        break;
                    }
                    case "DataTevTjzsDto": {
                        DataTevTjzsDto dataDto = copyTo(data, DataTevTjzsDto.class);
                        dataDto.setDataAnalyzeId(dataAnalyzeDto.getId());
                        dataTevTjzsService.saveOrUpdateData(dataDto);
                        break;
                    }
                    case "DataTevXjDto": {
                        DataTevXjDto dataDto = copyTo(data, DataTevXjDto.class);
                        dataDto.setDataAnalyzeId(dataAnalyzeDto.getId());
                        dataTevXjService.saveOrUpdateData(dataDto);
                        break;
                    }
                    case "DataTevXjlhDto": {
                        DataTevXjlhDto dataDto = copyTo(data, DataTevXjlhDto.class);
                        dataDto.setDataAnalyzeId(dataAnalyzeDto.getId());
                        dataTevXjlhService.saveOrUpdateData(dataDto);
                        break;
                    }
                    case "DataTevXjzsDto": {
                        DataTevXjzsDto dataDto = copyTo(data, DataTevXjzsDto.class);
                        dataDto.setDataAnalyzeId(dataAnalyzeDto.getId());
                        dataTevXjzsService.saveOrUpdateData(dataDto);
                        break;
                    }
                    case "DataAaBxDto": {
                        DataAaBxDto dataDto = copyTo(data, DataAaBxDto.class);
                        dataDto.setDataAnalyzeId(dataAnalyzeDto.getId());
                        dataAaBxService.saveOrUpdateData(dataDto);
                        break;
                    }
                    case "DataAaFxDto": {
                        DataAaFxDto dataDto = copyTo(data, DataAaFxDto.class);
                        dataDto.setDataAnalyzeId(dataAnalyzeDto.getId());
                        dataAaFxService.saveOrUpdateData(dataDto);
                        break;
                    }
                    case "DataAaTjDto": {
                        DataAaTjDto dataDto = copyTo(data, DataAaTjDto.class);
                        dataDto.setDataAnalyzeId(dataAnalyzeDto.getId());
                        dataAaTjService.saveOrUpdateData(dataDto);
                        break;
                    }
                    case "DataAaTjlbDto": {
                        DataAaTjlbDto dataDto = copyTo(data, DataAaTjlbDto.class);
                        dataDto.setDataAnalyzeId(dataAnalyzeDto.getId());
                        dataAaTjlbService.saveOrUpdateData(dataDto);
                        break;
                    }
                    case "DataAaTjzsDto": {
                        DataAaTjzsDto dataDto = copyTo(data, DataAaTjzsDto.class);
                        dataDto.setDataAnalyzeId(dataAnalyzeDto.getId());
                        dataAaTjzsService.saveOrUpdateData(dataDto);
                        break;
                    }
                    case "DataAaXjDto": {
                        DataAaXjDto dataDto = copyTo(data, DataAaXjDto.class);
                        dataDto.setDataAnalyzeId(dataAnalyzeDto.getId());
                        dataAaXjService.saveOrUpdateData(dataDto);
                        break;
                    }
                    case "DataAaXjzsDto": {
                        DataAaXjzsDto dataDto = copyTo(data, DataAaXjzsDto.class);
                        dataDto.setDataAnalyzeId(dataAnalyzeDto.getId());
                        dataAaXjzsService.saveOrUpdateData(dataDto);
                        break;
                    }
                    case "DataAeBxDto": {
                        DataAeBxDto dataDto = copyTo(data, DataAeBxDto.class);
                        dataDto.setDataAnalyzeId(dataAnalyzeDto.getId());
                        dataAeBxService.saveOrUpdateData(dataDto);
                        break;
                    }
                    case "DataAeFxDto": {
                        DataAeFxDto dataDto = copyTo(data, DataAeFxDto.class);
                        dataDto.setDataAnalyzeId(dataAnalyzeDto.getId());
                        dataAeFxService.saveOrUpdateData(dataDto);
                        break;
                    }
                    case "DataAeTjDto": {
                        DataAeTjDto dataDto = copyTo(data, DataAeTjDto.class);
                        dataDto.setDataAnalyzeId(dataAnalyzeDto.getId());
                        dataAeTjService.saveOrUpdateData(dataDto);
                        break;
                    }
                    case "DataAeTjlbDto": {
                        DataAeTjlbDto dataDto = copyTo(data, DataAeTjlbDto.class);
                        dataDto.setDataAnalyzeId(dataAnalyzeDto.getId());
                        dataAeTjlbService.saveOrUpdateData(dataDto);
                        break;
                    }
                    case "DataAeTjzsDto": {
                        DataAeTjzsDto dataDto = copyTo(data, DataAeTjzsDto.class);
                        dataDto.setDataAnalyzeId(dataAnalyzeDto.getId());
                        dataAeTjzsService.saveOrUpdateData(dataDto);
                        break;
                    }
                    case "DataAeXjDto": {
                        DataAeXjDto dataDto = copyTo(data, DataAeXjDto.class);
                        dataDto.setDataAnalyzeId(dataAnalyzeDto.getId());
                        dataAeXjService.saveOrUpdateData(dataDto);
                        break;
                    }
                    case "DataAeXjzsDto": {
                        DataAeXjzsDto dataDto = copyTo(data, DataAeXjzsDto.class);
                        dataDto.setDataAnalyzeId(dataAnalyzeDto.getId());
                        dataAeXjzsService.saveOrUpdateData(dataDto);
                        break;
                    }
                    case "DataHfctTjDto": {
                        DataHfctTjDto dataDto = copyTo(data, DataHfctTjDto.class);
                        dataDto.setDataAnalyzeId(dataAnalyzeDto.getId());
                        dataHfctTjService.saveOrUpdateData(dataDto);
                        break;
                    }
                    case "DataHfctTjlbDto": {
                        DataHfctTjlbDto dataDto = copyTo(data, DataHfctTjlbDto.class);
                        dataDto.setDataAnalyzeId(dataAnalyzeDto.getId());
                        dataHfctTjlbService.saveOrUpdateData(dataDto);
                        break;
                    }
                    case "DataHfctTjzsDto": {
                        DataHfctTjzsDto dataDto = copyTo(data, DataHfctTjzsDto.class);
                        dataDto.setDataAnalyzeId(dataAnalyzeDto.getId());
                        dataHfctTjzsService.saveOrUpdateData(dataDto);
                        break;
                    }
                    case "DataHfctXjDto": {
                        DataHfctXjDto dataDto = copyTo(data, DataHfctXjDto.class);
                        dataDto.setDataAnalyzeId(dataAnalyzeDto.getId());
                        dataHfctXjService.saveOrUpdateData(dataDto);
                        break;
                    }
                    case "DataHfctXjzsDto": {
                        DataHfctXjzsDto dataDto = copyTo(data, DataHfctXjzsDto.class);
                        dataDto.setDataAnalyzeId(dataAnalyzeDto.getId());
                        dataHfctXjzsService.saveOrUpdateData(dataDto);
                        break;
                    }
                    case "DataUhfTj1Dto": {
                        DataUhfTj1Dto dataDto = copyTo(data, DataUhfTj1Dto.class);
                        dataDto.setDataAnalyzeId(dataAnalyzeDto.getId());
                        dataUhfTj1Service.saveOrUpdateData(dataDto);
                        break;
                    }
                    case "DataUhfTj2Dto": {
                        DataUhfTj2Dto dataDto = copyTo(data, DataUhfTj2Dto.class);
                        dataDto.setDataAnalyzeId(dataAnalyzeDto.getId());
                        dataUhfTj2Service.saveOrUpdateData(dataDto);
                        break;
                    }
                    case "DataUhfTjlb1Dto": {
                        DataUhfTjlb1Dto dataDto = copyTo(data, DataUhfTjlb1Dto.class);
                        dataDto.setDataAnalyzeId(dataAnalyzeDto.getId());
                        dataUhfTjlb1Service.saveOrUpdateData(dataDto);
                        break;
                    }
                    case "DataUhfTjlb2Dto": {
                        DataUhfTjlb2Dto dataDto = copyTo(data, DataUhfTjlb2Dto.class);
                        dataDto.setDataAnalyzeId(dataAnalyzeDto.getId());
                        dataUhfTjlb2Service.saveOrUpdateData(dataDto);
                        break;
                    }
                    case "DataUhfTjzs1Dto": {
                        DataUhfTjzs1Dto dataDto = copyTo(data, DataUhfTjzs1Dto.class);
                        dataDto.setDataAnalyzeId(dataAnalyzeDto.getId());
                        dataUhfTjzs1Service.saveOrUpdateData(dataDto);
                        break;
                    }
                    case "DataUhfTjzs2Dto": {
                        DataUhfTjzs2Dto dataDto = copyTo(data, DataUhfTjzs2Dto.class);
                        dataDto.setDataAnalyzeId(dataAnalyzeDto.getId());
                        dataUhfTjzs2Service.saveOrUpdateData(dataDto);
                        break;
                    }
                    default:
                        log.error("未知对象!");
                }
            }
        } catch (Exception e) {
            log.error("对象转换异常!", e);
        }
    }
//    private void successPareseLog(String msg) {
//        //successList.getItems().add(sdfTime.format(new Date()) + "    解析文件:" + msg);
//    }

    private Object parseTEV(Map<String, Object> dataMap) throws Exception {
//        String space = (String) dataMap.get("space");
        File file = (File) dataMap.get("file");
//        DataDeviceDto dataDeviceDto = (DataDeviceDto) dataMap.get("dataDeviceDto");
//        DataDeviceSiteDto dataDeviceSiteDto = (DataDeviceSiteDto) dataMap.get("dataDeviceSiteDto");
        //TEV_XJZS:
        //	同时保存噪声数据文件和界面的截屏图片，二者文件名相同；
        //	文件名：变电站_检测对象_XJZS_噪声序号_日期时间；
        //	噪声数据文件采用二进制编码，后缀为.dat，图片为JPG文件，噪声序号为0/1/2，共3个。如110kV夏河变_10kV开关柜_XJZS_0_20171112121212.dat。
        if (file.getPath().indexOf("_XJZS_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
            if (file.getPath().indexOf("_XJZS_0_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//            log.debug(AnalyzeData.DataType.TEV_XJZS.name() + "0==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.TEV_XJZS));
                dataMap.put("orderNo", 0);
            } else if (file.getPath().indexOf("_XJZS_1_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//            log.debug(AnalyzeData.DataType.TEV_XJZS.name() + "1==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.TEV_XJZS));
                dataMap.put("orderNo", 1);
            } else if (file.getPath().indexOf("_XJZS_2_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//            log.debug(AnalyzeData.DataType.TEV_XJZS.name() + "2==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.TEV_XJZS));
                dataMap.put("orderNo", 2);
            }
            dataMap.put("imageAmount", 1);
            dataMap.put("dataFormat", "TEV_XJZS");
            return analyzeData.parse(file.getPath(), AnalyzeData.DataType.TEV_XJZS);
        } else
            //TEV_XJ:
            //	同时保存巡检数据文件和界面的截屏图片，二者文件名相同；
            //	文件名：被测部位+测试点_XJ_日期时间；
            //	巡检数据文件采用二进制编码，后缀为.dat，图片为JPG文件。如前下_XJ_20171112121212.dat。
            if (file.getPath().indexOf("_XJ_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//                log.debug(AnalyzeData.DataType.TEV_XJ.name() + "==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.TEV_XJ));
                dataMap.put("imageAmount", 1);
                dataMap.put("dataFormat", "TEV_XJ");
                return analyzeData.parse(file.getPath(), AnalyzeData.DataType.TEV_XJ);
            } else
                //TEV_XJL:
                //	同时保存巡检数据文件和界面的截屏图片，二者文件名相同；
                //	文件名：被测部位+测试点_XJ_日期时间；
                //	巡检数据文件采用二进制编码，后缀为.dat，图片为JPG文件。如前下_XJL_20171112121212.dat。
                if (file.getPath().indexOf("_XJL_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//                log.debug(AnalyzeData.DataType.TEV_XJL.name() + "==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.TEV_XJL));
                    dataMap.put("imageAmount", 1);
                    dataMap.put("dataFormat", "TEV_XJL");
                    return analyzeData.parse(file.getPath(), AnalyzeData.DataType.TEV_XJL);
                } else
                    //TEV_TJZS:
                    //	同时保存噪声数据文件和界面的截屏图片，二者文件名相同；
                    //	文件名：变电站_检测对象_TJZS_噪声序号_日期时间；
                    //	噪声数据文件采用二进制编码，后缀为.dat，图片为JPG文件，噪声序号为0/1/2，共3个。如110kV夏河变_10kV开关柜_TJZS_0_20171112121212.dat。
                    if (file.getPath().indexOf("_TJZS_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
                        if (file.getPath().indexOf("_TJZS_0_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//                    log.debug(AnalyzeData.DataType.TEV_TJZS.name() + "0==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.TEV_TJZS));
                            dataMap.put("orderNo", 0);
                        } else if (file.getPath().indexOf("_TJZS_1_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//                    log.debug(AnalyzeData.DataType.TEV_TJZS.name() + "1==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.TEV_TJZS));
                            dataMap.put("orderNo", 1);
                        } else if (file.getPath().indexOf("_TJZS_2_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//                    log.debug(AnalyzeData.DataType.TEV_TJZS.name() + "2==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.TEV_TJZS));
                            dataMap.put("orderNo", 2);
                        }
                        dataMap.put("imageAmount", 1);
                        dataMap.put("dataFormat", "TEV_TJZS");
                        return analyzeData.parse(file.getPath(), AnalyzeData.DataType.TEV_TJZS);
                    } else
                        //TEV_TJ:
                        //文件采用扩展名为.TEV的二进制数据格式进行存储，每个.TEV文件存储一张PRPS图谱的数据。
                        //文件名规则为：仪器厂家及型号编码_图谱类型_图谱生成时间.TEV。仪器厂家及型号编码为数字、字母或两者的组合，最多为32个字符。图谱类型： PRPS图谱为 1。图谱生成日期格式为：YYYYMMDDhhmmss。
                        //仪器厂家及型号编码、图谱类型、图谱生成时间之间用下划线分隔。
                        //示例：RedPhasePDT400_1_20140823185809.TEV
                        //每个文件大小不超过500KB。
                        if (file.getPath().indexOf("_1_") != -1 && "TEV".equalsIgnoreCase(getFileExt(file.getName()))) {
//                        log.debug(AnalyzeData.DataType.TEV_TJ.name() + "==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.TEV_TJ));
                            dataMap.put("imageAmount", 1);
                            dataMap.put("dataFormat", "TEV_TJ");
                            return analyzeData.parse(file.getPath(), AnalyzeData.DataType.TEV_TJ);
                        } else
                            //TEV_TJLB:
                            //文件采用扩展名为.TJLB的二进制数据格式进行存储，每个.TJLB文件存储录波数据。
                            //文件名规则为：仪器厂家及型号编码_图谱类型_图谱生成时间. TJLB。仪器厂家及型号编码为数字、字母或两者的组合，最多为32个字符。图谱类型： PRPS图谱为 1。图谱生成日期格式为：YYYYMMDDhhmmss。
                            //仪器厂家及型号编码、图谱类型、图谱生成时间之间用下划线分隔。
                            //录波数据文件的文件名与统计数据文件名相同。
                            //示例：RedPhasePDT400_1_20140823185809. TJLB
                            if (file.getPath().indexOf("_1_") != -1 && "TJLB".equalsIgnoreCase(getFileExt(file.getName()))) {
//                            log.debug(AnalyzeData.DataType.TEV_TJLB.name() + "==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.TEV_TJLB));
                                dataMap.put("dataFormat", "TEV_TJLB");
                                return analyzeData.parse(file.getPath(), AnalyzeData.DataType.TEV_TJLB);
                            }
        return null;
    }

    private Object parseAA(Map<String, Object> dataMap) throws Exception {
//        String space = (String) dataMap.get("space");
        File file = (File) dataMap.get("file");
//        DataDeviceDto dataDeviceDto = (DataDeviceDto) dataMap.get("dataDeviceDto");
//        DataDeviceSiteDto dataDeviceSiteDto = (DataDeviceSiteDto) dataMap.get("dataDeviceSiteDto");
        //AA_XJZS:
        //	同时保存噪声数据文件和界面的截屏图片，二者文件名相同；
        //	文件名：变电站_检测对象_XJZS_噪声序号_日期时间；
        //	噪声数据文件采用二进制编码，后缀为.dat，图片为JPG文件，噪声序号为0/1/2，共3个。如110kV夏河变_10kV开关柜_XJZS_0_20171112121212.dat
        if (file.getPath().indexOf("_XJZS_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
            if (file.getPath().indexOf("_XJZS_0_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//            log.debug(AnalyzeData.DataType.AA_XJZS.name() + "0==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.AA_XJZS));
                dataMap.put("orderNo", 0);
            } else if (file.getPath().indexOf("_XJZS_1_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//            log.debug(AnalyzeData.DataType.AA_XJZS.name() + "1==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.AA_XJZS));
                dataMap.put("orderNo", 1);
            } else if (file.getPath().indexOf("_XJZS_2_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//            log.debug(AnalyzeData.DataType.AA_XJZS.name() + "2==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.AA_XJZS));
                dataMap.put("orderNo", 2);
            }
            dataMap.put("imageAmount", 1);
            dataMap.put("dataFormat", "AA_XJZS");
            return analyzeData.parse(file.getPath(), AnalyzeData.DataType.AA_XJZS);
        } else
            //AA_XJ:
            //	同时保存巡检数据文件和界面的截屏图片，二者文件名相同；
            //	文件名：被测部位+测试点_XJ_日期时间；
            //	巡检数据文件采用二进制编码，后缀为.dat，图片为JPG文件。如前下_XJ_20171112121212.dat。
            if (file.getPath().indexOf("_XJ_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//                log.debug(AnalyzeData.DataType.AA_XJ.name() + "==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.AA_XJ));
                dataMap.put("imageAmount", 1);
                dataMap.put("dataFormat", "AA_XJ");
                return analyzeData.parse(file.getPath(), AnalyzeData.DataType.AA_XJ);
            } else
                //AA_TJZS:
                //	同时保存噪声数据文件和界面的截屏图片，二者文件名相同；
                //	文件名：变电站_检测对象_TJZS_噪声序号_日期时间；
                //	噪声数据文件采用二进制编码，后缀为.dat，图片为JPG文件，噪声序号为0/1/2，共3个。如110kV夏河变_10kV开关柜_TJZS_0_20171112121212.dat。
                if (file.getPath().indexOf("_TJZS_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
                    if (file.getPath().indexOf("_TJZS_0_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//                    log.debug(AnalyzeData.DataType.AA_TJZS.name() + "0==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.AA_TJZS));
                        dataMap.put("orderNo", 0);
                    } else if (file.getPath().indexOf("_TJZS_1_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//                    log.debug(AnalyzeData.DataType.AA_TJZS.name() + "1==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.AA_TJZS));
                        dataMap.put("orderNo", 1);
                    } else if (file.getPath().indexOf("_TJZS_2_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//                    log.debug(AnalyzeData.DataType.AA_TJZS.name() + "2==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.AA_TJZS));
                        dataMap.put("orderNo", 2);
                    }
                    dataMap.put("imageAmount", 1);
                    dataMap.put("dataFormat", "AA_TJZS");
                    return analyzeData.parse(file.getPath(), AnalyzeData.DataType.AA_TJZS);
                } else
                    //AA_TJ:
                    //文件采用扩展名为.AA的二进制数据格式进行存储，每个.AA文件存储一张PRPS图谱的数据。
                    //文件名规则为：仪器厂家及型号编码_图谱类型_图谱生成时间.AA。仪器厂家及型号编码为数字、字母或两者的组合，最多为32个字符。图谱类型： PRPS图谱为 1。图谱生成日期格式为：YYYYMMDDhhmmss。
                    //仪器厂家及型号编码、图谱类型、图谱生成时间之间用下划线分隔。
                    //示例：RedPhasePDT400_1_20140823185809.AA
                    //每个.dat文件大小不超过500KB。
                    if (file.getPath().indexOf("_1_") != -1 && "AA".equalsIgnoreCase(getFileExt(file.getName()))) {
//                        log.debug(AnalyzeData.DataType.AA_TJ.name() + "==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.AA_TJ));
                        dataMap.put("imageAmount", 1);
                        dataMap.put("dataFormat", "AA_TJ");
                        return analyzeData.parse(file.getPath(), AnalyzeData.DataType.AA_TJ);
                    } else
                        //AA_TJLB:
                        //文件采用扩展名为.TJLB的二进制数据格式进行存储，每个.TJLB文件存储录波数据。
                        //文件名规则为：仪器厂家及型号编码_图谱类型_图谱生成时间. TJLB。仪器厂家及型号编码为数字、字母或两者的组合，最多为32个字符。图谱类型： PRPS图谱为 1。图谱生成日期格式为：YYYYMMDDhhmmss。
                        //仪器厂家及型号编码、图谱类型、图谱生成时间之间用下划线分隔。
                        //录波数据文件的文件名与统计数据文件名相同。
                        //示例：RedPhasePDT400_1_20140823185809. TJLB
                        if (file.getPath().indexOf("_1_") != -1 && "TJLB".equalsIgnoreCase(getFileExt(file.getName()))) {
                            log.debug(AnalyzeData.DataType.AA_TJLB.name() + "==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.AA_TJLB));
                            dataMap.put("dataFormat", "AA_TJLB");
                            return analyzeData.parse(file.getPath(), AnalyzeData.DataType.AA_TJLB);
                        } else
                            //AA_FX:
                            //	文件名：被测部位+测试点_FX_日期时间，其中，FX为AE的飞行参数数据。刀闸A相侧_FX_20170809102930。时间格式——YYYYMMDDHHMMSS。文件采用二进制编码，后缀主要为.dat。
                            if (file.getPath().indexOf("_FX_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
                                //            log.debug(AnalyzeData.DataType.AE_FX.name() + "==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.AE_FX));
                                dataMap.put("imageAmount", 1);
                                dataMap.put("dataFormat", "AA_FX");
                                return analyzeData.parse(file.getPath(), AnalyzeData.DataType.AA_FX);
                            } else
                                //AA_BX:
                                //	文件名：被测部位+测试点_BX_日期时间，其中，BX为AE的波形参数数据。刀闸A相侧_BX_20170809102930。时间格式——YYYYMMDDHHMMSS。文件采用二进制编码，后缀主要为.dat。
                                if (file.getPath().indexOf("_BX_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
                                    //                log.debug(AnalyzeData.DataType.AE_BX.name() + "==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.AE_BX));
                                    dataMap.put("imageAmount", 1);
                                    dataMap.put("dataFormat", "AA_BX");
                                    return analyzeData.parse(file.getPath(), AnalyzeData.DataType.AA_BX);
                                }
        return null;
    }

    private Object parseHFCT(Map<String, Object> dataMap) throws Exception {
//        String space = (String) dataMap.get("space");
        File file = (File) dataMap.get("file");
//        DataDeviceDto dataDeviceDto = (DataDeviceDto) dataMap.get("dataDeviceDto");
//        DataDeviceSiteDto dataDeviceSiteDto = (DataDeviceSiteDto) dataMap.get("dataDeviceSiteDto");
        //HFCT_XJZS:
        //	同时保存噪声数据文件和界面的截屏图片，二者文件名相同；
        //	文件名：变电站_检测对象_XJZS_噪声序号_日期时间；
        //	噪声数据文件采用二进制编码，后缀为.dat，图片为JPG文件，噪声序号为0/1/2，共3个。如110kV夏河变_10kV开关柜_XJZS_0_20171112121212.dat。
        if (file.getPath().indexOf("_XJZS_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
            if (file.getPath().indexOf("_XJZS_0_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//            log.debug(AnalyzeData.DataType.HFCT_XJZS.name() + "0==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.HFCT_XJZS));
                dataMap.put("orderNo", 0);
            } else if (file.getPath().indexOf("_XJZS_1_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//            log.debug(AnalyzeData.DataType.HFCT_XJZS.name() + "1==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.HFCT_XJZS));
                dataMap.put("orderNo", 1);
            } else if (file.getPath().indexOf("_XJZS_2_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//            log.debug(AnalyzeData.DataType.HFCT_XJZS.name() + "2==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.HFCT_XJZS));
                dataMap.put("orderNo", 2);
            }
            dataMap.put("imageAmount", 1);
            dataMap.put("dataFormat", "HFCT_XJZS");
            return analyzeData.parse(file.getPath(), AnalyzeData.DataType.HFCT_XJZS);
        } else
            //HFCT_XJ:
            //	同时保存巡检数据文件和界面的截屏图片，二者文件名相同；
            //	文件名：被测部位+测试点_XJ_日期时间；
            //	巡检数据文件采用二进制编码，后缀为.dat，图片为JPG文件。如前下_XJ_20171112121212.dat。
            if (file.getPath().indexOf("_XJ_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//                log.debug(AnalyzeData.DataType.HFCT_XJ.name() + "==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.HFCT_XJ));
                dataMap.put("imageAmount", 1);
                dataMap.put("dataFormat", "HFCT_XJ");
                return analyzeData.parse(file.getPath(), AnalyzeData.DataType.HFCT_XJ);
            } else
                //HFCT_TJZS:
                //	同时保存噪声数据文件和界面的截屏图片，二者文件名相同；
                //	文件名：变电站_检测对象_TJZS_噪声序号_日期时间；
                //	噪声数据文件采用二进制编码，后缀为.dat，图片为JPG文件，噪声序号为0/1/2，共3个。如110kV夏河变_10kV开关柜_TJZS_0_20171112121212.dat。
                if (file.getPath().indexOf("_TJZS_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
                    if (file.getPath().indexOf("_TJZS_0_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//                    log.debug(AnalyzeData.DataType.HFCT_TJZS.name() + "0==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.HFCT_TJZS));
                        dataMap.put("orderNo", 0);
                    } else if (file.getPath().indexOf("_TJZS_1_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//                    log.debug(AnalyzeData.DataType.HFCT_TJZS.name() + "1==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.HFCT_TJZS));
                        dataMap.put("orderNo", 1);
                    } else if (file.getPath().indexOf("_TJZS_2_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//                    log.debug(AnalyzeData.DataType.HFCT_TJZS.name() + "2==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.HFCT_TJZS));
                        dataMap.put("orderNo", 2);
                    }
                    dataMap.put("imageAmount", 1);
                    dataMap.put("dataFormat", "HFCT_TJZS");
                    return analyzeData.parse(file.getPath(), AnalyzeData.DataType.HFCT_TJZS);
                } else
                    //HFCT_TJ:
                    //文件采用扩展名为.HF的二进制数据格式进行存储，每个.HF文件存储一张PRPS图谱的数据。
                    //文件名规则为：仪器厂家及型号编码_图谱类型_图谱生成时间.HF。仪器厂家及型号编码为数字、字母或两者的组合，最多为32个字符。图谱类型： PRPS图谱为 1。图谱生成日期格式为：YYYYMMDDhhmmss。
                    //仪器厂家及型号编码、图谱类型、图谱生成时间之间用下划线分隔。
                    //示例：RedPhasePDT400_1_20140823185809.HF
                    //每个.dat文件大小不超过500KB。
                    if (file.getPath().indexOf("_1_") != -1 && "HF".equalsIgnoreCase(getFileExt(file.getName()))) {
//                        log.debug(AnalyzeData.DataType.HFCT_TJ.name() + "==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.HFCT_TJ));
                        dataMap.put("imageAmount", 1);
                        dataMap.put("dataFormat", "HFCT_TJ");
                        return analyzeData.parse(file.getPath(), AnalyzeData.DataType.HFCT_TJ);
                    } else
                        //HFCT_TJLB:
                        //文件采用扩展名为.TJLB的二进制数据格式进行存储，每个.TJLB文件存储录波数据。
                        //文件名规则为：仪器厂家及型号编码_图谱类型_图谱生成时间. TJLB。仪器厂家及型号编码为数字、字母或两者的组合，最多为32个字符。图谱类型： PRPS图谱为 1。图谱生成日期格式为：YYYYMMDDhhmmss。
                        //仪器厂家及型号编码、图谱类型、图谱生成时间之间用下划线分隔。
                        //录波数据文件的文件名与统计数据文件名相同。
                        //示例：RedPhasePDT400_1_20140823185809. TJLB
                        if (file.getPath().indexOf("_1_") != -1 && "TJLB".equalsIgnoreCase(getFileExt(file.getName()))) {
//                            log.debug(AnalyzeData.DataType.HFCT_TJLB.name() + "==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.HFCT_TJLB));
                            dataMap.put("dataFormat", "HFCT_TJLB");
                            return analyzeData.parse(file.getPath(), AnalyzeData.DataType.HFCT_TJLB);
                        }
        return null;
    }

    private Object parseAE(Map<String, Object> dataMap) throws Exception {
//        String space = (String) dataMap.get("space");
        File file = (File) dataMap.get("file");
//        DataDeviceDto dataDeviceDto = (DataDeviceDto) dataMap.get("dataDeviceDto");
//        DataDeviceSiteDto dataDeviceSiteDto = (DataDeviceSiteDto) dataMap.get("dataDeviceSiteDto");
        //AE_XJZS:
        //	同时保存噪声数据文件和界面的截屏图片，二者文件名相同；
        //	文件名：变电站_检测对象_XJZS_噪声序号_日期时间；
        //	噪声数据文件采用二进制编码，后缀为.dat，图片为JPG文件，噪声序号为0/1/2，共3个。如110kV夏河变_10kV开关柜_XJZS_0_20171112121212.dat。
        if (file.getPath().indexOf("_XJZS_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
            if (file.getPath().indexOf("_XJZS_0_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//            log.debug(AnalyzeData.DataType.AE_XJZS.name() + "0==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.AE_XJZS));
                dataMap.put("orderNo", 0);
            } else if (file.getPath().indexOf("_XJZS_1_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//            log.debug(AnalyzeData.DataType.AE_XJZS.name() + "1==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.AE_XJZS));
                dataMap.put("orderNo", 1);
            } else if (file.getPath().indexOf("_XJZS_2_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//            log.debug(AnalyzeData.DataType.AE_XJZS.name() + "2==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.AE_XJZS));
                dataMap.put("orderNo", 2);
            }
            dataMap.put("imageAmount", 1);
            dataMap.put("dataFormat", "AE_XJZS");
            return analyzeData.parse(file.getPath(), AnalyzeData.DataType.AE_XJZS);
        } else
            //AE_XJ:
            //	同时保存巡检数据文件和界面的截屏图片，二者文件名相同；
            //	文件名：被测部位+测试点_XJ_日期时间；
            //	巡检数据文件采用二进制编码，后缀为.dat，图片为JPG文件。如前下_XJ_20171112121212.dat。
            if (file.getPath().indexOf("_XJ_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
////                successPareseLog(spacespace + AnalyzeData.DataType.AE_XJ.name() + "==>" + file.getName());
//                log.debug(AnalyzeData.DataType.AE_XJ.name() + "==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.AE_XJ));
                dataMap.put("imageAmount", 1);
                dataMap.put("dataFormat", "AE_XJ");
                return analyzeData.parse(file.getPath(), AnalyzeData.DataType.AE_XJ);
            } else
                //AE_TJZS:
                //	同时保存噪声数据文件和界面的截屏图片，二者文件名相同；
                //	文件名：变电站_检测对象_TJZS_噪声序号_日期时间；
                //	噪声数据文件采用二进制编码，后缀为.dat，图片为JPG文件，噪声序号为0/1/2，共3个。如110kV夏河变_10kV开关柜_TJZS_0_20171112121212.dat。
                if (file.getPath().indexOf("_TJZS_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
                    if (file.getPath().indexOf("_TJZS_0_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//                    log.debug(AnalyzeData.DataType.AE_TJZS.name() + "0==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.AE_TJZS));
                        dataMap.put("orderNo", 0);
                    } else if (file.getPath().indexOf("_TJZS_1_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//                    log.debug(AnalyzeData.DataType.AE_TJZS.name() + "1==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.AE_TJZS));
                        dataMap.put("orderNo", 1);
                    } else if (file.getPath().indexOf("_TJZS_2_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//                    log.debug(AnalyzeData.DataType.AE_TJZS.name() + "2==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.AE_TJZS));
                        dataMap.put("orderNo", 2);
                    }
                    dataMap.put("imageAmount", 1);
                    dataMap.put("dataFormat", "AE_TJZS");
                    return analyzeData.parse(file.getPath(), AnalyzeData.DataType.AE_TJZS);
                } else
                    //AE_TJ:
                    //文件采用扩展名为.AE的二进制数据格式进行存储，每个.AE文件存储一张PRPS图谱的数据。
                    //文件名规则为：仪器厂家及型号编码_图谱类型_图谱生成时间.AE。仪器厂家及型号编码为数字、字母或两者的组合，最多为32个字符。图谱类型： PRPS图谱为 1。图谱生成日期格式为：YYYYMMDDhhmmss。
                    //仪器厂家及型号编码、图谱类型、图谱生成时间之间用下划线分隔。
                    //示例：RedPhasePDT400_1_20140823185809.AE
                    //每个.dat文件大小不超过500KB。
                    if (file.getPath().indexOf("_1_") != -1 && "AE".equalsIgnoreCase(getFileExt(file.getName()))) {
//                        log.debug(AnalyzeData.DataType.AE_TJ.name() + "==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.AE_TJ));
                        dataMap.put("imageAmount", 1);
                        dataMap.put("dataFormat", "AE_TJ");
                        return analyzeData.parse(file.getPath(), AnalyzeData.DataType.AE_TJ);
                    } else
                        //AE_TJLB:
                        //文件采用扩展名为.TJLB的二进制数据格式进行存储，每个.TJLB文件存储录波数据。
                        //文件名规则为：仪器厂家及型号编码_图谱类型_图谱生成时间. TJLB。仪器厂家及型号编码为数字、字母或两者的组合，最多为32个字符。图谱类型： PRPS图谱为 1。图谱生成日期格式为：YYYYMMDDhhmmss。
                        //仪器厂家及型号编码、图谱类型、图谱生成时间之间用下划线分隔。
                        //录波数据文件的文件名与统计数据文件名相同。
                        //示例：RedPhasePDT400_1_20140823185809. TJLB
                        if (file.getPath().indexOf("_1_") != -1 && "TJLB".equalsIgnoreCase(getFileExt(file.getName()))) {
//                            log.debug(AnalyzeData.DataType.AE_TJLB.name() + "==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.AE_TJLB));
                            dataMap.put("dataFormat", "AE_TJLB");
                            return analyzeData.parse(file.getPath(), AnalyzeData.DataType.AE_TJLB);
                        } else
                            //AE_FX:
                            //	文件名：被测部位+测试点_FX_日期时间，其中，FX为AE的飞行参数数据。刀闸A相侧_FX_20170809102930。时间格式——YYYYMMDDHHMMSS。文件采用二进制编码，后缀主要为.dat。
                            if (file.getPath().indexOf("_FX_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//            log.debug(AnalyzeData.DataType.AE_FX.name() + "==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.AE_FX));
                                dataMap.put("imageAmount", 1);
                                dataMap.put("dataFormat", "AE_FX");
                                return analyzeData.parse(file.getPath(), AnalyzeData.DataType.AE_FX);
                            } else
                                //AE_BX:
                                //	文件名：被测部位+测试点_BX_日期时间，其中，BX为AE的波形参数数据。刀闸A相侧_BX_20170809102930。时间格式——YYYYMMDDHHMMSS。文件采用二进制编码，后缀主要为.dat。
                                if (file.getPath().indexOf("_BX_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//                log.debug(AnalyzeData.DataType.AE_BX.name() + "==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.AE_BX));
                                    dataMap.put("imageAmount", 1);
                                    dataMap.put("dataFormat", "AE_BX");
                                    return analyzeData.parse(file.getPath(), AnalyzeData.DataType.AE_BX);
                                }
        return null;
    }

    private Object parseUHF(Map<String, Object> dataMap) throws Exception {
        String space = (String) dataMap.get("space");
        File file = (File) dataMap.get("file");
        DataDeviceDto dataDeviceDto = (DataDeviceDto) dataMap.get("dataDeviceDto");
        DataDeviceSiteDto dataDeviceSiteDto = (DataDeviceSiteDto) dataMap.get("dataDeviceSiteDto");
        //UHF_TJZS_1:
        //	同时保存噪声数据文件和界面的截屏图片，二者文件名相同；
        //	文件名：变电站_检测对象_TJZS_噪声序号_日期时间；
        //	噪声数据文件采用二进制编码，后缀为.dat，图片为JPG文件，噪声序号为0/1/2，共3个。如110kV夏河变_10kV开关柜_TJZS_0_20171112121212.dat。
        if (file.getPath().indexOf("_TJZS_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
            if (file.getPath().indexOf("_TJZS_0_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//            log.debug(AnalyzeData.DataType.UHF_TJZS_1.name() + "1.0==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.UHF_TJZS_1));
                dataMap.put("orderNo", 0);
            } else if (file.getPath().indexOf("_TJZS_1_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//            log.debug(AnalyzeData.DataType.UHF_TJZS_1.name() + "1.1==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.UHF_TJZS_1));
                dataMap.put("orderNo", 1);
            } else if (file.getPath().indexOf("_TJZS_2_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//            log.debug(AnalyzeData.DataType.UHF_TJZS_1.name() + "1.2==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.UHF_TJZS_1));
                dataMap.put("orderNo", 2);
            }
            dataMap.put("imageAmount", 1);
            //判断图谱类型 PRPS:0x01,PRPD:0x00
            DataInputStream dis=new DataInputStream(new FileInputStream(file));
            byte[] bytes = new byte[1];
            dis.skip(8);
            dis.read(bytes);
            int x3type = ByteConverterUtil.byte2int(bytes);
            if (x3type == 1) {
                DataUhfTjzs1Dto dataUhfTjzs1Dto = (DataUhfTjzs1Dto) analyzeData.parse(file.getPath(), AnalyzeData.DataType.UHF_TJZS_1);
                dataMap.put("dataFormat", "UHF_TJZS_1");
                return dataUhfTjzs1Dto;
            } else {
                DataUhfTjzs2Dto dataUhfTjzs2Dto = (DataUhfTjzs2Dto) analyzeData.parse(file.getPath(), AnalyzeData.DataType.UHF_TJZS_2);
                dataMap.put("dataFormat", "UHF_TJZS_2");
                return dataUhfTjzs2Dto;
            }
        } else
            //UHF_TJ_1:
            //文件采用扩展名为.dat的二进制数据格式进行存储，每个.dat文件存储一张PRPS图谱的数据。
            //文件名规则为：仪器厂家及型号编码_图谱类型_图谱生成时间.dat。仪器厂家及型号编码为数字、字母或两者的组合，最多为32个字符。图谱类型： PRPS图谱为 1。图谱生成日期格式为：YYYYMMDDhhmmss。
            //仪器厂家及型号编码、图谱类型、图谱生成时间之间用下划线分隔。
            //示例：RedPhasePDT400_1_20140823185809.dat
            //每个.dat文件大小不超过500KB。
            if (file.getPath().indexOf("_1_") != -1 && "UHF".equalsIgnoreCase(getFileExt(file.getName()))) {
//                log.debug(AnalyzeData.DataType.UHF_TJ_1.name() + "==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.UHF_TJ_1));
                dataMap.put("imageAmount", 1);
                dataMap.put("dataFormat", "UHF_TJ_1");
                return analyzeData.parse(file.getPath(), AnalyzeData.DataType.UHF_TJ_1);
            } else
                //UHF_TJLB_1:
                //文件采用扩展名为.TJLB的二进制数据格式进行存储，每个.TJLB文件存储录波数据。
                //文件名规则为：仪器厂家及型号编码_图谱类型_图谱生成时间. TJLB。仪器厂家及型号编码为数字、字母或两者的组合，最多为32个字符。图谱类型： PRPS图谱为 1。图谱生成日期格式为：YYYYMMDDhhmmss。
                //仪器厂家及型号编码、图谱类型、图谱生成时间之间用下划线分隔。
                //录波数据文件的文件名与统计数据文件名相同。
                //示例：RedPhasePDT400_1_20140823185809. TJLB
                if (file.getPath().indexOf("_1_") != -1 && "TJLB".equalsIgnoreCase(getFileExt(file.getName()))) {
//                    log.debug(AnalyzeData.DataType.UHF_TJLB_1.name() + "==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.UHF_TJLB_1));
                    dataMap.put("dataFormat", "UHF_TJLB_1");
                    return analyzeData.parse(file.getPath(), AnalyzeData.DataType.UHF_TJLB_1);
                } else
//                    //UHF_TJZS_2:
//                    //	同时保存噪声数据文件和界面的截屏图片，二者文件名相同；
//                    //	文件名：变电站_检测对象_TJZS_噪声序号_日期时间；
//                    //	噪声数据文件采用二进制编码，后缀为.dat，图片为JPG文件，噪声序号为0/1/2，共3个。如110kV夏河变_10kV开关柜_TJZS_0_20171112121212.dat。
//                    if (file.getPath().indexOf("_TJZS_0_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//                        if (file.getPath().indexOf("_TJZS_0_0_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
////                        log.debug(AnalyzeData.DataType.UHF_TJZS_2.name() + "2.0==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.UHF_TJZS_2));
//                            dataMap.put("orderNo", 0);
//                        } else if (file.getPath().indexOf("_TJZS_0_1_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
////                        log.debug(AnalyzeData.DataType.UHF_TJZS_2.name() + "2.1==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.UHF_TJZS_2));
//                            dataMap.put("orderNo", 1);
//                        } else if (file.getPath().indexOf("_TJZS_0_2_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
////                        log.debug(AnalyzeData.DataType.UHF_TJZS_2.name() + "2.2==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.UHF_TJZS_2));
//                            dataMap.put("orderNo", 2);
//                        }
//                        dataMap.put("imageAmount", 1);
//                        dataMap.put("dataFormat", "UHF_TJZS_2");
//                        return analyzeData.parse(file.getPath(), AnalyzeData.DataType.UHF_TJZS_2);
//                    } else
                    //UHF_TJ_2:
                    //文件采用扩展名为.dat的二进制数据格式进行存储，每个.dat文件存储一张PRPS图谱的数据。
                    //文件名规则为：仪器厂家及型号编码_图谱类型_图谱生成时间.dat。仪器厂家及型号编码为数字、字母或两者的组合，最多为32个字符。图谱类型： PRPD图谱为 0。图谱生成日期格式为：YYYYMMDDhhmmss。
                    //仪器厂家及型号编码、图谱类型、图谱生成时间之间用下划线分隔。
                    //示例：RedPhasePDT400_0_20140823185809.dat
                    //每个.dat文件大小不超过500KB。
                    if (file.getPath().indexOf("_0_") != -1 && "UHF".equalsIgnoreCase(getFileExt(file.getName()))) {
//                            log.debug(AnalyzeData.DataType.UHF_TJ_2.name() + "==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.UHF_TJ_2));
                        dataMap.put("imageAmount", 1);
                        dataMap.put("dataFormat", "UHF_TJ_2");
                        return analyzeData.parse(file.getPath(), AnalyzeData.DataType.UHF_TJ_2);
                    } else
                        //UHF_TJLB_2:
                        //文件采用扩展名为.TJLB的二进制数据格式进行存储，每个.TJLB文件存储录波数据。
                        //文件名规则为：仪器厂家及型号编码_图谱类型_图谱生成时间. TJLB。仪器厂家及型号编码为数字、字母或两者的组合，最多为32个字符。图谱类型： PRPD图谱为 0。图谱生成日期格式为：YYYYMMDDhhmmss。
                        //仪器厂家及型号编码、图谱类型、图谱生成时间之间用下划线分隔。
                        //录波数据文件的文件名与统计数据文件名相同。
                        //示例：RedPhasePDT400_0_20140823185809. TJLB
                        if (file.getPath().indexOf("_0_") != -1 && "TJLB".equalsIgnoreCase(getFileExt(file.getName()))) {
//                                log.debug(AnalyzeData.DataType.UHF_TJLB_1.name() + "==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.UHF_TJLB_2));
                            dataMap.put("dataFormat", "UHF_TJLB_2");
                            return analyzeData.parse(file.getPath(), AnalyzeData.DataType.UHF_TJLB_2);
                        }
        return null;

    }

    private Object parseLC(Map<String, Object> dataMap) throws Exception {
//        String space = (String) dataMap.get("space");
        File file = (File) dataMap.get("file");
//        DataDeviceDto dataDeviceDto = (DataDeviceDto) dataMap.get("dataDeviceDto");
//        DataDeviceSiteDto dataDeviceSiteDto = (DataDeviceSiteDto) dataMap.get("dataDeviceSiteDto");
        //LC:
        //	在保存界面中，设计一个文本框，用于人工输入负荷电流，浮点数，一位小数，单位A，用户点击文本框旁边的确定按钮，则自动保存一个负荷电流数据文件。
        //	对于地电波检测技术，该文件保存于第三级目录下，即该负荷电流用于设备一级。负荷电流文件名格式——被测设备名称_LC_日期，其中，LC为负荷电流。时间格式——YYYYMMDD。文件采用二进制编码，后缀主要为.dat。每个设备只有一个负荷电流文件，新数据的覆盖文件的旧数据。
        //	对于其他检测技术，该文件保存于第四级目录下，即该负荷电流用于设备检测位置一级；负荷电流文件名格式——被测设备名称_被测部位+测试点_LC_日期，其中，LC为负荷电流。时间格式——YYYYMMDD。文件采用二进制编码，后缀主要为.dat。设备的每个检测位置只有一个负荷电流文件，新数据的覆盖文件的旧数据。
        //	负荷电流可以为空，即用户不保存负荷电流文件。
        if (file.getPath().indexOf("_LC_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//            log.debug(AnalyzeData.DataType.LC.name() + "==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.LC));
            dataMap.put("dataFormat", "LC");
            return analyzeData.parse(file.getPath(), AnalyzeData.DataType.LC);
        }
        return null;
    }

    private Object parseHJ(Map<String, Object> dataMap) throws Exception {
//        String space = (String) dataMap.get("space");
        File file = (File) dataMap.get("file");
//        DataDeviceDto dataDeviceDto = (DataDeviceDto) dataMap.get("dataDeviceDto");
//        DataDeviceSiteDto dataDeviceSiteDto = (DataDeviceSiteDto) dataMap.get("dataDeviceSiteDto");
        //HJ:
        //	在台账选择界面中，选择台账和确定环境信息后，除了需要创建二级目录以外，还需要生成一个环境信息数据文件，保存包括天气、温度和湿度，检测人员等信息。
        //	文件名格式——变电站_检测对象_HJ_日期时间，采用二进制文件，后缀为.dat，110kV夏河变_10kV开关柜_HJ_20171112121212.dat
        if (file.getPath().indexOf("_HJ_") != -1 && "dat".equalsIgnoreCase(getFileExt(file.getName()))) {
//            log.debug(AnalyzeData.DataType.HJ.name() + "==>{}", analyzeData.parse(file.getPath(), AnalyzeData.DataType.HJ));
            dataMap.put("dataFormat", "HJ");
            return analyzeData.parse(file.getPath(), AnalyzeData.DataType.HJ);
        }
        return null;
    }

    public <T> T copyTo(Object obj, Class<T> toObj) throws Exception {
        if (obj == null) {
            return null;
        }
        return ObjectCopy.copyTo(obj, toObj);
    }

    public <T> List<T> copyTo(List from, Class<T> to) {
        if (from == null) {
            return null;
        }
        return ObjectCopy.copyTo(from, to);
    }

    /**
     * 获取文件后缀
     */
    public String getFileExt(String fileName) {
        int pos = fileName.lastIndexOf(".");
        if (pos > -1) {
            String temp = fileName.substring(pos + 1).toLowerCase();
            pos = temp.indexOf("?");
            if (pos > -1) {
                return temp.substring(0, pos);
            }
            return temp.trim().replaceAll("\"", "");
        }
        return "";
    }

//    public static void main(String[] args) throws Exception {
//        File file = new File("D:\\PDT840\\gzip\\UHF\\车间实验室_220kV组合电器_UHF_20180926\\#001号组合电器\\车间实验室_220kV组合电器_TJZS_0_20180926170236.dat");
//        //判断图谱类型 PRPS:0x01,PRPD:0x00
//        DataInputStream dis=new DataInputStream(new FileInputStream(file));
//        byte[] typeBuffer = new byte[1];
//        dis.skip(8);
//        dis.read(typeBuffer);
//        int x3type = ByteConverterUtil.byte2int(typeBuffer);
//        if (x3type == 1) {
//            DataUhfTjzs1Dto dataUhfTjzs1Dto = (DataUhfTjzs1Dto) new AnalyzeData().parse(file.getPath(), AnalyzeData.DataType.UHF_TJZS_1);
//            System.out.println(dataUhfTjzs1Dto);
//        } else {
//            DataUhfTjzs2Dto dataUhfTjzs2Dto = (DataUhfTjzs2Dto) new AnalyzeData().parse(file.getPath(), AnalyzeData.DataType.UHF_TJZS_2);
//            System.out.println(dataUhfTjzs2Dto);
//        }
//    }
}
