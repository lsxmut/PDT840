package com.redphase.dto.atlas.tev;


import com.redphase.framework.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
/** <p> 地电波_巡检噪声 DTO */
public class DataTevXjzsDto extends BaseDto {
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
     * |4|uint8|1字节|检测技术类型编码标识该文件是采用什么技术获得。,TEV:0x01,AA:0x02,AE:0x03,HFCT:0x04,UHF:0x05,TEV+AA：0x06
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
     * |9|uint8|1字节|地电波幅值单位表示幅值的单位,
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
     * |17|uint8|1字节|同步类型外同步:0x01,内同步:0x02,默认写入内同步。
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
     * |21|uint8|1字节|空气超声幅值单位表示幅值的单位,dBuv:0x04
     */
    private Integer x21;
    /**
     * |22|float|4字节|空气超声幅值下限仪器所能检测到的放电信号幅值的下限。暂定：-10
     */
    private Float x22;
    /**
     * |23|float|4字节|空气超声幅值上限仪器所能检测到的放电信号幅值的上限。暂定：70
     */
    private Float x23;
    /**
     * |24|float|4字节|空气超声参考阈值
     */
    private Float x24;
    /**
     * |25|uint8|1字节|空气超声增益60dB:0x01,80dB:0x02,100dB:0x03
     */
    private Integer x25;
    /**
     * |26|float|4字节|空气超声警戒值1如6
     */
    private Float x26;
    /**
     * |27|float|4字节|空气超声警戒值2如15
     */
    private Float x27;
    /**
     * |28|float|4字节|空气超声警戒值3如20
     */
    private Float x28;
    /**
     * |29|char|32字节|仪器厂家从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：RedPhase。
     */
    private String x29;
    /**
     * |30|char|32字节|仪器型号从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：PDT-400。
     */
    private String x30;
    /**
     * |31|uint8|4字节|仪器版本号从系统设置中提取，所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。,实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。
     */
    private String x31;
    /**
     * |32|char|32字节|仪器序列号从系统设置中提取，即仪器出厂编号，使用ASCII编码。以\0结尾，例如：S12300000000000000。
     */
    private String x32;
    /**
     * |33|int32|4字节|校验和1到校验和项前一项的数据校验。
     */
    private Integer x33;
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