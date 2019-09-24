package com.redphase.dto.atlas;


import com.redphase.framework.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
/** <p> 检查点 DTO */
public class DataDeviceSiteDto extends BaseDto {
    /**
     * 主键
     */
    private Long id;
    /**
     * 检测设备id
     */
    //@NotNull(message = "dataDeviceId不能为空")
    private Long dataDeviceId;
    /**
     * 检查点名称
     */
    private String siteName;
    /**
     * 间隔名称
     */
    private String spaceName;
    /**
     * 文件路径
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