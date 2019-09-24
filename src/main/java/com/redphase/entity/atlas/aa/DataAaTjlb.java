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
/** <p> 空气式超声波_统计录波 entity */
public class DataAaTjlb extends BaseEntity {
    /**
     * 主键
     */
    private Long id;
    /**
     * 数据分析id
     */
    private Long dataAnalyzeId;
    /**
     * |1|int32|4字节|数据长度数据项1到校验和的字节长度。
     */
    private Integer x1;
    /**
     * |2|float|4字节|规范版本号所使用的数据格式规范版本号，例如1.0。
     */
    private Float x2;
    /**
     * |3|int64|8字节|图谱生成时间生成图谱的时间，格式为YYYYMMDDhhmmssfff，例如20100818151010001。该时间与文件名的时间相同，只是文件名中的时间到秒，这边的到毫秒。
     */
    private Long x3;
    /**
     * |4|uint8|1字节|检测技术类型编码标识该文件是采用什么技术获得。TEV:0x01,AA:0x02,AA:0x03,HFCT:0x04,UHF:0x05
     */
    private Integer x4;
    /**
     * |5|uint8|1字节|图谱类型编码标识该文件是PRPD图谱还是PRPS图谱。PRPS:0x01
     */
    private Integer x5;
    /**
     * |6|int32|4字节|相位窗数m工频周期被等分成m个相位窗，每个相位窗跨360/m度。,m=120（暂定）
     */
    private Integer x6;
    /**
     * |7|uint8|1字节|幅值单位表示幅值的单位,dBuV:0x04
     */
    private Integer x7;
    /**
     * |8|float|4字节|幅值下限仪器所能检测到的放电信号幅值的下限。暂定：-10
     */
    private Float x8;
    /**
     * |9|float|4字节|幅值上限仪器所能检测到的放电信号幅值的上限。暂定：70
     */
    private Float x9;
    /**
     * |10|int32|4字节|工频周期数p图谱工频周期的个数。,工频周期数=500.
     */
    private Integer x10;
    /**
     * |11|float[p][m]|4*500*120字节|局部放电图谱数据该文件是PRPS图谱，则为float[p][m]，p为工频周期数——500（表示10秒，每秒50个），m为相位窗数——120，数组元素[x][y]的值表示在对应第x个周期的第y相位窗处发生的放电的幅值。
     */
    private String x11;
    /**
     * |12|float|4字节|AA参考阈值
     */
    private Float x12;
    /**
     * |13|float|4字节|AA相位
     */
    private Float x13;
    /**
     * |14|uint8|1字节|同步类型外同步:0x01,内同步:0x02
     */
    private Integer x14;
    /**
     * |15|float|4字节|同步频率
     */
    private Float x15;
    /**
     * |16|float|4字节|系统频率
     */
    private Float x16;
    /**
     * |17|uint8|1字节|放大增益60dB:0x01,80dB:0x02,100dB:0x03
     */
    private Integer x17;
    /**
     * |18|float|4字节|空气超声警戒值1如6
     */
    private Float x18;
    /**
     * |19|float|4字节|空气超声警戒值2如15
     */
    private Float x19;
    /**
     * |20|float|4字节|空气超声警戒值3如20
     */
    private Float x20;
    /**
     * |21|char|32字节|仪器厂家从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：RedPhase。
     */
    private String x21;
    /**
     * |22|char|32字节|仪器型号从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：PDT-400。
     */
    private String x22;
    /**
     * |23|uint8|4字节|仪器版本号从系统设置中提取，所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。,实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。
     */
    private String x23;
    /**
     * |24|char|32字节|仪器序列号从系统设置中提取，即仪器出厂编号，使用ASCII编码。以\0结尾，例如：S12300000000000000。
     */
    private String x24;
    /**
     * |25|int|4字节|检验和1到校验和前一项的数据校验。
     */
    private Integer x25;
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