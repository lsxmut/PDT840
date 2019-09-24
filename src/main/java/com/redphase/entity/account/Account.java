package com.redphase.entity.account;


import com.redphase.entity.task.AccountSiteInfo;
import com.redphase.framework.entity.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 台账数据
 */
@Data
@NoArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class Account extends BaseEntity {
    private Long id;
    /**
     * 所属目录ID
     */
    private Long dirId;
    /**
     * 电业局
     */
    private String electricBureau;
    /**
     * 变电站
     */
    private String substation;
    /**
     * 变电站编码
     */
    private String substationCode;
    /**
     * 间隔名称
     */
    private String spaceName;
    /**
     * 间隔编码
     */
    private String spaceCode;
    /**
     * 设备类型(1、开关柜；2、变压器；3、组合电器；4、电缆)
     */
    private Integer deviceType;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 设备编码
     */
    private String deviceCode;
    /**
     * 电压等级(单位：kV)
     */
    private Integer voltageLevel;
    /**
     * 制造商
     */
    private String manufacturer;
    /**
     * 设备型号
     */
    private String deviceVersion;
    /**
     * 运行单位
     */
    private String runDept;
    /**
     * 投运日期
     */
    private Date useDate;
    /**
     * 出厂日期
     */
    private Date produceDate;
    /**
     * 运行状态
     */
    private String runState;
    /**
     * 停运日期
     */
    private Date outageDate;
    /**
     * 排序号
     */
    private Integer sortNum;

    private Integer delFlag;
    private Date dateCreated;
    private Date dateUpdated;
    private Long createId;
    private Long updateId;
    private Date biUpdateTs;
    private String isAtlas;
    /**
     * 台账位置
     */
    private List<AccountSiteInfo> siteList;
}