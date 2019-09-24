package com.redphase.controller.atlas;

import com.Application;
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
public class AtlasImportInitController extends AtlasBaseController implements Initializable {

    @Autowired
    private AtlasController atlasController;
    @FXML
    private AnchorPane importInitAnchorPane;
    @FXML
    private VBox importInitVBox;

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
            setContent(atlasImportRunView.getView());
        }};
        atlasController.getRightAnchorPane().setStyle("-fx-background-color: #ffffff");
        atlasController.getRightAnchorPane().getChildren().setAll(scrollPane);
    }

    @Override
    public void dispose() {

    }
}