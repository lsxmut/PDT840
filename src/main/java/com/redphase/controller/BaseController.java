package com.redphase.controller;

import com.Application;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.github.pagehelper.PageInfo;
import com.redphase.api.setup.ISetupService;
import com.redphase.dto.ViewDto;
import com.redphase.dto.sys.SysVariableDto;
import com.redphase.framework.ObjectCopy;
import com.redphase.framework.PageDto;
import com.redphase.framework.PageUtil;
import com.redphase.framework.util.*;
import com.redphase.service.GlobalService;
import com.redphase.view.AlertView;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigInteger;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
public abstract class BaseController {
    @Autowired
    protected GlobalService globalService;
    @Autowired
    protected EhcacheUtil ehcacheUtil;
    @Autowired
    protected ISetupService setupService;
    @Autowired
    protected AlertView ialert;
    @Autowired
    protected IDialogController idialog;
    @Autowired
    protected JwtUtil jwtUtil;
    protected Map<String, ViewDto> tabPaneViewMap;
    @Autowired
    protected IndexController iwin;
    protected String successLab = "-fx-text-fill: #999999;";
    protected String errorLab = "-fx-text-fill: red;";
    /**
     * 线程池
     */
    @Autowired
    protected ThreadPoolTaskExecutor taskExecutor;
    public final static SerializeConfig resultJsonConfig = SerializeConfig.globalInstance;

    static {
        resultJsonConfig.put(BigInteger.class, ToStringSerializer.instance);
        resultJsonConfig.put(Long.class, ToStringSerializer.instance);
        resultJsonConfig.put(Long.TYPE, ToStringSerializer.instance);
    }

    /**
     * 获取参数绑定错误消息
     *
     * @param bindingResult
     * @return
     */
    public String getBindErrorMsg(BindingResult bindingResult) {
        StringBuilder errorMsg = new StringBuilder();
        List<ObjectError> errorList = bindingResult.getAllErrors();
        for (ObjectError error : errorList) {
            errorMsg.append(error.getDefaultMessage()).append(";");
        }
        return errorMsg.toString();
    }

    public Integer getPageNum(Object obj) {
        Object pageNum = ReflectUtil.getValueByFieldName(obj, "pageNum");
        return ValidatorUtil.notEmpty("" + pageNum) ? Integer.parseInt("" + pageNum) : 1;
    }

    public PageDto getPageDto(PageInfo pageInfo) {
        return PageUtil.copy(pageInfo);
    }

    public <T> T copyTo(Object obj, Class<T> toObj) throws Exception {
        if (obj == null) {
            return null;
        }
        return ObjectCopy.copyTo(obj, toObj);
    }

    public <T> List<T> copyTo(List from, Class<T> to) {
        if (from == null) {
            return null;
        }
        return ObjectCopy.copyTo(from, to);
    }

    public String toJson(Object obj) {
        return JSON.toJSONString(obj, resultJsonConfig, SerializerFeature.DisableCircularReferenceDetect);
    }

    public ImageView loadMenuIcon(String url) {
        if (ValidatorUtil.isEmpty(url)) {
            return null;
        }
        return new ImageView("/com/redphase/images/" + url) {{
            setFitHeight(15);
            setFitWidth(15);
        }};
    }

    public TreeItem loadLeftMenuItem(ViewDto menuDto) {
        TreeItem treeItem = new TreeItem<String>(menuDto.getName());
        if (ValidatorUtil.notEmpty(menuDto.getIcon())) {
            treeItem.setGraphic(loadMenuIcon(menuDto.getIcon()));
        }
        if (menuDto.getCallHandlers() != null || menuDto.getUrl() != null) {
            if (menuDto.getCallHandlers() == null) {
                menuDto.setCallHandlers(new HashMap<>());
            }
            menuDto.getCallHandlers().put("iwin", iwin);
            menuDto.getCallHandlers().put("ialert", ialert);
            menuDto.getCallHandlers().put("idialog", idialog);
        }
        tabPaneViewMap.put(menuDto.getName(), menuDto);
        return treeItem;
    }

    public Map<String, ViewDto> getTabPaneViewMap() {
        return tabPaneViewMap;
    }

    public String directoryChooser(String title, String defaultPath) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        if (ValidatorUtil.notEmpty(defaultPath) && new File(defaultPath).exists()) {
            log.debug("默认文件夹:{}", defaultPath);
            directoryChooser.setInitialDirectory(new File(defaultPath));
        }
        directoryChooser.setTitle(title);
        File file = directoryChooser.showDialog(Application.getStage());
        if (file == null || !file.isDirectory()) return null;
        return file.getPath();
    }

    public String fileChooser(String title, String defaultPath, String description, String... extensions) {
        FileChooser fileChooser = new FileChooser();
        if (ValidatorUtil.notEmpty(defaultPath) && new File(defaultPath).exists()) {
            log.debug("默认文件夹:{}", defaultPath);
            fileChooser.setInitialDirectory(new File(defaultPath));
        }
        fileChooser.setTitle(title);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(description, extensions);
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(Application.getStage());
        if (file == null || !file.exists()) return null;
        return file.getPath();
    }

    /**
     * 获取文件后缀
     */
    public String getFileExt(String fileName) {
        int pos = fileName.lastIndexOf(".");
        if (pos > -1) {
            String temp = fileName.substring(pos + 1).toLowerCase();
            pos = temp.indexOf("?");
            if (pos > -1) {
                return temp.substring(0, pos);
            }
            return temp.trim().replaceAll("\"", "");
        }
        return "";
    }

    public void screenshot(Node view, String filename) {
        try {
            WritableImage image = view.snapshot(new SnapshotParameters(), null);
            SysVariableDto variableDto = setupService.getVariableByCode(new SysVariableDto() {{
                setCode("i-path-images");
            }});
            String path = (variableDto != null && ValidatorUtil.notEmpty(variableDto.getValue())) ? variableDto.getValue() : directoryChooser("请选择截图存放文件夹", (String) ehcacheUtil.getCache("SNAPSHOT_DIR_PATH"));
            if (ValidatorUtil.isEmpty(path)) {
                return;
            }
            File pathFile = new File(path);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }
            ehcacheUtil.setCache("SNAPSHOT_DIR_PATH", path);
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", new File(path + File.separator + filename + DateUtil.getCurDateStr("yyyyMMddHHmmsss") + ".png"));
            ialert.success("截屏保存成功!");
        } catch (Exception ex) {
            ialert.error("截屏保存失败:" + ex.getMessage());
        }
    }

    public BufferedImage loadScreenshot(Node view) {
        BufferedImage bi = null;
        try {
            WritableImage image = view.snapshot(new SnapshotParameters(), null);
            bi = SwingFXUtils.fromFXImage(image, null);
        } catch (Exception ex) {
            ialert.error("截屏转换失败:" + ex.getMessage());
        }
        return bi;
    }

    public void createPdf(Node view, String filename) {
        try {
            SysVariableDto variableDto = setupService.getVariableByCode(new SysVariableDto() {{
                setCode("i-path-pdf");
            }});
            String path = (variableDto != null && ValidatorUtil.notEmpty(variableDto.getValue())) ? variableDto.getValue() : directoryChooser("请选择PDF存放文件夹", (String) ehcacheUtil.getCache("SNAPSHOT_DIR_PATH"));
            if (ValidatorUtil.isEmpty(path)) {
                return;
            }
            File pathFile = new File(path);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }
            PdfUtil.createPdf(loadScreenshot(view), path + File.separator + filename + (DateUtil.getCurDateStr("yyyyMMddHHmmsss")));
            ialert.success("PDF保存成功!");
        } catch (Exception ex) {
            ialert.error("截屏保存失败:" + ex.getMessage());
        }
    }

//    /**
//     * 使用 JSR-303 Bean Validation 实现校验
//     * 如果返回null则表示没有错误
//     *
//     * @param obj
//     * @return
//     */
//    public void jsr303Check(Object obj) throws Exception {
//        if (null == obj) {
//            throw new RuntimeException("入参dto不能为空");
//        }
//        Set<ConstraintViolation<Object>> validResult = Validation.buildDefaultValidatorFactory().getValidator().validate(obj);
//        if (null != validResult && validResult.size() > 0) {
//            StringBuilder sb = new StringBuilder();
//            for (Iterator<ConstraintViolation<Object>> iterator = validResult.iterator(); iterator.hasNext(); ) {
//                ConstraintViolation<Object> constraintViolation = (ConstraintViolation<Object>) iterator.next();
//                if (StringUtils.isNotBlank(constraintViolation.getMessage())) {
//                    sb.append(constraintViolation.getMessage()).append("、");
//                } else {
//                    sb.append(constraintViolation.getPropertyPath().toString()).append("不合法、");
//                }
//            }
//            if (sb.lastIndexOf("、") == sb.length() - 1) {
//                sb.delete(sb.length() - 1, sb.length());
//            }
//            throw new RuntimeException(sb.toString());
//        }
//    }

    protected static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    protected Integer getDeviceTypeValue(String value) {
        Integer deviceType = null;
        if (ValidatorUtil.notEmpty(value)) {
            switch (value) {
                case "请选择":
                    break;
                case "开关柜":
                    deviceType = 1;
                    break;
                case "变压器":
                    deviceType = 2;
                    break;
                case "组合电器":
                    deviceType = 3;
                    break;
                case "电缆":
                    deviceType = 4;
                    break;
            }
        }
        return deviceType;
    }

    protected Integer getvoltageLevelValue(String value) {
        Integer levelValue = null;
        if (ValidatorUtil.notEmpty(value)) {
            switch (value) {
                case "请选择":
                    break;
                default:
                    levelValue = Integer.parseInt(value.replaceAll("([^\\d])", ""));
            }
        }
        return levelValue;
    }

    public abstract void dispose();

    public static void main(String[] args) {
        //取得系统默认的国家/语言环境
        Locale myLocale = Locale.forLanguageTag("zh");
        //根据指定国家/语言环境加载资源文件
        ResourceBundle i18nBundle = ResourceBundle.getBundle("com.redphase.i18n.message", myLocale);
        //打印从资源文件中取得的消息
        System.out.println(i18nBundle.getString("index.description"));
    }
}
