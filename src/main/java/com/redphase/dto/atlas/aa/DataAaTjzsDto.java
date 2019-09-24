package com.redphase.dto.atlas.aa;


import com.redphase.framework.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
/** <p> 空气式超声波_统计噪声 DTO */
public class DataAaTjzsDto extends BaseDto {
    /**
     * 主键
     */
    private Long id;
    /**
     * 数据分析id
     */
    //@NotNull(message = "dataAnalyzeId不能为空")
    private Long dataAnalyzeId;
    /**
     * |1|int32 |4字节| 数据长度 数据项1到12的字节长度。
     */
    private Integer x1;
    /**
     * |2|float |4字节| 规范版本号 所使用的数据格式规范版本号，例如1.0。
     */
    private Float x2;
    /**
     * |3|uint8 |1字节| 图谱类型编码 标识该文件是PRPD图谱还是PRPS图谱。,PRPS: 0x01
     */
    private Integer x3;
    /**
     * |4|int32 |4字节| 相位窗数m 工频周期被等分成m个相位窗，每个相位窗跨 360/m 度。,m = 120（暂定）
     */
    private Integer x4;
    /**
     * |5|int32 |4字节| 量化幅值n 赋值为：0x00000000。
     */
    private Integer x5;
    /**
     * |6|uint8 |1字节| 幅值单位 表示幅值的单位,dBuV: 0x04
     */
    private Integer x6;
    /**
     * |7|float |4字节| 幅值下限 仪器所能检测到的放电信号幅值的下限。暂定：-10
     */
    private Float x7;
    /**
     * |8|float |4字节| 幅值上限 仪器所能检测到的放电信号幅值的上限。暂定：70
     */
    private Float x8;
    /**
     * |9|int32 |4字节| 工频周期数p 图谱工频周期的个数。,工频周期数=50.
     */
    private Integer x9;
    /**
     * |10|uint8 |8字节| 放电类型概率 表示仪器诊断结果的放电类型概率。数组元素[0]至[7]分别存储正常、尖端放电、悬浮放电、沿面放电、内部放电、颗粒放电、外部干扰和其它共8种情况的概率，每个元素可以是整数0至100中的某数。实例：数组元素[3]为69，表示沿面放电的概率为69%。,如果诊断结果为正常，则为数组元素[0]赋值100，数组元素[1]至[7]赋值0。,先按照以下规则处理,如果仪器不具备放电类型诊断功能，则为数组元素[0]至[7]赋值0x00。
     */
    private String x10;
    /**
     * |11| float[p][m] |4*50*120字节| 局部放电图谱数据 该文件是PRPS图谱，则为float[p][m]，p为工频周期数——50，m为相位窗数——120，数组元素[x][y]的值表示在对应第x个周期的第y相位窗处发生的放电的幅值。
     */
    private String x11;
    /**
     * |12|int|4字节| CRC32 1到11项的数据校验，使用CRC32算法。
     */
    private Integer x12;
    /**
     * |13|int|4字节| 数据长度 数据项13到校验和的字节长度。
     */
    private Integer x13;
    /**
     * |14|int64 |8字节| 图谱生成时间 生成图谱的时间，格式为YYYYMMDDhhmmssfff，例如20100818151010001。该时间与文件名的时间相同，只是文件名中的时间到秒，这边的到毫秒。
     */
    private Long x14;
    /**
     * |15|uint8 |1字节| 检测技术类型编码 标识该文件是采用什么技术获得。,TEV: 0x01,AA: 0x02,AA: 0x03,HFCT: 0x04,UHF: 0x05
     */
    private Integer x15;
    /**
     * |16|float |4字节| 超声波幅值 表示统计模式下当前1秒的幅值
     */
    private Float x16;
    /**
     * |17|int32 |4字节| 超声波放电频度，即脉冲个数 表示统计模式下当前1秒的脉冲个数
     */
    private Integer x17;
    /**
     * |18|float |4字节| 空气超声参考阈值
     */
    private Float x18;
    /**
     * |19|float |4字节| 相位
     */
    private Float x19;
    /**
     * |20|uint8 |1字节| 同步类型 外同步: 0x01,内同步: 0x02
     */
    private Integer x20;
    /**
     * |21|float |4字节| 同步频率
     */
    private Float x21;
    /**
     * |22|float |4字节| 系统频率
     */
    private Float x22;
    /**
     * |23|uint8 |1字节| 放大增益 60dB: 0x01,80dB: 0x02,100dB: 0x03
     */
    private Integer x23;
    /**
     * |24|float |4字节| 空气超声警戒值1 如6
     */
    private Float x24;
    /**
     * |25|float |4字节| 空气超声警戒值2 如15
     */
    private Float x25;
    /**
     * |26|float |4字节| 空气超声警戒值3 如20
     */
    private Float x26;
    /**
     * |27|char|32字节 | 仪器厂家 从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：RedPhase。
     */
    private String x27;
    /**
     * |28|char|32字节 | 仪器型号 从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：PDT-400。
     */
    private String x28;
    /**
     * |29|uint8 |4字节| 仪器版本号 从系统设置中提取，所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。,实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。
     */
    private String x29;
    /**
     * |30|char|32字节 | 仪器序列号 从系统设置中提取，即仪器出厂编号，使用ASCII编码。以\0结尾，例如：S12300000000000000。
     */
    private String x30;
    /**
     * |31|int32 |4字节| 检验和 13到校验和前一项的数据校验。
     */
    private Integer x31;
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