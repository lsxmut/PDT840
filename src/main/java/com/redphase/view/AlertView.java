package com.redphase.view;

import com.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class AlertView {
    public void alert(String message) {
        new Alert(Alert.AlertType.NONE, message, new ButtonType[]{ButtonType.CLOSE}).showAndWait();
    }

    public void info(String message) {
        new Alert(Alert.AlertType.INFORMATION, message, new ButtonType[]{ButtonType.OK}).showAndWait();
    }

    public void warning(String message) {
        new Alert(Alert.AlertType.WARNING, message, new ButtonType[]{ButtonType.OK}).showAndWait();
    }

    public void error(String message) {
        new Alert(Alert.AlertType.ERROR, message, new ButtonType[]{ButtonType.OK}).showAndWait();
    }

    public void success(String message) {
        new Alert(Alert.AlertType.NONE, message, new ButtonType[]{ButtonType.OK}).showAndWait();
    }

    public boolean confirm(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, new ButtonType[]{ButtonType.CANCEL, ButtonType.OK});
        alert.initOwner(Application.getStage());
        Optional<ButtonType> buttonType = alert.showAndWait();
//      根据点击结果返回
        if (buttonType.get().getButtonData().equals(ButtonBar.ButtonData.OK_DONE)) {
            return true;
        }
        return false;

    }
}
