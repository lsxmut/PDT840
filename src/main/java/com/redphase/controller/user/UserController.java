package com.redphase.controller.user;

import com.redphase.controller.BaseController;
import com.redphase.dto.user.UserDto;
import com.redphase.framework.PageDto;
import com.redphase.framework.annotation.auth.Logical;
import com.redphase.framework.annotation.auth.RequiresPermissions;
import com.redphase.framework.security.MD5;
import com.redphase.framework.util.CommonConstant;
import com.redphase.framework.util.I18nUtil;
import com.redphase.framework.util.JwtUtil;
import com.redphase.framework.util.ValidatorUtil;
import com.redphase.service.user.UserService;
import com.redphase.view.DataButtonCell;
import com.redphase.view.DataTableCell;
import com.redphase.view.PageBar;
import com.redphase.view.user.UserEditView;
import com.redphase.view.user.UserPwdView;
import com.redphase.view.user.UserView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

@FXMLController
@Slf4j
@Data
public class UserController extends BaseController implements Initializable {
    @Autowired
    UserEditView userEditView;
    @Autowired
    UserView userView;
    @Autowired
    UserPwdView userPwdView;
    @FXML
    TableView<UserDto> dataTableView;
    @FXML
    TableColumn seqTableColumn;//序号
    @FXML
    TableColumn nameTableColumn;//姓名
    @FXML
    TableColumn accountTableColumn;//账号
    @FXML
    TableColumn jobTableColumn;//工号
    @FXML
    TableColumn roleTableColumn;//角色
    @FXML
    TableColumn stateTableColumn;//状态
    @FXML
    TableColumn dateCreatedTableColumn;//创建时间
    @FXML
    TableColumn operTableColumn;//操作
    @FXML
    DatePicker dateBeginT;
    @FXML
    DatePicker dateEndT;
    @FXML
    TextField keywordT;
    @FXML
    FlowPane pageBar;

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @FXML
    TextField idT;
    @FXML
    TextField accountT;
    @FXML
    TextField nameT;
    @FXML
    TextField jobNoT;
    @FXML
    PasswordField pwdT;
    @FXML
    PasswordField oldPwdT;
    @FXML
    PasswordField newPwdT;
    @FXML
    PasswordField pwdConfirmT;
    @FXML
    ChoiceBox typeT;
    @FXML
    ChoiceBox stateT;
    @FXML
    Pane pwdPane;

    Integer pageNum;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize...");
        pageNum = 1;
        loadPage();
    }

    //    @RequiresPermissions("user:info")
    public void info(long id) {
        log.info("info.........");
        try {
            UserDto userDto = userService.findDataById(new UserDto() {{
                setId(id);
            }});
            showInfo(userDto);
        } catch (Exception e) {
            ialert.error(e.getMessage());
            return;
        }
    }

    private String getType(int type) {
        String value = null;
        switch (type) {
            case 0:
                value = "初级用户";
                break;
            case 1:
                value = "中级用户";
                break;
            case 2:
                value = "高级用户";
                break;
            case 3:
                value = "超级管理员";
                break;
        }
        return value;
    }

    private void showInfo(UserDto userDto) {
        UserDto currentUserDto = jwtUtil.getSubject();

        ObservableList<String> typeItems = FXCollections.observableArrayList(new ArrayList<String>() {{
            if (currentUserDto.getType() > 0) add("初级用户");
            if (currentUserDto.getType() > 1) add("中级用户");
            if (currentUserDto.getType() > 2) add("高级用户");
            if (currentUserDto.getType() > 3) add("超级管理员");
        }});
        typeT.getItems().setAll(typeItems);
        stateT.getItems().setAll(FXCollections.observableArrayList(new ArrayList<String>() {{
            add("启用");
            add("禁用");
        }}));
        restFrom();
        if (userDto != null) {
            typeItems.add(getType(userDto.getType()));
            typeT.getItems().setAll(typeItems);
            idT.setText("" + userDto.getId());
            accountT.setText(userDto.getAccount());
            accountT.setDisable(true);
            nameT.setText(userDto.getName());
            jobNoT.setText(userDto.getJobNo());
            typeT.setValue(getType(userDto.getType()));
            stateT.getSelectionModel().select(userDto.getState() == 0 ? "启用" : "禁用");
            pwdPane.setVisible(false);
            if (currentUserDto.getAccount().equalsIgnoreCase(userDto.getAccount())) {
                typeT.setDisable(true);
                stateT.setDisable(true);
            }
        }
    }

    private void restFrom() {
        idT.setText(null);
        accountT.setText(null);
        nameT.setText(null);
        jobNoT.setText(null);
        pwdPane.setVisible(true);
        pwdT.setText(null);
        pwdConfirmT.setText(null);
        typeT.getSelectionModel().select(0);
        stateT.getSelectionModel().select(0);

        accountT.setDisable(false);
        stateT.setDisable(false);
        typeT.setDisable(false);
    }


    /**
     * <li>物理删除。
     */
    @RequiresPermissions("user:del")
    public void del(long id) {
        log.info("del.........");
        try {
            UserDto user = jwtUtil.getSubject();
            if (user.getId().equals(id)) {
                throw new RuntimeException("不能删除当前登陆用户!");
            }
            UserDto dto = new UserDto();
            dto.setId(id);
            userService.deleteData(dto);
            loadPage();
        } catch (Exception e) {
            ialert.error(e.getMessage());
            return;
        }
        ialert.success(I18nUtil.get("ialert.success"));
    }

    /**
     * 判断员工id是否存在
     */
    public void isAccountYN(String account) {
        try {
            userService.isAccountYN(account);
        } catch (Exception e) {
            ialert.error(e.getMessage());
        }
    }

    /**
     * <p> 信息保存
     */
    @RequiresPermissions(value = {"user:add", "user:edit"}, logical = Logical.OR)
    @FXML
    public void save() {
        log.info("save.........");
        try {
            if (ValidatorUtil.isEmpty(accountT.getText())) {
                ialert.error("账号不能为空");
                return;
            }
            if (ValidatorUtil.isEmpty(nameT.getText())) {
                ialert.error("姓名不能为空");
                return;
            }
            String onePwd = pwdT.getText();
            String twoPwd = pwdConfirmT.getText();
            if (pwdPane.isVisible()) {
                if (ValidatorUtil.isEmpty(onePwd)) {
                    ialert.error("密码不能为空");
                    return;
                }
                if (ValidatorUtil.isEmpty(onePwd) || !(onePwd.equals(twoPwd))) {
                    ialert.error("两次密码必须一致");
                    return;
                }
            }
            UserDto dto = new UserDto() {{
                if (ValidatorUtil.notEmpty(idT.getText())) {
                    setId(Long.parseLong(idT.getText()));
                } else {
                    setNewFlag(1);
                }
                setAccount(accountT.getText());
                setName(nameT.getText());
                setJobNo(jobNoT.getText());
                setType(typeT.getSelectionModel().getSelectedIndex());
                setState(stateT.getSelectionModel().getSelectedIndex());
                if (pwdPane.isVisible()) {
                    setPwd(MD5.md5Hex(onePwd));
                    setConfirmpwd(MD5.md5Hex(twoPwd));
                }
            }};
            UserDto currentUserDto = jwtUtil.getSubject();
            if (currentUserDto != null) {
                if (ValidatorUtil.isEmpty(dto.getAccount())) {
                    dto.setAccount(currentUserDto.getAccount());
                }
            }
//            jsr303Check(dto);
            userService.saveOrUpdateData(dto);
            cancel();
            loadPage();
        } catch (Exception e) {
            log.error("保存异常:", e);
            ialert.error(e.getMessage());
            return;
        }
        ialert.success(I18nUtil.get("ialert.success"));
    }

    @FXML
    @RequiresPermissions("user:page")
    public void loadPage() {
        log.info("loadPage.........");
        try {
            ObservableList<UserDto> dataList = FXCollections.observableArrayList();
            UserDto dto = new UserDto() {{
                setPageSize(CommonConstant.PAGEROW_DEFAULT_COUNT);
                setPageNum(pageNum);
                setDelFlag(0);
                if (dateBeginT.getValue() != null)
                    setDateBegin(dateBeginT.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                if (dateEndT.getValue() != null)
                    setDateEnd(dateEndT.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                setKeyword(keywordT.getText());
            }};
            PageDto pageDto = getPageDto(userService.findDataIsPage(dto));
            dataList.addAll(pageDto.getData());
            dataTableView.setItems(dataList);
            seqTableColumn.setCellFactory((col) -> {
                TableCell<UserDto, String> cell = new TableCell<UserDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        this.setStyle("-fx-pref-width: 60;-fx-text-overrun: center-ellipsis;");
                        if (!empty) {
                            int rowIndex = ((pageDto.getPageIndex() - 1) * pageDto.getPageSize()) + this.getIndex() + 1;
                            this.setText(String.valueOf(rowIndex));
                        }
                    }
                };
                return cell;
            });
            //姓名
            nameTableColumn.setCellFactory((col) -> new DataTableCell("name"));
            //账号
            accountTableColumn.setCellFactory((col) -> new DataTableCell("account"));
            //工号
            jobTableColumn.setCellFactory((col) -> new DataTableCell("jobNo"));
            //角色
            roleTableColumn.setCellFactory((col) -> new DataTableCell("type", new HashMap<String, Object>() {{
                put("" + 0, "初级用户");
                put("" + 1, "中级用户");
                put("" + 2, "高级用户");
                put("" + 3, "超级管理员");
            }}));
            //状态
            stateTableColumn.setCellFactory((col) -> new DataTableCell("state", new HashMap<String, Object>() {{
                put("" + 0, "启用");
                put("" + 1, "禁用");
            }}));
            //创建时间
            dateCreatedTableColumn.setCellFactory((col) -> new DataTableCell("dateCreated"));
            //操作
            operTableColumn.setCellFactory((col) -> new ButtonCell(dataTableView));

            pageBar.getChildren().setAll(new PageBar(this, pageDto, "loadPage").getBar());

        } catch (Exception e) {
            log.error("分页..", e);
        }

    }

    @FXML
    public void resetSearch() {
        log.info("resetSearch.........");
        dateBeginT.setValue(null);
        dateEndT.setValue(null);
        keywordT.setText(null);
    }

    @FXML
    public void updatePwd() {
        try {
            if (ValidatorUtil.isEmpty(oldPwdT.getText())) {
                ialert.error("原密码不能为空");
                return;
            }
            if (ValidatorUtil.isEmpty(newPwdT.getText())) {
                ialert.error("新密码不能为空");
                return;
            }
            if (ValidatorUtil.isEmpty(pwdConfirmT.getText()) || !(newPwdT.getText().equals(pwdConfirmT.getText()))) {
                ialert.error("两次密码必须一致");
                return;
            }
            UserDto dto = new UserDto() {{
                setId(jwtUtil.getSubject().getId());
                setOldpwd(MD5.md5Hex(oldPwdT.getText()));
                setNewpwd(MD5.md5Hex(newPwdT.getText()));
                setConfirmpwd(MD5.md5Hex(pwdConfirmT.getText()));
            }};
            userService.updatePwd(dto);
            cancelPwd();
            ialert.success("密码修改成功!");
        } catch (Exception e) {
            ialert.error(e.getMessage());
        }
    }

    @FXML
    public void cancel() {
        log.info("cancel.........");
        idialog.getStage(userEditView).hide();
    }

    @FXML
    public void cancelPwd() {
        log.info("cancelPwd.........");
        idialog.getStage(userPwdView).hide();
    }

    @FXML
    public void add() {
        log.info("add.........");
        idialog.openDialog("用户新增", userEditView, 700.0, 500.0, false);
        showInfo(null);
    }

    @Override
    public void dispose() {

    }

    class ButtonCell<T> extends DataButtonCell {
        Label delBtn = new Label("删除");
        Label editBtn = new Label("编辑");
        Label pwdBtn = new Label("密码修改");

        {
            paddedButton.getStylesheets().add("/com/redphase/css/user.css");
            delBtn.getStyleClass().addAll("ob");
            ImageView delImageView = new ImageView(new Image("/com/redphase/images/del.png"));
            delImageView.setFitWidth(20);
            delImageView.setFitHeight(20);
            delBtn.setGraphic(delImageView);

            editBtn.getStyleClass().addAll("ob");
            ImageView editImageView = new ImageView(new Image("/com/redphase/images/edit.png"));
            editImageView.setFitWidth(20);
            editImageView.setFitHeight(20);
            editBtn.setGraphic(editImageView);

            pwdBtn.getStyleClass().addAll("ob");
            ImageView pwdImageView = new ImageView(new Image("/com/redphase/images/barrage_fill.png"));
            pwdImageView.setFitWidth(20);
            pwdImageView.setFitHeight(20);
            pwdBtn.setGraphic(pwdImageView);

        }

        public ButtonCell(final TableView table) {
            paddedButton.setPadding(new Insets(0));
            if (jwtUtil.isPermitted("user:del")) {
                delBtn.setOnMouseClicked((m) -> {
                    log.debug("OperButtonCell...del");
                    UserDto userDto = (UserDto) table.getSelectionModel().getSelectedItem();
                    del(userDto.getId());
                });
                paddedButton.getChildren().add(delBtn);
            }

            if (jwtUtil.isPermitted("user:edit")) {
                editBtn.setOnMouseClicked((m) -> {
                    log.debug("OperButtonCell...edit");
                    UserDto dto = (UserDto) table.getSelectionModel().getSelectedItem();
                    idialog.openDialog("用户编辑", userEditView, 700.0, 500.0, false);
                    showInfo(dto);
                });
                paddedButton.getChildren().add(editBtn);
            }

            if (jwtUtil.isPermitted("user:edit:pwd")) {
                pwdBtn.setOnMouseClicked((m) -> {
                    log.debug("OperButtonCell...pwdBtn");
                    UserDto dto = (UserDto) table.getSelectionModel().getSelectedItem();
                    idialog.openDialog("密码修改", userPwdView, 700.0, 500.0, false);
                    idT.setText("" + dto.getId());
                });
                paddedButton.getChildren().add(pwdBtn);
            }
        }
    }
}
