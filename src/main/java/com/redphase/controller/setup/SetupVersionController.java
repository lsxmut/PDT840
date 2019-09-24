package com.redphase.controller.setup;

import com.Application;
import com.redphase.api.setup.ISetupService;
import com.redphase.controller.BaseController;
import com.redphase.dto.sys.SysVariableDto;
import com.redphase.framework.util.FileUtil;
import com.redphase.framework.util.ValidatorUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.*;

@FXMLController
@Slf4j
public class SetupVersionController extends BaseController implements Initializable {

    @Autowired
    private ISetupService setupService;
    @FXML
    private TextField versionName;
    @FXML
    private TextField versionCode;
    @FXML
    private TextField copyright;
    @FXML
    private TextField companyAddress;
    @FXML
    private TextField companyWebsite;
    @FXML
    private ImageView logoImage;
    @FXML
    private ImageView iconImage;

    private Map<String, SysVariableDto> iSysVariableDtoMap = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize...");
        getVariableListByPcode("i-version");
    }

    @FXML
    public void cancel() {

    }

    @FXML
    public void submit() {
        log.info("submit..");
        try {
            getSetSysVariableDto(versionName, versionCode, copyright, companyAddress, companyWebsite);
            setupService.saveVariableList(new ArrayList<>(iSysVariableDtoMap.values()));
            ialert.success("保存成功!");
        } catch (Exception e) {
            ialert.error(e.getMessage());
        }
    }

    @FXML
    public void setIcon() {
        log.info("setIcon..");
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("请选择ICON文件");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Db files (*.jpg,*.jpeg,*.png,*.icon)", "*.jpg", "*.jpeg", "*.png", "*.icon");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showOpenDialog(Application.getStage());
            if (file == null || !file.exists()) return;
            String resourcesPath = FileUtil.getPath() + "/config/";
            if (!(new File(resourcesPath).exists())) {
                resourcesPath = "";
            }
            String iconPath = "images/icon." + getFileExt(file.getPath());
            log.debug("file.getPath()==>{}", (ValidatorUtil.notEmpty(resourcesPath) ? "file:" + resourcesPath : "") + iconPath);
            setupService.copyFile(file, new File((ValidatorUtil.isEmpty(resourcesPath) ? Thread.currentThread().getContextClassLoader().getResource("").getPath() : resourcesPath) + iconPath));
            iconImage.setImage(new Image((ValidatorUtil.notEmpty(resourcesPath) ? "file:" + resourcesPath : "") + iconPath));
            iSysVariableDtoMap.get(iconImage.getId()).setValue(iconPath);
        } catch (Exception e) {
            log.error("设置ICON文件异常!", e);
            ialert.error("设置ICON文件异常!" + e.getMessage());
        }
    }

    @FXML
    public void setLogo() {
        log.info("setLogo..");
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("请选择LOGO文件");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Db files (*.jpg,*.jpeg,*.png)", "*.jpg", "*.jpeg", "*.png");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showOpenDialog(Application.getStage());
            if (file == null || !file.exists()) return;
            String resourcesPath = FileUtil.getPath() + "/config/";
            if (!(new File(resourcesPath).exists())) {
                resourcesPath = "";
            }
            String iconPath = "images/logo." + getFileExt(file.getPath());
            log.debug("file.getPath()==>{}", (ValidatorUtil.notEmpty(resourcesPath) ? "file:" + resourcesPath : "") + iconPath);
            setupService.copyFile(file, new File((ValidatorUtil.isEmpty(resourcesPath) ? Thread.currentThread().getContextClassLoader().getResource("").getPath() : resourcesPath) + iconPath));
            logoImage.setImage(new Image((ValidatorUtil.notEmpty(resourcesPath) ? "file:" + resourcesPath : "") + iconPath));
            iSysVariableDtoMap.get(logoImage.getId()).setValue(iconPath);
        } catch (Exception e) {
            log.error("设置LOGO文件异常!", e);
            ialert.error("设置LOGO文件异常!" + e.getMessage());
        }
    }

    private void getSetSysVariableDto(TextField... currentTexts) {
        for (TextField currentText : currentTexts) {
            iSysVariableDtoMap.get(currentText.getId()).setValue(currentText.getText());
        }
    }

    private void getVariableListByPcode(String pcode) {
        try {
            List<SysVariableDto> list = setupService.getVariableListByPCode(new SysVariableDto() {{
                setCode(pcode);
            }});
            log.debug("get-variable-list-by-p-code-list==>{}", list);
            list.forEach(variableDto -> {
                String resourcesPath = "";
                try {
                    resourcesPath = FileUtil.getPath() + "/config/";
                } catch (UnsupportedEncodingException e) {
                }
                if (!(new File(resourcesPath).exists())) {
                    resourcesPath = "";
                }
                String code = variableDto.getCode();
                log.debug("code==>{}", code);
                switch (code) {
                    case "i-version-name": {
                        setVariableById(versionName, variableDto);
                        break;
                    }
                    case "i-version-code": {
                        setVariableById(versionCode, variableDto);
                        break;
                    }
                    case "i-version-copyright": {
                        setVariableById(copyright, variableDto);
                        break;
                    }
                    case "i-version-company-address": {
                        setVariableById(companyAddress, variableDto);
                        break;
                    }
                    case "i-version-company-website": {
                        setVariableById(companyWebsite, variableDto);
                        break;
                    }
                    case "i-version-company-icon": {
                        try {
                            iconImage.setImage(new Image((ValidatorUtil.notEmpty(resourcesPath) ? "file:" + resourcesPath : "") + variableDto.getValue()));
                        } catch (Exception e) {
                            iconImage.setImage(new Image("/images/404.png"));
                        }
                        iSysVariableDtoMap.put(iconImage.getId(), variableDto);
                        break;
                    }
                    case "i-version-company-logo": {
                        try {
                            logoImage.setImage(new Image((ValidatorUtil.notEmpty(resourcesPath) ? "file:" + resourcesPath : "") + variableDto.getValue()));
                        } catch (Exception e) {
                            logoImage.setImage(new Image("/images/404.png"));
                        }
                        iSysVariableDtoMap.put(logoImage.getId(), variableDto);
                        break;
                    }
                }
            });
        } catch (Exception e) {
            log.error("路径设置获取异常!", e);
            ialert.error("路径设置获取异常!" + e.getMessage());
        }
    }

    private void setVariableById(TextField currentText, SysVariableDto sysVariableDto) {
        currentText.setText(sysVariableDto.getValue());
        iSysVariableDtoMap.put(currentText.getId(), sysVariableDto);
    }

    @Override
    public void dispose() {

    }

    public static void main(String[] args) {
        System.out.println(new File("D:\\IDE\\IdeaProjects\\hxdlcs\\target\\jfx\\native\\PDT840\\app\\config\\images").getAbsoluteFile().toURI().toString());
    }
}