package com.redphase.controller.nda.data;

import com.redphase.controller.nda.NdaBaseController;
import com.redphase.view.nda.data.NdaDataPhaseView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

@FXMLController
@Slf4j
public class NdaDataPhaseController extends NdaBaseController implements Initializable {
    @Autowired
    NdaDataPhaseView ndaDataPhaseView;

    @FXML
    AnchorPane contentAnchorPane;
    @FXML
    Label testTechnologyL;//检测技术
    @FXML
    Button IMGButton;//保存为img
    @FXML
    Button PDFButton;//保存为pdf

    Double[] maxAtlasArr;//最大值
    Double[] minAtlasArr;//最小值
    Double[] avgAtlasArr;//平均值
    String title;

    @FXML
    Canvas maxCanvasBack;
    @FXML
    Canvas maxCanvasAtlas;
    @FXML
    Canvas avgCanvasBack;
    @FXML
    Canvas avgCanvasAtlas;
    @FXML
    Canvas minCanvasBack;
    @FXML
    Canvas minCanvasAtlas;

    double hPixel = 0;
    double ndaLenght = 60.0;
    double xMultiple = 12.0;
    double yMultiple = 4.0;
    double xZeroSpot = 33.0;//初始x轴坐标
    double yZeroSpot = 250;//初始y轴坐标
    double yLength = 200.0;//y轴总长
    double xLength = 1150.0;//x轴总长
    double xNdaLength = xLength / ndaLenght;//1150像素等比{atlasPhaseLenght}份
    double xStep = xLength / xMultiple;
    double yStep = yLength / yMultiple;


    boolean initFlag = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize...");
        if (initFlag) return;
        initFlag = true;
    }

    public void init(String testTechnology, Float[][] atlasArr, Map map) {
        testTechnologyL.setText(testTechnology);
        this.title = (String) map.get("title");
        maxAtlasArr = (Double[]) map.get("maxPhaseAtlasArr");
        minAtlasArr = (Double[]) map.get("minAtlasArr");
        avgAtlasArr = (Double[]) map.get("avgAtlasArr");

        double VF = (Double) map.get("VF");//范围
        double VFC = (Double) map.get("VFC");//范围修正

        GraphicsContext maxCanvasBackGC = maxCanvasBack.getGraphicsContext2D();
        GraphicsContext maxCanvasAtlasGC = maxCanvasAtlas.getGraphicsContext2D();
        GraphicsContext avgCanvasBackGC = avgCanvasBack.getGraphicsContext2D();
        GraphicsContext avgCanvasAtlasGC = avgCanvasAtlas.getGraphicsContext2D();
        GraphicsContext minCanvasBackGC = minCanvasBack.getGraphicsContext2D();
        GraphicsContext minCanvasAtlasGC = minCanvasAtlas.getGraphicsContext2D();

        maxCanvasAtlasGC.setLineWidth(2);
        avgCanvasAtlasGC.setLineWidth(2);
        minCanvasAtlasGC.setLineWidth(2);

        //清屏
        maxCanvasBackGC.clearRect(0, 0, maxCanvasBack.getWidth(), maxCanvasBack.getHeight());
        maxCanvasBackGC.setStroke(Color.web("#cccccc", 1.0));
        maxCanvasBackGC.setLineWidth(1);
        maxCanvasBackGC.setFont(Font.font(10));
        maxCanvasBackGC.setFill(Color.web("#999999"));

        avgCanvasBackGC.clearRect(0, 0, avgCanvasBack.getWidth(), avgCanvasBack.getHeight());
        avgCanvasBackGC.setStroke(Color.web("#cccccc", 1.0));
        avgCanvasBackGC.setLineWidth(1);
        avgCanvasBackGC.setFont(Font.font(10));
        avgCanvasBackGC.setFill(Color.web("#999999"));

        minCanvasBackGC.clearRect(0, 0, minCanvasBack.getWidth(), minCanvasBack.getHeight());
        minCanvasBackGC.setStroke(Color.web("#cccccc", 1.0));
        minCanvasBackGC.setLineWidth(1);
        minCanvasBackGC.setFont(Font.font(10));
        minCanvasBackGC.setFill(Color.web("#999999"));


        //---
        maxCanvasAtlasGC.clearRect(0, 0, maxCanvasAtlas.getWidth(), maxCanvasAtlas.getHeight());
        maxCanvasAtlasGC.setStroke(Color.web("#79C1FD", 1.0));
        maxCanvasAtlasGC.setLineWidth(10);

        avgCanvasAtlasGC.clearRect(0, 0, avgCanvasAtlas.getWidth(), avgCanvasAtlas.getHeight());
        avgCanvasAtlasGC.setStroke(Color.web("#79C1FD", 1.0));
        avgCanvasAtlasGC.setLineWidth(10);

        minCanvasAtlasGC.clearRect(0, 0, minCanvasAtlas.getWidth(), minCanvasAtlas.getHeight());
        minCanvasAtlasGC.setStroke(Color.web("#79C1FD", 1.0));
        minCanvasAtlasGC.setLineWidth(10);

        hPixel = VF / yMultiple;
        //纵面图--横线  +1
        for (int i = 0; i < yMultiple + 1; i++) {
            maxCanvasBackGC.strokeLine(xZeroSpot, yZeroSpot - (i * yStep), xZeroSpot + xLength, yZeroSpot - (i * yStep));
            maxCanvasBackGC.fillText("" + new BigDecimal(i * hPixel - VFC).setScale(2, RoundingMode.UP), 0, yZeroSpot - (i * yStep) + 5);

            avgCanvasBackGC.strokeLine(xZeroSpot, yZeroSpot - (i * yStep), xZeroSpot + xLength, yZeroSpot - (i * yStep));
            avgCanvasBackGC.fillText("" + new BigDecimal(i * hPixel - VFC).setScale(2, RoundingMode.UP), 0, yZeroSpot - (i * yStep) + 5);

            minCanvasBackGC.strokeLine(xZeroSpot, yZeroSpot - (i * yStep), xZeroSpot + xLength, yZeroSpot - (i * yStep));
            minCanvasBackGC.fillText("" + new BigDecimal(i * hPixel - VFC).setScale(2, RoundingMode.UP), 0, yZeroSpot - (i * yStep) + 5);
        }
        //纵面图--竖线
        for (int i = 0; i < xMultiple + 1; i++) {
            //yLength像素分为4等份
            double x1 = xZeroSpot + (i * xStep);
            if (i == 0) {
                maxCanvasBackGC.strokeLine(x1, yZeroSpot, xZeroSpot + (i * xStep), yZeroSpot - yLength);
                avgCanvasBackGC.strokeLine(x1, yZeroSpot, xZeroSpot + (i * xStep), yZeroSpot - yLength);
                minCanvasBackGC.strokeLine(x1, yZeroSpot, xZeroSpot + (i * xStep), yZeroSpot - yLength);
            }
            if (i == xMultiple) {
                maxCanvasBackGC.strokeLine(x1, yZeroSpot, xZeroSpot + (i * xStep), yZeroSpot - yLength);
                avgCanvasBackGC.strokeLine(x1, yZeroSpot, xZeroSpot + (i * xStep), yZeroSpot - yLength);
                minCanvasBackGC.strokeLine(x1, yZeroSpot, xZeroSpot + (i * xStep), yZeroSpot - yLength);
            }
            maxCanvasBackGC.fillText("" + (30 * i), x1 - 5, yZeroSpot + 15);
            avgCanvasBackGC.fillText("" + (30 * i), x1 - 5, yZeroSpot + 15);
            minCanvasBackGC.fillText("" + (30 * i), x1 - 5, yZeroSpot + 15);
        }
        //数据
        for (int i = 0; i < ndaLenght; i++) {
            double x1 = xZeroSpot + 10 + (i * xNdaLength);
            double x2 = xZeroSpot + 10 + (i * xNdaLength);
            double y1 = yZeroSpot;
            maxCanvasAtlasGC.strokeLine(x1, y1, x2, yZeroSpot - (yLength / 100.0 * maxAtlasArr[i]));
            avgCanvasAtlasGC.strokeLine(x1, y1, x2, yZeroSpot - (yLength / 100.0 * avgAtlasArr[i]));
            minCanvasAtlasGC.strokeLine(x1, y1, x2, yZeroSpot - (yLength / 100.0 * minAtlasArr[i]));
        }
        //解决setLineWidth多出来部分 擦除
        maxCanvasAtlasGC.clearRect(xZeroSpot, yZeroSpot, xZeroSpot + xLength, yZeroSpot + 10);
        avgCanvasAtlasGC.clearRect(xZeroSpot, yZeroSpot, xZeroSpot + xLength, yZeroSpot + 10);
        minCanvasAtlasGC.clearRect(xZeroSpot, yZeroSpot, xZeroSpot + xLength, yZeroSpot + 10);

    }

    @FXML
    public void toIMG() {
        log.debug("toIMG...");
        screenshot(contentAnchorPane, title);
    }

    @FXML
    public void toPDF() {
        log.debug("toPDF...");
        createPdf(contentAnchorPane, title);
    }

    @Override
    public void dispose() {

    }
}