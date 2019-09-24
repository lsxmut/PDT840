package com.redphase.entity.atlas.ae;


import com.redphase.framework.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
/** <p> 接触式超声波_飞行图谱 entity */
public class DataAeFx extends BaseEntity {
    /**
     * 主键
     */
    private Long id;
    /**
     * 数据分析id
     */
    private Long dataAnalyzeId;
    /**
     * |1|int|4字节|数据长度数据项1到校验和的字节长度。
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
     * |4|char|1字节|检测技术类型编码标识该文件是采用什么技术获得。TEV:0x01AA:0x02AE:0x03HFCT:0x04UHF:0x05
     */
    private Integer x4;
    /**
     * |5|uint8|1字节|图谱类型编码标识该文件是飞行图谱。编码:0x33
     */
    private Integer x5;
    /**
     * |6|char|1字节|幅值单位表示幅值的单位mV:0x06
     */
    private Integer x6;
    /**
     * |7|float|4字节|幅值下限仪器所能检测到的放电信号幅值的下限。暂定：0
     */
    private Float x7;
    /**
     * |8|float|4字节|幅值上限仪器所能检测到的放电信号幅值的上限。暂定：300
     */
    private Float x8;
    /**
     * |9|uint8|1字节|脉冲间隔时间单位表示脉冲间隔时间的单位。微秒:0x01毫秒:0x02秒:0x03
     */
    private Integer x9;
    /**
     * |10|int32|4字节|数据点数n数据点个数，默认为1000。
     */
    private Integer x10;
    /**
     * |11|uint8|8字节|放电类型概率表示仪器诊断结果的放电类型概率。参见放电类型概率定义。先按照以下规则处理如果仪器不具备放电类型诊断功能，则为数组元素[0]至[7]赋值0x00。
     */
    private String x11;
    /**
     * |12|float|4字节|参考阈值
     */
    private Float x12;
    /**
     * |13|char|1字节|增益20dB:0x0140dB:0x0260dB:0x03
     */
    private Integer x13;
    /**
     * |14|int|4字节|开门时间以us为单位
     */
    private Integer x14;
    /**
     * |15|int|4字节|闭锁时间以ms为单位
     */
    private Integer x15;
    /**
     * |16|int|4字节|等待时间以秒为单位
     */
    private Integer x16;
    /**
     * |17|int|4字节|最大时间间隔以ms为单位
     */
    private Integer x17;
    /**
     * |18|int|4字节|前置增益单位dB
     */
    private Integer x18;
    /**
     * |19|float|4字节|系统频率
     */
    private Float x19;
    /**
     * |20|uint8|1字节|同步类型外同步:0x01内同步:0x02默认写入内同步。
     */
    private Integer x20;
    /**
     * |21|float|4字节|同步频率同步频率=系统频率
     */
    private Float x21;
    /**
     * |22|float|4*2*n字节|超声脉冲图数据根据存储数据类型t获取数据的存储方式。实例1：uint8数组，k=1；实例2：int32数组，k=4；实例3：float数组，k=4。依次存储每个数据点的飞行时间T和信号幅值Q，存储顺序：T[0]，Q[0]；T[1]，Q[1]；……；T[n-1]，Q[n-1]。
     */
    private String x22;
    /**
     * |23|char|32字节|仪器厂家从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：RedPhase。
     */
    private String x23;
    /**
     * |24|char|32字节|仪器型号从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：PDT-400。
     */
    private String x24;
    /**
     * |25|uint8|4字节|仪器版本号从系统设置中提取，所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。
     */
    private String x25;
    /**
     * |26|char|32字节|仪器序列号从系统设置中提取，即仪器出厂编号，使用ASCII编码。以\0结尾，例如：S12300000000000000。
     */
    private String x26;
    /**
     * |27|int|4字节|检验和1到校验和前一项的数据校验。
     */
    private Integer x27;
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