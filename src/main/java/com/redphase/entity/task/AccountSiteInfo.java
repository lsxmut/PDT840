package com.redphase.entity.task;


import com.redphase.framework.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountSiteInfo extends BaseEntity {
    /**
     * ID
     */
    private String id;
    /**
     * 台帐id
     */
    private String accountId;
    /**
     * 检查编码
     */
    private String siteCode;
    /**
     * 检查名称
     */
    private String siteName;
    /**
     * 检测技术
     */
    private String testTechnology;
    /**
     * 删除标识
     */
    private String delFlag;
    /**
     * 创建时间
     */
    private Date dateCreated;
    /**
     * 更新时间
     */
    private Date dateUpdated;
    /**
     * 修改者id
     */
    private String updateId;
    /**
     * BI时间戳
     */
    private Date biUpdateTs;

    /**
     * 位置序号
     */
    private Integer sortNo;
}