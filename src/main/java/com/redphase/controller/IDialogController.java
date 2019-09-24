package com.redphase.controller;

import com.Application;
import com.redphase.framework.util.CommonConstant;
import de.felixroske.jfxsupport.AbstractFxmlView;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Hashtable;
import java.util.Map;
import java.util.ResourceBundle;

@Service
@Slf4j
public class IDialogController extends BaseController implements Initializable {
    @Setter
    @Getter
    private Map<String, Stage> stageMap = new Hashtable<>();
    private Map<String, VBox> dialogVBoxMap = new Hashtable<>();
    private Map<String, HBox> dialogHBoxMap = new Hashtable<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize...");
    }

    public Stage getStage(AbstractFxmlView nodeView) {
        return stageMap.get(nodeView.getClass().getName());
    }

    /**
     * 设置标题
     *
     * @param nodeView
     * @param title
     */
    public void setTitle(AbstractFxmlView nodeView, String title) {
        Stage stage = stageMap.get(nodeView.getClass().getName());
        if (stage == null) {
            stage = new Stage();
            stageMap.put(nodeView.getClass().getName(), stage);
        }
        stage.setTitle(title);
    }

    /**
     * 打开新窗口
     *
     * @param title
     * @param nodeView
     * @param width
     * @param height
     */
    public void openDialog(String title, AbstractFxmlView nodeView, Double width, Double height, boolean isFullScreen) {
        try {
            ehcacheUtil.setCache(CommonConstant.SYS_TIMEOUT_KEY, System.currentTimeMillis());//更新当前时间
            Stage stage = stageMap.get(nodeView.getClass().getName());
            VBox dialogVBox = dialogVBoxMap.get(nodeView.getClass().getName());
            HBox dialogHBox = dialogHBoxMap.get(nodeView.getClass().getName());

            Pane currentNodeView = (Pane) nodeView.getView();
            if (stage == null
                    || dialogVBox == null
                    || dialogHBox == null) {
                stage = new Stage();
                stageMap.put(nodeView.getClass().getName(), stage);
                FXMLLoader loader = new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource("com/redphase/fxml/idialog.fxml"));
                ScrollPane dialogView = loader.load();
                Scene scene = new Scene(dialogView, width, height);
                stage.setScene(scene);

                dialogVBox = (VBox) dialogView.getContent().lookup("#dialogVBox");
                dialogVBoxMap.put(nodeView.getClass().getName(), dialogVBox);

                dialogHBox = (HBox) dialogVBox.lookup("#dialogHBox");
                dialogHBoxMap.put(nodeView.getClass().getName(), dialogHBox);

                dialogHBox.setMinWidth(currentNodeView.getWidth());
                dialogHBox.setMinHeight(currentNodeView.getHeight());

                dialogView.prefWidthProperty().bind(stage.widthProperty().subtract(25));
                dialogView.prefHeightProperty().bind(stage.heightProperty().subtract(45));

                if (isFullScreen) {
                    dialogVBox.prefHeightProperty().bind(dialogView.prefHeightProperty());
                    dialogHBox.prefWidthProperty().bind(dialogView.prefWidthProperty());
                    currentNodeView.prefWidthProperty().bind(dialogView.prefWidthProperty().subtract(5));
                    currentNodeView.prefHeightProperty().bind(dialogView.prefHeightProperty().subtract(5));
                }
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(Application.getStage());
                Stage finalStage = stage;
                stage.setOnCloseRequest(event -> {
                    //log.debug("监听到窗口关闭");
                    finalStage.hide();
                    try {
                        BaseController bizController = (BaseController) nodeView.getPresenter();
                        bizController.dispose();
                    } catch (Exception e) {
                    }
                });
//                dialogVBox.setStage(stage);//StageStyle.UTILITY
            }
            stage.setTitle(title);
            dialogHBox.getChildren().setAll(currentNodeView);
            stage.setMinWidth(width);
            stage.setMinHeight(height);
            stage.setWidth(width);
            stage.setHeight(height);
            stage.show();
        } catch (Throwable e) {
            log.error("打开窗口异常!", e);
            ialert.error("打开窗口异常!" + e.getMessage());
        }
    }

    @Override
    public void dispose() {

    }
}