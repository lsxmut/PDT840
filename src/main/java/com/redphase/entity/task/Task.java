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
public class Task extends BaseEntity {
    /**
     * ID
     */
    private String id;
    /**
     * 检查对象ID
     */
    private String accountId;
    /**
     * 台帐设备名称
     */
    private String deviceName;
    /**
     * 设备类型
     */
    private Integer deviceType;
    /**
     * 导出者id
     */
    private String exportId;
    /**
     * 导出时间
     */
    private Date dateExport;
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
}