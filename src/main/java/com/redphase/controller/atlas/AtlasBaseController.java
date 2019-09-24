package com.redphase.controller.atlas;

import com.alibaba.fastjson.JSONObject;
import com.redphase.api.account.IAccountService;
import com.redphase.api.atlas.*;
import com.redphase.api.atlas.aa.*;
import com.redphase.api.atlas.ae.*;
import com.redphase.api.atlas.hfct.*;
import com.redphase.api.atlas.tev.*;
import com.redphase.api.atlas.uhf.*;
import com.redphase.api.task.IAccountSiteInfoService;
import com.redphase.controller.BaseController;
import com.redphase.controller.IndexController;
import com.redphase.controller.atlas.data.AtlasDataTJLBController;
import com.redphase.controller.atlas.data.AtlasSiteCountController;
import com.redphase.controller.atlas.data.AtlasSiteImagesController;
import com.redphase.controller.atlas.data.AtlasSiteSoundController;
import com.redphase.dto.TreeItemDto;
import com.redphase.dto.ZTreeNodeDto;
import com.redphase.dto.atlas.DataDeviceDto;
import com.redphase.dto.SkillItemDto;
import com.redphase.dto.table.DataTableDto;
import com.redphase.framework.util.ValidatorUtil;
import com.redphase.view.atlas.AtlasRunView;
import com.redphase.view.atlas.data.*;
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
public abstract class AtlasBaseController extends BaseController {
    @Autowired
    protected IndexController iwin;
    @Autowired
    protected AtlasRunView atlasImportRunView;
    @Autowired
    protected AtlasDataSiteCountView atlasDataSiteCountView;
    @Autowired
    protected AtlasDataSiteImagesView atlasDataSiteImagesView;
    @Autowired
    protected AtlasDataSiteSoundView atlasDataSiteSoundView;
    @Autowired
    protected AtlasDataTEVAnalysisView atlasHorizontalAnalysisView;
    @Autowired
    protected AtlasDataTJLBView atlasDataTJLBView;
    @Autowired
    protected IAccountService accountService;
    @Autowired
    protected IAccountSiteInfoService accountSiteInfoService;

    @Autowired
    protected IAtlasService atlasService;
    @Autowired
    protected IDataDeviceService dataDeviceService;
    @Autowired
    protected IDataDeviceSiteService dataDeviceSiteService;
    @Autowired
    protected IDataAnalyzeService dataAnalyzeService;
    @Autowired
    protected IDataHjService dataHjService;
    @Autowired
    protected IDataLcService dataLcService;
    @Autowired
    protected IDataAaTjlbService dataAaTjlbService;
    @Autowired
    protected IDataAaTjService dataAaTjService;
    @Autowired
    protected IDataAaTjzsService dataAaTjzsService;
    @Autowired
    protected IDataAaXjService dataAaXjService;
    @Autowired
    protected IDataAaXjzsService dataAaXjzsService;
    @Autowired
    protected IDataAaBxService dataAaBxService;
    @Autowired
    protected IDataAaFxService dataAaFxService;
    @Autowired
    protected IDataAeBxService dataAeBxService;
    @Autowired
    protected IDataAeFxService dataAeFxService;
    @Autowired
    protected IDataAeTjlbService dataAeTjlbService;
    @Autowired
    protected IDataAeTjService dataAeTjService;
    @Autowired
    protected IDataAeTjzsService dataAeTjzsService;
    @Autowired
    protected IDataAeXjService dataAeXjService;
    @Autowired
    protected IDataAeXjzsService dataAeXjzsService;
    @Autowired
    protected IDataHfctTjlbService dataHfctTjlbService;
    @Autowired
    protected IDataHfctTjService dataHfctTjService;
    @Autowired
    protected IDataHfctTjzsService dataHfctTjzsService;
    @Autowired
    protected IDataHfctXjService dataHfctXjService;
    @Autowired
    protected IDataHfctXjzsService dataHfctXjzsService;
    @Autowired
    protected IDataTevTjlbService dataTevTjlbService;
    @Autowired
    protected IDataTevTjService dataTevTjService;
    @Autowired
    protected IDataTevTjzsService dataTevTjzsService;
    @Autowired
    protected IDataTevXjService dataTevXjService;
    @Autowired
    protected IDataTevXjlhService dataTevXjlhService;
    @Autowired
    protected IDataTevXjzsService dataTevXjzsService;
    @Autowired
    protected IDataUhfTj1Service dataUhfTj1Service;
    @Autowired
    protected IDataUhfTj2Service dataUhfTj2Service;
    @Autowired
    protected IDataUhfTjlb1Service dataUhfTjlb1Service;
    @Autowired
    protected IDataUhfTjlb2Service dataUhfTjlb2Service;
    @Autowired
    protected IDataUhfTjzs1Service dataUhfTjzs1Service;
    @Autowired
    protected IDataUhfTjzs2Service dataUhfTjzs2Service;

    @Autowired
    protected AtlasSiteImagesController atlasSiteImagesController;
    @Autowired
    protected AtlasSiteCountController atlasSiteCountController;
    @Autowired
    protected AtlasDataTJLBController atlasDataTJLBController;
    @Autowired
    protected AtlasController atlasController;
    @Autowired
    protected AtlasSiteSoundController atlasSiteSoundController;

    protected Set<String> treeSet = new HashSet<>();

    public void loadAccTree(TreeView accountTree, Map param) {
        TreeItem<TreeItemDto> root = new TreeItem<>(new TreeItemDto("Root", null));
        accountTree.setRoot(root);
        try {
            loadAccountTree(root, dataDeviceService.loadAccountTree(param, "electricBureau", "substation", "deviceType", "voltageLevel", "dateDetection", "testTechnology"));
            root.setExpanded(true);
        } catch (Exception e) {
            log.error("台账树加载失败!", e);
            ialert.error("台账树加载失败!");
        }
    }

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

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
                idialog.openDialog(finalTitle, atlasDataSiteImagesView, 1150.0, 745.0,true);
                atlasSiteImagesController.init(testTechnology, new ArrayList() {{
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
                idialog.openDialog(finalTitle, atlasDataSiteSoundView, 1250.0, 745.0,true);
                atlasSiteSoundController.init(new ArrayList() {{
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
                    idialog.openDialog(title, atlasDataSiteCountView, 1285.0, 700.0,true);
                    atlasSiteCountController.init(testTechnology, tjList, new HashMap() {{
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
                    idialog.openDialog(title, atlasDataTJLBView, 1330.0, 768.0,false);
                    atlasDataTJLBController.init(testTechnology, tjlbList);
                });
            } else {
                ((TableCell) node).setText("--");
            }
        });
    }
}
