package com.redphase.controller.task;

import com.Application;
import com.alibaba.fastjson.JSONObject;
import com.redphase.api.account.IAccountService;
import com.redphase.api.task.IAccountSiteInfoService;
import com.redphase.api.task.ITaskService;
import com.redphase.controller.BaseController;
import com.redphase.dto.TreeItemDto;
import com.redphase.dto.ZTreeNodeDto;
import com.redphase.dto.account.AccountDto;
import com.redphase.dto.task.AccountSiteInfoDto;
import com.redphase.dto.task.TaskDetailDto;
import com.redphase.dto.task.TaskDto;
import com.redphase.dto.task.TaskHistoryConfigDto;
import com.redphase.framework.util.CommonConstant;
import com.redphase.framework.util.IdUtil;
import com.redphase.framework.util.ValidatorUtil;
import com.redphase.view.DataButtonCell;
import com.redphase.view.DataTableCell;
import com.redphase.view.task.TaskSiteOtherView;
import com.redphase.view.task.TaskSiteSwitchView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@FXMLController
@Slf4j
@Data
public class TaskController extends BaseController implements Initializable {
    @Autowired
    ITaskService taskService;
    @Autowired
    IAccountService accountService;
    @Autowired
    IAccountSiteInfoService accountSiteInfoService;
    @Autowired
    TaskSiteSwitchView taskSiteSwitchView;
    @Autowired
    TaskSiteOtherView taskSiteOtherView;
    @FXML
    BorderPane rPane;
    @FXML
    VBox otherVBox;
    @FXML
    VBox otherAnchorVBoxPane;
    @FXML
    ScrollPane otherScrollPane;
    @FXML
    AnchorPane otherAnchorPane;
    @FXML
    Button searchButton;//搜索
    @FXML
    Button refreshButton;//刷新
    @FXML
    TreeView accountTree;
    @FXML
    Pane rightAnchorPane;
    @FXML
    TableView<AccountDto> dataTable;
    @FXML
    TableColumn codeColumn;//序号
    @FXML
    CheckBox allCheck;
    @FXML
    TableColumn electricBureauColumn;//电力局名称
    @FXML
    TableColumn substationColumn;//变电站
    @FXML
    TableColumn deviceTypeColumn;//设备类型
    @FXML
    TableColumn deviceNameColumn;//设备名称
    @FXML
    TableColumn configStateColumn;//检查位置信息
    @FXML
    TableColumn runStateColumn;//运行状态
    @FXML
    TableColumn operColumn;//操作
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

    Set<AccountDto> accountDtoSet = new HashSet<>();
    Map<String, CheckBox> dataCheckMap = new HashMap<>();

    @FXML
    Label electricBureauLabel;//实测电力局
    @FXML
    Label substationLabel;//变电站
    @FXML
    Label deviceTypeLabel;//设备类型
    @FXML
    Label voltageLevelLabel;//电压等级
    @FXML
    Label deviceNameLabel;//设备名称
    @FXML
    TextField accountIdText;

    @FXML
    CheckBox uhfCheckBox;//特高频（UHF）
    @FXML
    CheckBox aeCheckBox;//接触式超声波(AE)
    @FXML
    CheckBox hfctCheckBox;//高频（HFCT）
    @FXML
    HBox uhfHbox;
    @FXML
    FlowPane uhfFlowPane;
    @FXML
    HBox aeHbox;
    @FXML
    FlowPane aeFlowPane;
    @FXML
    HBox hfctHbox;
    @FXML
    FlowPane hfctFlowPane;


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
    Pane uhfPane;
    @FXML
    Pane aePane;
    @FXML
    Pane hfctPane;

    Set<String> treeSet = new HashSet<>();

    boolean initFlag = false;
    /**
     * 开关柜复选框
     */
    private Map<String, Map<String, AccountSiteInfoDto>> tempSiteMap = new HashMap<>();

    List<AccountDto> accountDtos = new ArrayList<>();

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize...");
        if (dataTable != null) {
            dataTable.prefWidthProperty().bind(Application.getScene().widthProperty().subtract(395));
            dataTable.prefHeightProperty().bind(Application.getScene().heightProperty().subtract(170));
        }
        if (initFlag) return;
        initFlag = true;
        try {
            hideNodeView();
            closeSet.setOnMouseClicked(event -> {
                hideNodeView();
            });
            mlayer.prefWidthProperty().bind(Application.getStage().widthProperty());
            mlayer.prefHeightProperty().bind(Application.getStage().heightProperty());
            mVBox.prefWidthProperty().bind(mlayer.prefWidthProperty().subtract(200));
            mVBox.prefHeightProperty().bind(mlayer.prefHeightProperty().subtract(150));
            mHBox.prefWidthProperty().bind(mlayer.prefWidthProperty());
            mHBox.prefHeightProperty().bind(mlayer.prefHeightProperty());
            deviceTypeChoiceBox.setItems(FXCollections.observableArrayList("请选择", "开关柜", "变压器", "组合电器", "电缆"));
            deviceTypeChoiceBox.getSelectionModel().select(0);
            testTechnologyChoiceBox.setItems(FXCollections.observableArrayList("请选择", "AA", "AE", "HFCT", "UHF", "TEV"));
            testTechnologyChoiceBox.getSelectionModel().select(0);
            voltageLevelChoiceBox.setItems(FXCollections.observableArrayList("请选择", "10kV", "35kV", "110kV", "220kV", "500kV"));
            voltageLevelChoiceBox.getSelectionModel().select(0);
            accountTree.prefHeightProperty().bind(Application.getScene().heightProperty().subtract(150));
            treeSet.clear();
            refreshTree();
            accountTree.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                try {
                    Node node = event.getPickResult().getIntersectedNode();
                    if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
                        TreeItem currentTreeItem = ((TreeItem) accountTree.getSelectionModel().getSelectedItem());
                        Object object = currentTreeItem.getValue();
                        TreeItemDto treeItemDto = (TreeItemDto) object;
                        ZTreeNodeDto zTreeNodeDto = (ZTreeNodeDto) treeItemDto.getValue();
                        if (currentTreeItem.getChildren().size() > 0) {
                            currentTreeItem.setExpanded(!currentTreeItem.isExpanded());
                            if (currentTreeItem.isExpanded()) {
                                treeSet.add(zTreeNodeDto.getId());
                            } else {
                                treeSet.remove(zTreeNodeDto.getId());
                            }
                        }
                        accountDtos = new ArrayList<>();
                        if (zTreeNodeDto.getData() != null) {
                            AccountDto dto = (AccountDto) zTreeNodeDto.getData();
                            if (dto.getDeviceType() != 1) {
                                accountDtos.add(accountService.findDataById(dto.getId()));
                            }
                        } else if (null != zTreeNodeDto.getChildren() && zTreeNodeDto.getChildren().size() > 0 && zTreeNodeDto.getChildren().get(0).isParent() == false) {
                            String fullName = zTreeNodeDto.getFullName();
                            if (ValidatorUtil.notEmpty(fullName)) {
                                AccountDto accountDto = new AccountDto();
                                String[] paramArr = fullName.split(CommonConstant.FEIGN_ERROR_SYMBOL_STRING);
                                for (int i = 0; i < paramArr.length; i++) {
                                    switch (i) {
                                        case 0: {
                                            accountDto.setElectricBureau(paramArr[i]);
                                            break;
                                        }
                                        case 1: {
                                            accountDto.setSubstation(paramArr[i]);
                                            break;
                                        }
                                        case 2: {
                                            accountDto.setVoltageLevel(Integer.parseInt(paramArr[i].replace("kV", "")));
                                            break;
                                        }
                                        case 3: {
                                            accountDto.setDeviceType(AccountDto.DeviceType.getValueByText(paramArr[i]));
                                            break;
                                        }
                                        case 4: {
                                            accountDto.setDeviceName(paramArr[i]);
                                            break;
                                        }
                                    }
                                }
                                accountDtos = accountService.findDataIsListDirId(accountDto);
                            }
                        }
                        List<AccountDto> finalAccountDtos = accountDtos;
                        Platform.runLater(() -> {
                            loadList(finalAccountDtos);
                        });
                    }
                } catch (Exception e) {
                    log.error("查询台帐失败!", e);
                }
            });
        } catch (Exception e) {
            log.error("系统异常!", e);
        }
    }

    public void loadList(List<AccountDto> accountDtos) {
        accountDtoSet.clear();
        dataCheckMap.clear();
        dataTable.getItems().clear();
        allCheck.setSelected(false);
        //装载参数
        ObservableList<AccountDto> data = FXCollections.observableArrayList(accountDtos);
        if (data != null) {
            AtomicInteger atomicInteger = new AtomicInteger(1);
            data.forEach(accountDto -> {
                CheckBox checkBox = new CheckBox(""+atomicInteger.get());
                checkBox.setId("" + accountDto.getId());
                checkBox.selectedProperty().addListener((ov, oldVal, newVal) -> {
                    if (newVal) {
                        accountDtoSet.add(accountDto);
                    } else {
                        allCheck.setSelected(false);
                        accountDtoSet.remove(accountDto);
                    }
                });
                if (allCheck.isSelected()) {
                    checkBox.setSelected(allCheck.isSelected());
                } else {
                    checkBox.setSelected(accountDtoSet.contains(accountDto));
                }
                dataCheckMap.put(checkBox.getText(), checkBox);
                atomicInteger.getAndAdd(1);
            });
        }
        dataTable.setItems(data);
        codeColumn.setCellFactory((col) -> new DataButtonCell<AccountDto>() {
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    Label label= new Label("");
                    label.setGraphic(dataCheckMap.get("" + (this.getIndex() + 1)));
                    this.setGraphic(label);
                    this.setStyle("-fx-pref-width: 75;-fx-text-overrun: center-ellipsis;");
                } else {
                    this.setText(null);
                    this.setGraphic(null);
                }
            }
        });
        electricBureauColumn.setCellFactory((col) -> new DataTableCell("electricBureau"));
        substationColumn.setCellFactory((col) -> new DataTableCell("substation"));
        deviceTypeColumn.setCellFactory((col) -> new DataTableCell("deviceType", new HashMap() {{
            put("1", "开关柜");
            put("2", "组合电器");
            put("3", "变压器");
            put("4", "电缆");
        }}));
        deviceNameColumn.setCellFactory((col) -> new DataTableCell("deviceName"));
        configStateColumn.setCellFactory((col) -> new DataTableCell("configState", new HashMap() {{
            put("1", "已配置");
            put("0", "未配置");
        }}));
        runStateColumn.setCellFactory((col) -> {
            TableCell<AccountDto, String> cell = new TableCell<AccountDto, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        AccountDto accountDto = this.getTableView().getItems().get(this.getIndex());
                        if (accountDto.getRunState() != null && accountDto.getRunState().indexOf("停") != -1) {
                            this.setStyle("-fx-text-fill: #FF1A00;-fx-underline: false;");
                        }
                        this.setText(accountDto.getRunState());

                        if (siteMap.get("" + accountDto.getId()) == null) {
                            try {
                                List<AccountSiteInfoDto> serviceDataIsList = accountSiteInfoService.findDataIsList(new AccountSiteInfoDto() {{
                                    setAccountId("" + accountDto.getId());
                                }});
                                siteMap.put("" + accountDto.getId(), serviceDataIsList);
                                if (serviceDataIsList != null) {
                                    Map<String, AccountSiteInfoDto> siteDtoMap = new HashMap<>();
                                    for (AccountSiteInfoDto siteInfoDto : serviceDataIsList) {
                                        if (accountDto.getDeviceType() == 1) {
                                            siteDtoMap.put(siteInfoDto.getSiteName(), siteInfoDto);
                                        } else {
                                            String testTechnology = "";
                                            switch (siteInfoDto.getTestTechnology()) {
                                                case "1":
                                                    testTechnology = "uhf";
                                                    break;
                                                case "2":
                                                    testTechnology = "ae";
                                                    break;
                                                case "3":
                                                    testTechnology = "hfct";
                                                    break;
                                            }
                                            siteDtoMap.put(testTechnology + CommonConstant.FEIGN_ERROR_SYMBOL_STRING + siteInfoDto.getSiteName(), siteInfoDto);
                                        }
                                    }
                                    tempSiteMap.put("" + accountDto.getId(), siteDtoMap);
                                }
                            } catch (Exception e) {
                                log.error("设备位置数据加载失败!", e);
                                ialert.warning("设备位置数据加载失败!" + e.getMessage());
                            }
                        }
                    }
                }
            };
            return cell;
        });
        operColumn.setCellFactory((col) -> new DataButtonCell<AccountDto>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);
                if (!empty) {
                    HBox paddedButton = new HBox();
                    this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    Label editBtn = new Label("位置配置");
                    Label delBtn = new Label("删除");
                    paddedButton.getStylesheets().add("/com/redphase/css/user.css");

                    editBtn.getStyleClass().addAll("ob");
                    ImageView editImageView = new ImageView(new Image("/com/redphase/images/edit.png"));
                    editImageView.setFitWidth(20);
                    editImageView.setFitHeight(20);
                    editBtn.setGraphic(editImageView);

                    delBtn.getStyleClass().addAll("ob");
                    ImageView delImageView = new ImageView(new Image("/com/redphase/images/del.png"));
                    delImageView.setFitWidth(20);
                    delImageView.setFitHeight(20);
                    delBtn.setGraphic(delImageView);

                    if (jwtUtil.isPermitted("task:edit")) {
                        paddedButton.getChildren().add(editBtn);
                    }
                    if (jwtUtil.isPermitted("task:phydel")) {
                        paddedButton.getChildren().add(delBtn);
                    }
                    paddedButton.setPadding(new Insets(0));
                    editBtn.setOnMouseClicked((m) -> {
                        log.debug("TaskController...edit");
                        AccountDto accountDto = (AccountDto) dataTable.getSelectionModel().getSelectedItem();
                        if (accountDto.getConfigState() == 1) {
                            currentDeviceType = accountDto.getDeviceType();
                            if (accountDto.getDeviceType() == 1) {
                                idialog.openDialog("选择设备检查位置-" + accountDto.getDeviceName(), taskSiteSwitchView, 1210.0, 734.0, true);
                            } else {
                                idialog.openDialog("选择设备检查位置-" + accountDto.getDeviceName(), taskSiteOtherView, 1210.0, 734.0, true);
                            }
                            sitePageInit(accountDto);
                        } else {
                            ialert.warning("请到台帐管理配置位置信息!");
                        }
                    });
                    delBtn.setOnMouseClicked((m) -> {
                        AccountDto accountDto = (AccountDto) dataTable.getSelectionModel().getSelectedItem();
                        log.debug("TaskController...del");
                        dataTable.getItems().remove(accountDto);
                        dataTable.refresh();
                    });
                    this.setGraphic(paddedButton);
                } else {
                    this.setGraphic(null);
                }
            }
        });
        dataTable.refresh();
    }

    private void sitePageInit(AccountDto dto) {
        currentAccountId = dto.getId();
        electricBureauLabel.setText(dto.getElectricBureau());//实测电力局
        substationLabel.setText(dto.getSubstation());//变电站
        String deviceTypeName = "";
        switch (dto.getDeviceType()) {
            case 1:
                deviceTypeName = "开关柜";
                break;
            case 2:
                deviceTypeName = "变压器";
                break;
            case 3:
                deviceTypeName = "组合电器";
                break;
            case 4:
                deviceTypeName = "电缆";
                break;
        }
        deviceTypeLabel.setText(deviceTypeName);//设备类型
        voltageLevelLabel.setText(dto.getVoltageLevel() + "");//电压等级
        deviceNameLabel.setText(dto.getDeviceName());//设备名称
        accountIdText.setText("" + dto.getId());
        if (dto.getDeviceType() == 1) {
            clearSwitch();
        } else {
            rPane.prefWidth(915);
            rPane.prefHeight(700.0);
            otherVBox.prefWidth(915);
            otherVBox.prefHeight(620);
            otherScrollPane.prefWidthProperty().bind(otherVBox.prefWidthProperty());
            otherScrollPane.prefHeightProperty().bind(otherVBox.prefHeightProperty().subtract(80));
            otherAnchorPane.prefWidthProperty().bind(otherScrollPane.prefWidthProperty().subtract(10));
            otherAnchorPane.minHeightProperty().bind(otherScrollPane.prefHeightProperty().subtract(3));
            otherAnchorVBoxPane.prefWidthProperty().bind(otherAnchorPane.prefWidthProperty().subtract(10));
            otherAnchorVBoxPane.minHeightProperty().bind(otherAnchorPane.prefHeightProperty());

            uhfFlowPane.getChildren().clear();
            aeFlowPane.getChildren().clear();
            hfctFlowPane.getChildren().clear();
            uhfCheckBox.setVisible(false);
            uhfCheckBox.setManaged(false);
            showUhfSite();
            aeCheckBox.setVisible(false);
            aeCheckBox.setManaged(false);
            showAeSite();
            hfctCheckBox.setVisible(false);
            hfctCheckBox.setManaged(false);
            showHfctSite();
        }
        List<AccountSiteInfoDto> results = null;
        try {
            results = accountSiteInfoService.findDataIsList(new AccountSiteInfoDto() {{
                setAccountId("" + dto.getId());
            }});
        } catch (Exception e) {
        }
        Map<String, AccountSiteInfoDto> selectResults = tempSiteMap.get("" + dto.getId()) == null ? new HashMap<>() : tempSiteMap.get("" + dto.getId());
        if (results != null && results.size() > 0) {
            results.forEach(siteInfoDto -> {
                if (siteMap.get("" + siteInfoDto.getAccountId()) == null) {
                    siteMap.put("" + siteInfoDto.getAccountId(), new ArrayList<>());
                }
                if (dto.getDeviceType() == 1) {
                    switch (siteInfoDto.getSiteName()) {
                        case "前中":
                            qzCheckBox.setDisable(false);
                            if (null != selectResults.get("前中")) {
                                qzCheckBox.setSelected(true);
                                siteMap.get(siteInfoDto.getAccountId()).add(siteInfoDto);
                            }
                            break;
                        case "前下":
                            qxCheckBox.setDisable(false);
                            if (null != selectResults.get("前下")) {
                                qxCheckBox.setSelected(true);
                                siteMap.get(siteInfoDto.getAccountId()).add(siteInfoDto);
                            }
                            break;
                        case "后上":
                            hsCheckBox.setDisable(false);
                            if (null != selectResults.get("后上")) {
                                hsCheckBox.setSelected(true);
                                siteMap.get(siteInfoDto.getAccountId()).add(siteInfoDto);
                            }
                            break;
                        case "后中":
                            hzCheckBox.setDisable(false);
                            if (null != selectResults.get("后中")) {
                                hzCheckBox.setSelected(true);
                                siteMap.get(siteInfoDto.getAccountId()).add(siteInfoDto);
                            }
                            break;
                        case "后下":
                            hxCheckBox.setDisable(false);
                            if (null != selectResults.get("后下")) {
                                hxCheckBox.setSelected(true);
                                siteMap.get(siteInfoDto.getAccountId()).add(siteInfoDto);
                            }
                            break;
                        case "侧上":
                            csCheckBox.setDisable(false);
                            if (null != selectResults.get("侧上")) {
                                csCheckBox.setSelected(true);
                                siteMap.get(siteInfoDto.getAccountId()).add(siteInfoDto);
                            }
                            break;
                        case "侧中":
                            czCheckBox.setDisable(false);
                            if (null != selectResults.get("侧中")) {
                                czCheckBox.setSelected(true);
                                siteMap.get(siteInfoDto.getAccountId()).add(siteInfoDto);
                            }
                            break;
                        case "侧下":
                            cxCheckBox.setDisable(false);
                            if (null != selectResults.get("侧下")) {
                                cxCheckBox.setSelected(true);
                                siteMap.get(siteInfoDto.getAccountId()).add(siteInfoDto);
                            }
                            break;
                    }
                } else {
                    String uuid = IdUtil.createUUID(32);
                    uhfFlowPane.setPrefWidth(900);
                    aeFlowPane.setPrefWidth(900);
                    hfctFlowPane.setPrefWidth(900);
                    switch (siteInfoDto.getTestTechnology()) {
                        case "1": {//特高频（UHF）
                            uhfCheckBox.setSelected(true);
                            uhfCheckBox.setVisible(true);
                            uhfCheckBox.setManaged(true);
                            uhfPane.setVisible(true);
                            uhfPane.setManaged(true);
//                            uhfSet.add(uuid);
                            uhfFlowPane.getChildren().add(editSiteHBox("uhf", siteInfoDto, uuid, selectResults));
                            break;
                        }
                        case "2": {//AE
                            aeCheckBox.setSelected(true);
                            aeCheckBox.setVisible(true);
                            aeCheckBox.setManaged(true);
                            aePane.setVisible(true);
                            aePane.setManaged(true);
//                            aeSet.add(uuid);
                            aeFlowPane.getChildren().add(editSiteHBox("ae", siteInfoDto, uuid, selectResults));
                            break;
                        }
                        case "3": {//高频（HFCT）
                            hfctCheckBox.setSelected(true);
                            hfctCheckBox.setVisible(true);
                            hfctCheckBox.setManaged(true);
                            hfctPane.setVisible(true);
                            hfctPane.setManaged(true);
//                            hfctSet.add(uuid);
                            hfctFlowPane.getChildren().add(editSiteHBox("hfct", siteInfoDto, uuid, selectResults));
                            break;
                        }
                    }
                }
            });
        }
    }

    Integer currentDeviceType = null;

    private void clearSwitch() {
        try {
            qzCheckBox.setSelected(false);
            qxCheckBox.setSelected(false);
            hsCheckBox.setSelected(false);
            hzCheckBox.setSelected(false);
            hxCheckBox.setSelected(false);
            csCheckBox.setSelected(false);
            czCheckBox.setSelected(false);
            cxCheckBox.setSelected(false);
            qzCheckBox.setDisable(true);
            qxCheckBox.setDisable(true);
            hsCheckBox.setDisable(true);
            hzCheckBox.setDisable(true);
            hxCheckBox.setDisable(true);
            csCheckBox.setDisable(true);
            czCheckBox.setDisable(true);
            cxCheckBox.setDisable(true);
        } catch (Exception e) {
        }
    }

    @FXML
    public void showUhfSite() {
        uhfPane.setVisible(uhfCheckBox.isSelected());
        uhfPane.setManaged(uhfCheckBox.isSelected());
    }

    @FXML
    public void showAeSite() {
        aePane.setVisible(aeCheckBox.isSelected());
        aePane.setManaged(aeCheckBox.isSelected());
    }

    @FXML
    public void showHfctSite() {
        hfctPane.setVisible(hfctCheckBox.isSelected());
        hfctPane.setManaged(hfctCheckBox.isSelected());
    }

    private HBox editSiteHBox(String testTechnology, AccountSiteInfoDto siteInfoDto, String uuid, Map<String, AccountSiteInfoDto> selectResults) {
        return new HBox() {{
            setAlignment(Pos.CENTER_LEFT);
            setPrefHeight(60);
            setPrefWidth(400);
            HBox hBox = this;
            this.getChildren().addAll(new Label("位置信息：") {{
                                          setId(uuid + "L");
                                          setAlignment(Pos.CENTER);
                                          setPrefHeight(26);
                                          setPrefWidth(130);
                                          setTextFill(Color.web("#999999"));
                                          setFont(new Font(16));
                                          setGraphic(new Label("*") {{
                                              setPrefWidth(8);
                                              setPrefHeight(24);
                                              setTextFill(Color.RED);
                                              setFont(new Font(18));
                                          }});
                                          setPadding(new Insets(0, 0, 2, 0));
                                      }}
                    ,
                    new Label() {{
                        setId(uuid + "T");
                        setPrefHeight(35);
                        setPrefWidth(300);
                        setAlignment(Pos.CENTER);
                        setStyle("-fx-background-color: #f3f4f5;");
                        if (siteInfoDto != null) {
                            setText(siteInfoDto.getSiteName());
                        }
                    }}
                    ,
                    new CheckBox() {{
                        setId(testTechnology + CommonConstant.FEIGN_ERROR_SYMBOL_STRING + siteInfoDto.getAccountId() + CommonConstant.FEIGN_ERROR_SYMBOL_STRING + siteInfoDto.getId() + CommonConstant.FEIGN_ERROR_SYMBOL_STRING + uuid + CommonConstant.FEIGN_ERROR_SYMBOL_STRING + siteInfoDto.getSiteName() + CommonConstant.FEIGN_ERROR_SYMBOL_STRING + JSONObject.toJSONString(siteInfoDto));
                        //setId(JSONObject.toJSONString(siteInfoDto));
                        setPrefHeight(35);
                        setPrefWidth(30);
                        setAlignment(Pos.CENTER);
                        setContentDisplay(ContentDisplay.RIGHT);
                        setMnemonicParsing(false);
                        AccountSiteInfoDto oldAccountSiteInfoDto = selectResults.get(testTechnology + CommonConstant.FEIGN_ERROR_SYMBOL_STRING + siteInfoDto.getSiteName());

                        String testTechnologyNum = "-";
                        switch (testTechnology) {
                            case "uhf":
                                testTechnologyNum = "1";
                                break;
                            case "ae":
                                testTechnologyNum = "2";
                                break;
                            case "hfct":
                                testTechnologyNum = "3";
                                break;
                        }

                        if (null != oldAccountSiteInfoDto && testTechnologyNum.equalsIgnoreCase(oldAccountSiteInfoDto.getTestTechnology())) {
                            if (null == siteMap.get(siteInfoDto.getId())) {
                                siteMap.put(siteInfoDto.getId(), new ArrayList<>());
                            }
                            siteMap.get(siteInfoDto.getId()).add(siteInfoDto);
                            setSelected(true);
                        }
                        setPadding(new Insets(30, 0, 0, 15));
                        this.setOnMouseClicked((click) -> {
                            String[] ids = this.getId().split(CommonConstant.FEIGN_ERROR_SYMBOL_STRING);
                            if (this.isSelected()) {
                                siteMap.get(ids[1]).add(siteInfoDto);
                            } else {
                                siteMap.get(ids[1]).remove(siteInfoDto);
                            }
                        });
                    }}
            );
        }};
    }

    @FXML
    public void checkAll() {
        log.debug("TaskController...checkAll..");
        dataCheckMap.values().forEach(node -> {
            node.setSelected(allCheck.isSelected());
        });
    }

    Map<String, List<AccountSiteInfoDto>> siteMap = new HashMap<>();
    Long currentAccountId = null;

    /**
     * 生成任务单
     *
     * @return
     */
    @FXML
    public void batchSave() {
        log.info("batchSave..");
        try {
            if (accountDtoSet == null || accountDtoSet.size() == 0) {
                ialert.error("请选择任务单!");
                return;
            }
            List<TaskDto> list = new ArrayList() {{
                for (AccountDto dto : accountDtoSet) {
                    add(new TaskDto() {{
                        setAccountId("" + dto.getId());
                        setDeviceName(dto.getDeviceName());
                        setDeviceType(dto.getDeviceType());
                        setSortNum(dto.getSortNum());

                        setElectricBureau(dto.getElectricBureau());//电业局
                        setSubstation(dto.getSubstation());//变电站
                        setVoltageLevel(dto.getVoltageLevel());//电压等级
                        setDeviceType(dto.getDeviceType());//设备类型

                        setTaskDetailDtos(new ArrayList<TaskDetailDto>() {{
                            List<AccountSiteInfoDto> sets = siteMap.get("" + dto.getId());
                            if (sets != null) {
                                sets.sort(Comparator.comparing(AccountSiteInfoDto::getSortNo));
                            }
                            if (sets != null && sets.size() > 0) {
                                for (AccountSiteInfoDto siteInfoDto : sets) {
                                    add(new TaskDetailDto() {{
                                        setAccountId(siteInfoDto.getAccountId());
                                        setSiteName(siteInfoDto.getSiteName());
                                        setTestTechnology(siteInfoDto.getTestTechnology());
                                    }});
                                }
                            }
                        }});
                    }});
                }
            }};

            if (list != null) {
                list.sort(Comparator.comparing(TaskDto::getSortNum));
            }

            for (TaskDto taskDto : list) {
                if (taskDto.getTaskDetailDtos().size() <= 0) {
                    ialert.error("请配置任务单位置信息!");
                    return;
                }
            }
            taskService.batchSave(list);
            taskService.createFile(list);
            ialert.success("创建任务单文件完成");
        } catch (Exception e) {
            log.error("批量保存失败!", e);
            ialert.error("批量保存失败!" + e.getMessage());
        }
    }

    /**
     * 导入历史任务单文件
     *
     * @return
     */
    @FXML
    public void importTask() {
        log.info("importTask..");
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("请选择txt文件");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("excel files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showOpenDialog(Application.getStage());
            if (file == null || !file.exists()) {
                ialert.error("请选择正确的历史任务单文件!");
                return;
            } else {
                HashMap<String, Object> map = taskService.importTask(file);
                List<AccountDto> accountDtos = (List<AccountDto>) map.get("account");
                if (accountDtos != null || accountDtos.size() > 0) {
                    loadList(accountDtos);
                    List<List<TaskHistoryConfigDto>> configDtos = (List<List<TaskHistoryConfigDto>>) map.get("config");
                    if (null != configDtos && configDtos.size() > 0) {
                        tempSiteMap.clear();
                        configDtos.forEach(configDto -> {
                            if (null != configDto && configDto.size() > 0) {
                                Map<String, AccountSiteInfoDto> siteDtoMap = new HashMap<>();
                                configDto.forEach(dto -> {
                                    siteDtoMap.put(dto.getTestTechnology() + CommonConstant.FEIGN_ERROR_SYMBOL_STRING + dto.getSiteName(), new AccountSiteInfoDto());
                                });
                                tempSiteMap.put(configDto.get(0).getAccountId(), siteDtoMap);
                            }
                        });
                    }
                }
            }
            ialert.success("历史任务单文件导入完成！");
        } catch (Exception e) {
            log.error("任务单文件解析异常!", e);
            ialert.error("任务单文件解析异常!" + e.getMessage());
        }
    }

    @FXML
    public void refreshTree() {
        log.info("refreshTree..");
        refreshButton.setDisable(true);
        loadAccTree(accountTree);
        refreshButton.setDisable(false);
    }

    @FXML
    public void addSite() {
        log.info("addSite..");
        Map<String, AccountSiteInfoDto> siteDtoMap = new HashMap<>();
        List<AccountSiteInfoDto> results = new ArrayList<>();
        if (currentDeviceType == 1) {
            try {
                results = accountSiteInfoService.findDataIsList(new AccountSiteInfoDto() {{
                    setAccountId(accountIdText.getText());
                }});
            } catch (Exception e) {
            }
            HashMap<String, AccountSiteInfoDto> dtoHashMap = new HashMap<>();
            for (AccountSiteInfoDto siteInfoDto : results) {
                dtoHashMap.put(siteInfoDto.getSiteName(), siteInfoDto);
            }
            if (qzCheckBox.isSelected()) siteDtoMap.put("前中", dtoHashMap.get("前中"));
            if (qxCheckBox.isSelected()) siteDtoMap.put("前下", dtoHashMap.get("前下"));
            if (hsCheckBox.isSelected()) siteDtoMap.put("后上", dtoHashMap.get("后上"));
            if (hzCheckBox.isSelected()) siteDtoMap.put("后中", dtoHashMap.get("后中"));
            if (hxCheckBox.isSelected()) siteDtoMap.put("后下", dtoHashMap.get("后下"));
            if (csCheckBox.isSelected()) siteDtoMap.put("侧上", dtoHashMap.get("侧上"));
            if (czCheckBox.isSelected()) siteDtoMap.put("侧中", dtoHashMap.get("侧中"));
            if (cxCheckBox.isSelected()) siteDtoMap.put("侧下", dtoHashMap.get("侧下"));
            tempSiteMap.put(accountIdText.getText(), siteDtoMap);
            siteMap.put(accountIdText.getText(), new ArrayList() {{
                addAll(siteDtoMap.values());
            }});
            idialog.getStage(taskSiteSwitchView).hide();
        } else {
            if (uhfCheckBox.isSelected()) {
                getSelectDate(uhfFlowPane, siteDtoMap);
            }
            if (aeCheckBox.isSelected()) {
                getSelectDate(aeFlowPane, siteDtoMap);
            }
            if (hfctCheckBox.isSelected()) {
                getSelectDate(hfctFlowPane, siteDtoMap);
            }
            tempSiteMap.put(accountIdText.getText(), siteDtoMap);
            siteMap.put(accountIdText.getText(), new ArrayList<>(siteDtoMap.values()));
            idialog.getStage(taskSiteOtherView).hide();
        }
    }

    private void getSelectDate(FlowPane flowPane, Map<String, AccountSiteInfoDto> siteDtoMap) {
        flowPane.getChildren().forEach(node -> {
            if (node instanceof HBox) {
                HBox hBox = (HBox) node;
                hBox.getChildren().forEach(node1 -> {
                    if (node1 instanceof CheckBox) {
                        CheckBox checkBox = (CheckBox) node1;
                        if (checkBox.isSelected()) {
                            String[] ids = checkBox.getId().split(CommonConstant.FEIGN_ERROR_SYMBOL_STRING);
                            siteDtoMap.put(ids[0] + CommonConstant.FEIGN_ERROR_SYMBOL_STRING + ids[4], JSONObject.parseObject(ids[5], AccountSiteInfoDto.class));
                        }
                    }
                });
            }
        });
    }

    public void loadAccTree(TreeView accountTree) {
        TreeItem<TreeItemDto> root = new TreeItem<>(new TreeItemDto("Root", null));
        accountTree.setRoot(root);
        try {
            List tree = accountService.loadAccountTree(new HashMap() {{
                put("electricBureau", electricBureauText.getText());
                put("substation", substationText.getText());
                if (deviceTypeChoiceBox.getValue() != null) {
                    put("deviceType", getDeviceTypeValue(String.valueOf(deviceTypeChoiceBox.getValue())));
                }
                if (voltageLevelChoiceBox.getValue() != null) {
                    put("voltageLevel", getvoltageLevelValue(String.valueOf(voltageLevelChoiceBox.getValue())));
                }
                put("deviceName", deviceNameText.getText());
                if (testTechnologyChoiceBox.getValue() != null && !"请选择".equals("" + testTechnologyChoiceBox.getValue())) {
                    put("testTechnology", testTechnologyChoiceBox.getValue());
                }
                if (produceDateDatePicker.getValue() != null) {
                    put("produceDate", produceDateDatePicker.getValue().format(dateTimeFormatter));
                }
                if (useDateDatePicker.getValue() != null) {
                    put("useDate", useDateDatePicker.getValue().format(dateTimeFormatter));
                }
            }}, "electricBureau", "substation", "voltageLevel", "deviceType", "deviceName");
            loadAccountTree(root, tree);
            root.setExpanded(true);
        } catch (Exception e) {
            log.error("台账树加载失败!", e);
            ialert.error("台账树加载失败!");
        }
    }

    public void loadAccountTree(TreeItem root, List<ZTreeNodeDto> tree) {
        synchronized (TreeItem.class) {
            if (tree != null) {
                tree.forEach(t -> {
                    if (t instanceof ZTreeNodeDto) {
                        ZTreeNodeDto zTreeNodeDto = t;
                        String fullName = "";
                        TreeItem treeItem;
                        if (zTreeNodeDto.getChildren() != null && zTreeNodeDto.isParent() && zTreeNodeDto.getChildren().size() > 0) {
                            treeItem = new TreeItem(new TreeItemDto(zTreeNodeDto.getName(), fullName, zTreeNodeDto));
                            root.getChildren().add(treeItem);
                            loadAccountTree(treeItem, zTreeNodeDto.getChildren());
                        } else {
                            treeItem = new TreeItem(new TreeItemDto(zTreeNodeDto.getName(), zTreeNodeDto.getId(), zTreeNodeDto));
                            root.getChildren().add(treeItem);
                        }
                        treeItem.setExpanded(treeSet.contains(zTreeNodeDto.getId()));
                    }
                });
            }
        }
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
        refreshTree();
    }

    @Override
    public void dispose() {
        log.info("dispose..");
        siteMap.remove("" + currentAccountId);
        currentAccountId = null;
    }


    @FXML
    public void switchCheckAll() {
        CheckBox[] checkBoxes = new CheckBox[]{qzCheckBox, qxCheckBox, hsCheckBox, hzCheckBox, hxCheckBox, csCheckBox, czCheckBox, cxCheckBox};
        for (int i = 0; i < checkBoxes.length; i++) {
            if (!checkBoxes[i].isDisable()) {
                checkBoxes[i].setSelected(true);
            }
        }
    }

    @FXML
    public void switchClearAll() {
        CheckBox[] checkBoxes = new CheckBox[]{qzCheckBox, qxCheckBox, hsCheckBox, hzCheckBox, hxCheckBox, csCheckBox, czCheckBox, cxCheckBox};
        for (int i = 0; i < checkBoxes.length; i++) {
            if (!checkBoxes[i].isDisable()) {
                checkBoxes[i].setSelected(false);
            }
        }
    }

    @FXML
    public void otherCheckAll() {
        otherCheck(true);
    }

    @FXML
    public void otherClearAll() {
        otherCheck(false);
    }

    void otherCheck(boolean flag) {
        if (flag) {
            uhfPane.setVisible(true);
            uhfPane.setManaged(true);
            aePane.setVisible(true);
            aePane.setManaged(true);
            hfctPane.setVisible(true);
            hfctPane.setManaged(true);
        }
        uhfCheckBox.setSelected(flag);
        aeCheckBox.setSelected(flag);
        hfctCheckBox.setSelected(flag);
        setSelectDate(uhfFlowPane, flag);
        setSelectDate(aeFlowPane, flag);
        setSelectDate(hfctFlowPane, flag);
    }

    void setSelectDate(FlowPane flowPane, boolean flag) {
        if (flag) {
            flowPane.setVisible(flag);
            flowPane.setManaged(flag);
        }
        flowPane.getChildren().forEach(node -> {
            if (node instanceof HBox) {
                HBox hBox = (HBox) node;
                hBox.getChildren().forEach(node1 -> {
                    if (node1 instanceof CheckBox) {
                        CheckBox checkBox = (CheckBox) node1;
                        checkBox.setSelected(flag);
                    }
                });
            }
        });
    }
}