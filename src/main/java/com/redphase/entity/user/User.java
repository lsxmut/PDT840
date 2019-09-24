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
public class User extends BaseEntity {
    /**
     * ID
     */
    private Long id;
    /**
     * 员工账号
     */
    private String account;
    /**
     * 成员名称
     */
    private String name;
    /**
     * 工号
     */
    private String jobNo;
    /**
     * 性别[0男1女3保密]
     */
    private Integer gender;
    /**
     * 手机号
     */
    private String cellphone;
    /**
     * 头像url
     */
    private String avatar;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 用户类型 0初级用户,1中级用户,2高级用户,3超级管理员
     */
    private Integer type;
    /**
     * 最后登录日期
     */
    private Date lastLogin;
    /**
     * 登录次数
     */
    private Integer count;
    /**
     * 状态0在职
     */
    private Integer state;
    /**
     * 备注
     */
    private String memo;
    /**
     * 版本号
     */
    private Integer version;
    /**
     * 排序
     */
    private Integer orderNo;
    /**
     * 删除标记(0正常1删除)
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
     * 员工密码
     */
    private String pwd;

    /**
     * 员工密码(旧)
     */
    private String oldpwd;
    /**
     * 员工密码(新)
     */
    private String confirmpwd;

    /**
     * 超级管理员
     */
    private Integer isSuper;
}