package com.redphase.dto.convert;


import com.redphase.framework.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
/** <p> 国网_高频电流_统计数据 DTO */
public class GWDataHfctTjDto extends BaseDto {
    /**
     * 数据长度数据项1到12的字节长度。
     */
    private Integer x1;
    /**
     * 规范版本号所使用的数据格式规范版本号，例如1.0。
     */
    private Float x2;
    /**
     * 图谱类型编码	标识该文件是PRPD图谱还是PRPS图谱。PRPD:0x00 PRPS: 0x01
     */
    private Integer x3;
    /**
     * 相位窗数m工频周期被等分成m个相位窗，每个相位窗跨360/m度。m=50（暂定）
     */
    private Integer x4;
    /**
     * 量化幅值n 幅值范围的等分区间数。如果该文件是PRPS图谱，则该4个字节清零，赋值为：0x00000000。
     */
    private Integer x5;
    /**
     * 幅值单位 表示幅值的单位 dBm: 0x01 mV: 0x02 %: 0x03
     */
    private Integer x6;
    /**
     * 幅值下限 仪器所能检测到的放电信号幅值的下限。暂定：-75
     */
    private Float x7;
    /**
     * 幅值上限 仪器所能检测到的放电信号幅值的上限。暂定：-10
     */
    private Float x8;
    /**
     * 工频周期数p 图谱工频周期的个数。工频周期数=50.
     */
    private Integer x9;
    /**
     * 放电类型概率表示仪器诊断结果的放电类型概率。数组元素[0]至[7]分别存储正常、尖端放电、悬浮放电、沿面放电、内部放电、颗粒放电、外部干扰和其它共8种情况的概率，每个元素可以是整数0至100中的某数。实例：数组元素[3]为69，表示沿面放电的概率为69%。如果诊断结果为正常，则为数组元素[0]赋值100，数组元素[1]至[7]赋值0。先按照以下规则处理如果仪器不具备放电类型诊断功能，则为数组元素[0]至[7]赋值0x00。
     */
    private String x10;
    /**
     * 局部放电图谱数据该文件是PRPS图谱，则为float[p][m]，p为工频周期数——50，m为相位窗数——120，数组元素[x][y]的值表示在对应第x个周期的第y相位窗处发生的放电的幅值。
     */
    private String x11;
    /**
     * CRC32数据校验，使用CRC32算法。
     */
    private Integer x12;
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