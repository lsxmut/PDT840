package com.redphase.entity.atlas;


import com.redphase.framework.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
/** <p> 电力设备 entity */
public class DataDevice extends BaseEntity {
    /**
     * 主键
     */
    private Long id;
    /**
     * 检测日期
     */
    private String dateDetection;
    /**
     * 检测技术
     */
    private String testTechnology;
    /**
     * 电业局
     */
    private String electricBureau;
    /**
     * 变电站
     */
    private String substation;
    /**
     * 电压等级
     */
    private String voltageLevel;
    /**
     * 设备类型
     */
    private String deviceType;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 间隔名称
     */
    private String spaceName;
    /**
     * 文件夹路径
     */
    private String folderPath;
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
     * 排序序号
     */
    private Integer sortNo;
}