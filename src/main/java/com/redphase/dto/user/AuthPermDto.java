package com.redphase.dto.user;


import com.redphase.framework.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthPermDto extends BaseDto {
    /**
     * 权限id
     */
    //@Size(max = 32, message = "id最大32字符")
    private String id;
    /**
     * 权限名称
     */
    //@NotNull(message = "name不能为空")
    //@Size(max = 50, message = "name最大50字符")
    private String name;
    /**
     * 权限匹配符
     */
    //@NotNull(message = "match_str不能为空")
    //@Size(max = 50, message = "match_str最大50字符")
    private String matchStr;
    /**
     * 父级ID
     */
    //@Size(max = 32, message = "parent_id最大32字符")
    private String parentId;
    /**
     * 备注
     */
    //@Size(max = 255, message = "memo最大255字符")
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
    //@Size(max = 255, message = "keyword最大255字符")
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

    /**
     * 子对象集合
     */
    private List<AuthPermDto> nodes;
    private String text;
    private Integer[] tags;

    public Integer[] getTags() {
        if (text != null) {
            return new Integer[]{nodes != null ? nodes.size() : 0};
        } else {
            return null;
        }
    }
}