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
/** <p> 历史任务单 entity */
public class TaskHistory extends BaseEntity {
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
    private Integer voltageLevel;
    /**
     * 设备类型
     */
    private Integer deviceType;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件\路径
     */
    private String filePath;
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
}