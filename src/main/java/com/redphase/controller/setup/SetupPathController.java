package com.redphase.controller.setup;

import com.redphase.api.setup.ISetupService;
import com.redphase.controller.BaseController;
import com.redphase.dto.sys.SysVariableDto;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.*;

@FXMLController
@Slf4j
public class SetupPathController extends BaseController implements Initializable {

    @Autowired
    private ISetupService setupService;
    @FXML
    private TextField dataPath;
    @FXML
    private TextField imagesPath;
    @FXML
    private TextField pdfPath;
    @FXML
    private TextField reportsPath;
    @FXML
    private TextField testsPath;
    @FXML
    private TextField stationsPath;

    private Map<String, SysVariableDto> iSysVariableDtoMap = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize...");
        getVariableListByPcode("i-path");
    }

    @FXML
    public void cancel() {

    }

    @FXML
    //@ALogOperation(type = "路径设置", desc = "系统设置")
    public void submit() {
        log.info("submit..");
        try {
            getSetSysVariableDto(dataPath, imagesPath, pdfPath, reportsPath, testsPath, stationsPath);
            setupService.savePathVariableList(new ArrayList<>(iSysVariableDtoMap.values()));
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
                    case "i-path-data": {
                        setVariableById(dataPath, variableDto);
                        break;
                    }
                    case "i-path-images": {
                        setVariableById(imagesPath, variableDto);
                        break;
                    }
                    case "i-path-pdf": {
                        setVariableById(pdfPath, variableDto);
                        break;
                    }
                    case "i-path-reports": {
                        setVariableById(reportsPath, variableDto);
                        break;
                    }
                    case "i-path-tests": {
                        setVariableById(testsPath, variableDto);
                        break;
                    }
                    case "i-path-stations": {
                        setVariableById(stationsPath, variableDto);
                        break;
                    }
                }
            });
        } catch (Exception e) {
            log.error("路径设置获取异常!", e);
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
