package com.redphase.entity.user;


import com.redphase.framework.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthPerm extends BaseEntity {
    /**
     * 权限id
     */
    private String id;
    /**
     * 权限名称
     */
    private String name;
    /**
     * 权限匹配符
     */
    private String matchStr;
    /**
     * 父级ID
     */
    private String parentId;
    /**
     * 备注
     */
    private String memo;
    /**
     * 排序
     */
    private Integer orderNo;
    /**
     * 版本号
     */
    private Integer version;
    /**
     * 关键字
     */
    private String keyword;
    /**
     * 是否删除
     */
    private Integer delFlag;
    /**
     * 建立者ID
     */
    private Long createId;
    /**
     * 创建时间
     */
    private Date dateCreated;
    /**
     * 更新时间
     */
    private Date dateUpdated;
    /**
     * 已存在的禁止删除(0否1是)
     */
    private Integer noDelFlag;
}