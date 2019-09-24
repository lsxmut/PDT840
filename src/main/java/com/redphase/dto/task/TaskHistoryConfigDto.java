package com.redphase.dto.task;


import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wenliang
 * @Title: TaskHistoryConfigDto
 * @Package com.redphase.dto.task
 * @Description: 任务单历史配置dto
 * @date 2018/5/28 14:37
 */
@Data
@NoArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskHistoryConfigDto {
    /**
     * 台帐ID
     */
    private String accountId;
    /**
     * 检测位置名称
     */
    private String siteName;
    /**
     * 检测技术
     */
    private String testTechnology;
    /**
     * 多选框值
     */
    private String checkboxValue;
}
