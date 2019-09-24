package com.redphase.view;

import com.redphase.framework.util.DateUtil;
import com.redphase.framework.util.ReflectUtil;
import javafx.scene.control.TableCell;

import java.util.Date;
import java.util.Map;

public class DataTableCell<T> extends TableCell<T, String> {
    private String attribute;
    private Map<String, Object> valMap;

    public DataTableCell(String attribute) {
        this.attribute = attribute;
    }

    public DataTableCell(String attribute, Map<String, Object> valMap) {
        this.attribute = attribute;
        this.valMap = valMap;
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            T dto = this.getTableView().getItems().get(this.getIndex());
            Object value = ReflectUtil.getValueByFieldName(dto, attribute);
            if (value instanceof Date) {
                this.setText(value != null ? "" + DateUtil.getDateTimeStr((Date) value) : "");
            } else {
                if (valMap != null) {
                    this.setText("" + valMap.get("" + value));
                } else {
                    this.setText(value != null ? "" + value : "");
                }
            }
        } else {
            this.setText(null);
            setGraphic(null);
        }
    }
}
