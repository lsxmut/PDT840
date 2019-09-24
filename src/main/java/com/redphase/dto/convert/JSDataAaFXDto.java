package com.redphase.dto.convert;


import com.redphase.framework.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
/** <p> 江苏_超声_脉冲图数据 DTO */
public class JSDataAaFXDto extends BaseDto {
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
     * 必备 uint8 1字节 超声传感器类型 - 0xFF：未记录0x01：空声0x02：SF6气体绝缘电力设备检测用接触式0x03：充油电力设备检测用接触式0x04：其他类型
     */
    private Integer x30;
    /**
     * 必备 uint8 1字节 脉冲间隔时间单位 - 表示脉冲间隔时间的单位。0x01：微秒0x02：毫秒0x03：秒微秒: 0x01毫秒: 0x02秒: 0x03
     */
    private Integer x31;
    /**
     * 必备 int32 4字节 数据点数n - 数据点个数，默认为1000。
     */
    private Integer x32;
    /**
     * 必备 uint8 8字节 放电类型概率 - 表示仪器诊断结果的放电类型概率。参见放电类型概率定义。
     */
    private String x33;
    /**
     * 可选 自定义 152字节 预留 - 预留为厂家自定义可选字段。
     */
    private String x34;
    /**
     * 必备 d[2*n] k*2*n字节 超声脉冲图数据 - 根据存储数据类型t获取数据的存储方式。实例1：t是0x02，d表示uint8数组，k=1；实例2：t是0x04，d表示int32数组，k=4；实例3：t是0x06，d表示float数组，k=4。依次存储每个数据点的飞行时间T和信号幅值Q，存储顺序：T[0]，Q[0]；T[1]，Q[1]；……；T[n-1]，Q[n-1]。
     */
    private String x35;
    /**
     * 必备 - 32字节 预留 - 预留
     */
    private String x36;
    /**
     * 必备 int32 4字节 CRC32 - 数据校验，使用CRC32算法
     */
    private Integer x37;
}