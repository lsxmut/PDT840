package com.redphase.dto.atlas;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * @author wenliang
 * @Title: DataAnalyzeTableDto
 * @Package com.redphase.dto.atlas
 * @Description: ${todo}
 * @date 2018/7/9 15:56
 */
public class DataAnalyzeTableDto {

    private SimpleStringProperty dataAnalyzeId;
    private SimpleStringProperty dataFormat;
    private SimpleStringProperty fileName;
    private SimpleBooleanProperty selected;

    public DataAnalyzeTableDto(Boolean selected, String fileName, String dataFormat, String dataAnalyzeId) {
        this.selected = new SimpleBooleanProperty(selected);
        this.fileName = new SimpleStringProperty(fileName);
        this.dataFormat = new SimpleStringProperty(dataFormat);
        this.dataAnalyzeId = new SimpleStringProperty(dataAnalyzeId);

    }

    public String getDataFormat() {
        return dataFormat.get();
    }

    public SimpleStringProperty dataFormatProperty() {
        return dataFormat;
    }

    public void setDataFormat(String dataFormat) {
        this.dataFormat.set(dataFormat);
    }

    public String getFileName() {
        return fileName.get();
    }

    public SimpleStringProperty fileNameProperty() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName.set(fileName);
    }

    public boolean isSelected() {
        return selected.get();
    }

    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public String getDataAnalyzeId() {
        return dataAnalyzeId.get();
    }

    public SimpleStringProperty dataAnalyzeIdProperty() {
        return dataAnalyzeId;
    }

    public void setDataAnalyzeId(String dataAnalyzeId) {
        this.dataAnalyzeId.set(dataAnalyzeId);
    }
}
