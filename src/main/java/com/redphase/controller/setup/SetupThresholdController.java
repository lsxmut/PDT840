package com.redphase.controller.setup;

import com.redphase.api.setup.ISetupService;
import com.redphase.controller.BaseController;
import com.redphase.dto.sys.SysVariableDto;
import com.redphase.framework.util.ValidatorUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.*;

@FXMLController
@Slf4j
public class SetupThresholdController extends BaseController implements Initializable {

    @Autowired
    private ISetupService setupService;

    @FXML
    private TextField tev1Text;
    @FXML
    private TextField tev2Text;
    @FXML
    private TextField tev3Text;
    @FXML
    private TextField aa1Text;
    @FXML
    private TextField aa2Text;
    @FXML
    private TextField aa3Text;
    @FXML
    private TextField hfct1Text;
    @FXML
    private TextField hfct2Text;
    @FXML
    private TextField hfct3Text;
    @FXML
    private TextField ae1Text;
    @FXML
    private TextField ae2Text;
    @FXML
    private TextField ae3Text;
    @FXML
    private TextField uhf1Text;
    @FXML
    private TextField uhf2Text;
    @FXML
    private TextField uhf3Text;
    @FXML
    private ToggleGroup device;
    @FXML
    private RadioButton deviceRadioButton;
    @FXML
    private RadioButton softwanRadioButton;

    private Map<String, SysVariableDto> iSysVariableDtoMap = new HashMap<>();
    SysVariableDto thresholdTypeDto = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize...");
        //获取阈值生效类型0客户端设备1服务端软件
        try {
            thresholdTypeDto = setupService.getVariableByCode(new SysVariableDto() {{
                setCode("threshold-type");
            }});
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ("0".equalsIgnoreCase(thresholdTypeDto.getValue())) {
            deviceRadioButton.setSelected(true);
        } else {
            softwanRadioButton.setSelected(true);
        }
        //获取服务端软件阈值
        getVariableListByPcode("i-threshold");
    }

    @FXML
    private void isDevice() {
        thresholdTypeDto.setValue("0");
    }

    @FXML
    private void isSoftwan() {
        thresholdTypeDto.setValue("1");
    }

    @FXML
    public void doSmall(ActionEvent event) {
        log.info("doSmall..");
        Button smallButton = (Button) event.getSource();
        doChange(smallButton.getId());
    }

    @FXML
    public void doBig(ActionEvent event) {
        log.info("doBig..");
        Button smallButton = (Button) event.getSource();
        doChange(smallButton.getId());
    }

    @FXML
    public void resetForm() {
        log.info("resetForm..");
        getVariableListByPcode("d-threshold");
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
                switch (code.substring(2, 4)) {
                    case "te": {
                        setVariableById("x-tev", variableDto);
                        break;
                    }
                    case "aa": {
                        setVariableById("x-aa", variableDto);
                        break;
                    }
                    case "hf": {
                        setVariableById("x-hfct", variableDto);
                        break;
                    }
                    case "ae": {
                        setVariableById("x-ae", variableDto);
                        break;
                    }
                    case "uh": {
                        setVariableById("x-uhf", variableDto);
                        break;
                    }
                }
            });
        } catch (Exception e) {
            log.error("阈值获取异常!", e);
            ialert.error(e.getMessage());
        }
    }

    private void setVariableById(String key, SysVariableDto sysVariableDto) {
        String code = sysVariableDto.getCode();
        int slen = key.length();
        TextField currentText = null;
        if ("Normal".equalsIgnoreCase(code.substring(slen))) {
            switch (key.substring(2)) {
                case "tev":
                    currentText = tev1Text;
                    tev1Text.setText(sysVariableDto.getValue());
                    break;
                case "aa":
                    currentText = aa1Text;
                    aa1Text.setText(sysVariableDto.getValue());
                    break;
                case "hfct":
                    currentText = hfct1Text;
                    hfct1Text.setText(sysVariableDto.getValue());
                    break;
                case "ae":
                    currentText = ae1Text;
                    ae1Text.setText(sysVariableDto.getValue());
                    break;
                case "uhf":
                    currentText = uhf1Text;
                    uhf1Text.setText(sysVariableDto.getValue());
                    break;
            }
        } else if ("EarlyWarning".equalsIgnoreCase(code.substring(slen))) {
            switch (key.substring(2)) {
                case "tev":
                    currentText = tev2Text;
                    tev2Text.setText(sysVariableDto.getValue());
                    break;
                case "aa":
                    currentText = aa2Text;
                    aa2Text.setText(sysVariableDto.getValue());
                    break;
                case "hfct":
                    currentText = hfct2Text;
                    hfct2Text.setText(sysVariableDto.getValue());
                    break;
                case "ae":
                    currentText = ae2Text;
                    ae2Text.setText(sysVariableDto.getValue());
                    break;
                case "uhf":
                    currentText = uhf2Text;
                    uhf2Text.setText(sysVariableDto.getValue());
                    break;
            }
        } else if ("Abnormal".equalsIgnoreCase(code.substring(slen))) {
            switch (key.substring(2)) {
                case "tev":
                    currentText = tev3Text;
                    tev3Text.setText(sysVariableDto.getValue());
                    break;
                case "aa":
                    currentText = aa3Text;
                    aa3Text.setText(sysVariableDto.getValue());
                    break;
                case "hfct":
                    currentText = hfct3Text;
                    hfct3Text.setText(sysVariableDto.getValue());
                    break;
                case "ae":
                    currentText = ae3Text;
                    ae3Text.setText(sysVariableDto.getValue());
                    break;
                case "uhf":
                    currentText = uhf3Text;
                    uhf3Text.setText(sysVariableDto.getValue());
                    break;
            }
        }
        sysVariableDto.setCode(sysVariableDto.getCode().replace("d-", "i-"));
        iSysVariableDtoMap.put(currentText.getId(), sysVariableDto);
    }

    @FXML
    public void submit(ActionEvent event) {
        log.info("submit..");
        try {
            iSysVariableDtoMap.put("threshold-type", thresholdTypeDto);
            getSetSysVariableDto(tev1Text, tev2Text, tev3Text, aa1Text, aa2Text, aa3Text, hfct1Text, hfct2Text, hfct3Text, ae1Text, ae2Text, ae3Text, uhf1Text, uhf2Text, uhf3Text);
            setupService.saveThresholdVariableList(new ArrayList<>(iSysVariableDtoMap.values()));
            ialert.success("保存成功!");
        } catch (Exception e) {
            ialert.error(e.getMessage());
        }

    }

    private void getSetSysVariableDto(TextField... currentTexts) {
        for (TextField currentText : currentTexts) {
            iSysVariableDtoMap.get(currentText.getId()).setValue(currentText.getText());
        }
    }

    /**
     * ui控件值改变
     *
     * @param id
     */
    private void doChange(String id) {
        TextField currentText = null;
        int type = 0;//0减小,1放大
        switch (id.substring(0, 2)) {
            case "te": {
                switch (id) {
                    case "tev1Small":
                        currentText = tev1Text;
                        break;
                    case "tev2Small":
                        currentText = tev2Text;
                        break;
                    case "tev3Small":
                        currentText = tev3Text;
                        break;
                    case "tev1Big":
                        currentText = tev1Text;
                        type = 1;
                        break;
                    case "tev2Big":
                        currentText = tev2Text;
                        type = 1;
                        break;
                    case "tev3Big":
                        currentText = tev3Text;
                        type = 1;
                        break;
                }
            }
            case "aa": {
                switch (id) {
                    case "aa1Small":
                        currentText = aa1Text;
                        break;
                    case "aa2Small":
                        currentText = aa2Text;
                        break;
                    case "aa3Small":
                        currentText = aa3Text;
                        break;
                    case "aa1Big":
                        currentText = aa1Text;
                        type = 1;
                        break;
                    case "aa2Big":
                        currentText = aa2Text;
                        type = 1;
                        break;
                    case "aa3Big":
                        currentText = aa3Text;
                        type = 1;
                        break;
                }
            }
            case "hf": {
                switch (id) {
                    case "hfct1Small":
                        currentText = hfct1Text;
                        break;
                    case "hfct2Small":
                        currentText = hfct2Text;
                        break;
                    case "hfct3Small":
                        currentText = hfct3Text;
                        break;
                    case "hfct1Big":
                        currentText = hfct1Text;
                        type = 1;
                        break;
                    case "hfct2Big":
                        currentText = hfct2Text;
                        type = 1;
                        break;
                    case "hfct3Big":
                        currentText = hfct3Text;
                        type = 1;
                        break;
                }
            }
            case "ae": {
                switch (id) {
                    case "ae1Small":
                        currentText = ae1Text;
                        break;
                    case "ae2Small":
                        currentText = ae2Text;
                        break;
                    case "ae3Small":
                        currentText = ae3Text;
                        break;
                    case "ae1Big":
                        currentText = ae1Text;
                        type = 1;
                        break;
                    case "ae2Big":
                        currentText = ae2Text;
                        type = 1;
                        break;
                    case "ae3Big":
                        currentText = ae3Text;
                        type = 1;
                        break;
                }
            }
            case "uh": {
                switch (id) {
                    case "uhf1Small":
                        currentText = uhf1Text;
                        break;
                    case "uhf2Small":
                        currentText = uhf2Text;
                        break;
                    case "uhf3Small":
                        currentText = uhf3Text;
                        break;
                    case "uhf1Big":
                        currentText = uhf1Text;
                        type = 1;
                        break;
                    case "uhf2Big":
                        currentText = uhf2Text;
                        type = 1;
                        break;
                    case "uhf3Big":
                        currentText = uhf3Text;
                        type = 1;
                        break;
                }
            }
        }
        String value = currentText.getText();
        if (ValidatorUtil.isEmpty(value)) {
            ialert.error("不能有空值!");
        }
        int num = Integer.parseInt(value);
        if (type == 0) {
            currentText.setText(String.valueOf(--num));
        } else {
            currentText.setText(String.valueOf(++num));
        }
    }

    @Override
    public void dispose() {

    }
}
