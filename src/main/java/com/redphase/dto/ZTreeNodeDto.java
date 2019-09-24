package com.redphase.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Ztree节点数据DTO
 */
@Data
@NoArgsConstructor
public class ZTreeNodeDto {
    private String id;
    private Integer state;
    private String pid;
    private String name;
    private String fullName;
    private boolean open;
    @JSONField(name = "isParent")
    private boolean isParent;
    /**
     * 附加数据
     */
    private Object data;
    private List<ZTreeNodeDto> children = new ArrayList<>();
}
