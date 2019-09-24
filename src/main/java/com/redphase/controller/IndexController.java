package com.redphase.controller;

import com.Application;
import com.redphase.dto.ViewDto;
import com.redphase.dto.sys.SysVariableDto;
import com.redphase.dto.user.UserDto;
import com.redphase.framework.util.CommonConstant;
import com.redphase.framework.util.I18nUtil;
import com.redphase.framework.util.ValidatorUtil;
import com.redphase.view.LoginView;
import com.redphase.view.ViewFactory;
import com.redphase.view.account.AccountView;
import com.redphase.view.atlas.AtlasView;
import com.redphase.view.convert.ConvertView;
import com.redphase.view.nda.NdaView;
import com.redphase.view.setup.*;
import com.redphase.view.task.TaskView;
import com.redphase.view.trend.TrendView;
import com.redphase.view.user.OperactionView;
import com.redphase.view.user.UserView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

@FXMLController
@Slf4j
public class IndexController extends BaseController implements Initializable {
    @FXML
    private TabPane tabPane;
    @FXML
    private VBox welcomeVBox;
    @FXML
    private TreeView leftMenu;
    @FXML
    private ImageView avatar;
    @FXML
    private Label username;
    @Autowired
    private SetupThresholdView setupThresholdView;
    @Autowired
    private SetupVersionView setupVersionView;
    @Autowired
    private SetupDbView setupDbView;
    @Autowired
    private SetupSuperView setupSuperView;
    @Autowired
    private SetupPathView setupPathView;
    @Autowired
    private SetupSocketView setupSocketView;
    @Autowired
    private SetupSysView setupSysView;

    @Autowired
    private AtlasView atlasView;
    @Autowired
    private TrendView trendView;
    @Autowired
    private ConvertView convertView;
    @Autowired
    private UserView userView;
    @Autowired
    private OperactionView operactionView;
    @Autowired
    private AccountView accountView;
    @Autowired
    private TaskView taskView;
    @Autowired
    private NdaView ndaView;
    @Getter
    Timeline timeline = new Timeline();
    Duration oneFrameAmt = Duration.millis(10 * 1000);
    KeyFrame oneFrame;
    @Setter
    @Getter
    SysVariableDto iSysTimeoutDto = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("IndexController initialize...");
        this.iwin = this;
        this.tabPaneViewMap = globalService.getTabPaneViewMap();
        welcomeVBox.prefWidthProperty().bind(Application.getScene().widthProperty().subtract(240));
        welcomeVBox.prefHeightProperty().bind(Application.getScene().heightProperty().subtract(40.0));
        //获取用户
        UserDto userDto = jwtUtil.getSubject();
        String avatarUrl = userDto.getAvatar();
        if (ValidatorUtil.isEmpty(avatarUrl)) {
            avatarUrl = "/com/redphase/images/avatar.png";
        }
        avatar.setImage(new Image(avatarUrl));
        username.setText(userDto.getName());
        try {
            initLeftMenu();
        } catch (Exception e) {
            ialert.error(e.getMessage());
        }
        Application.getStage().addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
            //log.info("屏幕活动!{},{}", event.getX(), event.getY());
            ehcacheUtil.setCache(CommonConstant.SYS_TIMEOUT_KEY, System.currentTimeMillis());//更新当前时间
        });
        if (oneFrame == null) {
            oneFrame = new KeyFrame(oneFrameAmt, event -> {
                try {
                    //log.info("System.currentTimeMillis()=={},{}", System.currentTimeMillis(), ehcacheUtil.getCache(CommonConstant.SYS_TIMEOUT_KEY));
                    if (iSysTimeoutDto == null) {
                        iSysTimeoutDto = setupService.getVariableByCode(new SysVariableDto() {{
                            setCode("i-sys-timeout");
                        }});
                    }
                    Long lastTimeOut = (Long) ehcacheUtil.getCache(CommonConstant.SYS_TIMEOUT_KEY) + (Long.parseLong(iSysTimeoutDto.getValue()) * 60 * 1000);
                    if (lastTimeOut < System.currentTimeMillis()) {
                        Platform.runLater(() -> {
                            timeline.stop();
                            for (Stage stage : idialog.getStageMap().values()) {
                                stage.hide();
                            }
                            Application.getStage().setMaximized(false);
                            Application.getStage().setScene(null);
                            Application.showView(LoginView.class);
                            Application.getStage().centerOnScreen();
                            Application.getStage().setWidth(480);
                            Application.getStage().setHeight(570);
                            ialert.warning(I18nUtil.get("error.timtout"));
                        });
                    }
                } catch (Throwable e) {
                    log.error("长时间未使用!", e);
                }
            });
        }
        timeline.getKeyFrames().setAll(oneFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void initLeftMenu() throws Exception {
        log.info("初始化左侧菜单..");
        TreeItem<String> root = new TreeItem<>("Root");
        root.setExpanded(true);
        leftMenu.setRoot(root);

        ObservableList<TreeItem<String>> rootTreeItems = root.getChildren();
        if (jwtUtil.isPermitted("account:menu")) {
            rootTreeItems.add(loadLeftMenuItem(new ViewDto() {{
                setName(I18nUtil.get("index.tab.account"));
                setIcon("account.png");
                setFxmlView(accountView);
            }}));
        }
        if (jwtUtil.isPermitted("task:menu")) {
            rootTreeItems.add(loadLeftMenuItem(new ViewDto() {{
                setName(I18nUtil.get("index.tab.task"));
                setIcon("activity.png");
                setFxmlView(taskView);
            }}));
        }
        if (jwtUtil.isPermitted("analyze:menu")) {
            rootTreeItems.add(loadLeftMenuItem(new ViewDto() {{
                setName(I18nUtil.get("index.tab.analyze"));
                setIcon("407Report.png");
                setFxmlView(atlasView);
            }}));
        }
        if (jwtUtil.isPermitted("trend:menu")) {
            rootTreeItems.add(loadLeftMenuItem(new ViewDto() {{
                setName(I18nUtil.get("index.tab.trend"));
                setIcon("409Report.png");
                setFxmlView(trendView);
            }}));
        }
//        if (jwtUtil.isPermitted("trend:menu")) {
            rootTreeItems.add(loadLeftMenuItem(new ViewDto() {{
//                setName(I18nUtil.get("index.tab.trend"));
                setName("数据查看");
                setIcon("chakan.png");
                setFxmlView(ndaView);
            }}));
//        }
//        if (jwtUtil.isPermitted("export:menu")) {
            rootTreeItems.add(loadLeftMenuItem(new ViewDto() {{
//                setName(I18nUtil.get("index.tab.export"));
                setName("数据导出");
                setIcon("export.png");
                setFxmlView(convertView);
            }}));
//        }
        if (jwtUtil.isPermitted("user:menu")) {
            rootTreeItems.add(loadLeftMenuItem(new ViewDto() {{
                setName(I18nUtil.get("index.tab.user"));
                setIcon("addressbook.png");
                setFxmlView(userView);
            }}));
        }
        if (jwtUtil.isPermitted("alog:menu")) {
            rootTreeItems.add(loadLeftMenuItem(new ViewDto() {{
                setName(I18nUtil.get("index.tab.alog"));
                setIcon("createtask.png");
                setFxmlView(operactionView);
            }}));
        }
        if (jwtUtil.isPermitted("sys:menu")) {
            TreeItem setupItems = new TreeItem<>(I18nUtil.get("index.tab.setup"), loadMenuIcon("setup.png"));
            root.getChildren().add(setupItems);
            ObservableList<TreeItem<String>> setupTreeItems = setupItems.getChildren();
            if (jwtUtil.isPermitted("sys:threshold")) {
                setupTreeItems.add(loadLeftMenuItem(new ViewDto() {{
                    setName(I18nUtil.get("index.tab.threshold"));
                    setNodeView(setupThresholdView.getView());
                }}));
            }
            if (jwtUtil.isPermitted("sys:path")) {
                setupTreeItems.add(loadLeftMenuItem(new ViewDto() {{
                    setName(I18nUtil.get("index.tab.path"));
                    setNodeView(setupPathView.getView());
                }}));
            }
            if (jwtUtil.isPermitted("sys:socket")) {
                setupTreeItems.add(loadLeftMenuItem(new ViewDto() {{
                    setName(I18nUtil.get("index.tab.socket"));
                    setNodeView(setupSocketView.getView());
                }}));
            }
            if (jwtUtil.isPermitted("sys:timeout")) {
                setupTreeItems.add(loadLeftMenuItem(new ViewDto() {{
                    setName(I18nUtil.get("index.tab.timeout"));
                    setNodeView(setupSysView.getView());
                }}));
            }
            if (jwtUtil.isPermitted("sys:db")) {
                setupTreeItems.add(loadLeftMenuItem(new ViewDto() {{
                    setName(I18nUtil.get("index.tab.db"));
                    setNodeView(setupDbView.getView());
                }}));
            }
            if (jwtUtil.isPermitted("sys:version")) {
                setupTreeItems.add(loadLeftMenuItem(new ViewDto() {{
                    setName(I18nUtil.get("index.tab.version"));
                    setNodeView(setupVersionView.getView());
                }}));
            }
            if (jwtUtil.isPermitted("sys:super")) {
                setupTreeItems.add(loadLeftMenuItem(new ViewDto() {{
                    setName(I18nUtil.get("index.tab.super"));
                    setNodeView(setupSuperView.getView());
                }}));
            }
        }

        leftMenu.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                tabPane.getTabs().forEach(tab -> {
                    if (tab.getText().equalsIgnoreCase(I18nUtil.get("index.tab.home"))) {
                        return;
                    }
                });
                Node node = event.getPickResult().getIntersectedNode();
                // Accept clicks only on node cells, and not on empty spaces of the TreeView
                if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
                    TreeItem currentTreeItem = ((TreeItem) leftMenu.getSelectionModel().getSelectedItem());
                    String name = (String) currentTreeItem.getValue();
                    AtomicBoolean flag = new AtomicBoolean(false);
                    tabPane.getTabs().forEach(tab -> {
                        if (tab.getText().equalsIgnoreCase(name)) {
                            flag.set(true);
                            tabPane.getSelectionModel().select(tab);
                            return;
                        }
                    });
                    if (!flag.get()) {
                        ViewDto viewDto = tabPaneViewMap.get(name);
                        if (viewDto != null) {
                            Tab tab = new Tab(name);
                            if (ValidatorUtil.notEmpty(viewDto.getUrl())) {
                                viewDto.setNodeView(ViewFactory.createWebView(viewDto.getUrl(), viewDto.getCallHandlers()));
                                tab.setContent(viewDto.getNodeView());
                            } else if (viewDto.getFxmlView() != null) {
                                tab.setContent(viewDto.getFxmlView().getView());
                            } else if (viewDto.getNodeView() != null) {
                                tab.setContent(viewDto.getNodeView());
                            }
                            //tab.setGraphic(loadMenuIcon(menuDto.getIcon()));
                            tabPane.getTabs().add(tab);
                            tabPane.getSelectionModel().select(tab);
                        } else {
                            TreeItem treeItem = ((TreeItem) leftMenu.getSelectionModel().getSelectedItem());
                            treeItem.setExpanded(true);
                        }
                    }
                    if (currentTreeItem.isLeaf()) {
                        List<TreeItem<String>> menus = leftMenu.getRoot().getChildren();
                        menus.forEach(item -> {
                            if (!item.isLeaf() && !currentTreeItem.getParent().equals(item)) {
                                item.setExpanded(false);
                            }
                        });
                    }
                }
            } catch (Exception e) {
                log.error("系统异常!", e);
                ialert.error(I18nUtil.get("error.sys") + e.getMessage());
            }
        });
    }

    @Override
    public void dispose() {

    }
//
//    public void openTab(String name, Parent node) {
//        tabPane.getTabs().forEach(tab -> {
//            if (name.equalsIgnoreCase(tab.getText())) {
//                tabPane.getSelectionModel().select(tab);
//                tab.setContent(node);
//                return;
//            }
//        });
//    }
//
//    public void openTab(String name) {
//        AtomicBoolean flag = new AtomicBoolean(false);
//        tabPane.getTabs().forEach(tab -> {
//            if (name.equalsIgnoreCase(tab.getText())) {
//                flag.set(true);
//                tabPane.getSelectionModel().select(tab);
//                return;
//            }
//        });
//        if (!flag.get()) {
//            ViewDto viewDto = globalService.getTabPaneViewMap().get(name);
//            if (viewDto != null) {
//                Tab tab = new Tab(name);
//                if (ValidatorUtil.notEmpty(viewDto.getUrl())) {
//                    try {
//                        viewDto.setNodeView(ViewFactory.createWebView(viewDto.getUrl(), viewDto.getCallHandlers()));
//                    } catch (Exception e) {
//                        ialert.error("系统页面加载失败!"+e.getMessage());
//                    }
//                    tab.setContent(viewDto.getNodeView());
//                } else if (viewDto.getNodeView() != null) {
//                    tab.setContent(viewDto.getNodeView());
//                }
//                tabPane.getTabs().add(tab);
//                tabPane.getSelectionModel().select(tab);
//            }
//        }
//    }
//
//    public void closeTab(String name) {
//        tabPane.getTabs().forEach(tab -> {
//            if (name.equalsIgnoreCase(tab.getText())) {
//                tabPane.getTabs().remove(tab);
//                return;
//            }
//        });
//    }
}