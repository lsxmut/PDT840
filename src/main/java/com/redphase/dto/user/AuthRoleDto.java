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
public class AuthRoleDto extends BaseDto {
    /**
     * 角色ID
     */
    private Long id;
    /**
     * 角色名称
     */
    //@NotNull(message = "name不能为空")
    //@Size(max = 50, message = "name最大50字符")
    private String name;
    /**
     * 超级管理员0否1是
     */
    private Integer isSuper;
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
     * 创建时间
     */
    private Date dateCreated;
    /**
     * 更新时间
     */
    private Date dateUpdated;

    /**
     * 权限id集合
     */
    private List<String> permIds;
    /**
     * 菜单id集合
     */
    private List<Long> menuIds;
    /**
     * 应用id集合
     */
    private List<String> appIds;
}