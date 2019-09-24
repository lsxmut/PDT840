package com.redphase.view;

import javafx.scene.control.TableCell;

public class SeqlTableCell extends TableCell<Object, String> {
    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        this.setText(null);
        this.setGraphic(null);
        this.setStyle("-fx-pref-width: 60;-fx-text-overrun: center-ellipsis;");
        if (!empty) {
            int rowIndex = this.getIndex() + 1;
            this.setText(String.valueOf(rowIndex));
        }
    }
}
