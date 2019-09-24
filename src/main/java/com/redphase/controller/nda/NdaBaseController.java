package com.redphase.controller.nda;

import com.alibaba.fastjson.JSONObject;
import com.redphase.api.account.IAccountService;
import com.redphase.api.nda.*;
import com.redphase.api.nda.aa.*;
import com.redphase.api.nda.ae.*;
import com.redphase.api.nda.hfct.*;
import com.redphase.api.nda.tev.*;
import com.redphase.api.nda.uhf.*;
import com.redphase.api.task.IAccountSiteInfoService;
import com.redphase.controller.BaseController;
import com.redphase.controller.IndexController;
import com.redphase.controller.nda.data.NdaDataTJLBController;
import com.redphase.controller.nda.data.NdaSiteCountController;
import com.redphase.controller.nda.data.NdaSiteImagesController;
import com.redphase.controller.nda.data.NdaSiteSoundController;
import com.redphase.dto.SkillItemDto;
import com.redphase.dto.TreeItemDto;
import com.redphase.dto.ZTreeNodeDto;
import com.redphase.dto.atlas.DataDeviceDto;
import com.redphase.dto.table.DataTableDto;
import com.redphase.framework.util.ValidatorUtil;
import com.redphase.view.nda.NdaRunView;
import com.redphase.view.nda.data.*;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

@FXMLController
@Slf4j
public abstract class NdaBaseController extends BaseController {
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    @Autowired
    protected IndexController iwin;
    @Autowired
    protected NdaRunView ndaRunView;
    @Autowired
    protected NdaDataSiteCountView ndaDataSiteCountView;
    @Autowired
    protected NdaDataSiteImagesView ndaDataSiteImagesView;
    @Autowired
    protected NdaDataSiteSoundView ndaDataSiteSoundView;
    @Autowired
    protected NdaDataTEVAnalysisView ndaHorizontalAnalysisView;
    @Autowired
    protected NdaDataTJLBView ndaDataTJLBView;
    @Autowired
    protected IAccountService accountService;
    @Autowired
    protected IAccountSiteInfoService accountSiteInfoService;
    @Autowired
    protected INdaDeviceService dataDeviceService;
    @Autowired
    protected INdaDeviceSiteService dataDeviceSiteService;
    @Autowired
    protected INdaAnalyzeService dataAnalyzeService;
    @Autowired
    protected INdaHjService dataHjService;
    @Autowired
    protected INdaLcService dataLcService;
    @Autowired
    protected INdaAaTjlbService dataAaTjlbService;
    @Autowired
    protected INdaAaTjService dataAaTjService;
    @Autowired
    protected INdaAaTjzsService dataAaTjzsService;
    @Autowired
    protected INdaAaXjService dataAaXjService;
    @Autowired
    protected INdaAaXjzsService dataAaXjzsService;
    @Autowired
    protected INdaAaBxService dataAaBxService;
    @Autowired
    protected INdaAaFxService dataAaFxService;
    @Autowired
    protected INdaAeBxService dataAeBxService;
    @Autowired
    protected INdaAeFxService dataAeFxService;
    @Autowired
    protected INdaAeTjlbService dataAeTjlbService;
    @Autowired
    protected INdaAeTjService dataAeTjService;
    @Autowired
    protected INdaAeTjzsService dataAeTjzsService;
    @Autowired
    protected INdaAeXjService dataAeXjService;
    @Autowired
    protected INdaAeXjzsService dataAeXjzsService;
    @Autowired
    protected INdaHfctTjlbService dataHfctTjlbService;
    @Autowired
    protected INdaHfctTjService dataHfctTjService;
    @Autowired
    protected INdaHfctTjzsService dataHfctTjzsService;
    @Autowired
    protected INdaHfctXjService dataHfctXjService;
    @Autowired
    protected INdaHfctXjzsService dataHfctXjzsService;
    @Autowired
    protected INdaTevTjlbService dataTevTjlbService;
    @Autowired
    protected INdaTevTjService dataTevTjService;
    @Autowired
    protected INdaTevTjzsService dataTevTjzsService;
    @Autowired
    protected INdaTevXjService dataTevXjService;
    @Autowired
    protected INdaTevXjlhService dataTevXjlhService;
    @Autowired
    protected INdaTevXjzsService dataTevXjzsService;
    @Autowired
    protected INdaUhfTj1Service dataUhfTj1Service;
    @Autowired
    protected INdaUhfTj2Service dataUhfTj2Service;
    @Autowired
    protected INdaUhfTjlb1Service dataUhfTjlb1Service;
    @Autowired
    protected INdaUhfTjlb2Service dataUhfTjlb2Service;
    @Autowired
    protected INdaUhfTjzs1Service dataUhfTjzs1Service;
    @Autowired
    protected INdaUhfTjzs2Service dataUhfTjzs2Service;
    @Autowired
    protected NdaSiteImagesController ndaSiteImagesController;
    @Autowired
    protected NdaSiteCountController ndaSiteCountController;
    @Autowired
    protected NdaDataTJLBController ndaDataTJLBController;
    @Autowired
    protected NdaSiteSoundController ndaSiteSoundController;
    @Autowired
    protected NdaController ndaController;
    protected Set<String> treeSet = new HashSet<>();

    public void loadAccTree(TreeView accountTree, Map param) {
        TreeItem<TreeItemDto> root = new TreeItem<>(new TreeItemDto("Root", null));
        accountTree.setRoot(root);
        try {
            loadAccountTree(root, dataDeviceService.loadAccountTree(param, "electricBureau", "substation", "deviceType", "voltageLevel", "dateDetection", "testTechnology"));//
            root.setExpanded(true);
        } catch (Exception e) {
            log.error("台账树加载失败!", e);
            ialert.error("台账树加载失败!");
        }
    }

    public void loadAccountTree(TreeItem root, List<ZTreeNodeDto> tree) {
        synchronized (TreeItem.class) {
            if (tree != null) {
                tree.forEach(t -> {
                    if (t instanceof ZTreeNodeDto) {
                        ZTreeNodeDto zTreeNodeDto = t;
                        Object rootObject = root.getValue();
                        String fullName = "";
                        if (rootObject != null && rootObject instanceof TreeItemDto) {
                            String parentFullName = ((TreeItemDto) rootObject).getFullName();
                            if (ValidatorUtil.isEmpty(parentFullName)) parentFullName = "";
                            fullName = parentFullName + zTreeNodeDto.getName() + "_";
                        }
                        log.debug("fullName==={}", fullName);
                        if (zTreeNodeDto.getChildren() != null && zTreeNodeDto.isParent() && zTreeNodeDto.getChildren().size() > 0) {
                            TreeItem treeItem = new TreeItem(new TreeItemDto(zTreeNodeDto.getName(), fullName, zTreeNodeDto));
                            treeItem.setExpanded(treeSet.contains(zTreeNodeDto.getId()));
                            root.getChildren().add(treeItem);
                            loadAccountTree(treeItem, zTreeNodeDto.getChildren());
                        } else {
                            DataDeviceDto deviceDto = (DataDeviceDto) zTreeNodeDto.getData();
                            SkillItemDto skillItemDto = null;
                            String technologyFullName = fullName + "_" + deviceDto.getTestTechnology();
                            switch (deviceDto.getTestTechnology()) {
                                case "AA": {//"空气式超声波
                                    skillItemDto = new SkillItemDto(technologyFullName, SkillItemDto.Skill.AA, zTreeNodeDto);
                                    break;
                                }
                                case "AE": {//"接触式超声波
                                    skillItemDto = new SkillItemDto(technologyFullName, SkillItemDto.Skill.AE, zTreeNodeDto);
                                    break;
                                }
                                case "TEV": {//"地电波
                                    skillItemDto = new SkillItemDto(technologyFullName, SkillItemDto.Skill.TEV, zTreeNodeDto);
                                    break;
                                }
                                case "UHF": {//"特高频
                                    skillItemDto = new SkillItemDto(technologyFullName, SkillItemDto.Skill.UHF, zTreeNodeDto);
                                    break;
                                }
                                case "HFCT": {//"高频电流
                                    skillItemDto = new SkillItemDto(technologyFullName, SkillItemDto.Skill.HFCT, zTreeNodeDto);
                                    break;
                                }
                            }
                            TreeItem testTechnologyTree = new TreeItem(skillItemDto);
                            testTechnologyTree.setExpanded(treeSet.contains(deviceDto.getTestTechnology()));
                            root.getChildren().add(testTechnologyTree);
                        }
                    }
                });
            }
        }
    }

    protected void clearL(Label... labels) {
        for (Label label : labels) {
            String noImgStyle = "-fx-underline: false;-fx-text-fill: #000000;";
            if (label == null) continue;
            label.setStyle(noImgStyle);
            label.setText("--");
        }
    }

    protected void hide(Label... labels) {
        for (Label label : labels) {
            if (label == null) continue;
            label.setManaged(false);
            label.setVisible(false);
        }
    }

    protected void show(Label... labels) {
        for (Label label : labels) {
            if (label == null) continue;
            label.setManaged(true);
            label.setVisible(true);
        }
    }

    protected void setLabelText(Label label, Object object, String append) {
        if (ValidatorUtil.notEmpty("" + object) && !("" + object).equals("#")) {
            label.setText("" + object + append);
        } else {
            label.setText("--");
        }
    }

    /**
     * 相册显示
     *
     * @param type      1截图 2照片
     * @param node
     * @param textValue
     * @param objList
     */
    protected void loadImageDialog(String testTechnology, Integer type, Node node, String textValue, List objList) {
        Platform.runLater(() -> {
            DataTableDto dataTableDto = null;
            node.setStyle("-fx-underline: false;-fx-text-fill: #000000;");
            if (objList == null || objList.size() == 0) {
                if (node instanceof TableCell) {
                    ((TableCell) node).setText("--");
                } else if (node instanceof Label) {
                    ((Label) node).setText("--");
                }
                return;
            }
            Set sets = new HashSet<>();
            Object object = objList.get(0);
            if (object instanceof String) {
                for (Object img : objList) {
                    sets.add(img.toString());
                }
            } else {
                List<DataTableDto> list = copyTo(objList, DataTableDto.class);
                if (list != null) {
                    for (DataTableDto dto : list) {
                        boolean flag = true;
                        String imgUrlArr = null;
                        switch (type) {
                            case 1: {
                                if (ValidatorUtil.isEmpty(dto.getScreenshots()) || "[]".equals(dto.getScreenshots())) {
                                    flag = false;
                                }
                                sets.add(dto);
                                break;
                            }
                            case 2: {
                                if (ValidatorUtil.isEmpty(dto.getPhotos()) || "[]".equals(dto.getPhotos())) {
                                    flag = false;
                                } else {
                                    imgUrlArr = dto.getPhotos();
                                }
                                List<String> imgs = JSONObject.parseArray(imgUrlArr, String.class);
                                if (imgs != null) {
                                    for (String img : imgs) {
                                        sets.add(img);
                                    }
                                }
                                break;
                            }
                        }
                        if (!flag) {
                            continue;
                        }
                        dataTableDto = dto;
                    }
                }
            }
            if (sets.size() == 0) {
                node.setStyle("-fx-underline: false;-fx-text-fill: #000000;");
                if (node instanceof TableCell) {
                    ((TableCell) node).setText("--");
                } else if (node instanceof Label) {
                    ((Label) node).setText("--");
                }
                return;
            }
            if (node instanceof TableCell) {
                ((TableCell) node).setText(textValue);
            } else if (node instanceof Label) {
                ((Label) node).setText(textValue);
            }
            node.setStyle("-fx-text-fill: #31a1ff;-fx-underline: true;-fx-cursor: hand;");
            String title = "";
            Object titleObj = sets.iterator().next();
            if (titleObj instanceof String) {
                title = "" + titleObj;
            } else {
                title = ((DataTableDto) titleObj).getScreenshots();
            }
            DataTableDto finalDataTableDto = dataTableDto;
            String finalTitle = title;
            node.setOnMouseClicked((me) -> {
                idialog.openDialog(finalTitle, ndaDataSiteImagesView, 1150.0, 745.0, true);
                ndaSiteImagesController.init(testTechnology, new ArrayList() {{
                    addAll(sets);
                }}, new HashMap() {{
                    put("dataTableDto", finalDataTableDto);
                }});
            });
        });
    }

    /**
     * 音频播放器显示
     *
     * @param node
     * @param textValue
     * @param objList
     */
    protected void loadSoundDialog(Node node, String textValue, List objList) {
        Platform.runLater(() -> {
            DataTableDto dataTableDto = null;
            node.setStyle("-fx-underline: false;-fx-text-fill: #000000;");
            if (objList == null || objList.size() == 0) {
                if (node instanceof TableCell) {
                    ((TableCell) node).setText("--");
                } else if (node instanceof Label) {
                    ((Label) node).setText("--");
                }
                return;
            }
            Set sets = new HashSet<>();
            Object object = objList.get(0);
            if (object instanceof String) {
                for (Object img : objList) {
                    sets.add(img.toString());
                }
            } else {
                List<DataTableDto> list = copyTo(objList, DataTableDto.class);
                if (list != null) {
                    for (DataTableDto dto : list) {
                        boolean flag = true;
                        String audiosUrlArr = null;
                        if (ValidatorUtil.isEmpty(dto.getAudios()) || "[]".equals(dto.getAudios())) {
                            flag = false;
                        } else {
                            audiosUrlArr = dto.getAudios();
                        }
                        List<String> audiosList = JSONObject.parseArray(audiosUrlArr, String.class);
                        if (audiosList != null) {
                            for (String audio : audiosList) {
                                sets.add(audio);
                            }
                        }
                        if (!flag) {
                            continue;
                        }
                        dataTableDto = dto;
                    }
                }
            }
            if (sets.size() == 0) {
                node.setStyle("-fx-underline: false;-fx-text-fill: #000000;");
                if (node instanceof TableCell) {
                    ((TableCell) node).setText("--");
                } else if (node instanceof Label) {
                    ((Label) node).setText("--");
                }
                return;
            }
            if (node instanceof TableCell) {
                ((TableCell) node).setText(textValue);
            } else if (node instanceof Label) {
                ((Label) node).setText(textValue);
            }
            node.setStyle("-fx-text-fill: #31a1ff;-fx-underline: true;-fx-cursor: hand;");
            String title = "";
            Object titleObj = sets.iterator().next();
            if (titleObj instanceof String) {
                title = "" + titleObj;
            } else {
                title = ((DataTableDto) titleObj).getScreenshots();
            }
            DataTableDto finalDataTableDto = dataTableDto;
            String finalTitle = title;
            node.setOnMouseClicked((me) -> {
                idialog.openDialog(finalTitle, ndaDataSiteSoundView, 1250.0, 745.0, true);
                ndaSiteSoundController.init(new ArrayList() {{
                    addAll(sets);
                }}, new HashMap() {{
                    put("dataTableDto", finalDataTableDto);
                }});
            });
        });
    }

    /**
     * 统计检测文件
     */
    protected void loadTjDialog(String testTechnology, Node node, String title, String textValue, List tjList) {
        Platform.runLater(() -> {
            node.setStyle("-fx-underline: false;-fx-text-fill: #000000;");
            if (tjList != null && tjList.size() > 0) {
                node.setStyle("-fx-text-fill: #31a1ff;-fx-underline: true;-fx-cursor: hand;");
                ((TableCell) node).setText(textValue);
                node.setOnMouseClicked((me) -> {
//                    log.debug(testTechnology+"统计检测文件==>{}", dataAaTableDto.getFileUrl());
                    idialog.openDialog(title, ndaDataSiteCountView, 1285.0, 700.0, true);
                    ndaSiteCountController.init(testTechnology, tjList, new HashMap() {{
                    }});
                });
            } else {
                ((TableCell) node).setText("--");
            }
        });
    }

    /**
     * 统计录波文件
     */
    protected void loadTjlbDialog(String testTechnology, Node node, String title, String textValue, List tjlbList) {
        Platform.runLater(() -> {
            node.setStyle("-fx-underline: false;-fx-text-fill: #000000;");
            if (tjlbList != null && tjlbList.size() > 0) {
                node.setStyle("-fx-text-fill: #31a1ff;-fx-underline: true;-fx-cursor: hand;");
                ((TableCell) node).setText(textValue);
                node.setOnMouseClicked((me) -> {
//                    log.debug(testTechnology+"统计录波==>{}", dataAaTableDto.getFileUrl());
                    idialog.openDialog(title, ndaDataTJLBView, 1330.0, 768.0, false);
                    ndaDataTJLBController.init(testTechnology, tjlbList);
                });
            } else {
                ((TableCell) node).setText("--");
            }
        });
    }
}
