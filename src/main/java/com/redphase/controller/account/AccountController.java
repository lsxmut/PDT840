package com.redphase.controller.account;

import com.Application;
import com.redphase.api.account.IAccountService;
import com.redphase.controller.BaseController;
import com.redphase.controller.task.AccountSiteInfoController;
import com.redphase.dto.TreeItemDto;
import com.redphase.dto.ZTreeNodeDto;
import com.redphase.dto.account.AccountDto;
import com.redphase.dto.sys.SysVariableDto;
import com.redphase.dto.user.UserDto;
import com.redphase.framework.Response;
import com.redphase.framework.annotation.auth.RequiresPermissions;
import com.redphase.framework.util.DateUtil;
import com.redphase.framework.util.I18nUtil;
import com.redphase.framework.util.ValidatorUtil;
import com.redphase.view.account.AccountBaseView;
import com.redphase.view.account.AccountSiteOtherView;
import com.redphase.view.account.AccountSiteSwitchView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

/**
 * 台帐控制器
 */
@FXMLController
@Slf4j
public class AccountController extends BaseController implements Initializable {

    @Autowired
    private AccountSiteInfoController accountSiteInfoController;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private AccountBaseView accountBaseView;
    @Autowired
    private AccountSiteOtherView accountSiteOtherView;
    @Autowired
    private AccountSiteSwitchView accountSiteSwitchView;
    @FXML
    @Getter
    private TreeView accountTree;
    @FXML
    private Button searchButton;
    @FXML
    @Getter
    private Button refreshButton;
    @FXML
    private Button importButton;
    @FXML
    private Button exportButton;
    @FXML
    private Button templateButton;
    @FXML
    private Button backupButton;
    @FXML
    private Button restoreButton;
    @FXML
    @Getter
    private ScrollPane rightAnchorPane;
    @FXML
    private AnchorPane siteAnchorPane;
    @FXML
    private VBox initVBox;
    @FXML
    private TabPane accountBasePane;
    @FXML
    private HBox bHBox;
    @FXML
    private VBox bVBox;
    @FXML
    private HBox sHBox;
    @FXML
    private VBox sVBox;

    @FXML
    TextField idT;//主键id
    @FXML
    Label electricBureauL;//电业局
    @FXML
    TextField electricBureauT;//请输入电力局名称
    @FXML
    Label substationL;//变电站
    @FXML
    TextField substationT;//请输入变电站名称
    @FXML
    Label substationCodeL;//变电站编码
    @FXML
    TextField substationCodeT;//请输入变电站编码
    @FXML
    Label deviceTypeL;//设备类型
    @FXML
    ChoiceBox deviceTypeT;//选择设备类型
    @FXML
    Label spaceNameL;//间隔名称
    @FXML
    TextField spaceNameT;//请输入间隔名称
    @FXML
    Label spaceCodeL;//间隔编码
    @FXML
    TextField spaceCodeT;//请输入间隔编码
    @FXML
    Label deviceNameL;//设备名称
    @FXML
    TextField deviceNameT;//请输入设备名称
    @FXML
    Label deviceCodeL;//设备编码
    @FXML
    TextField deviceCodeT;//请输入设备编码
    @FXML
    Label voltageLevelL;//电压等级
    @FXML
    ChoiceBox voltageLevelT;//选择电压等级
    @FXML
    Label manufacturerL;//制造商
    @FXML
    TextField manufacturerT;//请输入制造商
    @FXML
    Label deviceVersionL;//设备型号
    @FXML
    TextField deviceVersionT;//请输入设备型号
    @FXML
    Label runDeptL;//运行单位
    @FXML
    TextField runDeptT;//请输入运行单位
    @FXML
    Label runStateL;//运行状态
    @FXML
    TextField runStateT;//请输入运行状态
    @FXML
    Label produceDateL;//出厂日期
    @FXML
    DatePicker produceDateT;//点击输入出厂日期
    @FXML
    Label useDateL;//投运日期
    @FXML
    DatePicker useDateT;//点击输入投运日期
    @FXML
    Label outageDateL;//停运日期
    @FXML
    DatePicker outageDateT;//点击输入停运日期
    @FXML
    Button moveStepUpButton;
    @FXML
    Button moveStepDownButton;
    @FXML
    Button moveTopButton;
    @FXML
    Button moveBottomButton;
    @FXML
    Button updateButton;
    @FXML
    Button addButton;
    @FXML
    Button delButton;
    @FXML
    Button deviceQRButton;

    Set<String> treeSet = new HashSet<>();

    boolean initFlag = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize...");
        if (initFlag) return;
        initFlag = true;
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

        rightAnchorPane.prefWidthProperty().bind(Application.getStage().widthProperty().subtract(395));
        rightAnchorPane.prefHeightProperty().bind(Application.getStage().heightProperty().subtract(90));
        initVBox.prefWidthProperty().bind(Application.getStage().widthProperty().subtract(395));
        initVBox.prefHeightProperty().bind(Application.getStage().heightProperty().subtract(90));
        accountTree.prefHeightProperty().bind(Application.getScene().heightProperty().subtract(160));
        hideNodeView();
        closeSet.setOnMouseClicked(event -> hideNodeView());
        Platform.runLater(() -> {
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
                        currentZTreeNodeDto = zTreeNodeDto;
                        if (currentTreeItem.getChildren().size() > 0) {
                            currentTreeItem.setExpanded(!currentTreeItem.isExpanded());
                            if (currentTreeItem.isExpanded()) {
                                treeSet.add(zTreeNodeDto.getId());
                            } else {
                                treeSet.remove(zTreeNodeDto.getId());
                            }
                        }
                        if (object instanceof TreeItemDto && currentTreeItem.getChildren().size() == 0) {
//                            log.debug("treeItemDto=>{}",zTreeNodeDto);
                            setAccountPane((AccountDto) zTreeNodeDto.getData());
                        }
                    }
                } catch (Exception e) {
                    log.error("系统异常!", e);
                    ialert.error(I18nUtil.get("error.sys") + e.getMessage());
                }
            });
//            deviceTypeT.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//                try {
//                    if (newValue == null) {
//                        return;
//                    }
//                    if (!newValue.equals(oldValue) && !"请选择".equals(newValue)) {
//                        AccountDto accountDto = null;
//                        if (currentZTreeNodeDto != null && currentZTreeNodeDto.getData() instanceof AccountDto) {
//                            accountDto = (AccountDto) currentZTreeNodeDto.getData();
//                            ehcacheUtil.setCache("ACCOUNT_ID", "" + accountDto.getId());
//                        }
//                        switch ("" + newValue) {
//                            case "开关柜": {
//                                sVBox.getChildren().setAll(accountSiteSwitchView.getView());
//                                if (accountDto != null) {
//                                    accountSiteInfoController.findDataIsListByAccountId(0);
//                                }
//                                break;
//                            }
//                            default:
//                                sVBox.getChildren().setAll(accountSiteOtherView.getView());
//                                if (accountDto != null) {
//                                    accountSiteInfoController.findDataIsListByAccountId(1);
//                                }
//                                break;
//                        }
//                    }
//                } catch (Exception e) {
//                    log.error("系统异常!", e);
//                    ialert.error(I18nUtil.get("error.sys") + e.getMessage());
//                }
//            });
        });
    }

    /**
     * 根据ID查询台帐数据
     */
    @RequiresPermissions("account:info")
    public void info(String id) {
        log.info("根据ID查询台帐数据......");
        try {
            AccountDto result = accountService.findDataById(Long.valueOf(id));
            setAccountPane(result);
        } catch (Exception e) {
            log.error("根据ID查询台帐数据!", e);
            ialert.error(I18nUtil.get("error.sys") + e.getMessage());
        }
    }

    @Getter
    boolean isValidFalg = true;

    public AccountDto getAccountDto() {
        isValidFalg = true;
        AccountDto accountDto = new AccountDto() {{
            if (ValidatorUtil.notEmpty(idT.getText()))
                setId(Long.parseLong(idT.getText()));
            if (isValidDto(electricBureauL, electricBureauT.getText()))
                setElectricBureau(electricBureauT.getText());//请输入电力局名称
            if (isValidDto(substationL, substationT.getText()))
                setSubstation(substationT.getText());//请输入变电站名称
            setSubstationCode(substationCodeT.getText());//请输入变电站编码
            Integer deviceType = getDeviceTypeValue(String.valueOf(deviceTypeT.getValue()));
            if (isValidDto(deviceTypeL, String.valueOf(deviceTypeT.getValue())))
                setDeviceType(deviceType);//选择设备类型
            setSpaceName(spaceNameT.getText());//请输入间隔名称
            setSpaceCode(spaceCodeT.getText());//请输入间隔编码
            if (isValidDto(deviceNameL, deviceNameT.getText()))
                setDeviceName(deviceNameT.getText());//请输入设备名称
            setDeviceCode(deviceCodeT.getText());//请输入设备编码
            Integer voltageLevel = getvoltageLevelValue(String.valueOf(voltageLevelT.getValue()));
            if (isValidDto(voltageLevelL, String.valueOf(voltageLevelT.getValue())))
                setVoltageLevel(voltageLevel);//选择电压等级
            setManufacturer(manufacturerT.getText());//请输入制造商
            if (isValidDto(deviceVersionL, deviceVersionT.getText()))
                setDeviceVersion(deviceVersionT.getText());//请输入设备型号
            setRunDept(runDeptT.getText());//请输入运行单位
            if (isValidDto(runStateL, runStateT.getText()))
                setRunState(runStateT.getText());//请输入运行状态
            if (produceDateT.getValue() != null)
                setProduceDate(DateUtil.parseDate(produceDateT.getValue().format(dateTimeFormatter)));//点击输入出厂日期
            if (useDateT.getValue() != null)
                setUseDate(DateUtil.parseDate(useDateT.getValue().format(dateTimeFormatter)));//点击输入投运日期
            if (outageDateT.getValue() != null)
                setOutageDate(DateUtil.parseDate(outageDateT.getValue().format(dateTimeFormatter)));//点击输入停运日期
        }};

        //出厂日期早于投运日期，投运日期早于停运日期——优化
        long produceDate = -1;
        long useDate = -1;
        long outageDate = -1;
        if (accountDto.getProduceDate() != null) {
            produceDate = accountDto.getProduceDate().getTime();
        }
        if (accountDto.getUseDate() != null) {
            useDate = accountDto.getUseDate().getTime();
        }
        if (accountDto.getOutageDate() != null) {
            outageDate = accountDto.getOutageDate().getTime();
        }
        if (outageDate > 0 && useDate > 0 && outageDate < useDate) {
            useDateL.setStyle(errorLab);
            outageDateL.setStyle(errorLab);
            isValidFalg = false;
        } else {
            useDateL.setStyle(successLab);
            outageDateL.setStyle(successLab);
        }
        if (produceDate > 0 && useDate > 0 && useDate < produceDate) {
            produceDateL.setStyle(errorLab);
            useDateL.setStyle(errorLab);
            isValidFalg = false;
        } else {
            produceDateL.setStyle(successLab);
            useDateL.setStyle(successLab);
        }
        return accountDto;
    }

    private boolean isValidDto(Label label, String value) {
        boolean successFlag = true;
        if (ValidatorUtil.isEmpty(value) || "请选择".equalsIgnoreCase(value)) {
            isValidFalg = false;
            label.setStyle(errorLab);
            successFlag = false;
        } else {
            label.setStyle(successLab);
        }
        return successFlag;
    }

    /**
     * 新增台帐
     */
    @RequiresPermissions("account:add")
    @FXML
    public void addAccount() {
        log.info("新增台帐......");
        try {
            AccountDto accountDto = getAccountDto();
            if (isValidFalg) {
                accountService.addAccount(accountDto);
                ialert.success(I18nUtil.get("ialert.success"));
                refreshTree();
            }
        } catch (Exception e) {
            log.error("新增台帐!", e);
            ialert.error(I18nUtil.get("error.sys") + e.getMessage());
        }
    }

    /**
     * 修改台帐
     */
    @RequiresPermissions("account:edit")
    @FXML
    public void updateAccount() {
        log.info("修改台帐......");
        try {
            AccountDto accountDto = getAccountDto();
            if (isValidFalg) {
                accountService.updateAccount(accountDto);
                ialert.success(I18nUtil.get("ialert.success"));
                refreshTree();
            }
        } catch (Exception e) {
            log.error("修改台帐!", e);
            ialert.error(I18nUtil.get("error.sys") + e.getMessage());
        }
    }

    /**
     * 开关柜上移
     */
    @RequiresPermissions("account:edit")
    @FXML
    public void moveStepUp() {
        log.info("开关柜上移......");
        try {
            Long accountId = Long.parseLong(idT.getText());
            Integer tag = 1;
            accountService.moveStep(accountId, tag);
            ialert.success(I18nUtil.get("ialert.success"));
            refreshTree();
        } catch (Exception e) {
            log.error("开关柜上移!", e);
            ialert.error(I18nUtil.get("error.sys") + e.getMessage());
        }
    }

    /**
     * 开关柜下移
     */
    @RequiresPermissions("account:edit")
    @FXML
    public void moveStepDown() {
        log.info("开关柜下移......");
        try {
            Long accountId = Long.parseLong(idT.getText());
            Integer tag = 2;
            accountService.moveStep(accountId, tag);
            ialert.success(I18nUtil.get("ialert.success"));
            refreshTree();
        } catch (Exception e) {
            log.error("开关柜下移!", e);
            ialert.error(I18nUtil.get("error.sys") + e.getMessage());
        }
    }

    /**
     * 开关柜置顶
     */
    @RequiresPermissions("account:edit")
    @FXML
    public void moveTop() {
        log.info("开关柜置顶......");
        try {
            String accountId = idT.getText();
            accountService.moveTop(Long.valueOf(accountId));
            ialert.success(I18nUtil.get("ialert.success"));
            refreshTree();
        } catch (Exception e) {
            log.error("开关柜置顶!", e);
            ialert.error(I18nUtil.get("error.sys") + e.getMessage());
        }
    }

    /**
     * 开关柜置底
     */
    @RequiresPermissions("account:edit")
    @FXML
    public void moveBottom() {
        log.info("开关柜置底......");
        try {
            String accountId = idT.getText();
            accountService.moveBottom(Long.valueOf(accountId));
            ialert.success(I18nUtil.get("ialert.success"));
            refreshTree();
        } catch (Exception e) {
            log.error("开关柜置底!", e);
            ialert.error(I18nUtil.get("error.sys") + e.getMessage());
        }
    }

    /**
     * 导入台帐
     */
    @RequiresPermissions("account:add")
    @FXML
    public void importAccount() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("请选择excel文件");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("excel files (*.xls,*.xlsx)", "*.xls", "*.xlsx");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showOpenDialog(Application.getStage());
            if (file == null || !file.exists()) {
                ialert.warning("未选择正确的台账excel文件!");
                return;
            }
            Response result = accountService.importAccount(file);
            ialert.success(I18nUtil.get("ialert.success") + result.message);
            refreshTree();
        } catch (Exception e) {
            log.error("导入台帐!", e);
            ialert.error(I18nUtil.get("error.sys") + e.getMessage());
        }
    }

    ZTreeNodeDto currentZTreeNodeDto = null;

    /**
     * 导出台帐
     *
     * @return
     */
    @RequiresPermissions("account:page")
    @FXML
    public void exportAccount() {
        log.info("导出台帐......");
        try {
//            if (currentZTreeNodeDto == null) {
//                ialert.warning("请选择需要导出的台账树!");
//                return;
//            }
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("请选择导出目录");
            File dir = directoryChooser.showDialog(Application.getStage());
            if (dir == null || !dir.exists() || !dir.isDirectory()) {
                ialert.warning("未正确选择导出目录!");
                return;
            } else {
                //{id:o.id,isParent:o.isParent}
                accountService.exportAccount(dir, currentZTreeNodeDto);
            }
            ialert.success(I18nUtil.get("ialert.success"));
            refreshTree();
        } catch (Exception e) {
            log.error("导出台帐!", e);
            ialert.error(I18nUtil.get("error.sys") + e.getMessage());
        }
    }

    /**
     * 导出模板
     *
     * @return
     */
    @FXML
    public void exportTemplate() {
        log.info("导出台帐模板......");
        try {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("请选择导出目录");
            File dir = directoryChooser.showDialog(Application.getStage());
            if (dir == null || !dir.exists() || !dir.isDirectory()) {
                ialert.warning("未正确选择导出目录!");
                return;
            } else {
                accountService.copyTemplate(dir, "/台帐模板.xls");
            }
            ialert.success(I18nUtil.get("ialert.success"));
        } catch (Exception e) {
            log.error("导出台帐模板!", e);
            ialert.error(I18nUtil.get("error.sys") + e.getMessage());
        }
    }

    /**
     * 删除台帐
     *
     * @return
     */
    @RequiresPermissions("account:del")
    @FXML
    public void deleteAccount() {
        log.info("删除台帐......");
        try {
            String id = idT.getText();
            accountService.deleteAccount(Long.valueOf(id));
            ialert.success(I18nUtil.get("ialert.success"));
            refreshTree();
        } catch (Exception e) {
            log.error("删除台帐!", e);
            ialert.error(I18nUtil.get("error.sys") + e.getMessage());
        }
    }

    /**
     * 生成设备QR编码
     */
    @RequiresPermissions("account:qr")
    @FXML
    public void deviceQR() {
        log.info("生成设备QR编码......");
        try {
            AccountDto accountDto = getAccountDto();
            if (isValidFalg) {
                SysVariableDto variableDto = setupService.getVariableByCode(new SysVariableDto() {{
                    setCode("i-path-reports");
                }});
                File file = new File(variableDto.getValue());
                String qrPath = file.getParent() + "/QR/";
                File qrDirFile = new File(qrPath);
                if (!qrDirFile.exists()) {
                    qrDirFile.mkdirs();
                }
                PrintWriter pw = new PrintWriter(qrPath + "" + accountDto.getElectricBureau() + "_" + accountDto.getSubstation() + "_" + accountDto.getDeviceName() + "_设备编码.txt");
                pw.append(accountDto.getElectricBureau() + "\r\n");
                pw.append(accountDto.getSubstation() + "\r\n");
                String deviceType = "";
                switch (accountDto.getDeviceType()) {
                    case 1:
                        deviceType = "开关柜";
                        break;
                    case 2:
                        deviceType = "变压器";
                        break;
                    case 3:
                        deviceType = "组合电器";
                        break;
                    case 4:
                        deviceType = "电缆";
                        break;
                }
                pw.append(deviceType + "\r\n");
                pw.append(accountDto.getVoltageLevel() + "kV\r\n");
                pw.append(accountDto.getDeviceName() + "\r\n");
                pw.flush();
                pw.close();
                ialert.success(I18nUtil.get("ialert.success"));
            }
        } catch (Exception e) {
            log.error("生成设备QR编码!", e);
            ialert.error(I18nUtil.get("error.sys") + e.getMessage());
        }
    }

//    public String findDataIsListDirId(String dirId) {
//        Response result = new Response(0, "success");
//        try {
//            if (StringUtils.isEmpty(dirId)) {
//                result = Response.error("台帐父节点ID不能为空!");
//            } else {
//                AccountDto account = new AccountDto() {{
//                    setDirId(Long.valueOf(dirId));
//                }};
//                result.data = accountService.findDataIsListDirId(account);
//            }
//        } catch (Exception e) {
//            result = Response.error(e.getMessage());
//        }
//        return JSONObject.toJSONString(result);
//    }


    @FXML
    public void refreshTree() {
        refreshButton.setDisable(true);
        loadAccTree(accountTree);
        refreshButton.setDisable(false);
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
            if (tree != null && tree.size() > 0) {
                showEditAccountBase();
            } else {
                initVBox.setVisible(true);
            }
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
    public void showEditAccountBase() {
        rightAnchorPane.setContent(accountBaseView.getView());
        accountBasePane.prefWidthProperty().bind(rightAnchorPane.prefWidthProperty().subtract(5));
        accountBasePane.prefHeightProperty().bind(rightAnchorPane.prefHeightProperty().subtract(5));
        bHBox.prefWidthProperty().bind(accountBasePane.prefWidthProperty());
        sHBox.prefHeightProperty().bind(accountBasePane.prefHeightProperty());
        if (deviceTypeT != null)
            deviceTypeT.setItems(FXCollections.observableArrayList("请选择", "开关柜", "变压器", "组合电器", "电缆"));
        if (voltageLevelT != null)
            voltageLevelT.setItems(FXCollections.observableArrayList("请选择", "10kV", "35kV", "110kV", "220kV", "500kV"));
    }

    private void setAccountPane(AccountDto result) {
        if (result == null) {
            updateButton.setDisable(true);
            delButton.setDisable(true);
            moveStepUpButton.setDisable(true);
            moveStepDownButton.setDisable(true);
            moveTopButton.setDisable(true);
            moveBottomButton.setDisable(true);
            deviceQRButton.setVisible(false);
            return;
        }
        UserDto userDto = jwtUtil.getSubject();
        if (userDto != null) {
            if (userDto.getType() >= 2) {
                if (deviceQRButton != null) deviceQRButton.setVisible(true);
            }
        }
        updateButton.setDisable(false);
        delButton.setDisable(false);
        moveStepUpButton.setDisable(false);
        moveStepDownButton.setDisable(false);
        moveTopButton.setDisable(false);
        moveBottomButton.setDisable(false);
        clearText();
        idT.setText("" + result.getId());
        ehcacheUtil.setCache("ACCOUNT_ID", "" + result.getId());
        electricBureauT.setText(result.getElectricBureau());//请输入电力局名称
        substationT.setText(result.getSubstation());//请输入变电站名称
        substationCodeT.setText(result.getSubstationCode());//请输入变电站编码
        deviceTypeT.getSelectionModel().select((int) result.getDeviceType());//选择设备类型
        spaceNameT.setText(result.getSpaceName());//请输入间隔名称
        spaceCodeT.setText(result.getSpaceCode());//请输入间隔编码
        deviceNameT.setText(result.getDeviceName());//请输入设备名称
        deviceCodeT.setText(result.getDeviceCode());//请输入设备编码

        int voltageLeve = 0;
        switch (result.getVoltageLevel()) {
            case 10:
                voltageLeve = 1;
                break;
            case 35:
                voltageLeve = 2;
                break;
            case 110:
                voltageLeve = 3;
                break;
            case 220:
                voltageLeve = 4;
                break;
            case 500:
                voltageLeve = 5;
                break;
            default:
                voltageLeve = 0;
        }
        voltageLevelT.getSelectionModel().select(voltageLeve);//选择电压等级
        manufacturerT.setText(result.getManufacturer());//请输入制造商
        deviceVersionT.setText(result.getDeviceVersion());//请输入设备型号
        runDeptT.setText(result.getRunDept());//请输入运行单位
        runStateT.setText(result.getRunState());//请输入运行状态
        if (result.getProduceDate() != null)
            produceDateT.setValue(getLocalDate(result.getProduceDate()));//点击输入出厂日期
        if (result.getUseDate() != null)
            useDateT.setValue(getLocalDate(result.getUseDate()));//点击输入投运日期
        if (result.getOutageDate() != null)
            outageDateT.setValue(getLocalDate(result.getOutageDate()));//点击输入停运日期

        if (result.getDeviceType() == 1) {
            sVBox.getChildren().setAll(accountSiteSwitchView.getView());
            accountSiteInfoController.findDataIsListByAccountId(0);
        } else {
            sVBox.getChildren().setAll(accountSiteOtherView.getView());
            accountSiteInfoController.findDataIsListByAccountId(1);
        }

    }

    private LocalDate getLocalDate(Date date) {
        LocalDate localDate = LocalDate.parse(DateUtil.getDateStr(date));
        return localDate;
    }

    private void clearText() {
        idT.setText(null);
        electricBureauT.setText(null);//请输入电力局名称
        substationT.setText(null);//请输入变电站名称
        substationCodeT.setText(null);//请输入变电站编码
        deviceTypeT.getSelectionModel().select(0);//选择设备类型
        spaceNameT.setText(null);//请输入间隔名称
        spaceCodeT.setText(null);//请输入间隔编码
        deviceNameT.setText(null);//请输入设备名称
        deviceCodeT.setText(null);//请输入设备编码
        voltageLevelT.getSelectionModel().select(0);//选择电压等级
        manufacturerT.setText(null);//请输入制造商
        deviceVersionT.setText(null);//请输入设备型号
        runDeptT.setText(null);//请输入运行单位
        runStateT.setText(null);//请输入运行状态
        produceDateT.setValue(null);//点击输入出厂日期
        useDateT.setValue(null);//点击输入投运日期
        outageDateT.setValue(null);//点击输入停运日期
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

    /**
     * 备份台账
     */
    @FXML
    public void backupAccount() {
        log.info("备份台账......");
        try {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("请选择备份目录");
            File dir = directoryChooser.showDialog(Application.getStage());
            if (dir == null || !dir.exists() || !dir.isDirectory()) {
                ialert.warning("未正确选择备份目录!");
                return;
            } else {
                //{id:o.id,isParent:o.isParent}
                accountService.backupAccount(dir, currentZTreeNodeDto);
            }
            ialert.success(I18nUtil.get("ialert.success"));
            refreshTree();
        } catch (Exception e) {
            log.error("备份台账!", e);
            ialert.error(I18nUtil.get("error.sys") + e.getMessage());
        }
    }

    /**
     * 还原台账
     */
    @FXML
    public void restoreAccount() {
        log.info("还原台账......");
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("请选择bak文件");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("backup files (*.bak)", "*.bak");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showOpenDialog(Application.getStage());
            if (file == null || !file.exists()) {
                ialert.warning("未选择正确的台账backup文件!");
                return;
            }
            Response result = accountService.restoreAccount(file);
            ialert.success(I18nUtil.get("ialert.success") + result.message);
            refreshTree();
        } catch (Exception e) {
            log.error("还原台帐!", e);
            ialert.error(I18nUtil.get("error.sys") + e.getMessage());
        }
    }
}
