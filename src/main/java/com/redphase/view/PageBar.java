package com.redphase.view;

import com.redphase.framework.PageDto;
import com.redphase.framework.util.ReflectUtil;
import com.redphase.framework.util.ValidatorUtil;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lombok.Data;

import java.lang.reflect.Method;

@Data
public class PageBar<T> {
    /**
     * 当前第几页
     */
    private int pageIndex = 0;
    /**
     * 每页数据条数
     */
    private int pageSize = 10;
    /**
     * 数据总数
     */
    private int dataCount = 0;
    /**
     * 总页数
     */
    private int pageCount;
    /**
     * 上一页
     */
    private int prePage;
    /**
     * 下一页
     */
    private int nextPage;
    T controller;
    PageDto pageInfo;
    String func;
    Object[] param;

    public PageBar(T controller, PageDto pageInfo) {
        this.pageInfo = pageInfo;
        this.controller = controller;
    }

    public PageBar(T controller, PageDto pageInfo, String func) {
        this.pageInfo = pageInfo;
        this.controller = controller;
        this.func = func;
    }

    public PageBar(T controller, PageDto pageInfo, String func, Object... param) {
        this.pageInfo = pageInfo;
        this.controller = controller;
        this.func = func;
        this.param = param;
    }

    public HBox getBar() {
        HBox pageHBox = new HBox();
        pageHBox.setPrefHeight(46);
//        Label HTML_3DOT_STRING = getLabel("...", "labDisable", null, false);
        if (pageInfo == null) {
            pageHBox.getChildren().setAll(getLabel("...", null, false));
            return pageHBox;
        }
        /** 当前页码 */
        int currentPageNo = pageInfo.getPageIndex();
        /** 总页数 */
        int pageCount = pageInfo.getPageCount();
        /** 总纪录数 */
        int dataCount = pageInfo.getDataCount();

        /** 是否存在后一条记录 */
        boolean hasNext = (pageInfo.getPageIndex() >= pageInfo.getPageCount()) ? false : true;
        /** 是否存在前一条记录 */
        boolean hasPrevious = pageInfo.getPageIndex() <= 1 ? false : true;
        /** 画面显示的页码数量 */
        int pageNoCount = 15;
        /** 计算分页起始页 */
        int tempPageNo = Math.max(1, currentPageNo - pageNoCount / 2);
        // 上一页
        Label prePage = null;
        if (hasPrevious) {
            prePage = getLabel("上一页", pageInfo.getPrePage(), true);
        } else {
            prePage = getLabel("上一页", 0, false);
            prePage.setDisable(true);
        }
        // 下一页
        Label nextPage = null;
        if (hasNext) {
            nextPage = getLabel("下一页", pageInfo.getNextPage(), true);
        } else {
            nextPage = getLabel("下一页", 0, false);
            nextPage.setDisable(true);
        }
        // 中间页
        HBox midPage = new HBox();
        midPage.setPrefHeight(46);
        // 总页数<=显示页数
        if (pageCount == 0) {
            midPage.getChildren().add(getLabel("...", "labDisable", null, false));
        } else if (pageCount >= 1 && pageCount <= pageNoCount) {
            for (int i = 1; i <= pageCount; i++) {
                if (currentPageNo == i) {
                    midPage.getChildren().add(getLabel("" + i, "labActive", 0, false));
                } else {
                    midPage.getChildren().add(getLabel("" + i, i, true));
                }
            }
        } else {// 总页数>显示页数
            int i = tempPageNo;
            if (pageCount - tempPageNo < pageNoCount) i = pageCount - pageNoCount + 1;
            boolean HTML_3DOT_STRING_FLAG = false;
            for (int x = 1; x <= pageNoCount && i <= pageCount; i++, x++) {
                if (tempPageNo > 1 && !HTML_3DOT_STRING_FLAG) {
                    // 加上首页数字
                    midPage.getChildren().add(getLabel("" + 1, 1, true));
                    // 加上...
                    midPage.getChildren().add(getLabel("...", "labDisable", null, false));
                    HTML_3DOT_STRING_FLAG = true;
                }
                if (currentPageNo == i) {
                    midPage.getChildren().add(getLabel("" + i, "labActive", 0, false));
                } else {
                    midPage.getChildren().add(getLabel("" + i, i, true));
                }
//                if (x == pageNoCount && i<pageCount) {
//                    // 加上...
//                    midPage.getChildren().add(getLabel("...", "labDisable", null, false));
//                    // 加上末页数字
//                    midPage.getChildren().add(getLabel("" + pageCount, pageCount, true));
//                    break;
//                }
            }
        }
        // 汇总输出
        Label dataCountLab = getLabel("共" + dataCount + "条", false);
        dataCountLab.setDisable(true);
        pageHBox.getChildren().add(dataCountLab);
        pageHBox.getChildren().add(prePage);
        pageHBox.getChildren().add(midPage);
        pageHBox.getChildren().add(nextPage);
        Label pageCountLab = getLabel("共" + pageCount + "页", false);
        pageCountLab.setDisable(true);
        pageHBox.getChildren().add(pageCountLab);

        return pageHBox;
    }

    private Label getLabel(String title, boolean flag) {
        return getLabel(title, "lab", -1, flag);
    }

    private Label getLabel(String title, Integer pageNum, boolean flag) {
        return getLabel(title, "lab", pageNum, flag);
    }

    private Label getLabel(String title, String className, Integer pageNum, boolean flag) {
        Label label = new Label(title);
        if (ValidatorUtil.notEmpty(className)) {
            label.getStyleClass().setAll(className);
        }
        if (flag) {
            label.setOnMouseClicked((col) -> {
                ReflectUtil.setValueByFieldName(controller, "pageNum", pageNum);
                Class[] parameterTypes = null;
                if (param != null) {
                    parameterTypes = new Class[param.length];
                    for (int i = 0; i < param.length; i++) {
                        parameterTypes[i] = param[i].getClass();
                    }
                }
                //调用带参方法
                Method method = ReflectUtil.getMethodByMethodName(controller, func, parameterTypes);
                try {
                    method.invoke(controller, param);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        return label;
    }
}
