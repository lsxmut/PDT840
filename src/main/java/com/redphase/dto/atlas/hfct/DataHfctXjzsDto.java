package com.redphase.dto.atlas.hfct;

import com.redphase.framework.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
/** <p> 高频电流_巡检噪声 DTO */
public class DataHfctXjzsDto extends BaseDto {
    /**
     * 主键
     */
    private Long id;
    /**
     * 数据分析id
     */
    // @NotNull(message = "dataAnalyzeId不能为空")
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
     * |4|uint8|1字节|检测技术类型编码标识该文件是采用什么技术获得。TEV:0x01AA:0x02AE:0x03HFCT:0x04UHF:0x05
     */
    private Integer x4;
    /**
     * |5|float|4字节|幅值m2
     */
    private Float x5;
    /**
     * |6|int32|4字节|放电频率n2
     */
    private Integer x6;
    /**
     * |7|uint8|1字节|幅值单位表示幅值的单位dB:0x01mV:0x02
     */
    private Integer x7;
    /**
     * |8|float|4字节|幅值下限仪器所能检测到的放电信号幅值的下限。暂定：0
     */
    private Float x8;
    /**
     * |9|float|4字节|幅值上限仪器所能检测到的放电信号幅值的上限。暂定：300
     */
    private Float x9;
    /**
     * |10|float|4字节|参考阈值
     */
    private Float x10;
    /**
     * |11|uint8|1字节|增益20dB:0x0140dB:0x0260dB:0x03
     */
    private Integer x11;
    /**
     * |12|int32|4字节|前置衰减以dB为单位
     */
    private Integer x12;
    /**
     * |13|float|4字节|警戒值1如50
     */
    private Float x13;
    /**
     * |14|float|4字节|警戒值2如150
     */
    private Float x14;
    /**
     * |15|float|4字节|警戒值3如200
     */
    private Float x15;
    /**
     * |16|uint8|1字节|频带
     */
    private Integer x16;
    /**
     * |17|float|4字节|下限频率
     */
    private Float x17;
    /**
     * |18|float|4字节|上限频率
     */
    private Float x18;
    /**
     * |19|float|4字节|系统频率
     */
    private Float x19;
    /**
     * |20|uint8|1字节|同步类型外同步:0x01内同步:0x02默认写入内同步。
     */
    private Integer x20;
    /**
     * |21|float|4字节|同步频率此模式无法检测同步频率，因此将同步频率默认与系统频率相同。
     */
    private Float x21;
    /**
     * |22|char|32字节|仪器厂家从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：RedPhase。
     */
    private String x22;
    /**
     * |23|char|32字节|仪器型号从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：PDT-400。
     */
    private String x23;
    /**
     * |24|uint8|4字节|仪器版本号从系统设置中提取，所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。
     */
    private String x24;
    /**
     * |25|char|32字节|仪器序列号从系统设置中提取，即仪器出厂编号，使用ASCII编码。以\0结尾，例如：S12300000000000000。
     */
    private String x25;
    /**
     * |26|int32|4字节|校验和1到校验和前一项的数据校验。
     */
    private Integer x26;
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