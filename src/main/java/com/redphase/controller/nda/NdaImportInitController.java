package com.redphase.controller.nda;

import com.Application;
import com.redphase.view.nda.NdaRunView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.ResourceBundle;

@FXMLController
@Slf4j
public class NdaImportInitController extends NdaBaseController implements Initializable {

    @FXML
    private AnchorPane importInitAnchorPane;
    @FXML
    private VBox importInitVBox;
    @Autowired
    private NdaRunView ndaRunView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize...");
        importInitAnchorPane.minWidthProperty().bind(Application.getStage().widthProperty().subtract(395));
        importInitAnchorPane.minHeightProperty().bind(Application.getStage().heightProperty().subtract(90));
        importInitVBox.minWidthProperty().bind(Application.getStage().widthProperty().subtract(395));
        importInitVBox.minHeightProperty().bind(Application.getStage().heightProperty().subtract(90));
    }

    @FXML
    public void showImportPath() {
        ScrollPane scrollPane = new ScrollPane() {{
            prefWidthProperty().bind(Application.getStage().widthProperty().subtract(370));
            prefHeightProperty().bind(Application.getStage().heightProperty().subtract(20));
            setContent(ndaRunView.getView());
        }};
        ndaController.getRightAnchorPane().setStyle("-fx-background-color: #ffffff");
        ndaController.getRightAnchorPane().getChildren().setAll(scrollPane);
    }

    @Override
    public void dispose() {

    }
}