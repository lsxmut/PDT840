package com.redphase.entity.atlas.tev;


import com.redphase.framework.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
/** <p> 地电波_巡检 entity */
public class DataTevXj extends BaseEntity {
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
     * |4|uint8|1字节|检测技术类型编码标识该文件是采用什么技术获得。TEV:0x01AA:0x02AE:0x03HFCT:0x04UHF:0x05TEV+AA：0x06
     */
    private Integer x4;
    /**
     * |5|float|4字节|地电波幅值m1
     */
    private Float x5;
    /**
     * |6|float|4字节|地电波幅值最大值
     */
    private Float x6;
    /**
     * |7|int32|4字节|地电波放电频率n1
     */
    private Integer x7;
    /**
     * |8|float|4字节|地电波烈度
     */
    private Float x8;
    /**
     * |9|uint8|1字节|地电波幅值单位表示幅值的单位dBmv:0x03
     */
    private Integer x9;
    /**
     * |10|float|4字节|地电波幅值下限仪器所能检测到的放电信号幅值的下限。暂定：0
     */
    private Float x10;
    /**
     * |11|float|4字节|地电波幅值上限仪器所能检测到的放电信号幅值的上限。暂定：70
     */
    private Float x11;
    /**
     * |12|float|4字节|地电波参考阈值
     */
    private Float x12;
    /**
     * |13|float|4字节|地电波警戒值1如20
     */
    private Float x13;
    /**
     * |14|float|4字节|地电波警戒值2如25
     */
    private Float x14;
    /**
     * |15|float|4字节|地电波警戒值3如30
     */
    private Float x15;
    /**
     * |16|float|4字节|系统频率
     */
    private Float x16;
    /**
     * |17|uint8|1字节|同步类型外同步:0x01内同步:0x02默认写入内同步。
     */
    private Integer x17;
    /**
     * |18|float|4字节|同步频率此模式无法检测同步频率，因此将同步频率默认与系统频率相同。
     */
    private Float x18;
    /**
     * |19|float|4字节|空气超声幅值m2当仅选择地电波检测时，AA幅值为某一个特定的数，如-100
     */
    private Float x19;
    /**
     * |20|int32|4字节|空气超声放电频率n2当仅选择地电波检测时，AA放电频度值为某一个特定的数，如-100，
     */
    private Integer x20;
    /**
     * |21|uint8|1字节|空气超声监听结果表示空气超声的监听结果正常:0x01异常:0x02未监听:0x03未检测:0x04——当仅选择地电波检测时
     */
    private Integer x21;
    /**
     * |22|uint8|1字节|空气超声幅值单位表示幅值的单位dBuv:0x04
     */
    private Integer x22;
    /**
     * |23|float|4字节|空气超声幅值下限仪器所能检测到的放电信号幅值的下限。暂定：-10
     */
    private Float x23;
    /**
     * |24|float|4字节|空气超声幅值上限仪器所能检测到的放电信号幅值的上限。暂定：70
     */
    private Float x24;
    /**
     * |25|float|4字节|空气超声参考阈值
     */
    private Float x25;
    /**
     * |26|uint8|1字节|空气超声增益60dB:0x0180dB:0x02100dB:0x03
     */
    private Integer x26;
    /**
     * |27|float|4字节|空气超声警戒值1如6
     */
    private Float x27;
    /**
     * |28|float|4字节|空气超声警戒值2如15
     */
    private Float x28;
    /**
     * |29|float|4字节|空气超声警戒值3如20
     */
    private Float x29;
    /**
     * |30|char|32字节|仪器厂家从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：RedPhase。
     */
    private String x30;
    /**
     * |31|char|32字节|仪器型号从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：PDT-400。
     */
    private String x31;
    /**
     * |32|uint8|4字节|仪器版本号从系统设置中提取，所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。
     */
    private String x32;
    /**
     * |33|char|32字节|仪器序列号从系统设置中提取，即仪器出厂编号，使用ASCII编码。以\0结尾，例如：S12300000000000000。
     */
    private String x33;
    /**
     * |34|int32|4字节|校验和1到校验和项前一项的数据校验。
     */
    private Integer x34;
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