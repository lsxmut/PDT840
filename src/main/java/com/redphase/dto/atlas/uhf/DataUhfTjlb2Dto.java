package com.redphase.dto.atlas.uhf;


import com.redphase.framework.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
/** <p> 特高频_模式2_统计录波 DTO */
public class DataUhfTjlb2Dto extends BaseDto {
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
     * |5|char|1字节|图谱类型编码标识该文件是PRPD图谱还是PRPS图谱。PRPS:0x01
     */
    private Integer x5;
    /**
     * |6|int|4字节|相位窗数m工频周期被等分成m个相位窗，每个相位窗跨360/m度。m=dataBytes.length/4/xlen（暂定）
     */
    private Integer x6;
    /**
     * |7|char|1字节|幅值单位表示幅值的单位dBm:0x01mV:0x02%:0x03
     */
    private Integer x7;
    /**
     * |8|float|4字节|幅值下限仪器所能检测到的放电信号幅值的下限。暂定：-75
     */
    private Float x8;
    /**
     * |9|float|4字节|幅值上限仪器所能检测到的放电信号幅值的上限。暂定：-10
     */
    private Float x9;
    /**
     * |10|int|4字节|工频周期数p图谱工频周期的个数。工频周期数=500.
     */
    private Integer x10;
    /**
     * |11|float[p][m]|4*500*120字节|局部放电图谱数据该文件是PRPS图谱，则为float[p][m]，p为工频周期数——500（表示10秒，每秒50个），m为相位窗数——120，数组元素[x][y]的值表示在对应第x个周期的第y相位窗处发生的放电的幅值。
     */
    private String x11;
    /**
     * |12|float|4字节|参考阈值
     */
    private Float x12;
    /**
     * |13|float|4字节|相位
     */
    private Float x13;
    /**
     * |14|Char|1字节|同步类型外同步:0x01内同步:0x02
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
     * |17|float|4字节|警戒值1如-60
     */
    private Float x17;
    /**
     * |18|float|4字节|警戒值2如-40
     */
    private Float x18;
    /**
     * |19|float|4字节|警戒值3如-20
     */
    private Float x19;
    /**
     * |20|uint8|1字节|频带
     */
    private Integer x20;
    /**
     * |21|float|4字节|下限频率
     */
    private Float x21;
    /**
     * |22|float|4字节|上限频率
     */
    private Float x22;
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