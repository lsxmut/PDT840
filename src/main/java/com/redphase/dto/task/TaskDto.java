package com.redphase.dto.task;


import com.redphase.framework.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskDto extends BaseDto {
    public enum DeviceType {
        SWITCH_CUBICLE(1, "开关柜"),
        COMPOSITE_APPARATUS(2, "组合电器"),
        TRANSFORMER(3, "变压器"),
        CABLE(4, "电缆");

        @Getter
        private int value;
        @Getter
        private String text;

        public static String getTextByValue(int value) {
            for (DeviceType deviceType : DeviceType.values()) {
                if (deviceType.getValue() == value) {
                    return deviceType.getText();
                }
            }
            return "";
        }

        DeviceType(int value, String text) {
            this.value = value;
            this.text = text;
        }
    }

    /**
     * ID
     */
    //@Size(max = 20, message = "id最大20字符")
    private String id;
    /**
     * 检查对象ID
     */
    //@Size(max = 20, message = "accountId最大20字符")
    private String accountId;
    /**
     * 台帐设备名称
     */
    //@Size(max = 20, message = "accountId最大100字符")
    private String deviceName;
    /**
     * 设备类型:1.开关柜;2.组合电器;3.变压器;4.电缆
     */
    //@Size(max = 1, message = "deviceType最大1字符")
    private Integer deviceType;
    /**
     * 导出者id
     */
    //@Size(max = 20, message = "exportId最大20字符")
    private String exportId;
    /**
     * 导出时间
     */
    private Date dateExport;
    /**
     * 删除标识
     */
    //@Size(max = 1, message = "delFlag最大1字符")
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
    //@Size(max = 20, message = "updateId最大20字符")
    private String updateId;
    /**
     * BI时间戳
     */
    private Date biUpdateTs;
    /**
     * BI时间戳字符串
     */
    private String biUpdateTsStr;
    /**
     * 任务明细列表
     */
    private List<TaskDetailDto> taskDetailDtos;

    private Integer sortNum;

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
}