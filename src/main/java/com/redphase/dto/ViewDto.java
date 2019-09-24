package com.redphase.dto;

import de.felixroske.jfxsupport.AbstractFxmlView;
import javafx.scene.Node;
import lombok.Data;

import java.util.Map;

@Data
public class ViewDto {
    private String code;
    private String name;
    private String icon;
    private String url;
    private AbstractFxmlView fxmlView;
    private Node nodeView;
    private Map<String, Object> callHandlers;

    @Override
    public String toString() {
        return name;
    }
}
