package com.redphase.framework;

import com.redphase.framework.util.ReflectUtil;
import com.redphase.framework.util.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class NodeTree<T> {
    private List<T> newNodes = new ArrayList<>();
    private List<T> nodes;

    private String attrId;
    private String attrParentId;
    private String attrName;
    private String attrNodes;

    public NodeTree(List<T> nodes, String attrId, String attrParentId, String attrNodes) {
        this.nodes = nodes;
        this.attrId = attrId;
        this.attrParentId = attrParentId;
        this.attrNodes = attrNodes;
        this.attrName = "name";
    }

    public NodeTree(List<T> nodes, String attrId, String attrParentId, String attrNodes, String attrName) {
        this.nodes = nodes;
        this.attrId = attrId;
        this.attrParentId = attrParentId;
        this.attrNodes = attrNodes;
        this.attrName = attrName;
    }

    public List<T> buildTree() {
        for (T node : nodes) {
            try {
                ReflectUtil.setValueByFieldName(node, "text", ReflectUtil.getValueByFieldName(node, attrName));
            } catch (Exception e) {
            }
            Object parentId = ReflectUtil.getValueByFieldName(node, attrParentId);
            if (ValidatorUtil.isNullEmpty(parentId) || isRootNode(parentId)) {
                newNodes.add(node);
                build(node);
            }
        }
        return newNodes;
    }

    private Boolean isRootNode(Object parentId) {
        for (T nodeTemp : nodes) {
            if (parentId.equals(ReflectUtil.getValueByFieldName(nodeTemp, attrId))) {
                return false;
            }
        }
        return true;
    }

    private void build(T node) {
        List<T> children = getChildren(node);
        if (!children.isEmpty()) {
            if (ReflectUtil.getValueByFieldName(node, attrNodes) == null) {
                ReflectUtil.setValueByFieldName(node, attrNodes, new ArrayList());
            }
            for (T child : children) {
                ((List<T>) ReflectUtil.getValueByFieldName(node, attrNodes)).add(child);
                build(child);
            }
        }
    }

    private List<T> getChildren(T node) {
        List<T> children = new ArrayList<>();
        Object id = ReflectUtil.getValueByFieldName(node, attrId);
        for (T child : nodes) {
//            log.info("======"+id+":"+ReflectUtil.getValueByFieldName(child, attrParentId)+"======");
            if (id.equals(ReflectUtil.getValueByFieldName(child, attrParentId))) {
                children.add(child);
            }
        }
        return children;
    }
}