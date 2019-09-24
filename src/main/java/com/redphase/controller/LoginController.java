package com.redphase.controller;

import com.Application;
import com.redphase.api.nda.ISlaveService;
import com.redphase.dto.sys.SysVariableDto;
import com.redphase.dto.user.AuthPermDto;
import com.redphase.dto.user.AuthRoleDto;
import com.redphase.dto.user.LogLoginDto;
import com.redphase.dto.user.UserDto;
import com.redphase.framework.security.MD5;
import com.redphase.framework.util.*;
import com.redphase.httpserver.MyFileServer;
import com.redphase.service.user.LogLoginService;
import com.redphase.service.user.RoleSourceService;
import com.redphase.view.IndexView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.*;

@FXMLController
@Slf4j
public class LoginController extends BaseController implements Initializable {
    @FXML
    private ImageView img_logo;
    @FXML
    private TextField text_account;
    @FXML
    private TextField text_password;
    @Autowired
    private RoleSourceService roleSourceService;
    @Autowired
    private LogLoginService logLoginService;
    @Autowired
    private EhcacheUtil ehcacheUtil;
    @Autowired
    private IndexController indexController;
    @Autowired
    private MyFileServer myFileServer;
    @Autowired
    private ISlaveService slaveService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize...");
        try {
            try {
                ehcacheUtil.setCache(CommonConstant.SYS_TIMEOUT_KEY, System.currentTimeMillis());//更新当前时间
            } catch (Exception e) {
                log.error("初始SYS_TIMEOUT_KEY", e);
            }
            SysVariableDto logoVariableDto = setupService.getVariableByCode(new SysVariableDto() {{
                setCode("i-version-company-logo");
            }});
            String resourcesPath = "";
            try {
                resourcesPath = FileUtil.getPath() + "/config/";
            } catch (UnsupportedEncodingException e) {
            }
            if (!(new File(resourcesPath).exists())) {
                resourcesPath = "";
            }
            log.debug("获取系统LOGO==>" + (ValidatorUtil.notEmpty(resourcesPath) ? "file:" + resourcesPath : "") + logoVariableDto.getValue());
            img_logo.setImage(new Image((ValidatorUtil.notEmpty(resourcesPath) ? "file:" + resourcesPath : "") + logoVariableDto.getValue()));

            SysVariableDto iconVariableDto = setupService.getVariableByCode(new SysVariableDto() {{
                setCode("i-version-company-icon");
            }});
            log.debug("获取系统ICON==>" + (ValidatorUtil.notEmpty(resourcesPath) ? "file:" + resourcesPath : "") + iconVariableDto.getValue());
            List<Image> icons = new ArrayList();
            icons.add(new Image((ValidatorUtil.notEmpty(resourcesPath) ? "file:" + resourcesPath : "") + iconVariableDto.getValue()));
            Application.getStage().getIcons().addAll(icons);
        } catch (Exception e) {
            ialert.warning(I18nUtil.get("error.logo"));
        }
    }

    @FXML
    public void submit() {
        String account = text_account.getText();
        String password = text_password.getText();
        if (ValidatorUtil.isNullEmpty(account) || ValidatorUtil.isNullEmpty(password)) {
            ialert.error(I18nUtil.get("error.userEmpty"));
            return;
        }
        UserDto userDto = roleSourceService.findUserByAccount(account);
        String pwdhex = MD5.pwdMd5Hex(MD5.md5Hex(password));
        if (userDto == null || !(account.equals(userDto.getAccount()) && pwdhex.equals(userDto.getPwd()))) {
            ialert.error(I18nUtil.get("error.loginFail"));
            return;
        }
        roleSourceService.lastLogin(userDto);//更新最后登录时间

        //超级管理员标记
        if (3 == (userDto.getType())) {
            userDto.setIissuperman(1);
        } else {
            userDto.setIissuperman(0);
        }
        //用户角色
        Set<String> rolesSet = new HashSet<>();
        //用户权限
        Set<String> permsSet = new HashSet<>();
        //2.获取角色集合
        List<AuthRoleDto> roleList = roleSourceService.getRoleListByUserId(userDto);
        if (roleList != null) {
            for (AuthRoleDto role : roleList) {
                rolesSet.add(role.getName());
            }
        }
        //3.获取功能集合
        List<AuthPermDto> permList = roleSourceService.getPermListByUserId(userDto);
        if (permList != null) {
            for (AuthPermDto perm : permList) {
                if (perm.getMatchStr() != null && !"".equals(perm.getMatchStr())) {
                    permsSet.add(perm.getMatchStr());
                }
            }
        }
        userDto.setAuthorizationInfoRoles(rolesSet);
        userDto.setAuthorizationInfoPerms(permsSet);
        ehcacheUtil.setCache(CommonConstant.JWT_HEADER_TOKEN_KEY, userDto);

        taskExecutor.execute(new Thread(() -> {
            try {
                //读取session中的员工
                LogLoginDto logDto = new LogLoginDto();
                logDto.setType(0);//类型0登录1登出
                logDto.setUserId(userDto.getId());//id
                logDto.setUserName(userDto.getName());//员工名称
                logLoginService.saveOrUpdateData(logDto);
            } catch (Exception e) {
            }
            try {
                //启动通信模块
                myFileServer.fileHttpServer();
                slaveService.flushDb();
            } catch (Exception e) {
            }
        }));

        Application.getStage().setScene(null);
        Application.showView(IndexView.class);
//        Application.getStage().setMaximized(true);
        Application.getStage().setWidth(1333);
        Application.getStage().setHeight(768);
        Application.getStage().centerOnScreen();
        indexController.getTimeline().play();
        Application.getStage().setTitle(I18nUtil.get("title"));
        Application.getStage().setOnCloseRequest((event -> {
            log.debug("exit");
            System.exit(0);
        }));
    }

    @Override
    public void dispose() {

    }
}
