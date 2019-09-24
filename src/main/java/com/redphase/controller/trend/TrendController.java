package com.redphase.controller.trend;

import com.Application;
import com.redphase.controller.atlas.AtlasBaseController;
import com.redphase.dto.atlas.DataAnalyzeDto;
import com.redphase.dto.atlas.DataAnalyzeTableDto;
import com.redphase.dto.atlas.DataDeviceDto;
import com.redphase.dto.atlas.DataDeviceSiteDto;
import com.redphase.dto.atlas.aa.DataAaTjDto;
import com.redphase.dto.atlas.aa.DataAaXjDto;
import com.redphase.dto.atlas.ae.DataAeTjDto;
import com.redphase.dto.atlas.ae.DataAeXjDto;
import com.redphase.dto.atlas.hfct.DataHfctTjDto;
import com.redphase.dto.atlas.hfct.DataHfctXjDto;
import com.redphase.dto.atlas.tev.DataTevTjDto;
import com.redphase.dto.atlas.tev.DataTevXjDto;
import com.redphase.dto.atlas.uhf.DataUhfTj1Dto;
import com.redphase.dto.atlas.uhf.DataUhfTj2Dto;
import com.redphase.framework.util.DateUtil;
import com.redphase.framework.util.I18nUtil;
import com.redphase.framework.util.ValidatorUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@FXMLController
@Slf4j
public class TrendController extends AtlasBaseController implements Initializable {


    @FXML
    private ChoiceBox siteChoiceBox;
    @FXML
    private ChoiceBox equipmentChoiceBox;
    @FXML
    private ChoiceBox technologyChoiceBox;
    @FXML
    private ChoiceBox positionChoiceBox;
    @FXML
    private ChoiceBox typeChoiceBox;
    @FXML
    private CheckBox selectAll;
    @FXML
    private TableView fileNameTable;
    @FXML
    private TableColumn<DataAnalyzeTableDto, CheckBox> selectedUrlTableColumn;   //复选框
    @FXML
    private TableColumn<DataAnalyzeTableDto, String> fileNameTableColumn;   //文件名称
    /*
        @FXML
        private LineChart<String, Number> lineChart;
    */
    @FXML
    private Pane lineChartPane;
    @FXML
    private Button refreshButton;

    private Map<String, String> typeMap = new HashMap<String, String>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileNameTable.prefHeightProperty().bind(Application.getStage().heightProperty().subtract(470.0));
        refresh();
    }

    @FXML
    public void refresh() {
        refreshButton.setDisable(true);
        init();
        refreshButton.setDisable(false);
    }


    public void init() {
        try {
            //添加事件处理
            addEvent();
            //装载数据
            loadParamData();
//            searchFile();
            selectedUrlTableColumn.setCellValueFactory(arg0 -> {
                DataAnalyzeTableDto user = arg0.getValue();
                CheckBox checkBox = new CheckBox();
                checkBox.selectedProperty().setValue(user.isSelected());
                checkBox.selectedProperty().addListener((ov, old_val, new_val) -> user.setSelected(new_val));
                return new SimpleObjectProperty<>(checkBox);
            });
            fileNameTableColumn.setCellFactory((col) -> {
                TableCell<DataAnalyzeTableDto, String> cell = new TableCell<DataAnalyzeTableDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            DataAnalyzeTableDto dataAnalyzeTableDto = this.getTableView().getItems().get(this.getIndex());
                            this.setText(dataAnalyzeTableDto.getFileName());
                            this.setTooltip(new Tooltip(dataAnalyzeTableDto.getFileName()));
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

    /**
     * 装载查询参数数据
     *
     * @throws Exception
     */
    private void loadParamData() throws Exception {
        siteChoiceBox.setItems(FXCollections.observableArrayList(new ArrayList() {{
            add("--请选择--");
            addAll(dataDeviceService.findSubstationIsList());
        }}));
        siteChoiceBox.getSelectionModel().select(0);
//        equipmentChoiceBox.setItems(FXCollections.observableArrayList(dataDeviceService.findDeviceNameIsList()));equipmentChoiceBox.getSelectionModel().select(0);
//        technologyChoiceBox.setItems(FXCollections.observableArrayList("AA", "AE", "HFCT", "UHF", "TEV"));technologyChoiceBox.getSelectionModel().select(0);
//        positionChoiceBox.setItems(FXCollections.observableArrayList(dataDeviceSiteService.findSiteNameIsList()));positionChoiceBox.getSelectionModel().select(0);
    }

    /**
     * 查询文件
     */
    private void searchFile() {
        try {
            String typeValue = "" + typeChoiceBox.getValue();
            if (ValidatorUtil.isEmpty(typeValue) || "--请选择--".equals(typeValue)) {
                return;
            }
            Map<String, String> map = new HashMap<>();
            map.put("substations", (String) siteChoiceBox.getValue());
            map.put("deviceName", (String) equipmentChoiceBox.getValue());
            map.put("technology", (String) technologyChoiceBox.getValue());
            map.put("type", typeMap.get(typeValue));
            map.put("position", (String) positionChoiceBox.getValue());

            List<DataAnalyzeDto> dtos = dataAnalyzeService.thendSearch(map);
            List<DataAnalyzeTableDto> tableDtos = new ArrayList<>();
            for (DataAnalyzeDto analyzeDto : dtos) {
                DataAnalyzeTableDto dto = new DataAnalyzeTableDto(false, analyzeDto.getFileName(), analyzeDto.getDataFormat(), analyzeDto.getId().toString());
                tableDtos.add(dto);
            }
            ObservableList<DataAnalyzeTableDto> data = FXCollections.observableArrayList(tableDtos);
            fileNameTable.getItems().clear();
            fileNameTable.setItems(data);
            fileNameTable.refresh();
        } catch (Exception e) {
            log.error("系统异常!", e);
            ialert.error(I18nUtil.get("error.sys") + e.getMessage());
        }
    }

    Map<String, DataDeviceDto> deviceNameMap = new HashMap<>();

    /**
     * 添加下拉控件的事件
     */
    private void addEvent() {
        try {
            siteChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    if (!newValue.equals(oldValue)) {
                        selectAll.setSelected(false);
                    }
                    if ("--请选择--".equals(newValue)) {
                        equipmentChoiceBox.setItems(FXCollections.observableArrayList(new ArrayList<String>() {{
                            add("--请选择--");
                        }}));
                        equipmentChoiceBox.getSelectionModel().select(0);
                        technologyChoiceBox.setItems(FXCollections.observableArrayList(new ArrayList<String>() {{
                            add("--请选择--");
                        }}));
                        technologyChoiceBox.getSelectionModel().select(0);
                        positionChoiceBox.setItems(FXCollections.observableArrayList(new ArrayList<String>() {{
                            add("--请选择--");
                        }}));
                        positionChoiceBox.getSelectionModel().select(0);
                        typeChoiceBox.setItems(FXCollections.observableArrayList(new ArrayList<String>() {{
                            add("--请选择--");
                        }}));
                        typeChoiceBox.getSelectionModel().select(0);
                        fileNameTable.getItems().clear();
                        return;
                    }
                    List<String> data = new ArrayList<>();
                    if (newValue != null) {
                        List<DataDeviceDto> deviceDtos = dataDeviceService.findDeviceNameIsList(new DataDeviceDto() {{
                            setSubstation("" + newValue);
                        }});
                        deviceNameMap = new HashMap<>();
                        if (deviceDtos != null) {
                            for (DataDeviceDto deviceDto : deviceDtos) {
                                data.add(deviceDto.getDeviceName());
                                deviceNameMap.put(deviceDto.getDeviceName(), deviceDto);
                            }
                        }
                    }
                    equipmentChoiceBox.setItems(FXCollections.observableArrayList(new ArrayList<String>() {{
                        add("--请选择--");
                        addAll(data);
                    }}));
                } catch (Exception e) {
                }
                equipmentChoiceBox.getSelectionModel().select(0);
//                searchFile();
            });
            equipmentChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    if (oldValue == null || newValue == null) {
                        return;
                    }
                    if (!newValue.equals(oldValue)) {
                        selectAll.setSelected(false);
                    }
                    technologyChoiceBox.setItems(FXCollections.observableArrayList(new ArrayList<String>() {{
                        add("--请选择--");
                        if (siteChoiceBox != null && siteChoiceBox.getValue() != null && newValue != null) {
                            addAll(dataDeviceService.findTestTechnologyIsList(new DataDeviceDto() {{
                                setSubstation("" + siteChoiceBox.getValue());
                                setDeviceName("" + newValue);
                            }}));
                        }
                    }}));
                } catch (Exception e) {
                }
                technologyChoiceBox.getSelectionModel().select(0);
//                searchFile();
            });
            technologyChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (oldValue == null || newValue == null) {
                    return;
                }
                if (!newValue.equals(oldValue)) {
                    selectAll.setSelected(false);
                }
                switch ("" + newValue) {
                    case "AA": {
                        typeMap = new HashMap<String, String>() {{
                            put("空气式超声波_巡检", "AA_XJ");
                            put("空气式超声波_统计数据", "AA_TJ");
                        }};
                        break;
                    }
                    case "AE": {
                        typeMap = new HashMap<String, String>() {{
                            put("接触式超声波_巡检", "AE_XJ");
                            put("接触式超声波_统计数据", "AE_TJ");
                        }};
                        break;
                    }
                    case "HFCT": {
                        typeMap = new HashMap<String, String>() {{
                            put("高频电流_巡检", "HFCT_XJ");
                            put("高频电流_统计数据", "HFCT_TJ");
                        }};
                        break;
                    }
                    case "UHF": {
                        typeMap = new HashMap<String, String>() {{
                            put("特高频_模式1_统计数据", "UHF_TJ_1");
                            put("特高频_模式2_统计数据", "UHF_TJ_2");
                        }};
                        break;
                    }
                    case "TEV": {
                        typeMap = new HashMap<String, String>() {{
                            put("地电波_巡检", "TEV_XJ");
                            put("地电波_统计数据", "TEV_TJ");
                        }};
                        break;
                    }
                }
                typeChoiceBox.setItems(FXCollections.observableArrayList(new ArrayList<String>() {{
                    add("--请选择--");
                    addAll(typeMap.keySet());
                }}));
                typeChoiceBox.getSelectionModel().select(0);
                try {
                    DataDeviceDto deviceDto = deviceNameMap.get("" + equipmentChoiceBox.getValue());
                    if (deviceDto == null) {
                        return;
                    }
                    positionChoiceBox.setItems(FXCollections.observableArrayList(new ArrayList<String>() {{
                        add("--请选择--");
                        addAll(dataDeviceSiteService.findSiteNameIsList(new DataDeviceSiteDto() {{
                            setDataDeviceId(deviceDto.getId());
                        }}));
                    }}));
                } catch (Exception e) {
                }
                positionChoiceBox.getSelectionModel().select(0);
                if (!"--请选择--".equals(newValue)) {
                    searchFile();
                    return;
                }
            });
            positionChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (oldValue == null || newValue == null) {
                    return;
                }
                if (!newValue.equals(oldValue)) {
                    selectAll.setSelected(false);
                }
                if (!"--请选择--".equals(newValue)) {
                    searchFile();
                    return;
                }
            });
            typeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (oldValue == null || newValue == null) {
                    return;
                }
                if (!newValue.equals(oldValue)) {
                    selectAll.setSelected(false);
                }
                if (!"--请选择--".equals(newValue)) {
                    searchFile();
                    return;
                }
            });
            fileNameTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            fileNameTable.getSelectionModel().selectedItemProperty().addListener((ov, oldValue, newValue) -> {
                if (oldValue == null || newValue == null) {
                    return;
                }
                DataAnalyzeTableDto tableDto = (DataAnalyzeTableDto) newValue;
                if (tableDto.isSelected()) tableDto.setSelected(false);
                else tableDto.setSelected(true);
                fileNameTable.refresh();
            });
            selectAll.selectedProperty().addListener((ov, oldValue, newValue) -> {
                if (oldValue == null || newValue == null) {
                    return;
                }
                ObservableList<DataAnalyzeTableDto> tableDtos = fileNameTable.getItems();
                if (selectAll.isSelected()) {
                    for (DataAnalyzeTableDto tableDto : tableDtos) {
                        tableDto.setSelected(true);
                    }
                } else {
                    for (DataAnalyzeTableDto tableDto : tableDtos) {
                        tableDto.setSelected(false);
                    }
                }
                fileNameTable.refresh();
            });
        } catch (Exception e) {
            log.error("xxxxxxx", e);
        }
    }

    boolean isAnalyze = false;//已分析标记

    @FXML
    public void analyze() {
        try {
            if (null == siteChoiceBox.getValue() || "--请选择--".equals(siteChoiceBox.getValue())
                    || null == equipmentChoiceBox.getValue()
                    || null == technologyChoiceBox.getValue()
                    || null == positionChoiceBox.getValue()
                    || null == typeChoiceBox.getValue()
            ) {
                ialert.alert("请选择具体台帐的检测文件(筛选下拉框全部必填)!");
                return;
            }
            final CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel("采样时间");
            xAxis.setGapStartAndEnd(false);
            final NumberAxis yAxis = new NumberAxis();
//            yAxis.setLabel("幅值");
            yAxis.setForceZeroInRange(false);
            LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis);
            lineChart.setCreateSymbols(false);
            lineChart.setLayoutX(5.0);
            lineChart.prefWidthProperty().bind(Application.getStage().widthProperty().subtract(490.0));
            lineChart.prefHeightProperty().bind(Application.getStage().heightProperty().subtract(220.0));
            XYChart.Series dataSeries = new XYChart.Series();
            dataSeries.setName("采集值");

            XYChart.Series maxSeries = new XYChart.Series();
            maxSeries.setName("最大值");

            XYChart.Series minSeries = new XYChart.Series();
            minSeries.setName("最小值");

            XYChart.Series avgSeries = new XYChart.Series();
            avgSeries.setName("平均值");

            ObservableList<DataAnalyzeTableDto> tableDtos = fileNameTable.getItems();
            StringBuffer idsStringBuffer = new StringBuffer();
            String dataFormat = "";
            for (DataAnalyzeTableDto tableDto : tableDtos) {
                if (tableDto.isSelected()) {
                    idsStringBuffer.append(tableDto.getDataAnalyzeId());
                    idsStringBuffer.append(",");
                    dataFormat = tableDto.getDataFormat();
                }
            }
            if (ValidatorUtil.isEmpty(idsStringBuffer.toString())) {
                ialert.error("请选择具体数据文件!");
                return;
            }
            String ids = idsStringBuffer.substring(0, idsStringBuffer.length() - 1);
            Float max, min;
            Double avg;
            switch (dataFormat) {
                case "TEV_XJ":
                    List<DataTevXjDto> dataTevXjDtos = dataTevXjService.findDataByIds(ids);
                    if (dataTevXjDtos != null && dataTevXjDtos.size() > 0) {
                        max = dataTevXjDtos.parallelStream().max(Comparator.comparing(DataTevXjDto::getX5)).get().getX5();
                        min = dataTevXjDtos.parallelStream().min(Comparator.comparing(DataTevXjDto::getX5)).get().getX5();
                        avg = dataTevXjDtos.stream().collect(Collectors.averagingDouble(DataTevXjDto::getX5));
                        for (DataTevXjDto dataHfctXjDto : dataTevXjDtos) {
                            String dateStr = DateUtil.getDateTimeStr(parseDate(dataHfctXjDto.getX3().toString()));
                            dataSeries.getData().add(new XYChart.Data(dateStr, dataHfctXjDto.getX5()));
                            maxSeries.getData().add(new XYChart.Data(dateStr, max));
                            minSeries.getData().add(new XYChart.Data(dateStr, min));
                            avgSeries.getData().add(new XYChart.Data(dateStr, avg));
                        }
                    }
                    break;
                case "TEV_TJ":
                    List<DataTevTjDto> dataTevTjDtos = dataTevTjService.findDataByIds(ids);
                    if (dataTevTjDtos != null && dataTevTjDtos.size() > 0) {
                        max = dataTevTjDtos.parallelStream().max(Comparator.comparing(DataTevTjDto::getX16)).get().getX16();
                        min = dataTevTjDtos.parallelStream().min(Comparator.comparing(DataTevTjDto::getX16)).get().getX16();
                        avg = dataTevTjDtos.stream().collect(Collectors.averagingDouble(DataTevTjDto::getX16));
                        for (DataTevTjDto dataTevTjDto : dataTevTjDtos) {
                            String dateStr = DateUtil.getDateTimeStr(parseDate(dataTevTjDto.getX14().toString()));
                            dataSeries.getData().add(new XYChart.Data(dateStr, dataTevTjDto.getX16()));
                            maxSeries.getData().add(new XYChart.Data(dateStr, max));
                            minSeries.getData().add(new XYChart.Data(dateStr, min));
                            avgSeries.getData().add(new XYChart.Data(dateStr, avg));
                        }
                    }
                    break;
                case "AA_XJ":
                    List<DataAaXjDto> dataAavXjDtos = dataAaXjService.findDataByIds(ids);
                    if (dataAavXjDtos != null && dataAavXjDtos.size() > 0) {
                        max = dataAavXjDtos.parallelStream().max(Comparator.comparing(DataAaXjDto::getX5)).get().getX5();
                        min = dataAavXjDtos.parallelStream().min(Comparator.comparing(DataAaXjDto::getX5)).get().getX5();
                        avg = dataAavXjDtos.stream().collect(Collectors.averagingDouble(DataAaXjDto::getX5));
                        for (DataAaXjDto dataAaXjDto : dataAavXjDtos) {
                            String dateStr = DateUtil.getDateTimeStr(parseDate(dataAaXjDto.getX3().toString()));
                            dataSeries.getData().add(new XYChart.Data(dateStr, dataAaXjDto.getX5()));
                            maxSeries.getData().add(new XYChart.Data(dateStr, max));
                            minSeries.getData().add(new XYChart.Data(dateStr, min));
                            avgSeries.getData().add(new XYChart.Data(dateStr, avg));
                        }
                    }
                    break;
                case "AA_TJ":
                    List<DataAaTjDto> dataAaTjDtos = dataAaTjService.findDataByIds(ids);
                    if (dataAaTjDtos != null && dataAaTjDtos.size() > 0) {
                        max = dataAaTjDtos.parallelStream().max(Comparator.comparing(DataAaTjDto::getX16)).get().getX16();
                        min = dataAaTjDtos.parallelStream().min(Comparator.comparing(DataAaTjDto::getX16)).get().getX16();
                        avg = dataAaTjDtos.stream().collect(Collectors.averagingDouble(DataAaTjDto::getX16));
                        for (DataAaTjDto dataAaTjDto : dataAaTjDtos) {
                            String dateStr = DateUtil.getDateTimeStr(parseDate(dataAaTjDto.getX14().toString()));
                            dataSeries.getData().add(new XYChart.Data(dateStr, dataAaTjDto.getX16()));
                            maxSeries.getData().add(new XYChart.Data(dateStr, max));
                            minSeries.getData().add(new XYChart.Data(dateStr, min));
                            avgSeries.getData().add(new XYChart.Data(dateStr, avg));
                        }
                    }
                    break;
                case "HFCT_XJ":
                    List<DataHfctXjDto> dataHfctXjDtos = dataHfctXjService.findDataByIds(ids);
                    if (dataHfctXjDtos != null && dataHfctXjDtos.size() > 0) {
                        max = dataHfctXjDtos.parallelStream().max(Comparator.comparing(DataHfctXjDto::getX5)).get().getX5();
                        min = dataHfctXjDtos.parallelStream().min(Comparator.comparing(DataHfctXjDto::getX5)).get().getX5();
                        avg = dataHfctXjDtos.stream().collect(Collectors.averagingDouble(DataHfctXjDto::getX5));
                        for (DataHfctXjDto dataHfctXjDto : dataHfctXjDtos) {
                            String dateStr = DateUtil.getDateTimeStr(parseDate(dataHfctXjDto.getX3().toString()));
                            dataSeries.getData().add(new XYChart.Data(dateStr, dataHfctXjDto.getX5()));
                            maxSeries.getData().add(new XYChart.Data(dateStr, max));
                            minSeries.getData().add(new XYChart.Data(dateStr, min));
                            avgSeries.getData().add(new XYChart.Data(dateStr, avg));
                        }
                    }
                    break;
                case "HFCT_TJ":
                    List<DataHfctTjDto> dataHfctTjDtos = dataHfctTjService.findDataByIds(ids);
                    if (dataHfctTjDtos != null && dataHfctTjDtos.size() > 0) {
                        max = dataHfctTjDtos.parallelStream().max(Comparator.comparing(DataHfctTjDto::getX16)).get().getX16();
                        min = dataHfctTjDtos.parallelStream().min(Comparator.comparing(DataHfctTjDto::getX16)).get().getX16();
                        avg = dataHfctTjDtos.stream().collect(Collectors.averagingDouble(DataHfctTjDto::getX16));
                        for (DataHfctTjDto dataHfctTjDto : dataHfctTjDtos) {
                            String dateStr = DateUtil.getDateTimeStr(parseDate(dataHfctTjDto.getX14().toString()));
                            dataSeries.getData().add(new XYChart.Data(dateStr, dataHfctTjDto.getX16()));
                            maxSeries.getData().add(new XYChart.Data(dateStr, max));
                            minSeries.getData().add(new XYChart.Data(dateStr, min));
                            avgSeries.getData().add(new XYChart.Data(dateStr, avg));
                        }
                    }
                    break;
                case "AE_XJ":
                    List<DataAeXjDto> dataAeXjDtos = dataAeXjService.findDataByIds(ids);
                    if (dataAeXjDtos != null && dataAeXjDtos.size() > 0) {
                        max = dataAeXjDtos.parallelStream().max(Comparator.comparing(DataAeXjDto::getX5)).get().getX5();
                        min = dataAeXjDtos.parallelStream().min(Comparator.comparing(DataAeXjDto::getX5)).get().getX5();
                        avg = dataAeXjDtos.stream().collect(Collectors.averagingDouble(DataAeXjDto::getX5));
                        for (DataAeXjDto dataAeXjDto : dataAeXjDtos) {
                            String dateStr = DateUtil.getDateTimeStr(parseDate(dataAeXjDto.getX3().toString()));
                            dataSeries.getData().add(new XYChart.Data(dateStr, dataAeXjDto.getX5()));
                            maxSeries.getData().add(new XYChart.Data(dateStr, max));
                            minSeries.getData().add(new XYChart.Data(dateStr, min));
                            avgSeries.getData().add(new XYChart.Data(dateStr, avg));
                        }
                    }
                    break;
                case "AE_TJ":
                    List<DataAeTjDto> dataAeTjDtos = dataAeTjService.findDataByIds(ids);
                    if (dataAeTjDtos != null && dataAeTjDtos.size() > 0) {
                        max = dataAeTjDtos.parallelStream().max(Comparator.comparing(DataAeTjDto::getX16)).get().getX16();
                        min = dataAeTjDtos.parallelStream().min(Comparator.comparing(DataAeTjDto::getX16)).get().getX16();
                        avg = dataAeTjDtos.stream().collect(Collectors.averagingDouble(DataAeTjDto::getX16));
                        for (DataAeTjDto dataAeTjDto : dataAeTjDtos) {
                            String dateStr = DateUtil.getDateTimeStr(parseDate(dataAeTjDto.getX14().toString()));
                            dataSeries.getData().add(new XYChart.Data(dateStr, dataAeTjDto.getX16()));
                            maxSeries.getData().add(new XYChart.Data(dateStr, max));
                            minSeries.getData().add(new XYChart.Data(dateStr, min));
                            avgSeries.getData().add(new XYChart.Data(dateStr, avg));
                        }
                    }
                    break;
                case "UHF_TJ_1":
                    List<DataUhfTj1Dto> dataUhfTj1Dtos = dataUhfTj1Service.findDataByIds(ids);
                    if (dataUhfTj1Dtos != null && dataUhfTj1Dtos.size() > 0) {
                        max = dataUhfTj1Dtos.parallelStream().max(Comparator.comparing(DataUhfTj1Dto::getX16)).get().getX16();
                        min = dataUhfTj1Dtos.parallelStream().min(Comparator.comparing(DataUhfTj1Dto::getX16)).get().getX16();
                        avg = dataUhfTj1Dtos.stream().collect(Collectors.averagingDouble(DataUhfTj1Dto::getX16));
                        for (DataUhfTj1Dto dataUhfTj1Dto : dataUhfTj1Dtos) {
                            String dateStr = DateUtil.getDateTimeStr(parseDate(dataUhfTj1Dto.getX14().toString()));
                            dataSeries.getData().add(new XYChart.Data(dateStr, dataUhfTj1Dto.getX16()));
                            maxSeries.getData().add(new XYChart.Data(dateStr, max));
                            minSeries.getData().add(new XYChart.Data(dateStr, min));
                            avgSeries.getData().add(new XYChart.Data(dateStr, avg));
                        }
                    }
                    break;
                case "UHF_TJ_2":
                    List<DataUhfTj2Dto> dataUhfTj2Dtos = dataUhfTj2Service.findDataByIds(ids);
                    if (dataUhfTj2Dtos != null && dataUhfTj2Dtos.size() > 0) {
                        max = dataUhfTj2Dtos.parallelStream().max(Comparator.comparing(DataUhfTj2Dto::getX16)).get().getX16();
                        min = dataUhfTj2Dtos.parallelStream().min(Comparator.comparing(DataUhfTj2Dto::getX16)).get().getX16();
                        avg = dataUhfTj2Dtos.stream().collect(Collectors.averagingDouble(DataUhfTj2Dto::getX16));
                        for (DataUhfTj2Dto dataUhfTj2Dto : dataUhfTj2Dtos) {
                            String dateStr = DateUtil.getDateTimeStr(parseDate(dataUhfTj2Dto.getX14().toString()));
                            dataSeries.getData().add(new XYChart.Data(dateStr, dataUhfTj2Dto.getX16()));
                            maxSeries.getData().add(new XYChart.Data(dateStr, max));
                            minSeries.getData().add(new XYChart.Data(dateStr, min));
                            avgSeries.getData().add(new XYChart.Data(dateStr, avg));
                        }
                    }
                    break;
            }
            lineChart.getData().addAll(dataSeries, maxSeries, minSeries, avgSeries);
//            ObservableList<Node> children = lineChartPane.getChildren();
//            if (children.size() > 0) {
//                for (int i = 0; i < children.size(); i++) {
//                    children.remove(i);
//                }
//            }
            lineChartPane.getChildren().setAll(lineChart);
        } catch (Exception e) {
            log.error("系统异常!", e);
            ialert.error(I18nUtil.get("error.sys") + e.getMessage());
        }
        isAnalyze = true;
    }

    @FXML
    public void saveImages() {
        if (!isAnalyze || siteChoiceBox.getValue() == null) {
            ialert.error("请先分析数据!");
            return;
        }
        screenshot(lineChartPane, loadTitle());
    }

    @FXML
    public void savePDF() {
        if (!isAnalyze || siteChoiceBox.getValue() == null) {
            ialert.error("请先分析数据!");
            return;
        }
        createPdf(lineChartPane, loadTitle());
    }

    private String loadTitle() {
        String title = "";
        if (siteChoiceBox.getValue() != null) {
            title += "#" + siteChoiceBox.getValue();
        }
        if (equipmentChoiceBox.getValue() != null) {
            title += "#" + equipmentChoiceBox.getValue();
        }
        if (technologyChoiceBox.getValue() != null) {
            title += "#" + technologyChoiceBox.getValue();
        }
        if (typeChoiceBox.getValue() != null) {
            title += "#" + typeChoiceBox.getValue();
        }
        if (positionChoiceBox.getValue() != null) {
            title += "#" + positionChoiceBox.getValue();
        }
        return title;
    }

    @Override
    public void dispose() {

    }

    public Date parseDate(String s) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        try {
            return dateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}



