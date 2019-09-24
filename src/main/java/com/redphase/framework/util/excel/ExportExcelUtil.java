package com.redphase.framework.util.excel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @类描述：excel导出工具类
 * @创建人：wangdw
 * @创建时间：2017年11月16日 上午9:51:51
 * @版权：Copyright (c) 深圳市合时代金融服务有限公司-版权所有.
 */
public class ExportExcelUtil {
    private static final Logger logger = LoggerFactory.getLogger(ExportExcelUtil.class);

    private HSSFWorkbook book = null;

    private HSSFCellStyle columnStyle = null;// 默认列样式

    private String[] titles;// 标题数组

    private String[] fieldNames;// 字段名

    public int pageSize = 10;
    public boolean isSerial = false; //是否需要显示序号

    /**
     * 构造方法，传入表头和对应的字段名，下标及个数必须一一对应
     *
     * @param titles
     * @param fieldNames
     */
    public ExportExcelUtil(String[] titles, String[] fieldNames) {
        this.titles = titles;
        this.fieldNames = fieldNames;
        book = new HSSFWorkbook();
        initDefaultStyle();
    }


    /**
     * 方法描述：添加单个Bean到Excel
     *
     * @param data
     */
    public void addJSONObject(JSONObject data, HSSFRow row) {
        if (data == null) {
            return;
        }
        row.setHeight((short) 400);
        int temp = 0;
        if (isSerial) {
            temp = 1;
        }
        for (int j = 0 + temp; j < fieldNames.length + temp; j++) {
            HSSFCell cell = row.createCell(j);
            String value = data.get(fieldNames[j - temp]) == null ? "" : String.valueOf(data.get(fieldNames[j - temp]));
            cell.setCellValue(new HSSFRichTextString(value));
            cell.setCellStyle(columnStyle);
        }
    }

    /**
     * 方法描述：添加单个Bean到Excel
     *
     * @param data
     */
    public void addJSONArray(JSONArray data) {
        if (data == null) {
            HSSFSheet sheet = book.createSheet(String.valueOf(1));
            setColumnWidth(sheet);
            HSSFRow titleRow = sheet.createRow(0);
            createTitle(titleRow);
            return;
        }
        int totalCount = data.size();
        int pageCount = 1;
        if (pageSize > 0) {
            pageCount = (int) (Math.ceil((float) data.size() / pageSize));
        } else {
            pageSize = totalCount;
        }

        for (int i = 1; i <= pageCount; i++) {
            HSSFSheet sheet = book.createSheet(String.valueOf(i));
            setColumnWidth(sheet);
            HSSFRow titleRow = sheet.createRow(0);
            createTitle(titleRow);
            for (int j = 1; j <= pageSize; j++) {
                int serial = (i - 1) * pageSize + j;
                if (serial > totalCount) {
                    break;
                }
                HSSFRow row = sheet.createRow(j);
                if (isSerial) {
                    HSSFCell cell = row.createCell(0);
                    cell.setCellValue(serial);
                    cell.setCellStyle(columnStyle);
                }
                addJSONObject(data.getJSONObject(serial - 1), row);
            }
        }
    }

//
//    /**
//     * 方法描述：导出Excel文件
//     * @param fileName
//     */
//    public void export(String fileName) {
//        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
//
//
//        OutputStream output = null;
//        try{
//
//            String name = java.net.URLEncoder.encode(fileName, "UTF-8");
//            response.setHeader("Content-disposition", "attachment; filename=" + name);
//
//            response.setContentType("application/vnd.ms-excel");
//            output = response.getOutputStream();
//            book.write(output);
//            output.flush();
//        } catch (Exception e) {
//            logger.info("ExportExcelUtil.export发生异常：{}", e);
//        }
//        finally {
//            if (output != null) {
//                try {
//                    output.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    /**
     * 方法描述：设置列宽
     *
     * @param widths
     */
    public void setColumnWidth(HSSFSheet sheet, int... widths) {
        if (widths == null) {// 设置默认宽度
            for (int i = 0; i < titles.length; i++) {
                sheet.setColumnWidth(i, 8000);
            }
            return;
        }
        for (int i = 0; i < widths.length; i++) {
            sheet.setColumnWidth(i, widths[i]);
        }
    }

    /**
     * 设置普通样式
     */
    private void initDefaultStyle() {
        columnStyle = book.createCellStyle();
        HSSFFont columnFont = book.createFont();
        columnFont.setFontName("宋体");
        columnFont.setFontHeightInPoints((short) 10);
        columnStyle.setFont(columnFont);
        columnStyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中
        columnStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
    }

    /**
     * 创建标题
     */
    private void createTitle(HSSFRow titleRow) {
        // 设置标题样式
        HSSFFont columnHeadFont = book.createFont();
        columnHeadFont.setFontName("宋体");
        columnHeadFont.setFontHeightInPoints((short) 12);
        columnHeadFont.setBold(true);
        ;

        HSSFCellStyle columnHeadStyle = book.createCellStyle();
        columnHeadStyle.setFont(columnHeadFont);
        columnHeadStyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中
        columnHeadStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
        columnHeadStyle.setLocked(true);
        columnHeadStyle.setWrapText(true);

        /**
         * 创建列名
         */
        titleRow.setHeight((short) 500);

        HSSFFont columnFont = book.createFont();
        HSSFCellStyle columnStyle = book.createCellStyle();
        columnFont.setFontName("宋体");
        columnFont.setBold(true);
        columnFont.setFontHeightInPoints((short) 11);
        columnStyle.setFont(columnFont);
        columnStyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中
        columnStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
        int temp = 0;
        if (isSerial) {
            HSSFCell cell = titleRow.createCell(0);
            cell.setCellValue(new HSSFRichTextString("序号"));
            cell.setCellStyle(columnStyle);
            temp = 1;
        }

        for (int i = 0 + temp; i < titles.length + temp; i++) {
            HSSFCell cell = titleRow.createCell(i);
            cell.setCellValue(new HSSFRichTextString(titles[i - temp]));
            cell.setCellStyle(columnStyle);
        }
    }

}
