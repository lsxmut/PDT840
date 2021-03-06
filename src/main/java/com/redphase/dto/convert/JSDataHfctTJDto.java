package com.redphase.dto.convert;


import com.redphase.framework.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
/** <p> 江苏_高频电流_PRPD图和PRPS图数据 DTO */
public class JSDataHfctTJDto extends BaseDto {
    /**
     * 必备 int32 4字节 文件长度L - 文件长度，含CRC校验。
     */
    private Integer x1;
    /**
     * 必备 uint8 4字节 规范版本号 - 所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。建议：版本号采用“内部主版本号.内部子版本号.省公司编码.国家单位编码”的形式，以便于版本维护。
     */
    private String x2;
    /**
     * 必备 int64 8字节 文件生成时间 - 生成文件的时间，格式为YYYYMMDDhhmmssfff，例如20100818151010001。
     */
    private Long x3;
    /**
     * 必备 char 128字节 站点名称 - 使用UNICODE编码。以0x0000结尾，例如：110kV枫泾变电站。
     */
    private String x4;
    /**
     * 必备 char 32字节 站点编码 - 使用ASCII编码。以\0结尾，例如：A12300000000000000。
     */
    private String x5;
    /**
     * 必备 uint8 1字节 天气 - 表示天气。0xFF：未记录0x01：晴0x02：阴0x03：雨0x04：雪0x05：雾0x06：雷雨0x07：多云
     */
    private Integer x6;
    /**
     * 必备 float 4字节 温度 - 环境温度，单位摄氏度。
     */
    private Float x7;
    /**
     * 必备 int8 1字节 湿度 - 环境湿度，单位%。
     */
    private Integer x8;
    /**
     * 必备 char 32字节 仪器厂家 - 使用UNICODE编码。以0x0000结尾，例如：HuaCheng。
     */
    private String x9;
    /**
     * 必备 char 32字节 仪器型号 - 使用UNICODE编码。以0x0000结尾，例如：PDS-T95。
     */
    private String x10;
    /**
     * 可选 uint8 4字节 仪器版本号 - 所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。
     */
    private String x11;
    /**
     * 必备 char 32字节 仪器序列号 - 即仪器出厂编号，使用ASCII编码。以\0结尾，例如：S12300000000000000。
     */
    private String x12;
    /**
     * 必备 float 4字节 系统频率 - 被测设备电压频率，单位Hz。例如50Hz。
     */
    private Float x13;
    /**
     * 必备 int16 2字节 图谱数量N - 文件中包含的图谱数量。
     */
    private Integer x14;
    /**
     * 必备 float 4字节 同步频率 - 采集装置的同步频率，单位Hz。
     */
    private Float x15;
    /**
     * 可选 自定义 220字节 预留 - 预留为厂家自定义可选字段。
     */
    private String x16;
    /**
     * 必备 uint8 1字节 图谱类型编码 - 标识该文件的图谱类型。参见图谱类型编码表。
     */
    private Integer x17;
    /**
     * 必备 int32 4字节 图谱数据长度 - 图谱总长度，指从图谱类型编码到图谱数据结束的长度。
     */
    private Integer x18;
    /**
     * 必备 int64 8字节 图谱生成时间 - 生成图谱的时间，格式为YYYYMMDDhhmmssfff，例如20100818151010001。
     */
    private Long x19;
    /**
     * 必备 uint8 1字节 图谱性质 - 0xFF：未记录0x01：检测图谱0x02：背景噪声0x03：检测图谱及背景噪声
     */
    private Integer x20;
    /**
     * 必备 char 128字节 电力设备名称 - 使用UNICODE编码。以0x0000结尾，例如：10kV万达线#3开关柜。
     */
    private String x21;
    /**
     * 必备 char 32字节 电力设备编码 - 使用ASCII编码。以\0结尾，例如：B12300000000000000。
     */
    private String x22;
    /**
     * 可选 char 128字节 测点名称 - 使用UNICODE编码。以0x0000结尾，例如：电缆仓。
     */
    private String x23;
    /**
     * 可选 char 32字节 测点编码 - 使用ASCII编码。以\0结尾，例如：C12300000000000000。
     */
    private String x24;
    /**
     * 必备 int16 2字节 检测通道标志 - 仪器的检测通道标识，例如：1。
     */
    private Integer x25;
    /**
     * 必备 uint8 1字节 存储数据类型t - 表示图谱数据的存储数据类型。参见存储类型编码表。
     */
    private Integer x26;
    /**
     * 必备 uint8 1字节 幅值单位 - 表示幅值的单位。参见幅值单位表。
     */
    private Integer x27;
    /**
     * 必备 float 4字节 幅值下限 - 仪器所能检测到的信号幅值的下限。
     */
    private Float x28;
    /**
     * 必备 float 4字节 幅值上限 - 仪器所能检测到的信号幅值的上限。
     */
    private Float x29;
    /**
     * 必备 uint8 1字节 频带 - 表示滤波类型0xFF:未记录0x01:全通0x02:低通0x03:高通0x04:带通0x05:带阻0x06:可扩充
     */
    private Integer x30;
    /**
     * 必备 float 4字节 下限频率 - 表示检测仪器的下限频率，单位Hz。
     */
    private Float x31;
    /**
     * 必备 float 4字节 上限频率 - 表示检测仪器的上限频率，单位Hz。
     */
    private Float x32;
    /**
     * 必备 int32 4字节 相位窗数m - 工频周期被等分成m个相位窗，每个相位窗跨360/m度。
     */
    private Integer x33;
    /**
     * 必备 int32 4字节 量化幅值n - 幅值范围的等分区间数。如果该文件是PRPS图谱，则该4个字节清零，赋值为：0x00000000。
     */
    private Integer x34;
    /**
     * 必备 int32 4字节 工频周期数p - 图谱工频周期的个数。如果该文件是PRPD图谱，则该4个字节清零，赋值为：0x00000000。
     */
    private Integer x35;
    /**
     * 必备 uint8 8字节 放电类型概率 - 表示仪器诊断结果的放电类型概率。参见放电类型概率定义。
     */
    private String x36;
    /**
     * 可选 自定义 137字节 预留 - 预留为厂家自定义可选字段。
     */
    private String x37;
    /**
     * 必备 d[m][n]或d[p][m] k*m*n字节或k*p*m 字节 局部放电图谱数据 - 根据存储数据类型t获取数据的存储方式。实例1：t是0x02，d表示uint8数组，k=1；实例2：t是0x04，d表示int32数组，k=4；实例3：t是0x06，d表示float数组，k=4。如果该文件是PRPD图谱，则为d[m][n]，m为相位窗数，n为量化幅值，数组元素[x][y]的值表示在对应第x相位窗和第y幅值处发生的脉冲信号的次数。如果该文件是PRPS图谱，则为d[p][m]，p为工频周期数，m为相位窗数，数组元素[x][y]的值表示在对应第x个周期的第y相位窗处发生的脉冲信号的幅值。
     */
    private String x38;
    /**
     * 必备 自定义 32字节 预留 - 预留
     */
    private String x39;
    /**
     * 必备 int32 4字节 CRC32 - 数据校验，使用CRC32算法
     */
    private Integer x40;
}