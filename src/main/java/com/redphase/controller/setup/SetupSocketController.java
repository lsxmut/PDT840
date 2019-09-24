package com.redphase.controller.setup;

import com.redphase.api.setup.ISetupService;
import com.redphase.controller.BaseController;
import com.redphase.dto.sys.SysVariableDto;
import com.redphase.framework.util.IpUtil;
import com.redphase.framework.util.ValidatorUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.*;

@FXMLController
@Slf4j
public class SetupSocketController extends BaseController implements Initializable {

    @Autowired
    private ISetupService setupService;
    @FXML
    private TextField wifiIp;
    @FXML
    private TextField wifiPort;
    @FXML
    private TextField bluetooth;
    @FXML
    private TextField password;
    @FXML
    private HBox bluetoothHbox;

    private Map<String, SysVariableDto> iSysVariableDtoMap = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize...");
        bluetoothHbox.setVisible(false);
        bluetoothHbox.setManaged(false);
        getVariableListByPcode("i-socket");
    }

    @FXML
    public void cancel() {

    }

    @FXML
    public void submit() {
        log.info("submit..");
        try {
            if (ValidatorUtil.isEmpty(wifiPort.getText()) || !ValidatorUtil.isNumber(wifiPort.getText())) {
                ialert.info("请输入正确的端口数字!");
                return;
            }
            getSetSysVariableDto(wifiIp, wifiPort, bluetooth, password);
            setupService.saveSocketVariableList(new ArrayList<>(iSysVariableDtoMap.values()));
            ialert.success("修改成功!下次启动生效!");
        } catch (Exception e) {
            ialert.error(e.getMessage());
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
                String code = variableDto.getCode();
                log.debug("code==>{}", code);
                switch (code) {
                    case "i-socket-wifi-ip": {
                        try {
                            variableDto.setValue(IpUtil.getLocalHostLANAddress().getHostAddress());
                        } catch (Exception e) {
                            ialert.error("获取本机ip异常!" + e.getMessage());
                        }
                        setVariableById(wifiIp, variableDto);
                        break;
                    }
                    case "i-socket-wifi-port": {
                        setVariableById(wifiPort, variableDto);
                        break;
                    }
                    case "i-socket-bluetooth": {
                        setVariableById(bluetooth, variableDto);
                        break;
                    }
                    case "i-socket-password": {
                        setVariableById(password, variableDto);
                        break;
                    }
                }
            });
        } catch (Exception e) {
            log.error("通讯设置获取异常!", e);
            ialert.error(e.getMessage());
        }
    }

    private void setVariableById(TextField currentText, SysVariableDto sysVariableDto) {
        currentText.setText(sysVariableDto.getValue());
        iSysVariableDtoMap.put(currentText.getId(), sysVariableDto);
    }

    @Override
    public void dispose() {

    }
}
