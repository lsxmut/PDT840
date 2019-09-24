package com.redphase.controller.atlas.data;

import com.alibaba.fastjson.JSONObject;
import com.redphase.controller.atlas.AtlasBaseController;
import com.redphase.dto.atlas.DataDeviceDto;
import com.redphase.dto.atlas.DataDeviceSiteDto;
import com.redphase.dto.table.DataTableDto;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@FXMLController
@Slf4j
public class AtlasSiteImagesController extends AtlasBaseController implements Initializable {
    @FXML
    ImageView currentImageView;
    @FXML
    Button prevButton;
    @FXML
    Button nextButton;
    @FXML
    Label pageNumberL;
    @FXML
    Label bdzL;//变电站名称
    @FXML
    Label dydjL;//电压等级
    @FXML
    Label sbL;//设备名称
    @FXML
    Label wzL;//被测部位和测试点
    @FXML
    Label cssjL;//测试时间
    @FXML
    Label xwwyL;//相位位移
    @FXML
    Label ckyzL;//参考阈值
    @FXML
    Label tbfsL;//同步方式
    @FXML
    Label tbplL;//同步频率
    @FXML
    Label xtplL;//系统频率
    @FXML
    Label fdlxL;//放电类型（先隐藏）
    @FXML
    Label mcL;//脉冲
    @FXML
    Label ldL;//烈度
    @FXML
    Label zdzL;//最大值
    @FXML
    Label fdzyL;//放大增益
    @FXML
    Label qzsjL;//前置衰减
    @FXML
    Label qzzyL;//前置增益
    @FXML
    Label kmsjL;//开门时间
    @FXML
    Label bssjL;//闭锁时间
    @FXML
    Label ddsjL;//等待时间
    @FXML
    Label yxzL;//有效值
    @FXML
    Label f1flL;//ｆ１分量
    @FXML
    Label f2flL;//ｆ２分量
    @FXML
    Pane imagePane;
    String unit = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize...");
    }

    Map map;
    List imageList;
    Integer current;

    public void init(String testTechnology, List imageList, Map map) {
        this.imageList = imageList;
        current = 0;
        this.map = map;
        hide(cssjL, xwwyL, ckyzL, tbfsL, tbplL, xtplL, fdlxL, mcL, ldL, zdzL, fdzyL, qzsjL, qzzyL, kmsjL, bssjL, ddsjL, yxzL, f1flL, f2flL);//隐藏
        switch (testTechnology) {
            case "TEV": {
                unit = "dBmV";
                show(bdzL, dydjL, sbL, wzL, cssjL, xwwyL, ckyzL, tbfsL, tbplL, xtplL, zdzL, mcL);
                break;
            }
            case "AA": {
                unit = "dBuV";
                //AA巡检和AA统计数据是dBuV
                //AA飞行和AA波形数据是uV 不显示
                show(bdzL, dydjL, sbL, wzL, cssjL, xwwyL, ckyzL, fdzyL, tbfsL, tbplL, xtplL, kmsjL, bssjL, ddsjL, zdzL, mcL, yxzL, f1flL, f2flL);
                break;
            }
            case "AE": {
                unit = "mV";
                show(bdzL, dydjL, sbL, wzL, cssjL, xwwyL, ckyzL, fdzyL, qzzyL, tbfsL, tbplL, xtplL, kmsjL, bssjL, ddsjL, fdlxL, zdzL, mcL, yxzL, f1flL, f2flL);
                break;
            }
            case "HFCT": {
                unit = "mV";
                show(bdzL, dydjL, sbL, wzL, cssjL, xwwyL, ckyzL, fdzyL, qzzyL, tbfsL, tbplL, xtplL, fdlxL, zdzL, mcL);
                break;
            }
            case "UHF": {
                unit = "dBm";
                show(bdzL, dydjL, sbL, wzL, cssjL, xwwyL, ckyzL, tbfsL, tbplL, xtplL, fdlxL);
                break;
            }
        }

        loadImage();
    }

    public void loadImage() {
        try {
            if (imageList.size() > 0) {
                if (current > 0) {
                    prevButton.setDisable(false);
                } else {
                    prevButton.setDisable(true);
                }
                if (current < imageList.size() - 1) {
                    nextButton.setDisable(false);
                } else {
                    nextButton.setDisable(true);
                }
            } else {
                prevButton.setDisable(true);
                nextButton.setDisable(true);
            }

            Object imageObject = imageList.get(current);
            String imagePath = null;
            if (imageObject instanceof String) {
                imagePath = "" + imageObject;
            } else {
                DataTableDto dataTableDto = (DataTableDto) imageObject;
                imagePath = JSONObject.parseArray(dataTableDto.getScreenshots(), String.class).get(0);
                map.put("dataTableDto", dataTableDto);
            }

            File file = new File(imagePath);
            if (!(file.exists())) {
                currentImageView.setImage(new Image("/images/404.png"));
            } else {
                currentImageView.setImage(new Image(file.toURI().toURL().toString(), true));
            }
            pageNumberL.setText((current + 1) + "/" + imageList.size());
            idialog.setTitle(atlasDataSiteImagesView, imagePath);
            clearL(bdzL, dydjL, sbL, wzL, cssjL, xwwyL, ckyzL, tbfsL, tbplL, xtplL, fdlxL, cssjL, xwwyL, ckyzL, tbfsL, tbplL, xtplL, fdlxL, mcL, ldL, zdzL, fdzyL, qzsjL, qzzyL, kmsjL, bssjL, ddsjL, yxzL, f1flL, f2flL);
            if (map == null) {
                map = new HashMap();
            }
            DataTableDto dataTableDto = (DataTableDto) map.get("dataTableDto");
            if (dataTableDto != null) {
                DataDeviceDto dataDeviceDto = dataTableDto.getDataDeviceDto();
                setLabelText(bdzL, dataDeviceDto.getSubstation(), "");//变电站名称
                setLabelText(dydjL, dataDeviceDto.getVoltageLevel(), "");//电压等级
                setLabelText(sbL, dataDeviceDto.getDeviceName(), "");//设备名称
                setLabelText(cssjL, dataTableDto.getCssj(), "");//测试时间
                setLabelText(xwwyL, dataTableDto.getXwwy(), "");//相位位移
                setLabelText(ckyzL, dataTableDto.getCkyz(), "");//参考阈值
                setLabelText(tbfsL, dataTableDto.getTbfs(), "");//同步方式
                setLabelText(tbplL, dataTableDto.getTbpl(), "");//同步频率
                setLabelText(xtplL, dataTableDto.getXtpl(), "");//系统频率
                setLabelText(mcL, dataTableDto.getMc(), "");//脉冲
                setLabelText(ldL, dataTableDto.getLd(), "");//烈度
                setLabelText(zdzL, (dataTableDto.getZdz() != null ? dataTableDto.getZdz() : dataTableDto.getFz()), unit);//最大值/幅值
                setLabelText(fdzyL, dataTableDto.getFdzy(), "");//放大增益
                setLabelText(qzsjL, dataTableDto.getQzsj(), "");//前置衰减
                setLabelText(qzzyL, dataTableDto.getQzzy(), "");//前置增益
                setLabelText(kmsjL, dataTableDto.getKmsj(), "");//开门时间
                setLabelText(bssjL, dataTableDto.getBssj(), "");//闭锁时间
                setLabelText(ddsjL, dataTableDto.getDdsj(), "");//等待时间
                setLabelText(yxzL, dataTableDto.getYxz(), "");//有效值
                setLabelText(f1flL, dataTableDto.getF1(), "");//ｆ１分量
                setLabelText(f2flL, dataTableDto.getF2(), "");//ｆ２分量
                if (StringUtils.isNotEmpty(dataTableDto.getSite())) {
                    wzL.setText(dataTableDto.getSite());
                } else {
                    DataDeviceSiteDto dataDeviceSiteDto = dataTableDto.getDataDeviceSiteDto();
                    if (dataDeviceSiteDto != null) {
                        wzL.setText(dataDeviceSiteDto.getSiteName());//被测部位和测试点
                    }
                }
            }

        } catch (Exception e) {
        }
    }

    @FXML
    public void prev() {
        if (current > 0) {
            current--;
        }
        loadImage();
    }

    @FXML
    public void next() {
        if (current < imageList.size()) {
            current++;
        }
        loadImage();
    }

    @Override
    public void dispose() {

    }
}