package com.redphase.view;

import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import javafx.stage.Stage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@FXMLView(value = "/com/redphase/fxml/idialog.fxml", bundle = "com.redphase.i18n.message", encoding = "utf-8")
@Slf4j
@Data
public class DialogView extends AbstractFxmlView {
    private Stage stage;

}
