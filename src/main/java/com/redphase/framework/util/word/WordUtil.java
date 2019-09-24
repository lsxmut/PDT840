package com.redphase.framework.util.word;


import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.redphase.framework.util.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * 暂态地电压局部放电检测报告.doc
 * 1,建立文档
 * 2,创建一个书写器
 * 3,打开文档
 * 4,向文档中写入数据
 * 5,关闭文档
 */
@Slf4j
public class WordUtil {
    //初号=42磅,小初=36磅,一号=26磅,小一=24磅,二号=22磅,小二=18磅,三号=16磅,小三=15磅,四号=14磅,小四=12磅,五号=10.5磅,小五=9磅,六号=7.5磅,小六=6.5磅,七号=5.5磅,八号=5磅
    protected Font font;
    protected BaseFont bfChinese;
    //设置页码
    protected void setHeaderFooter(Document document){
        HeaderFooter footer = new HeaderFooter(new Phrase(""), true);
        footer.setAlignment(Rectangle.ALIGN_CENTER);
        footer.setPageNumber(1);
        document.setFooter(footer);
    }

    private void reScaleAbsolute(Image image) {
        // 设置图片的绝对大小，宽和高
        if (image.getScaledWidth() > 325f) {
            if (image.getScaledHeight() > 325f) {
                image.scaleAbsolute(image.getScaledWidth() * (325f / image.getScaledHeight()), 325f);
            } else {
                image.scaleAbsolute(325f, image.getScaledHeight() * (325f / image.getScaledWidth()));
            }
        }
        if (image.getScaledWidth() > 325f || image.getScaledHeight() > 325f) {
            reScaleAbsolute(image);
        }
        // 设置图片居中显示
        image.setAlignment(Image.ALIGN_CENTER);
    }

    protected void addImgDto(Document document, String imgUrl, String title) {
        try {
            if (ValidatorUtil.isEmpty(imgUrl)) {
                return;
            }
            Image image = Image.getInstance(imgUrl);
            reScaleAbsolute(image);
            document.add(image);
            Paragraph pg = new Paragraph(title);
            pg.setAlignment(Element.ALIGN_CENTER);
            pg.setSpacingAfter(20);
            document.add(pg);
        } catch (Exception e) {
            log.error("word列表异常!", e);
        }
    }

    protected void addImgDto(Document document, Image image, String title) {
        try {
            reScaleAbsolute(image);
            document.add(image);
            Paragraph pg = new Paragraph(title);
            pg.setAlignment(Element.ALIGN_CENTER);
            pg.setSpacingAfter(20);
            document.add(pg);
        } catch (Exception e) {
            log.error("word列表异常!", e);
        }
    }

    protected void addImgDto(Document document, BufferedImage bufferedImage, String title) {
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream(bufferedImage.getWidth() * bufferedImage.getHeight());
            ImageIO.write(bufferedImage, "png", bout);
            Image image = Image.getInstance(bout.toByteArray());// new Color(0, 0, 0, 0));
            reScaleAbsolute(image);
            document.add(image);
            Paragraph pg = new Paragraph(title);
            pg.setAlignment(Element.ALIGN_CENTER);
            pg.setSpacingAfter(20);
            document.add(pg);
        } catch (Exception e) {
            log.error("word列表异常!", e);
        }
    }

    protected Cell newCell(String str) throws BadElementException {
        return newCell(str, font, 0, 0, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER);
    }

    protected Cell newCell(String str, Font font) throws BadElementException {
        return newCell(str, font, 0, 0, Element.ALIGN_MIDDLE, null);
    }

    protected Cell newCell(String str, int colspan) throws BadElementException {
        return newCell(str, font, colspan, 0, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER);
    }

    protected Cell newCellRow(String str, int rowspan) throws BadElementException {
        return newCell(str, font, 0, rowspan, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER);
    }

    protected Cell newCell(String str, Font font, int colspan, int rowspan, Integer verticalAlignment, Integer horizontalAlignment) throws BadElementException {
        if (font == null) font = new Font(Font.NORMAL, 9, Font.NORMAL, new Color(0, 0, 0));
        Paragraph pg = new Paragraph(ValidatorUtil.notEmpty(str) ? str : "", font);
        pg.setSpacingAfter(5);
        pg.setSpacingBefore(5);
        if (horizontalAlignment != null) pg.setAlignment(horizontalAlignment);
        Cell cell4 = new Cell(pg);
        if (colspan > 0) cell4.setColspan(colspan);
        if (rowspan > 0) cell4.setRowspan(rowspan);
        if (verticalAlignment != null) cell4.setVerticalAlignment(verticalAlignment);
        if (horizontalAlignment != null) cell4.setHorizontalAlignment(horizontalAlignment);
        return cell4;
    }

    public static void main(String[] args) {
        File file = new File("d:/PDT840/reports/111.doc");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
    }
}
