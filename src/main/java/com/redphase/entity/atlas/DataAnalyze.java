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
/** <p> 检测文件 entity */
public class DataAnalyze extends BaseEntity {
    /**
     * 主键
     */
    private Long id;
    /**
     * 设备id,data_source=0,1生效
     */
    private Long dataDeviceId;
    /**
     * 设备检查点id,data_source=2生效
     */
    private Long dataDeviceSiteId;
    /**
     * 序号
     */
    private Integer orderNo;
    /**
     * 数据来源0设备类型1具体设备2具体位置
     */
    private String dataSource;
    /**
     * TEV_XJZS:地电波_巡检噪声,
     * TEV_XJ:地电波_巡检,
     * TEV_TJZS:地电波_统计噪声,
     * TEV_TJ:地电波_统计数据,
     * TEV_TJLB:地电波_统计录波,
     * AA_XJZS:空气式超声波_巡检噪声,
     * AA_XJ:空气式超声波_巡检,
     * AA_TJZS:空气式超声波_统计噪声,
     * AA_TJ:空气式超声波_统计数据,
     * AA_TJLB:空气式超声波_统计录波,
     * HFCT_XJZS:高频电流_巡检噪声,
     * HFCT_XJ:高频电流_巡检,
     * HFCT_TJZS:高频电流_统计噪声,
     * HFCT_TJ:高频电流_统计数据,
     * HFCT_TJLB:高频电流_统计录波,
     * UHF_TJZS_1:特高频_模式1_统计噪声,
     * UHF_TJ_1:特高频_模式1_统计数据,
     * UHF_TJLB_1:特高频_模式1_统计录波,
     * UHF_TJZS_2:特高频_模式2_统计噪声,
     * UHF_TJ_2:特高频_模式2_统计数据,
     * UHF_TJLB_2:特高频_模式2_统计录波,
     * AE_XJZS:接触式超声波_巡检噪声,
     * AE_XJ:接触式超声波_巡检,
     * AE_TJZS:接触式超声波_统计噪声,
     * AE_TJ:接触式超声波_统计数据,
     * AE_TJLB:接触式超声波_统计录波,
     * AE_FX:接触式超声波_飞行图谱,
     * AE_BX:接触式超声波_波形图谱,
     * LC:负荷电流数据,
     * HJ:环境参数数据
     */
    private String dataFormat;
    /**
     * 截图路径,逗号分割
     */
    private String screenshots;
    /**
     * 照片路径,逗号分割
     */
    private String photos;
    /**
     * 录音路径,逗号分割
     */
    private String audios;
    /**
     * 文件路径
     */
    private String fileUrl;
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
     * 设备信息
     */
    private DataDevice dataDevice;
    /**
     * 检查点信息
     */
    private DataDeviceSite dataDeviceSite;
}