package com.redphase.controller.task;

import com.Application;
import com.redphase.api.task.IAccountSiteInfoService;
import com.redphase.controller.BaseController;
import com.redphase.controller.account.AccountController;
import com.redphase.dto.account.AccountDto;
import com.redphase.dto.sys.SysVariableDto;
import com.redphase.dto.task.AccountSiteInfoDto;
import com.redphase.dto.user.UserDto;
import com.redphase.framework.annotation.auth.RequiresPermissions;
import com.redphase.framework.util.CommonConstant;
import com.redphase.framework.util.I18nUtil;
import com.redphase.framework.util.IdUtil;
import com.redphase.framework.util.ValidatorUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@FXMLController
@Slf4j
public class AccountSiteInfoController extends BaseController implements Initializable {
    @Autowired
    private IAccountSiteInfoService accountSiteInfoService;
    @Autowired
    private AccountController accountController;
    @FXML
    private BorderPane accountSiteOtherPane;
    @FXML
    private AnchorPane accountSiteSwitchPane;
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
    VBox otherVBox;
    @FXML
    VBox otherAnchorVBoxPane;
    @FXML
    ScrollPane otherScrollPane;
    @FXML
    AnchorPane otherAnchorPane;
    @FXML
    CheckBox uhfCheckBox;
    @FXML
    CheckBox aeCheckBox;
    @FXML
    CheckBox hfctCheckBox;
    @FXML
    Pane uhfPane;
    @FXML
    FlowPane uhfFlowPane;
    Set<String> uhfSet = new HashSet<>();
    @FXML
    Pane aePane;
    @FXML
    FlowPane aeFlowPane;
    Set<String> aeSet = new HashSet<>();
    @FXML
    Pane hfctPane;
    @FXML
    FlowPane hfctFlowPane;
    Set<String> hfctSet = new HashSet<>();
    @FXML
    Button siteOtherQRButton;
    @FXML
    Button siteSwitchQRButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize...");
        if (accountSiteSwitchPane != null) {
            accountSiteSwitchPane.prefWidthProperty().bind(Application.getStage().widthProperty().subtract(395));
        }
        if (accountSiteOtherPane != null) {
            accountSiteOtherPane.prefWidthProperty().bind(Application.getStage().widthProperty().subtract(395).subtract(5));
            accountSiteOtherPane.prefHeightProperty().bind(Application.getStage().heightProperty().subtract(80));
            otherVBox.prefWidthProperty().bind(accountSiteOtherPane.prefWidthProperty().subtract(30));
            otherVBox.prefHeightProperty().bind(accountSiteOtherPane.prefHeightProperty().subtract(130));
            otherScrollPane.prefWidthProperty().bind(otherVBox.prefWidthProperty());
            otherScrollPane.prefHeightProperty().bind(otherVBox.prefHeightProperty().subtract(80));
            otherAnchorPane.prefWidthProperty().bind(otherScrollPane.prefWidthProperty().subtract(10));
            otherAnchorPane.minHeightProperty().bind(otherScrollPane.prefHeightProperty().subtract(3));
            otherAnchorVBoxPane.prefWidthProperty().bind(otherAnchorPane.prefWidthProperty().subtract(10));
            otherAnchorVBoxPane.minHeightProperty().bind(otherAnchorPane.prefHeightProperty());
        }
        UserDto userDto = jwtUtil.getSubject();
        if (userDto != null) {
            if (userDto.getType() >= 2) {
                if (siteOtherQRButton != null) siteOtherQRButton.setVisible(true);
                if (siteSwitchQRButton != null) siteSwitchQRButton.setVisible(true);
            }
        }
    }

    private void hideTestTechnologySite(Pane... paneArr) {
        for (Pane pane : paneArr) {
            pane.setVisible(false);
            pane.setManaged(false);
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

    @FXML
    public void switchCheckAll() {
        CheckBox[] checkBoxes = new CheckBox[]{qzCheckBox, qxCheckBox, hsCheckBox, hzCheckBox, hxCheckBox, csCheckBox, czCheckBox, cxCheckBox};
        for (int i = 0; i < checkBoxes.length; i++) {
            checkBoxes[i].setSelected(true);
        }
    }

    @FXML
    public void switchClearAll() {
        CheckBox[] checkBoxes = new CheckBox[]{qzCheckBox, qxCheckBox, hsCheckBox, hzCheckBox, hxCheckBox, csCheckBox, czCheckBox, cxCheckBox};
        for (int i = 0; i < checkBoxes.length; i++) {
            checkBoxes[i].setSelected(false);
        }
    }

    /**
     * 根据台帐ID批量保存 开关柜位置
     */
    @FXML
    public void batchSaveSwitch() {
        try {
            String accountId = (String) ehcacheUtil.getCache("ACCOUNT_ID");
            List<AccountSiteInfoDto> dictDtos = getSwitchDictDtos();
            accountSiteInfoService.batchSaveByAccountId(accountId, dictDtos);
            ialert.success(I18nUtil.get("ialert.success"));
        } catch (Exception e) {
            log.error("根据台帐ID批量开关柜位置保存", e);
            ialert.error(I18nUtil.get("error.sys") + e.getMessage());
        }
    }

    private List<AccountSiteInfoDto> getSwitchDictDtos() {
        accountId = (String) ehcacheUtil.getCache("ACCOUNT_ID");
        List<AccountSiteInfoDto> dictDtos = new ArrayList() {{
            CheckBox[] checkBoxes = new CheckBox[]{qzCheckBox, qxCheckBox, hsCheckBox, hzCheckBox, hxCheckBox, csCheckBox, czCheckBox, cxCheckBox};
            for (int i = 0; i < checkBoxes.length; i++) {
                CheckBox checkBox = checkBoxes[i];
                if (checkBox.isSelected()) {
                    AtomicInteger atomicInteger = new AtomicInteger(i);
                    add(new AccountSiteInfoDto() {{
                        setSiteName(checkBox.getText());
                        setAccountId(accountId);
                        setSortNo(atomicInteger.intValue());
                    }});
                }
            }
        }};
        if (dictDtos != null) {
            dictDtos.sort(Comparator.comparing(AccountSiteInfoDto::getSortNo));
        }
        return dictDtos;
    }

    private List<AccountSiteInfoDto> getOtherDictDtos() {
        accountId = (String) ehcacheUtil.getCache("ACCOUNT_ID");
        List<AccountSiteInfoDto> dictDtos = new ArrayList();
        if (uhfCheckBox.isSelected()) {
            for (String uuid : uhfSet) {
                Label label = (Label) uhfFlowPane.lookup("#" + uuid + "L");
                TextField textField = (TextField) uhfFlowPane.lookup("#" + uuid + "T");
                if (ValidatorUtil.isEmpty(textField.getText())) {
                    label.setStyle(errorLab);
                    isValidFlag[0] = false;
                } else {
                    label.setStyle(successLab);
                    dictDtos.add(new AccountSiteInfoDto() {{
                        setSiteName(textField.getText());
                        setAccountId(accountId);
                        setTestTechnology("1");//uhf
                        setSortNo(Integer.parseInt(uuid.split("@")[1]));
                    }});
                }
            }
        }
        if (aeCheckBox.isSelected()) {
            for (String uuid : aeSet) {
                Label label = (Label) aeFlowPane.lookup("#" + uuid + "L");
                TextField textField = (TextField) aeFlowPane.lookup("#" + uuid + "T");
                if (ValidatorUtil.isEmpty(textField.getText())) {
                    label.setStyle(errorLab);
                    isValidFlag[0] = false;
                } else {
                    label.setStyle(successLab);
                    dictDtos.add(new AccountSiteInfoDto() {{
                        setSiteName(textField.getText());
                        setAccountId(accountId);
                        setTestTechnology("2");//ae
                        setSortNo(Integer.parseInt(uuid.split("@")[1]));
                    }});
                }
            }
        }
        if (hfctCheckBox.isSelected()) {
            for (String uuid : hfctSet) {
                Label label = (Label) hfctFlowPane.lookup("#" + uuid + "L");
                TextField textField = (TextField) hfctFlowPane.lookup("#" + uuid + "T");
                if (ValidatorUtil.isEmpty(textField.getText())) {
                    label.setStyle(errorLab);
                    isValidFlag[0] = false;
                } else {
                    label.setStyle(successLab);
                    dictDtos.add(new AccountSiteInfoDto() {{
                        setSiteName(textField.getText());
                        setAccountId(accountId);
                        setTestTechnology("3");//hfct
                        setSortNo(Integer.parseInt(uuid.split("@")[1]));
                    }});
                }
            }
        }
        if (dictDtos != null) {
            dictDtos.sort(Comparator.comparing(AccountSiteInfoDto::getTestTechnology).thenComparing(AccountSiteInfoDto::getSortNo));
        }
        return dictDtos;
    }

    boolean[] isValidFlag = {true};
    String accountId = null;

    /**
     * 根据台帐ID批量保存 开关柜位置
     */
    @FXML
    public void batchSaveOther() {
        try {
            isValidFlag[0] = true;
            List<AccountSiteInfoDto> dictDtos = getOtherDictDtos();
            if (isValidFlag[0]) {
                accountSiteInfoService.batchSaveByAccountId(accountId, dictDtos);
                ialert.success(I18nUtil.get("ialert.success"));
            }
        } catch (Exception e) {
            log.error("根据台帐ID批量开关柜位置保存", e);
            ialert.error(I18nUtil.get("error.sys") + e.getMessage());
        }
    }

    AtomicInteger uhfSortNo = new AtomicInteger(0);
    AtomicInteger aeSortNo = new AtomicInteger(0);
    AtomicInteger hfctSortNo = new AtomicInteger(0);

    /**
     * 根据台帐ID查询位置列表
     */
    public void findDataIsListByAccountId(int type) {//type 0开关柜 1其它设备
        try {
            if (type == 0) {
                clearSwitch();
            } else {
                uhfSet.clear();
                aeSet.clear();
                hfctSet.clear();
                uhfCheckBox.setSelected(false);
                aeCheckBox.setSelected(false);
                hfctCheckBox.setSelected(false);
                hideTestTechnologySite(uhfPane, aePane, hfctPane);
                uhfFlowPane.getChildren().clear();
                aeFlowPane.getChildren().clear();
                hfctFlowPane.getChildren().clear();
            }

            String accountId = (String) ehcacheUtil.getCache("ACCOUNT_ID");
            AccountSiteInfoDto dto = new AccountSiteInfoDto() {{
                setAccountId(accountId);
            }};
            List<AccountSiteInfoDto> results = accountSiteInfoService.findDataIsList(dto);
            uhfSortNo = new AtomicInteger(0);
            aeSortNo = new AtomicInteger(0);
            hfctSortNo = new AtomicInteger(0);
            if (results != null && results.size() > 0) {
                results.forEach(siteInfoDto -> {
                    if (type == 0) {
                        if (ValidatorUtil.notEmpty(siteInfoDto.getSiteName()))
                            switch (siteInfoDto.getSiteName()) {
                                case "前中":
                                    qzCheckBox.setSelected(true);
                                    break;
                                case "前下":
                                    qxCheckBox.setSelected(true);
                                    break;
                                case "后上":
                                    hsCheckBox.setSelected(true);
                                    break;
                                case "后中":
                                    hzCheckBox.setSelected(true);
                                    break;
                                case "后下":
                                    hxCheckBox.setSelected(true);
                                    break;
                                case "侧上":
                                    csCheckBox.setSelected(true);
                                    break;
                                case "侧中":
                                    czCheckBox.setSelected(true);
                                    break;
                                case "侧下":
                                    cxCheckBox.setSelected(true);
                                    break;
                            }
                    } else {
                        String uuid = IdUtil.createUUID(32);
                        uhfFlowPane.setPrefWidth(900);
                        aeFlowPane.setPrefWidth(900);
                        hfctFlowPane.setPrefWidth(900);
                        if (ValidatorUtil.notEmpty(siteInfoDto.getTestTechnology()))
                            switch (siteInfoDto.getTestTechnology()) {
                                case "1": {//特高频（UHF）
                                    uhfCheckBox.setSelected(true);
                                    showUhfSite();
                                    if (siteInfoDto.getSortNo() != null) {
                                        uhfSortNo = new AtomicInteger(siteInfoDto.getSortNo());
                                    }
                                    uuid += "@" + uhfSortNo.intValue();
                                    uhfSet.add(uuid);
                                    uhfFlowPane.getChildren().add(editSiteHBox("uhf", siteInfoDto, uuid));
                                    uhfSortNo.getAndAdd(1);
                                    break;
                                }
                                case "2": {//AE
                                    aeCheckBox.setSelected(true);
                                    showAeSite();
                                    if (siteInfoDto.getSortNo() != null) {
                                        aeSortNo = new AtomicInteger(siteInfoDto.getSortNo());
                                    }
                                    uuid += "@" + aeSortNo.intValue();
                                    aeSet.add(uuid);
                                    aeFlowPane.getChildren().add(editSiteHBox("ae", siteInfoDto, uuid));
                                    aeSortNo.getAndAdd(1);
                                    break;
                                }
                                case "3": {//高频（HFCT）
                                    hfctCheckBox.setSelected(true);
                                    showHfctSite();
                                    if (siteInfoDto.getSortNo() != null) {
                                        hfctSortNo = new AtomicInteger(siteInfoDto.getSortNo());
                                    }
                                    uuid += "@" + hfctSortNo.intValue();
                                    hfctSet.add(uuid);
                                    hfctFlowPane.getChildren().add(editSiteHBox("hfct", siteInfoDto, uuid));
                                    hfctSortNo.getAndAdd(1);
                                    break;
                                }
                            }
                    }
                });
            }
            if (type != 0) {
                uhfFlowPane.getChildren().addAll(newSiteHBox("addUhf"));
                aeFlowPane.getChildren().addAll(newSiteHBox("addAe"));
                hfctFlowPane.getChildren().addAll(newSiteHBox("addHfct"));
            }
        } catch (Exception e) {
            log.error("根据台帐ID查询位置列表", e);
            ialert.error(I18nUtil.get("error.sys") + e.getMessage());
        }
    }

    private HBox editSiteHBox(String testTechnology, AccountSiteInfoDto siteInfoDto, String uuid) {
        return new HBox() {{
            setAlignment(Pos.CENTER_LEFT);
            setPrefHeight(60);
            setPrefWidth(400);
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
                    new TextField() {{
                        setId(uuid + "T");
                        setPrefHeight(35);
                        setPrefWidth(300);
                        setPromptText("请输入位置名称");
                        if (siteInfoDto != null) {
                            setText(siteInfoDto.getSiteName());
                        }
                    }}
                    ,
                    new ImageView() {{
                        setId(testTechnology + CommonConstant.FEIGN_ERROR_SYMBOL_STRING + uuid);
                        setFitHeight(35);
                        setFitWidth(30);
                        setPickOnBounds(true);
                        setPreserveRatio(true);
                        this.getStyleClass().addAll("btn-white");
                        setImage(new Image("/com/redphase/images/minus-circle-fill.png"));
                        this.setOnMouseClicked((click) -> {
                            FlowPane flowPane = (FlowPane) this.getParent().getParent();
                            flowPane.getChildren().remove(this.getParent());
                            String[] ids = this.getId().split(CommonConstant.FEIGN_ERROR_SYMBOL_STRING);
                            switch (ids[0]) {
                                case "uhf": {
                                    uhfSet.remove(ids[1]);
                                    break;
                                }
                                case "ae": {
                                    aeSet.remove(ids[1]);
                                    break;
                                }
                                case "hfct": {
                                    hfctSet.remove(ids[1]);
                                    break;
                                }
                            }
                        });
                    }}
            );
        }};
    }

    private HBox newSiteHBox(String testTechnology) {
        return new HBox() {{
            setAlignment(Pos.CENTER);
            setPrefHeight(60);
            setPrefWidth(60);
            this.getChildren().addAll(
                    new ImageView() {{
                        setId(testTechnology);
                        setFitHeight(35);
                        setFitWidth(30);
                        setPickOnBounds(true);
                        setPreserveRatio(true);
                        this.getStyleClass().addAll("btn-white");
                        setImage(new Image("/com/redphase/images/plus-circle.png"));
                        this.setOnMouseClicked((click) -> {
                            FlowPane flowPane = (FlowPane) this.getParent().getParent();
                            String uuid = IdUtil.createUUID(32);
                            HBox siteHBox = null;
                            switch (testTechnology) {
                                case "addUhf": {
                                    uuid += "@" + uhfSortNo.intValue();
                                    uhfSet.add(uuid);
                                    siteHBox = editSiteHBox("uhf", null, uuid);
                                    uhfSortNo.getAndAdd(1);
                                    break;
                                }
                                case "addAe": {
                                    uuid += "@" + aeSortNo.intValue();
                                    aeSet.add(uuid);
                                    siteHBox = editSiteHBox("ae", null, uuid);
                                    aeSortNo.getAndAdd(1);
                                    break;
                                }
                                case "addHfct": {
                                    uuid += "@" + hfctSortNo.intValue();
                                    hfctSet.add(uuid);
                                    siteHBox = editSiteHBox("hfct", null, uuid);
                                    hfctSortNo.getAndAdd(1);
                                    break;
                                }
                            }
                            flowPane.getChildren().add(flowPane.getChildren().size() - 1, siteHBox);
                        });
                    }}
            );
        }};
    }

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
        } catch (Exception e) {
        }
    }

    /**
     * 生成位置QR编码2
     */
    @RequiresPermissions("account:qr")
    @FXML
    public void siteOtherQR() {
        log.info("生成位置QR编码....。siteOtherQR。");
        try {
            AccountDto accountDto = accountController.getAccountDto();
            if (accountController.isValidFalg()) {
                SysVariableDto variableDto = setupService.getVariableByCode(new SysVariableDto() {{
                    setCode("i-path-reports");
                }});
                File file = new File(variableDto.getValue());
                String qrPath = file.getParent() + "/QR/";
                File qrDirFile = new File(qrPath);
                if (!qrDirFile.exists()) {
                    qrDirFile.mkdirs();
                }
                List<AccountSiteInfoDto> dictDtos = getOtherDictDtos();
                if (!isValidFlag[0]) {
                    return;
                }
                PrintWriter pw = new PrintWriter(qrPath + "" + accountDto.getElectricBureau() + "_" + accountDto.getSubstation() + "_" + accountDto.getDeviceName() + "_位置编码.txt");
                if (dictDtos != null) {
                    for (AccountSiteInfoDto dictDto : dictDtos) {
                        pw.append(accountDto.getDeviceName() + "_");
                        String testTechnology = "";
                        switch (dictDto.getTestTechnology()) {
                            case "1":
                                testTechnology = "UHF";
                                break;
                            case "2":
                                testTechnology = "AE";
                                break;
                            case "3":
                                testTechnology = "HFCT";
                                break;
                        }
                        pw.append(testTechnology + "_");
                        pw.append(dictDto.getSiteName() + "\r\n");
                    }
                }
                pw.flush();
                pw.close();
                ialert.success(I18nUtil.get("ialert.success"));
            }
        } catch (Exception e) {
            log.error("生成设备QR编码!", e);
            ialert.error(I18nUtil.get("error.sys") + e.getMessage());
        }
    }

    /**
     * 生成位置QR编码1
     */
    @RequiresPermissions("account:qr")
    @FXML
    public void siteSwitchQR() {
        log.info("生成位置QR编码....。siteSwitchQR。");
        try {
            AccountDto accountDto = accountController.getAccountDto();
            if (accountController.isValidFalg()) {
                SysVariableDto variableDto = setupService.getVariableByCode(new SysVariableDto() {{
                    setCode("i-path-reports");
                }});
                File file = new File(variableDto.getValue());
                String qrPath = file.getParent() + "/QR/";
                File qrDirFile = new File(qrPath);
                if (!qrDirFile.exists()) {
                    qrDirFile.mkdirs();
                }
                List<AccountSiteInfoDto> dictDtos = getSwitchDictDtos();
                if (!isValidFlag[0]) {
                    return;
                }
                PrintWriter pw = new PrintWriter(qrPath + "" + accountDto.getElectricBureau() + "_" + accountDto.getSubstation() + "_" + accountDto.getDeviceName() + "_位置编码.txt");
                if (dictDtos != null) {
                    for (AccountSiteInfoDto dictDto : dictDtos) {
                        pw.append(accountDto.getDeviceName() + "_");
                        pw.append(dictDto.getSiteName() + "\r\n");
                    }
                }
                pw.flush();
                pw.close();
                ialert.success(I18nUtil.get("ialert.success"));
            }
        } catch (Exception e) {
            log.error("生成设备QR编码!", e);
            ialert.error(I18nUtil.get("error.sys") + e.getMessage());
        }
    }

    @Override
    public void dispose() {

    }
}