package com.redphase.dto.task;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wenliang
 * @Title: TaskFileDto
 * @Package com.redphase.dto.task
 * @Description: ${todo}
 * @date 2018/5/13 21:04
 */
@Data
@NoArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskFileDto {
    /**
     * 文件路径
     */
    private String filePath;
    /**
     * 检测文件目录
     */
    private String testFilePath;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 变电站名称
     */
    private String substationName;
    /**
     * 电压(包含单位)
     */
    private String voltage;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 检测类型名称(开关柜；变压器；组合电器；电缆)
     */
    private String deviceTypeName;
    /**
     * 检测技术:1.特高频（UHF）;2.AE;3.高频（HFCT）柜
     */
    private String testTechnology;
    /**
     * 检测日期
     */
    private String dateStr;
    /**
     * 检测时间
     */
    private String timeStr;
    /**
     * 任务列表
     */
    private List<TaskDetailDto> taskDetailDtos;
}
