package com.redphase.controller.atlas;

import com.Application;
import com.redphase.controller.atlas.data.AtlasDataAAController;
import com.redphase.controller.atlas.data.AtlasDataSkillController;
import com.redphase.controller.atlas.data.AtlasDataTEVController;
import com.redphase.dto.SkillItemDto;
import com.redphase.dto.TreeItemDto;
import com.redphase.dto.ZTreeNodeDto;
import com.redphase.dto.atlas.*;
import com.redphase.dto.atlas.aa.*;
import com.redphase.dto.atlas.ae.*;
import com.redphase.dto.atlas.hfct.*;
import com.redphase.dto.atlas.tev.*;
import com.redphase.dto.atlas.uhf.*;
import com.redphase.framework.util.CommonConstant;
import com.redphase.framework.util.I18nUtil;
import com.redphase.framework.util.ValidatorUtil;
import com.redphase.view.atlas.AtlasInitView;
import com.redphase.view.atlas.data.AtlasDataAAView;
import com.redphase.view.atlas.data.AtlasDataSkillView;
import com.redphase.view.atlas.data.AtlasDataTEVView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

@FXMLController
@Slf4j
public class AtlasController extends AtlasBaseController implements Initializable {
    @FXML
    @Getter
    private TreeView accountTree;
    @FXML
    @Getter
    private AnchorPane baseAtlasBorderPane;
    @FXML
    @Getter
    private Pane rightAnchorPane;
    @Autowired
    private AtlasInitView atlasImportInitView;
    @Autowired
    private AtlasDataTEVView atlasDataTEVView;
    @Autowired
    private AtlasDataAAView atlasDataAAView;
    @Autowired
    private AtlasDataSkillView atlasDataSkillView;
    @Autowired
    private AtlasDataSkillController atlasDataSkillController;
    @Autowired
    private AtlasDataTEVController atlasDataTEVController;
    @Autowired
    private AtlasDataAAController atlasDataAAController;
    @FXML
    private Button searchButton;
    @FXML
    @Getter
    private Button refreshButton;
    @FXML
    @Getter
    private Button exportButton;

    @FXML
    TextField electricBureauText;//请输入电业局名称
    @FXML
    TextField substationText;//请输入变电站名称
    @FXML
    ChoiceBox deviceTypeChoiceBox;//
    @FXML
    ChoiceBox voltageLevelChoiceBox;//
    @FXML
    TextField deviceNameText;//设备名称
    @FXML
    ChoiceBox testTechnologyChoiceBox;//
    @FXML
    DatePicker produceDateDatePicker;//点击输入出厂日期
    @FXML
    DatePicker useDateDatePicker;//点击输入投运日期

    /**
     * 装载查询参数数据
     *
     * @throws Exception
     */
    private void loadSearchParamData() {
        try {
            deviceTypeChoiceBox.setItems(FXCollections.observableArrayList("请选择", "开关柜", "变压器", "组合电器", "电缆"));
            deviceTypeChoiceBox.getSelectionModel().select(0);
            testTechnologyChoiceBox.setItems(FXCollections.observableArrayList("请选择", "AA", "AE", "HFCT", "UHF", "TEV"));
            testTechnologyChoiceBox.getSelectionModel().select(0);
            voltageLevelChoiceBox.setItems(FXCollections.observableArrayList("请选择", "10kV", "35kV", "110kV", "220kV", "500kV"));
            voltageLevelChoiceBox.getSelectionModel().select(0);
        } catch (Exception e) {

        }
    }

    private boolean isCtrl = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize...");
        hideNodeView();
        closeSet.setOnMouseClicked(event -> hideNodeView());
        loadSearchParamData();
        Platform.runLater(() -> {
            treeSet.clear();
            loadAccTree();
            initRightAnchorPane();
        });
        mlayer.prefWidthProperty().bind(Application.getStage().widthProperty());
        mlayer.prefHeightProperty().bind(Application.getStage().heightProperty());
        mVBox.prefWidthProperty().bind(mlayer.prefWidthProperty().subtract(200));
        mVBox.prefHeightProperty().bind(mlayer.prefHeightProperty().subtract(150));
        mHBox.prefWidthProperty().bind(mlayer.prefWidthProperty());
        mHBox.prefHeightProperty().bind(mlayer.prefHeightProperty());
        if (jwtUtil.isPermitted("analyze:del")) {//管理员隐藏功能
            accountTree.setOnKeyPressed((event) -> {//任意按键按下时响应
                try {
                    if (event.getCode() == KeyCode.CONTROL) {
                        isCtrl = true;
                    }
                    if (isCtrl && event.getCode() == KeyCode.D) {
                        String fullName = (String) ehcacheUtil.getCache(CommonConstant.ATLAS_DATA_SKILL_FULL_NAME);
                        if (ialert.confirm("删除分析数据!此操作无法恢复!\n" + fullName)) {
                            List<DataDeviceDto> dataDeviceDtoList = dataDeviceService.findListByFullName(fullName);
                            if (dataDeviceDtoList != null) {
                                for (DataDeviceDto deviceDto : dataDeviceDtoList) {
                                    List<DataAnalyzeDto> dataAnalyzeDtoList = dataAnalyzeService.findDataIsList(new DataAnalyzeDto() {{
                                        setDataDeviceId(deviceDto.getId());
                                    }});
                                    for (DataAnalyzeDto dataAnalyzeDto : dataAnalyzeDtoList) {
                                        try {
                                            switch (dataAnalyzeDto.getDataFormat()) {
                                                case "TEV_XJZS": {//地电波_巡检噪声
                                                    dataTevXjzsService.deleteData(new DataTevXjzsDto() {{
                                                        setDataAnalyzeId(dataAnalyzeDto.getId());
                                                    }});
                                                    break;
                                                }
                                                case "TEV_XJ": {//地电波_巡检
                                                    dataTevXjService.deleteData(new DataTevXjDto() {{
                                                        setDataAnalyzeId(dataAnalyzeDto.getId());
                                                    }});
                                                    break;
                                                }
                                                case "TEV_XJL": {//地电波_联合巡检
                                                    dataTevXjlhService.deleteData(new DataTevXjlhDto() {{
                                                        setDataAnalyzeId(dataAnalyzeDto.getId());
                                                    }});
                                                    break;
                                                }
                                                case "TEV_TJZS": {//地电波_统计噪声
                                                    dataTevTjzsService.deleteData(new DataTevTjzsDto() {{
                                                        setDataAnalyzeId(dataAnalyzeDto.getId());
                                                    }});
                                                    break;
                                                }
                                                case "TEV_TJ": {//地电波_统计数据
                                                    dataTevTjService.deleteData(new DataTevTjDto() {{
                                                        setDataAnalyzeId(dataAnalyzeDto.getId());
                                                    }});
                                                    break;
                                                }
                                                case "TEV_TJLB": {//地电波_统计录波
                                                    dataTevTjlbService.deleteData(new DataTevTjlbDto() {{
                                                        setDataAnalyzeId(dataAnalyzeDto.getId());
                                                    }});
                                                    break;
                                                }
                                                case "AA_XJZS": {//空气式超声波_巡检噪声
                                                    dataAaXjzsService.deleteData(new DataAaXjzsDto() {{
                                                        setDataAnalyzeId(dataAnalyzeDto.getId());
                                                    }});
                                                    break;
                                                }
                                                case "AA_XJ": {//空气式超声波_巡检
                                                    dataAaXjService.deleteData(new DataAaXjDto() {{
                                                        setDataAnalyzeId(dataAnalyzeDto.getId());
                                                    }});
                                                    break;
                                                }
                                                case "AA_TJZS": {//空气式超声波_统计噪声
                                                    dataAaTjzsService.deleteData(new DataAaTjzsDto() {{
                                                        setDataAnalyzeId(dataAnalyzeDto.getId());
                                                    }});
                                                    break;
                                                }
                                                case "AA_TJ": {//空气式超声波_统计数据
                                                    dataAaTjService.deleteData(new DataAaTjDto() {{
                                                        setDataAnalyzeId(dataAnalyzeDto.getId());
                                                    }});
                                                    break;
                                                }
                                                case "AA_TJLB": {//空气式超声波_统计录波
                                                    dataAaTjlbService.deleteData(new DataAaTjlbDto() {{
                                                        setDataAnalyzeId(dataAnalyzeDto.getId());
                                                    }});
                                                    break;
                                                }
                                                case "AA_FX": {//接触式超声波_飞行图谱
                                                    dataAaFxService.deleteData(new DataAaFxDto() {{
                                                        setDataAnalyzeId(dataAnalyzeDto.getId());
                                                    }});
                                                    break;
                                                }
                                                case "AA_BX": {//接触式超声波_波形图谱
                                                    dataAaBxService.deleteData(new DataAaBxDto() {{
                                                        setDataAnalyzeId(dataAnalyzeDto.getId());
                                                    }});
                                                    break;
                                                }
                                                case "HFCT_XJZS": {//高频电流_巡检噪声
                                                    dataHfctXjzsService.deleteData(new DataHfctXjzsDto() {{
                                                        setDataAnalyzeId(dataAnalyzeDto.getId());
                                                    }});
                                                    break;
                                                }
                                                case "HFCT_XJ": {//高频电流_巡检
                                                    dataHfctXjService.deleteData(new DataHfctXjDto() {{
                                                        setDataAnalyzeId(dataAnalyzeDto.getId());
                                                    }});
                                                    break;
                                                }
                                                case "HFCT_TJZS": {//高频电流_统计噪声
                                                    dataHfctTjzsService.deleteData(new DataHfctTjzsDto() {{
                                                        setDataAnalyzeId(dataAnalyzeDto.getId());
                                                    }});
                                                    break;
                                                }
                                                case "HFCT_TJ": {//高频电流_统计数据
                                                    dataHfctTjService.deleteData(new DataHfctTjDto() {{
                                                        setDataAnalyzeId(dataAnalyzeDto.getId());
                                                    }});
                                                    break;
                                                }
                                                case "HFCT_TJLB": {//高频电流_统计录波
                                                    dataHfctTjlbService.deleteData(new DataHfctTjlbDto() {{
                                                        setDataAnalyzeId(dataAnalyzeDto.getId());
                                                    }});
                                                    break;
                                                }
                                                case "UHF_TJZS_1": {//特高频_模式1_统计噪声
                                                    dataUhfTjzs1Service.deleteData(new DataUhfTjzs1Dto() {{
                                                        setDataAnalyzeId(dataAnalyzeDto.getId());
                                                    }});
                                                    break;
                                                }
                                                case "UHF_TJ_1": {//特高频_模式1_统计数据
                                                    dataUhfTj1Service.deleteData(new DataUhfTj1Dto() {{
                                                        setDataAnalyzeId(dataAnalyzeDto.getId());
                                                    }});
                                                    break;
                                                }
                                                case "UHF_TJLB_1": {//特高频_模式1_统计录波
                                                    dataUhfTjlb1Service.deleteData(new DataUhfTjlb1Dto() {{
                                                        setDataAnalyzeId(dataAnalyzeDto.getId());
                                                    }});
                                                    break;
                                                }
                                                case "UHF_TJZS_2": {//特高频_模式2_统计噪声
                                                    dataUhfTjzs2Service.deleteData(new DataUhfTjzs2Dto() {{
                                                        setDataAnalyzeId(dataAnalyzeDto.getId());
                                                    }});
                                                    break;
                                                }
                                                case "UHF_TJ_2": {//特高频_模式2_统计数据
                                                    dataUhfTj2Service.deleteData(new DataUhfTj2Dto() {{
                                                        setDataAnalyzeId(dataAnalyzeDto.getId());
                                                    }});
                                                    break;
                                                }
                                                case "UHF_TJLB_2": {//特高频_模式2_统计录波
                                                    dataUhfTjlb2Service.deleteData(new DataUhfTjlb2Dto() {{
                                                        setDataAnalyzeId(dataAnalyzeDto.getId());
                                                    }});
                                                    break;
                                                }
                                                case "AE_XJZS": {//接触式超声波_巡检噪声
                                                    dataAeXjzsService.deleteData(new DataAeXjzsDto() {{
                                                        setDataAnalyzeId(dataAnalyzeDto.getId());
                                                    }});
                                                    break;
                                                }
                                                case "AE_XJ": {//接触式超声波_巡检
                                                    dataAeXjService.deleteData(new DataAeXjDto() {{
                                                        setDataAnalyzeId(dataAnalyzeDto.getId());
                                                    }});
                                                    break;
                                                }
                                                case "AE_TJZS": {//接触式超声波_统计噪声
                                                    dataAeTjzsService.deleteData(new DataAeTjzsDto() {{
                                                        setDataAnalyzeId(dataAnalyzeDto.getId());
                                                    }});
                                                    break;
                                                }
                                                case "AE_TJ": {//接触式超声波_统计数据
                                                    dataAeTjService.deleteData(new DataAeTjDto() {{
                                                        setDataAnalyzeId(dataAnalyzeDto.getId());
                                                    }});
                                                    break;
                                                }
                                                case "AE_TJLB": {//接触式超声波_统计录波
                                                    dataAeTjlbService.deleteData(new DataAeTjlbDto() {{
                                                        setDataAnalyzeId(dataAnalyzeDto.getId());
                                                    }});
                                                    break;
                                                }
                                                case "AE_FX": {//接触式超声波_飞行图谱
                                                    dataAeFxService.deleteData(new DataAeFxDto() {{
                                                        setDataAnalyzeId(dataAnalyzeDto.getId());
                                                    }});
                                                    break;
                                                }
                                                case "AE_BX": {//接触式超声波_波形图谱
                                                    dataAeBxService.deleteData(new DataAeBxDto() {{
                                                        setDataAnalyzeId(dataAnalyzeDto.getId());
                                                    }});
                                                    break;
                                                }
                                                case "LC": {//负荷电流数据
                                                    dataLcService.deleteData(new DataLcDto() {{
                                                        setDataAnalyzeId(dataAnalyzeDto.getId());
                                                    }});
                                                    break;
                                                }
                                                case "HJ": {//环境参数数据
                                                    dataHjService.deleteData(new DataHjDto() {{
                                                        setDataAnalyzeId(dataAnalyzeDto.getId());
                                                    }});
                                                    break;
                                                }
                                            }
                                        } catch (Exception e) {

                                        }
                                        try {
                                            dataAnalyzeService.deleteData(dataAnalyzeDto);
                                        } catch (Exception e) {
                                        }
                                    }
                                    try {
                                        dataDeviceSiteService.deleteData(new DataDeviceSiteDto() {{
                                            setDataDeviceId(deviceDto.getId());
                                        }});
                                    } catch (Exception e) {
                                    }
                                    try {
                                        dataDeviceService.deleteData(deviceDto);
                                    } catch (Exception e) {
                                    }
                                }
                            }
                            Platform.runLater(() -> {
                                loadAccTree();
                                ialert.info(I18nUtil.get("ialert.success"));
                            });
                        }
                    }
                } catch (Exception e) {
                    log.error("--");
                }
            });
            accountTree.setOnKeyReleased((event) -> {//任意按键松开时响应
                isCtrl = false;
            });
        }
        accountTree.prefHeightProperty().bind(Application.getStage().heightProperty().subtract(170));
        rightAnchorPane.prefWidthProperty().bind(Application.getStage().widthProperty().subtract(370));
        rightAnchorPane.prefHeightProperty().bind(Application.getStage().heightProperty().subtract(85));
        accountTree.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Node node = event.getPickResult().getIntersectedNode();
            if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
                TreeItem currentTreeItem = ((TreeItem) accountTree.getSelectionModel().getSelectedItem());
                Object object = currentTreeItem.getValue();
                if (currentTreeItem.getChildren().size() > 0) {
                    currentTreeItem.setExpanded(!currentTreeItem.isExpanded());
                    if (object instanceof TreeItemDto) {
                        TreeItemDto treeItemDto = (TreeItemDto) object;
                        Object valObj = treeItemDto.getValue();
                        if (valObj instanceof ZTreeNodeDto) {
                            ZTreeNodeDto zTreeNodeDto = (ZTreeNodeDto) valObj;
                            if (currentTreeItem.isExpanded()) {
                                treeSet.add("" + zTreeNodeDto.getId());
                            } else {
                                treeSet.remove("" + zTreeNodeDto.getId());
                            }
                        } else {
                            if (currentTreeItem.isExpanded()) {
                                treeSet.add("" + treeItemDto.getValue());
                            } else {
                                treeSet.remove("" + treeItemDto.getValue());
                            }
                        }
                    } else if (object instanceof SkillItemDto) {
                        SkillItemDto skillItemDto = (SkillItemDto) object;
                        if (currentTreeItem.isExpanded()) {
                            treeSet.add("" + skillItemDto.getValue());
                        } else {
                            treeSet.remove("" + skillItemDto.getValue());
                        }
                    } else {
                        String value = (String) object;
                        if (currentTreeItem.isExpanded()) {
                            treeSet.add(value);
                        } else {
                            treeSet.remove(value);
                        }
                    }
                }

                if (object instanceof TreeItemDto) {
                    initRightAnchorPane();
                } else if (object instanceof SkillItemDto && currentTreeItem.getChildren().size() == 0) {
                    SkillItemDto skillItemDto = (SkillItemDto) object;
                    ehcacheUtil.setCache(CommonConstant.ATLAS_DATA_SKILL_FULL_NAME, skillItemDto.getFullName());
                    switch (skillItemDto.getSkill()) {
                        case AA: {
                            rightAnchorPane.getChildren().setAll(atlasDataAAView.getView());
                            atlasDataAAController.loadAAData(skillItemDto.getFullName());
                            break;
                        }
                        case AE: {
                            rightAnchorPane.getChildren().setAll(atlasDataSkillView.getView());
                            atlasDataSkillController.loadDeviceList("AE");
                            break;
                        }
                        case TEV: {
                            rightAnchorPane.getChildren().setAll(atlasDataTEVView.getView());
                            atlasDataTEVController.loadTEVData(skillItemDto.getFullName());
                            break;
                        }
                        case UHF: {
                            rightAnchorPane.getChildren().setAll(atlasDataSkillView.getView());
                            atlasDataSkillController.loadDeviceList("UHF");
                            break;
                        }
                        case HFCT: {
                            rightAnchorPane.getChildren().setAll(atlasDataSkillView.getView());
                            atlasDataSkillController.loadDeviceList("HFCT");
                            break;
                        }
                    }
                    BorderPane dataBorderPane = (BorderPane) rightAnchorPane.lookup("#dataBorderPane");
//                    log.debug("getStage==width:{},height:{}",Application.getStage().getWidth(),Application.getStage().getHeight());
                    dataBorderPane.minWidthProperty().bind(Application.getStage().widthProperty().subtract(370));
                    dataBorderPane.minHeightProperty().bind(Application.getStage().heightProperty().subtract(20));
//                    log.debug("dataScrollPane==height:{}",dataScrollPane.prefHeightProperty().doubleValue());
                }
            }
        });
    }

    @FXML
    public void loadAccTree() {
        refreshButton.setDisable(true);
        loadAccTree(accountTree, new HashMap() {{
            put("isAtlas", "1");
            if (ValidatorUtil.notEmpty(electricBureauText.getText())) {
                put("electricBureau", electricBureauText.getText());
            }
            if (ValidatorUtil.notEmpty(substationText.getText())) {
                put("substation", substationText.getText());
            }
            if (deviceTypeChoiceBox.getValue() != null && ValidatorUtil.notEmpty("" + deviceTypeChoiceBox.getValue())) {
                put("deviceType", getDeviceTypeValue(String.valueOf(deviceTypeChoiceBox.getValue())));
            }
            if (voltageLevelChoiceBox.getValue() != null && ValidatorUtil.notEmpty("" + voltageLevelChoiceBox.getValue())) {
                put("voltageLevel", getvoltageLevelValue(String.valueOf(voltageLevelChoiceBox.getValue())));
            }
            if (ValidatorUtil.notEmpty(deviceNameText.getText())) {
                put("deviceName", deviceNameText.getText());
            }
            if (testTechnologyChoiceBox.getValue() != null && !"请选择".equals("" + testTechnologyChoiceBox.getValue())) {
                put("testTechnology", testTechnologyChoiceBox.getValue());
            }
            if (produceDateDatePicker.getValue() != null) {
                put("produceDate", produceDateDatePicker.getValue().format(dateTimeFormatter));
            }
            if (useDateDatePicker.getValue() != null) {
                put("useDate", useDateDatePicker.getValue().format(dateTimeFormatter));
            }
        }});
        refreshButton.setDisable(false);
    }

    private void initRightAnchorPane() {
        rightAnchorPane.getChildren().setAll(atlasImportInitView.getView());
        rightAnchorPane.setStyle("-fx-background-color: #F3F4F5");
        ehcacheUtil.setCache(CommonConstant.SYS_TIMEOUT_KEY, System.currentTimeMillis());//更新当前时间
    }

    @FXML
    public void setCancel() {
        log.info("setCancel..");
        hideNodeView();
    }

    @FXML
    public void onReset() {
        log.info("onReset..");
        electricBureauText.setText(null);//请输入电业局名称
        substationText.setText(null);//请输入变电站名称
        deviceTypeChoiceBox.getSelectionModel().select(0);//
        voltageLevelChoiceBox.getSelectionModel().select(0);//
        deviceNameText.setText(null);//设备名称
        testTechnologyChoiceBox.getSelectionModel().select(0);//
        produceDateDatePicker.setValue(null);//点击输入出厂日期
        useDateDatePicker.setValue(null);//点击输入投运日期
    }

    @FXML
    public void setShowDone() {
        log.info("setShowDone..");
        hideNodeView();
        loadAccTree();
    }

    @FXML
    @Getter
    Pane mlayer;//蒙层
    @FXML
    VBox mVBox;
    @FXML
    HBox mHBox;
    @FXML
    Pane closeSet;

    private void hideNodeView(Parent... nodeViews) {
        if (nodeViews == null || nodeViews.length == 0) {
            nodeViews = new Parent[]{mlayer, mVBox};
        }
        for (Parent nodeView : nodeViews) {
            nodeView.setVisible(false);
        }
    }

    @FXML
    public void searhData() {
        log.info("searhData..");
        hideNodeView();

        mlayer.setVisible(true);
        mVBox.setVisible(true);
    }

    @Override
    public void dispose() {

    }
}



