package com.redphase.entity.atlas.aa;


import com.redphase.framework.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
/** <p> 空气式超声波_巡检 entity */
public class DataAaXj extends BaseEntity {
    /**
     * id
     */
    private Long id;
    /**
     * 数据分析id
     */
    private Long dataAnalyzeId;
    /**
     * |1|int32| 4字节| 数据长度 数据项1到校验和的字节长度。
     */
    private Integer x1;
    /**
     * |2|float| 4字节| 规范版本号 所使用的数据格式规范版本号，例如1.0。
     */
    private Float x2;
    /**
     * |3|int64| 8字节| 图谱生成时间 生成图谱的时间，格式为YYYYMMDDhhmmssfff，例如20100818151010001。该时间与文件名的时间相同，只是文件名中的时间到秒，这边的到毫秒。
     */
    private Long x3;
    /**
     * |4|uint8| 1字节| 检测技术类型编码 标识该文件是采用什么技术获得。,TEV: 0x01,AA: 0x02,AA: 0x03,HFCT: 0x04,UHF: 0x05
     */
    private Integer x4;
    /**
     * |5|float| 4字节| 幅值m2
     */
    private Float x5;
    /**
     * |6|int32| 4字节| 放电频率n2
     */
    private Integer x6;
    /**
     * |7|float| 4字节| 有效值 计算结果
     */
    private Float x7;
    /**
     * |8|float| 4字节| f1分量 计算结果
     */
    private Float x8;
    /**
     * |9|float| 4字节| f2分量 计算结果
     */
    private Float x9;
    /**
     * |10|char[8]| 8字节| 放电类型概率 表示仪器诊断结果的放电类型概率。数组元素[0]至[7]分别存储正常、尖端放电、悬浮放电、沿面放电、内部放电、颗粒放电、外部干扰和其它共8种情况的概率，每个元素可以是整数0至100中的某数。实例：数组元素[3]为69，表示沿面放电的概率为69%。,如果诊断结果为正常，则为数组元素[0]赋值100，数组元素[1]至[7]赋值0。,先按照以下规则处理,如果仪器不具备放电类型诊断功能，则为数组元素[0]至[7]赋值0x00。
     */
    private String x10;
    /**
     * |11|uint8| 1字节| 空气超声监听结果 表示空气超声的监听结果,正常: 0x01,异常: 0x02,未监听: 0x03
     */
    private Integer x11;
    /**
     * |12|uint8| 1字节| AA幅值单位 表示幅值的单位,dBuV: 0x04
     */
    private Integer x12;
    /**
     * |13|float| 4字节| AA幅值下限 仪器所能检测到的放电信号幅值的下限。暂定：-10
     */
    private Float x13;
    /**
     * |14|float| 4字节| AA幅值上限 仪器所能检测到的放电信号幅值的上限。暂定：70
     */
    private Float x14;
    /**
     * |15|float| 4字节| AA参考阈值
     */
    private Float x15;
    /**
     * |16|uint8| 1字节| 空气超声增益 60dB: 0x01,80dB: 0x02,100dB: 0x03
     */
    private Integer x16;
    /**
     * |17|float| 4字节| f1分量 检测参数，用户人工录入或点击获取的值。
     */
    private Float x17;
    /**
     * |18|float| 4字节| f2分量 检测参数，f2=f1×2；
     */
    private Float x18;
    /**
     * |19|float| 4字节| 系统频率
     */
    private Float x19;
    /**
     * |20|float| 4字节| 同步频率 同步频率=f1分量
     */
    private Float x20;
    /**
     * |21|float| 4字节| 空气超声警戒值1 如6
     */
    private Float x21;
    /**
     * |22|float| 4字节| 空气超声警戒值2 如15
     */
    private Float x22;
    /**
     * |23|float| 4字节| 空气超声警戒值3 如20
     */
    private Float x23;
    /**
     * |24|int32| 4字节| 前置增益
     */
    private Integer x24;
    /**
     * |25|char | 32字节| 仪器厂家 从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：RedPhase。
     */
    private String x25;
    /**
     * |26|char | 32字节| 仪器型号 从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：PDT-400。
     */
    private String x26;
    /**
     * |27|uint8| 4字节| 仪器版本号 从系统设置中提取，所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。,实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。
     */
    private String x27;
    /**
     * |28|char | 32字节| 仪器序列号 从系统设置中提取，即仪器出厂编号，使用ASCII编码。以\0结尾，例如：S12300000000000000。
     */
    private String x28;
    /**
     * |29|int32| 4字节| 校验和 1到校验和前一项的数据校验。
     */
    private Integer x29;
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