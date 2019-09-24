package com.redphase.controller.nda;

import com.Application;
import com.redphase.api.nda.ISlaveService;
import com.redphase.controller.nda.data.NdaDataAAController;
import com.redphase.controller.nda.data.NdaDataSkillController;
import com.redphase.controller.nda.data.NdaDataTEVController;
import com.redphase.dto.SkillItemDto;
import com.redphase.dto.TreeItemDto;
import com.redphase.dto.ZTreeNodeDto;
import com.redphase.framework.util.CommonConstant;
import com.redphase.view.nda.NdaInitView;
import com.redphase.view.nda.data.NdaDataAAView;
import com.redphase.view.nda.data.NdaDataSkillView;
import com.redphase.view.nda.data.NdaDataTEVView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

@FXMLController
@Slf4j
public class NdaController extends NdaBaseController implements Initializable {
    protected Set<String> treeSet = new HashSet<>();
    @FXML
    @Getter
    private TreeView accountTree;
    @FXML
    @Getter
    private AnchorPane baseAtlasBorderPane;
    @FXML
    @Getter
    private Pane rightAnchorPane;
    @Autowired
    private ISlaveService slaveService;
    @Autowired
    private NdaInitView ndaImportInitView;
    @Autowired
    private NdaDataTEVView ndaDataTEVView;
    @Autowired
    private NdaDataAAView ndaDataAAView;
    @Autowired
    private NdaDataSkillView ndaDataSkillView;
    @Autowired
    private NdaDataSkillController ndaDataSkillController;
    @Autowired
    private NdaDataTEVController ndaDataTEVController;
    @Autowired
    private NdaDataAAController ndaDataAAController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize...");
        Platform.runLater(() -> {
            treeSet.clear();
            initRightAnchorPane();
        });
        accountTree.prefHeightProperty().bind(Application.getStage().heightProperty().subtract(130));
        rightAnchorPane.prefWidthProperty().bind(Application.getStage().widthProperty().subtract(370));
        rightAnchorPane.prefHeightProperty().bind(Application.getStage().heightProperty().subtract(85));
        accountTree.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Node node = event.getPickResult().getIntersectedNode();
            if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
                TreeItem currentTreeItem = ((TreeItem) accountTree.getSelectionModel().getSelectedItem());
                Object object = currentTreeItem.getValue();
                if (currentTreeItem.getChildren().size() > 0) {
                    currentTreeItem.setExpanded(!currentTreeItem.isExpanded());
                    if (object instanceof TreeItemDto) {
                        TreeItemDto treeItemDto = (TreeItemDto) object;
                        Object valObj = treeItemDto.getValue();
                        if (valObj instanceof ZTreeNodeDto) {
                            ZTreeNodeDto zTreeNodeDto = (ZTreeNodeDto) valObj;
                            if (currentTreeItem.isExpanded()) {
                                treeSet.add("" + zTreeNodeDto.getId());
                            } else {
                                treeSet.remove("" + zTreeNodeDto.getId());
                            }
                        } else {
                            if (currentTreeItem.isExpanded()) {
                                treeSet.add("" + treeItemDto.getValue());
                            } else {
                                treeSet.remove("" + treeItemDto.getValue());
                            }
                        }
                    } else if (object instanceof SkillItemDto) {
                        SkillItemDto skillItemDto = (SkillItemDto) object;
                        if (currentTreeItem.isExpanded()) {
                            treeSet.add("" + skillItemDto.getValue());
                        } else {
                            treeSet.remove("" + skillItemDto.getValue());
                        }
                    } else {
                        String value = (String) object;
                        if (currentTreeItem.isExpanded()) {
                            treeSet.add(value);
                        } else {
                            treeSet.remove(value);
                        }
                    }
                }

                if (object instanceof TreeItemDto) {
                    initRightAnchorPane();
                } else if (object instanceof SkillItemDto && currentTreeItem.getChildren().size() == 0) {
                    SkillItemDto skillItemDto = (SkillItemDto) object;
                    ehcacheUtil.setCache(CommonConstant.NDA_DATA_SKILL_FULL_NAME, skillItemDto.getFullName());
                    switch (skillItemDto.getSkill()) {
                        case AA: {
                            rightAnchorPane.getChildren().setAll(ndaDataAAView.getView());
                            ndaDataAAController.loadAAData(skillItemDto.getFullName());
                            break;
                        }
                        case AE: {
                            rightAnchorPane.getChildren().setAll(ndaDataSkillView.getView());
                            ndaDataSkillController.loadDeviceList("AE");
                            break;
                        }
                        case TEV: {
                            rightAnchorPane.getChildren().setAll(ndaDataTEVView.getView());
                            ndaDataTEVController.loadTEVData(skillItemDto.getFullName());
                            break;
                        }
                        case UHF: {
                            rightAnchorPane.getChildren().setAll(ndaDataSkillView.getView());
                            ndaDataSkillController.loadDeviceList("UHF");
                            break;
                        }
                        case HFCT: {
                            rightAnchorPane.getChildren().setAll(ndaDataSkillView.getView());
                            ndaDataSkillController.loadDeviceList("HFCT");
                            break;
                        }
                    }
                    BorderPane dataBorderPane = (BorderPane) rightAnchorPane.lookup("#dataBorderPane");
//                    log.debug("getStage==width:{},height:{}",Application.getStage().getWidth(),Application.getStage().getHeight());
                    dataBorderPane.minWidthProperty().bind(Application.getStage().widthProperty().subtract(370));
                    dataBorderPane.minHeightProperty().bind(Application.getStage().heightProperty().subtract(20));
//                    log.debug("dataScrollPane==height:{}",dataScrollPane.prefHeightProperty().doubleValue());
                }
            }
        });
        loadAccTree();
    }

    @FXML
    public void loadAccTree() {
        loadAccTree(accountTree, new HashMap() {{
        }});
    }

    @FXML
    public void flushDb() {
        try {
            slaveService.flushDb();
            loadAccTree();
            ialert.success("操作完成!");
        } catch (Exception e) {
            log.error("系统异常!", e);
            ialert.error("系统异常!" + e.getMessage());
        }
    }

    private void initRightAnchorPane() {
        rightAnchorPane.getChildren().setAll(ndaImportInitView.getView());
        rightAnchorPane.setStyle("-fx-background-color: #F3F4F5");
        ehcacheUtil.setCache(CommonConstant.SYS_TIMEOUT_KEY, System.currentTimeMillis());//更新当前时间
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



