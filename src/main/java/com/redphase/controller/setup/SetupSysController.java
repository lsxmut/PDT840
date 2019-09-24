package com.redphase.controller.setup;

import com.redphase.api.setup.ISetupService;
import com.redphase.controller.BaseController;
import com.redphase.controller.IndexController;
import com.redphase.dto.sys.SysVariableDto;
import com.redphase.framework.util.ValidatorUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

@FXMLController
@Slf4j
public class SetupSysController extends BaseController implements Initializable {

    @Autowired
    private IndexController indexController;
    @Autowired
    private ISetupService setupService;
    @FXML
    private TextField sysTimeout;
    private SysVariableDto variable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize...");
        try {
            variable = setupService.getVariableByCode(new SysVariableDto() {{
                setCode("i-sys-timeout");
            }});
            if (variable == null || ValidatorUtil.isEmpty(variable.getValue())) {
                throw new RuntimeException("");
            }
            sysTimeout.setText(variable.getValue());
        } catch (Exception e) {
            ialert.error("设置获取失败!");
            sysTimeout.setText("30");
        }
    }

    @FXML
    public void cancel() {

    }

    @FXML
    //
    public void submit() {
        log.info("submit..");
        try {
            String value = sysTimeout.getText();
            if (ValidatorUtil.isEmpty(value) || !ValidatorUtil.isNum(value)) {
                ialert.warning("请输入正确的数字");
                return;
            }
            int num = new BigDecimal(value).intValue();
            if (num < 5 || num > 60) {
                ialert.warning("请输入正确的数字(5~60)");
                return;
            }
            variable.setValue(value);
            setupService.saveTimeoutVariableList(new ArrayList<SysVariableDto>() {{
                add(variable);
            }});
            indexController.setISysTimeoutDto(null);
            ialert.success("保存成功!");
        } catch (Exception e) {
            ialert.error(e.getMessage());
        }
    }

    @Override
    public void dispose() {

    }
}
