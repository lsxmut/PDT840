package com.redphase.controller.setup;

import com.redphase.api.user.IUserService;
import com.redphase.controller.BaseController;
import com.redphase.dto.user.UserDto;
import com.redphase.framework.security.MD5;
import com.redphase.framework.util.ValidatorUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.ResourceBundle;

@FXMLController
@Slf4j
public class SetupSuperController extends BaseController implements Initializable {

    @Autowired
    private IUserService userService;
    @FXML
    private TextField account;
    @FXML
    private TextField oldPassword;
    @FXML
    private TextField newPassword;
    @FXML
    private TextField confirmPassword;
    private UserDto userDto;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize...");
        userDto = userService.findDataById(new UserDto() {{
            setId(1L);
        }});
        account.setText(userDto.getAccount());
    }

    @FXML
    public void cancel() {

    }

    @FXML
    public void submit() {
        log.info("submit..");
        try {
            if (ValidatorUtil.isEmpty(oldPassword.getText())
                    || ValidatorUtil.isEmpty(newPassword.getText())
                    || ValidatorUtil.isEmpty(confirmPassword.getText())) {
                ialert.error("密码不能为空!");
                return;
            }
            userDto.setOldpwd(MD5.md5Hex(oldPassword.getText()));
            userDto.setNewpwd(MD5.md5Hex(newPassword.getText()));
            userDto.setConfirmpwd(MD5.md5Hex(confirmPassword.getText()));
            ialert.success(userService.updatePwd(userDto));
        } catch (Exception e) {
            ialert.error(e.getMessage());
        }

    }

    @Override
    public void dispose() {

    }
}
