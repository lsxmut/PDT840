package com.redphase.dto.account;

import com.alibaba.fastjson.annotation.JSONField;
import com.redphase.dto.task.AccountSiteInfoDto;
import com.redphase.framework.dto.BaseDto;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 台账数据
 */
@Data
@NoArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDto extends BaseDto {
    public enum DeviceType {
        DEVICE_TYPE_SWITCH(1, "开关柜"),
        DEVICE_TYPE_TRANSFORMER(2, "变压器"),
        DEVICE_TYPE_GROUP_APPLIANCE(3, "组合电器"),
        DEVICE_TYPE_CABLE(4, "电缆"),
        ;

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

        public static Integer getValueByText(String text) {
            for (DeviceType deviceType : DeviceType.values()) {
                if (deviceType.getText().equals(text)) {
                    return deviceType.getValue();
                }
            }
            return null;
        }

        DeviceType(int value, String text) {
            this.value = value;
            this.text = text;
        }
    }

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
    @JSONField(format = "yyyy-MM-dd")
    private Date useDate;
    /**
     * 出厂日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date produceDate;
    /**
     * 运行状态
     */
    private String runState;
    /**
     * 停运日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date outageDate;
    /**
     * 排序号
     */
    private Integer sortNum;

    private Integer delFlag;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date dateCreated;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date dateUpdated;

    private Long createId;
    private Long updateId;

    private Date biUpdateTs;

    //用于构造树的展示字段
    private String text;
    /**
     * 配置状态:0.未配置;1.已配置
     */
    private Integer configState;

    private String biUpdateTsStr;
    /**
     * 是否选中
     */
    private boolean selected = false;
    private String isAtlas;
    /**
     * 台账位置
     */
    private List<AccountSiteInfoDto> siteList;
}