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
public class LogLogin extends BaseEntity {
    /**
     * id
     */
    private Long id;
    /**
     * 员工ID
     */
    private Long userId;
    /**
     * 员工名称
     */
    private String userName;
    /**
     * 类型0登录1登出
     */
    private Integer type;
    /**
     * IP地址
     */
    private String ipAddr;
    /**
     * MAC地址
     */
    private String deviceMac;
    /**
     * 创建时间
     */
    private Date dateCreated;
    /**
     * app用户id
     */
    private Long appUserId;
    /**
     * 系统id
     */
    private String appId;
    /**
     * 系统名称
     */
    private String appName;
}