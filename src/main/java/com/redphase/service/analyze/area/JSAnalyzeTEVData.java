package com.redphase.service.analyze.area;

import com.alibaba.fastjson.JSON;
import com.redphase.framework.util.ByteConverterUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 地区-江苏数据
 */
@Slf4j
public class JSAnalyzeTEVData {
    /**
     * 江苏_暂态地电压_电压幅值数据
     * 2.数据格式要求。
     * ---------------------------------------------------------------------------------------
     * |    序号  |  数据类型   |  长度        |  备注
     * ---------------------------------------------------------------------------------------
     * |	1	|	int32	|	4字节	|	文件长度L	-	文件长度，含CRC校验。
     * |	2	|	uint8	|	4字节	|	规范版本号	-	所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。建议：版本号采用“内部主版本号.内部子版本号.省公司编码.国家单位编码”的形式，以便于版本维护。
     * |	3	|	int64	|	8字节	|	文件生成时间	-	生成文件的时间，格式为YYYYMMDDhhmmssfff，例如20100818151010001。
     * |	4	|	char	|	128字节	|	站点名称	-	使用UNICODE编码。以0x0000结尾，例如：110kV枫泾变电站。
     * |	5	|	char	|	32字节	|	站点编码	-	使用ASCII编码。以\0结尾，例如：A12300000000000000。
     * |	6	|	uint8	|	1字节	|	天气	-	表示天气。0xFF：未记录0x01：晴0x02：阴0x03：雨0x04：雪0x05：雾0x06：雷雨0x07：多云
     * |	7	|	float	|	4字节	|	温度	-	环境温度，单位摄氏度。
     * |	8	|	int8	|	1字节	|	湿度	-	环境湿度，单位%。
     * |	9	|	char	|	32字节	|	仪器厂家	-	使用UNICODE编码。以0x0000结尾，例如：HuaCheng。
     * |	10	|	char	|	32字节	|	仪器型号	-	使用UNICODE编码。以0x0000结尾，例如：PDS-T95。
     * |	11	|	uint8	|	4字节	|	仪器版本号	-	所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。
     * |	12	|	char	|	32字节	|	仪器序列号	-	即仪器出厂编号，使用ASCII编码。以\0结尾，例如：S12300000000000000。
     * |	13	|	float	|	4字节	|	系统频率	-	被测设备电压频率，单位Hz。例如50Hz。
     * |	14	|	int16	|	2字节	|	图谱数量N	-	文件中包含的图谱数量。
     * |	15	|	float	|	4字节	|	同步频率	-	采集装置的同步频率，单位Hz。
     * |	16	|	自定义	|	220字节	|	预留	-	预留为厂家自定义可选字段。
     * |	17	|	uint8	|	1字节	|	图谱类型编码	-	标识该文件的图谱类型。参见图谱类型编码表。
     * |	18	|	int32	|	4字节	|	图谱数据长度	-	图谱总长度，指从图谱类型编码到图谱数据结束的长度。
     * |	19	|	int64	|	8字节	|	图谱生成时间	-	生成图谱的时间，格式为YYYYMMDDhhmmssfff，例如20100818151010001。
     * |	20	|	uint8	|	1字节	|	图谱性质	-	0xFF：未记录0x01：检测图谱0x02：背景噪声0x03：检测图谱及背景噪声
     * |	21	|	char	|	128字节	|	电力设备名称	-	使用UNICODE编码。以0x0000结尾，例如：10kV万达线#3开关柜。
     * |	22	|	char	|	32字节	|	电力设备编码	-	使用ASCII编码。以\0结尾，例如：B12300000000000000。
     * |	23	|	char	|	128字节	|	测点名称	-	使用UNICODE编码。以0x0000结尾，例如：电缆仓。
     * |	24	|	char	|	32字节	|	测点编码	-	使用ASCII编码。以\0结尾，例如：C12300000000000000。
     * |	25	|	int16	|	2字节	|	检测通道标志	-	仪器的检测通道标识，例如：1。
     * |	26	|	uint8	|	1字节	|	存储数据类型t	-	表示图谱数据的存储数据类型。参见存储类型编码表。
     * |	27	|	uint8	|	1字节	|	幅值单位	-	表示幅值的单位。参见幅值单位表。
     * |	28	|	float	|	4字节	|	幅值下限	-	仪器所能检测到的信号幅值的下限。
     * |	29	|	float	|	4字节	|	幅值上限	-	仪器所能检测到的信号幅值的上限。
     * |	30	|	float	|	4字节	|	暂态地电压幅值数据	-	暂态地电压幅值数据。
     * |	31	|	float	|	4字节	|	暂态地电压幅值最大值	-
     * |	32	|	int32	|	4字节	|	脉冲数	-	脉冲个数。
     * |	33	|	float	|	4字节	|	烈度	-
     * |	34	|	自定义	|	150字节	|	预留	-	预留为厂家自定义可选字段。
     * |	35	|	-	|	32字节	|	预留	-	预留
     * |	36	|	int32	|	4字节	|	CRC32	-	数据校验，使用CRC32算法
     * ---------------------------------------------------------------------------------------
     */
    public Object TEV_XJ(int i, byte[] dataBytes) throws Exception {
        Object data = null;
        switch (i + 1) {
            case 6://	uint8	1字节
            case 8://	int8	1字节
            case 17://	uint8	1字节
            case 20://	uint8	1字节
            case 26://	uint8	1字节
            case 27://	uint8	1字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 11://	uint8	4字节
            case 2://	uint8	4字节
                data = JSON.toJSONString(new Integer[]{
                        ByteConverterUtil.byte2int(new byte[]{dataBytes[0]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[1]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[2]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[3]})
                });
                break;
            case 14://	int16	2字节
            case 25://	int16	2字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 1://	int32	4字节
            case 18://	int32	4字节
            case 32://	int32	4字节
            case 36://	int32	4字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 3://	int64	8字节
            case 19://	int64	8字节
                data = ByteConverterUtil.byte2long(dataBytes);
                break;
            case 7://	float	4字节
            case 13://	float	4字节
            case 15://	float	4字节
            case 28://	float	4字节
            case 29://	float	4字节
            case 30://	float	4字节
            case 31://	float	4字节
            case 33://	float	4字节
                data = ByteConverterUtil.byte2float(dataBytes);
                break;
            case 4://	char	128字节
            case 9://	char	32字节
            case 10://	char	32字节
            case 21://	char	128字节
            case 23://	char	128字节
                data = ByteConverterUtil.byte2unicode(dataBytes);
                break;
            case 5://	char	32字节
            case 12://	char	32字节
            case 16://	自定义	220字节
            case 22://	char	32字节
            case 24://	char	32字节
            case 34://	自定义	150字节
            case 35://	-	32字节
                data = ByteConverterUtil.byte2string(dataBytes);
                break;
            default:
        }
        return data;
    }

    /**
     * 江苏_暂态地电压_电压幅值数据(噪声)
     * 2.数据格式要求。
     * ---------------------------------------------------------------------------------------
     * |    序号  |  数据类型   |  长度        |  备注
     * ---------------------------------------------------------------------------------------
     * |	1	|	int32	|	4字节	|	文件长度L	-	文件长度，含CRC校验。
     * |	2	|	uint8	|	4字节	|	规范版本号	-	所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。建议：版本号采用“内部主版本号.内部子版本号.省公司编码.国家单位编码”的形式，以便于版本维护。
     * |	3	|	int64	|	8字节	|	文件生成时间	-	生成文件的时间，格式为YYYYMMDDhhmmssfff，例如20100818151010001。
     * |	4	|	char	|	128字节	|	站点名称	-	使用UNICODE编码。以0x0000结尾，例如：110kV枫泾变电站。
     * |	5	|	char	|	32字节	|	站点编码	-	使用ASCII编码。以\0结尾，例如：A12300000000000000。
     * |	6	|	uint8	|	1字节	|	天气	-	表示天气。0xFF：未记录0x01：晴0x02：阴0x03：雨0x04：雪0x05：雾0x06：雷雨0x07：多云
     * |	7	|	float	|	4字节	|	温度	-	环境温度，单位摄氏度。
     * |	8	|	int8	|	1字节	|	湿度	-	环境湿度，单位%。
     * |	9	|	char	|	32字节	|	仪器厂家	-	使用UNICODE编码。以0x0000结尾，例如：HuaCheng。
     * |	10	|	char	|	32字节	|	仪器型号	-	使用UNICODE编码。以0x0000结尾，例如：PDS-T95。
     * |	11	|	uint8	|	4字节	|	仪器版本号	-	所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。
     * |	12	|	char	|	32字节	|	仪器序列号	-	即仪器出厂编号，使用ASCII编码。以\0结尾，例如：S12300000000000000。
     * |	13	|	float	|	4字节	|	系统频率	-	被测设备电压频率，单位Hz。例如50Hz。
     * |	14	|	int16	|	2字节	|	图谱数量N	-	文件中包含的图谱数量。
     * |	15	|	float	|	4字节	|	同步频率	-	采集装置的同步频率，单位Hz。
     * |	16	|	自定义	|	220字节	|	预留	-	预留为厂家自定义可选字段。
     * |	17	|	uint8	|	1字节	|	图谱类型编码	-	标识该文件的图谱类型。参见图谱类型编码表。
     * |	18	|	int32	|	4字节	|	图谱数据长度	-	图谱总长度，指从图谱类型编码到图谱数据结束的长度。
     * |	19	|	int64	|	8字节	|	图谱生成时间	-	生成图谱的时间，格式为YYYYMMDDhhmmssfff，例如20100818151010001。
     * |	20	|	uint8	|	1字节	|	图谱性质	-	0xFF：未记录0x01：检测图谱0x02：背景噪声0x03：检测图谱及背景噪声
     * |	21	|	char	|	128字节	|	电力设备名称	-	使用UNICODE编码。以0x0000结尾，例如：10kV万达线#3开关柜。
     * |	22	|	char	|	32字节	|	电力设备编码	-	使用ASCII编码。以\0结尾，例如：B12300000000000000。
     * |	23	|	char	|	128字节	|	测点名称	-	使用UNICODE编码。以0x0000结尾，例如：电缆仓。
     * |	24	|	char	|	32字节	|	测点编码	-	使用ASCII编码。以\0结尾，例如：C12300000000000000。
     * |	25	|	int16	|	2字节	|	检测通道标志	-	仪器的检测通道标识，例如：1。
     * |	26	|	uint8	|	1字节	|	存储数据类型t	-	表示图谱数据的存储数据类型。参见存储类型编码表。
     * |	27	|	uint8	|	1字节	|	幅值单位	-	表示幅值的单位。参见幅值单位表。
     * |	28	|	float	|	4字节	|	幅值下限	-	仪器所能检测到的信号幅值的下限。
     * |	29	|	float	|	4字节	|	幅值上限	-	仪器所能检测到的信号幅值的上限。
     * |	30	|	float	|	4字节	|	暂态地电压幅值数据	-	暂态地电压幅值数据。
     * |	31	|	float	|	4字节	|	暂态地电压幅值最大值	-
     * |	32	|	int32	|	4字节	|	脉冲数	-	脉冲个数。
     * |	33	|	float	|	4字节	|	烈度	-
     * |	34	|	自定义	|	150字节	|	预留	-	预留为厂家自定义可选字段。
     * |	35	|	-	|	32字节	|	预留	-	预留
     * |	36	|	int32	|	4字节	|	CRC32	-	数据校验，使用CRC32算法
     * ---------------------------------------------------------------------------------------
     */
    public Object TEV_XJZS(int i, byte[] dataBytes) throws Exception {
        Object data = null;
        switch (i + 1) {
            case 6://	uint8	1字节
            case 8://	int8	1字节
            case 17://	uint8	1字节
            case 20://	uint8	1字节
            case 26://	uint8	1字节
            case 27://	uint8	1字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 2://	uint8	4字节
            case 11://	uint8	4字节
                data = JSON.toJSONString(new Integer[]{
                        ByteConverterUtil.byte2int(new byte[]{dataBytes[0]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[1]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[2]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[3]})
                });
                break;
            case 14://	int16	2字节
            case 25://	int16	2字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 1://	int32	4字节
            case 18://	int32	4字节
            case 32://	int32	4字节
            case 36://	int32	4字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 3://	int64	8字节
            case 19://	int64	8字节
                data = ByteConverterUtil.byte2long(dataBytes);
                break;
            case 7://	float	4字节
            case 13://	float	4字节
            case 15://	float	4字节
            case 28://	float	4字节
            case 29://	float	4字节
            case 30://	float	4字节
            case 31://	float	4字节
            case 33://	float	4字节
                data = ByteConverterUtil.byte2float(dataBytes);
                break;
            case 4://	char	128字节
            case 9://	char	32字节
            case 10://	char	32字节
            case 21://	char	128字节
            case 23://	char	128字节
                data = ByteConverterUtil.byte2unicode(dataBytes);
                break;
            case 5://	char	32字节
            case 12://	char	32字节
            case 16://	自定义	220字节
            case 22://	char	32字节
            case 24://	char	32字节
            case 34://	自定义	150字节
            case 35://	-	32字节
                data = ByteConverterUtil.byte2string(dataBytes);
                break;
            default:
        }
        return data;
    }
}

