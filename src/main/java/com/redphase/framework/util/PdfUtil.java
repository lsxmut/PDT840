package com.redphase.framework.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

@Slf4j
public class PdfUtil {
    public static void createPdf(String imagePath, String pdfPath) throws Exception {
        BufferedImage bufferedImage = ImageIO.read(new File(imagePath));
        FileOutputStream fileOutputStream = new FileOutputStream(pdfPath + ".pdf");
        Document document = new Document(null, 0, 0, 0, 0);
        document.setPageSize(new Rectangle(bufferedImage.getWidth(), bufferedImage.getHeight()));
        Image image = Image.getInstance(imagePath);
        PdfWriter.getInstance(document, fileOutputStream);
        document.open();
        document.add(image);
        document.close();
        fileOutputStream.close();
    }

    public static void createPdf(BufferedImage bufferedImage, String pdfPath) throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream(pdfPath + ".pdf");
        Document document = new Document(null, 0, 0, 0, 0);
        document.setPageSize(new Rectangle(bufferedImage.getWidth(), bufferedImage.getHeight()));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", os);
        Image image = Image.getInstance(os.toByteArray());
        PdfWriter.getInstance(document, fileOutputStream);
        document.open();
        document.add(image);
        document.close();
        os.close();
        fileOutputStream.close();
    }

    public static void main(String[] args) throws Exception {
        createPdf(ImageIO.read(new File("D:/Pictures/04191336f17u.jpg")), "e:/333");
    }
}
