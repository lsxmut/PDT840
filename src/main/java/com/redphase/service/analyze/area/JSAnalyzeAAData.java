package com.redphase.service.analyze.area;

import com.alibaba.fastjson.JSON;
import com.redphase.framework.util.ByteConverterUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * 地区-江苏数据
 */
@Slf4j
public class JSAnalyzeAAData {
    /**
     * 江苏_超声_波形图数据
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
     * |	30	|	uint8	|	1字节	|	超声传感器类型	-	0xFF：未记录0x01：空声0x02：SF6气体绝缘电力设备检测用接触式0x03：充油电力设备检测用接触式0x04：其他类型
     * |	31	|	int32	|	4字节	|	数据点数n	-	数据点个数。
     * |	32	|	int64	|	8字节	|	采样率	-	单位SPS。
     * |	33	|	uint8	|	8字节	|	放电类型概率	-	表示仪器诊断结果的放电类型概率。参见放电类型概率定义。
     * |	34	|	自定义	|	145字节	|	预留	-	预留为厂家自定义可选字段。
     * |	35	|	d[n]	|	k*n字节	|	超声波形图数据	-	根据存储数据类型t获取数据的存储方式。实例1：t是0x02，d表示uint8数组，k=1；实例2：t是0x04，d表示int32数组，k=4；实例3：t是0x06，d表示float数组，k=4。依次存储每个数据点的信号幅值Q，存储顺序：Q[0]；Q[1]；……；Q[n-1]。
     * |	36	|	  -     |	32字节	|	预留	-	预留
     * |	37	|	int32	|	4字节	|	CRC32	-	数据校验，使用CRC32算法
     * ---------------------------------------------------------------------------------------
     */
    public Object AA_BX(int i, byte[] dataBytes) throws Exception {
        Object data = null;
        switch (i + 1) {
            case 6:// uint8 1字节
            case 8:// int8 1字节
            case 17:// uint8 1字节
            case 20:// uint8 1字节
            case 26:// uint8 1字节
            case 27:// uint8 1字节
            case 30:// uint8 1字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 14:// int16 2字节
            case 25:// int16 2字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 2:// uint8 4字节
            case 11:// uint8 4字节
                data = JSON.toJSONString(new Integer[] {ByteConverterUtil.byte2int(new byte[] {dataBytes[0]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[1]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[2]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[3]})});
                break;
            case 1:// int32 4字节
            case 18:// int32 4字节
            case 31:// int32 4字节
            case 37:// int32 4字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 7:// float 4字节
            case 13:// float 4字节
            case 15:// float 4字节
            case 28:// float 4字节
            case 29:// float 4字节
                data = ByteConverterUtil.byte2float(dataBytes);
                break;
            case 3:// int64 8字节
            case 19:// int64 8字节
            case 32:// int64 8字节
                data = ByteConverterUtil.byte2long(dataBytes);
                break;
            case 33:// uint8 8字节
                data = JSON.toJSONString(new Integer[] {ByteConverterUtil.byte2int(new byte[] {dataBytes[0]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[1]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[2]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[3]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[4]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[5]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[6]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[7]})});
                break;
            case 5:// char 32字节
            case 12:// char 32字节
            case 22:// char 32字节
            case 24:// char 32字节
                data = ByteConverterUtil.byte2string(dataBytes);
                break;
            case 9:// char 32字节
            case 10:// char 32字节
                data = ByteConverterUtil.byte2unicode(dataBytes);
                break;
            case 4:// char 128字节
            case 21:// char 128字节
            case 23:// char 128字节
                data = ByteConverterUtil.byte2unicode(dataBytes);
                break;
            case 35:// d[n] k*n字节
                int arrlen = dataBytes.length / 4;
                int offset = 0;
                Integer[] byteArr = new Integer[arrlen];
                byte[] destBytes = new byte[4];
                for (int n = 0; n < arrlen; n++) {
                    System.arraycopy(dataBytes, offset * destBytes.length, destBytes, 0, destBytes.length);
                    byteArr[n] = ByteConverterUtil.byte2int(destBytes);
                    offset++;
                }
                data = JSON.toJSONString(byteArr);
                break;
            case 16:// 自定义 220字节
            case 34:// 自定义 145字节
            case 36:// - | 32字节
                data = ByteConverterUtil.byte2string(dataBytes);
            default:
        }
        return data;
    }

    /**
     * 江苏_超声_脉冲图数据
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
     * |	30	|	uint8	|	1字节	|	超声传感器类型	-	0xFF：未记录0x01：空声0x02：SF6气体绝缘电力设备检测用接触式0x03：充油电力设备检测用接触式0x04：其他类型
     * |	31	|	uint8	|	1字节	|	脉冲间隔时间单位	-	表示脉冲间隔时间的单位。0x01：微秒0x02：毫秒0x03：秒微秒:	0x01毫秒:
     * |	32	|	int32	|	4字节	|	数据点数n	-	数据点个数，默认为1000。
     * |	33	|	uint8	|	8字节	|	放电类型概率	-	表示仪器诊断结果的放电类型概率。参见放电类型概率定义。
     * |	34	|	自定义	|	152字节	|	预留	-	预留为厂家自定义可选字段。
     * |	35	|	d[2*n]	|	k*2*n字节	|	超声脉冲图数据	-	根据存储数据类型t获取数据的存储方式。实例1：t是0x02，d表示uint8数组，k=1；实例2：t是0x04，d表示int32数组，k=4；实例3：t是0x06，d表示float数组，k=4。依次存储每个数据点的飞行时间T和信号幅值Q，存储顺序：T[0]，Q[0]；T[1]，Q[1]；……；T[n-1]，Q[n-1]。
     * |	36	|	-	|	32字节	|	预留	-	预留
     * |	37	|	int32	|	4字节	|	CRC32	-	数据校验，使用CRC32算法
     * ---------------------------------------------------------------------------------------
     */
    public Object AA_FX(int i, byte[] dataBytes) throws Exception {
        Object data = null;
        switch (i + 1) {
            case 6:// uint8 1字节
            case 8:// int8 1字节
            case 17:// uint8 1字节
            case 20:// uint8 1字节
            case 26:// uint8 1字节
            case 27:// uint8 1字节
            case 30:// uint8 1字节
            case 31:// uint8 1字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 14:// int16 2字节
            case 25:// int16 2字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 2:// uint8 4字节
            case 11:// uint8 4字节
                data = JSON.toJSONString(new Integer[] {ByteConverterUtil.byte2int(new byte[] {dataBytes[0]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[1]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[2]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[3]})});
                break;
            case 1:// int32 4字节
            case 18:// int32 4字节
            case 32:// int32 4字节
            case 37:// int32 4字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 7:// float 4字节
            case 13:// float 4字节
            case 15:// float 4字节
            case 28:// float 4字节
            case 29:// float 4字节
                data = ByteConverterUtil.byte2float(dataBytes);
                break;
            case 3:// int64 8字节
            case 19:// int64 8字节
                data = ByteConverterUtil.byte2long(dataBytes);
                break;
            case 33:// uint8 8字节
                data = JSON.toJSONString(new Integer[] {ByteConverterUtil.byte2int(new byte[] {dataBytes[0]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[1]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[2]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[3]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[4]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[5]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[6]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[7]})});
                break;
            case 5:// char 32字节
            case 12:// char 32字节
            case 22:// char 32字节
            case 24:// char 32字节
                data = ByteConverterUtil.byte2string(dataBytes);
                break;
            case 9:// char 32字节
            case 10:// char 32字节
                data = ByteConverterUtil.byte2unicode(dataBytes);
                break;
            case 4:// char 128字节
            case 21:// char 128字节
            case 23:// char 128字节
                data = ByteConverterUtil.byte2unicode(dataBytes);
                break;
            case 35:// d[2*n] k*2*n字节
                int arrlen = dataBytes.length / 4;
                int offset = 0, tstep = 0, qstep = 0;
                Integer[] tbyteArr = new Integer[arrlen / 2];
                Integer[] qbyteArr = new Integer[arrlen / 2];
                byte[] destBytes = new byte[4];
                for (int n = 0; n < arrlen; n++) {
                    System.arraycopy(dataBytes, offset * destBytes.length, destBytes, 0, destBytes.length);
                    if (n % 2 == 0) {
                        tbyteArr[tstep] = ByteConverterUtil.byte2int(destBytes);
                        tstep++;
                    } else {
                        qbyteArr[qstep] = ByteConverterUtil.byte2int(destBytes);
                        qstep++;
                    }
                    offset++;
                }
                data = JSON.toJSONString(new HashMap() {
                    {
                        put("t", tbyteArr);
                        put("q", qbyteArr);
                    }
                });
                break;
            case 16:// 自定义 220字节
            case 34:// 自定义 152字节
            case 36:// - 32字节
                data = ByteConverterUtil.byte2string(dataBytes);
                break;
            default:
        }
        return data;
    }

    /**
     * 江苏_超声_相位图数据
     * 2.数据格式要求。
     * ---------------------------------------------------------------------------------------
     * |    序号  |  数据类型   |  长度        |  备注
     * ---------------------------------------------------------------------------------------
     * |	1	|	int32	|	4字节	|	文件长度L	-	文件长度，含CRC校验。		文件长度，含CRC校验。
     * |	2	|	uint8	|	4字节	|	规范版本号	-	所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。建议：版本号采用“内部主版本号.内部子版本号.省公司编码.国家单位编码”的形式，以便于版本维护。		所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。建议：版本号采用“内部主版本号.内部子版本号.省公司编码.国家单位编码”的形式，以便于版本维护。
     * |	3	|	int64	|	8字节	|	文件生成时间	-	生成文件的时间，格式为YYYYMMDDhhmmssfff，例如20100818151010001。		生成文件的时间，格式为YYYYMMDDhhmmssfff，例如20100818151010001。
     * |	4	|	char	|	128字节	|	站点名称	-	使用UNICODE编码。以0x0000结尾，例如：110kV枫泾变电站。		使用UNICODE编码。以0x0000结尾，例如：110kV枫泾变电站。
     * |	5	|	char	|	32字节	|	站点编码	-	使用ASCII编码。以\0结尾，例如：A12300000000000000。		使用ASCII编码。以\0结尾，例如：A12300000000000000。
     * |	6	|	uint8	|	1字节	|	天气	-	表示天气。0xFF：未记录0x01：晴0x02：阴0x03：雨0x04：雪0x05：雾0x06：雷雨0x07：多云		表示天气。0xFF：未记录0x01：晴0x02：阴0x03：雨0x04：雪0x05：雾0x06：雷雨0x07：多云
     * |	7	|	float	|	4字节	|	温度	-	环境温度，单位摄氏度。		环境温度，单位摄氏度。
     * |	8	|	int8	|	1字节	|	湿度	-	环境湿度，单位%。		环境湿度，单位%。
     * |	9	|	char	|	32字节	|	仪器厂家	-	使用UNICODE编码。以0x0000结尾，例如：HuaCheng。		使用UNICODE编码。以0x0000结尾，例如：HuaCheng。
     * |	10	|	char	|	32字节	|	仪器型号	-	使用UNICODE编码。以0x0000结尾，例如：PDS-T95。		使用UNICODE编码。以0x0000结尾，例如：PDS-T95。
     * |	11	|	uint8	|	4字节	|	仪器版本号	-	所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。		所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。
     * |	12	|	char	|	32字节	|	仪器序列号	-	即仪器出厂编号，使用ASCII编码。以\0结尾，例如：S12300000000000000。		即仪器出厂编号，使用ASCII编码。以\0结尾，例如：S12300000000000000。
     * |	13	|	float	|	4字节	|	系统频率	-	被测设备电压频率，单位Hz。例如50Hz。		被测设备电压频率，单位Hz。例如50Hz。
     * |	14	|	int16	|	2字节	|	图谱数量N	-	文件中包含的图谱数量。		文件中包含的图谱数量。
     * |	15	|	float	|	4字节	|	同步频率	-	采集装置的同步频率，单位Hz。		采集装置的同步频率，单位Hz。
     * |	16	|	自定义	|	220字节	|	预留	-	预留为厂家自定义可选字段。		预留为厂家自定义可选字段。
     * |	17	|	uint8	|	1字节	|	图谱类型编码	-	标识该文件的图谱类型。参见图谱类型编码表。		标识该文件的图谱类型。参见图谱类型编码表。
     * |	18	|	int32	|	4字节	|	图谱数据长度	-	图谱总长度，指从图谱类型编码到图谱数据结束的长度。		图谱总长度，指从图谱类型编码到图谱数据结束的长度。
     * |	19	|	int64	|	8字节	|	图谱生成时间	-	生成图谱的时间，格式为YYYYMMDDhhmmssfff，例如20100818151010001。		生成图谱的时间，格式为YYYYMMDDhhmmssfff，例如20100818151010001。
     * |	20	|	uint8	|	1字节	|	图谱性质	-	0xFF：未记录0x01：检测图谱0x02：背景噪声0x03：检测图谱及背景噪声		0xFF：未记录0x01：检测图谱0x02：背景噪声0x03：检测图谱及背景噪声
     * |	21	|	char	|	128字节	|	电力设备名称	-	使用UNICODE编码。以0x0000结尾，例如：10kV万达线#3开关柜。		使用UNICODE编码。以0x0000结尾，例如：10kV万达线#3开关柜。
     * |	22	|	char	|	32字节	|	电力设备编码	-	使用ASCII编码。以\0结尾，例如：B12300000000000000。		使用ASCII编码。以\0结尾，例如：B12300000000000000。
     * |	23	|	char	|	128字节	|	测点名称	-	使用UNICODE编码。以0x0000结尾，例如：电缆仓。		使用UNICODE编码。以0x0000结尾，例如：电缆仓。
     * |	24	|	char	|	32字节	|	测点编码	-	使用ASCII编码。以\0结尾，例如：C12300000000000000。		使用ASCII编码。以\0结尾，例如：C12300000000000000。
     * |	25	|	int16	|	2字节	|	检测通道标志	-	仪器的检测通道标识，例如：1。		仪器的检测通道标识，例如：1。
     * |	26	|	uint8	|	1字节	|	存储数据类型t	-	表示图谱数据的存储数据类型。参见存储类型编码表。		表示图谱数据的存储数据类型。参见存储类型编码表。
     * |	27	|	uint8	|	1字节	|	幅值单位	-	表示幅值的单位。参见幅值单位表。		表示幅值的单位。参见幅值单位表。
     * |	28	|	float	|	4字节	|	幅值下限	-	仪器所能检测到的信号幅值的下限。		仪器所能检测到的信号幅值的下限。
     * |	29	|	float	|	4字节	|	幅值上限	-	仪器所能检测到的信号幅值的上限。		仪器所能检测到的信号幅值的上限。
     * |	30	|	uint8	|	1字节	|	超声传感器类型	-	0xFF：未记录0x01：空声0x02：SF6气体绝缘电力设备检测用接触式0x03：充油电力设备检测用接触式0x04：其他类型		0xFF：未记录0x01：空声0x02：SF6气体绝缘电力设备检测用接触式0x03：充油电力设备检测用接触式0x04：其他类型
     * |	31	|	int32	|	4字节	|	数据点数n	-	数据点个数，默认为1000。		表示脉冲间隔时间的单位。0x01：微秒0x02：毫秒0x03：秒微秒:	0x01毫秒:	0x02秒:	0x03
     * |	32	|	uint8	|	8字节	|	放电类型概率	-	表示仪器诊断结果的放电类型概率。参见放电类型概率定义。		数据点个数，默认为1000。
     * |	33	|	自定义	|	153字节	|	预留	-	预留为厂家自定义可选字段。		表示仪器诊断结果的放电类型概率。参见放电类型概率定义。
     * |	34	|	d[2*n]	|	k*2*n字节	|	超声相位图数据	-	根据存储数据类型t获取数据的存储方式。实例1：t是0x02，d表示uint8数组，k=1；实例2：t是0x04，d表示int32数组，k=4；实例3：t是0x06，d表示float数组，k=4。依次存储每个数据点的相位Phase和幅值Q，存储顺序：	Phase[0]，Q[0]；Phase[1]，Q[1]；……；Phase[n-1]，Q[n-1]。相位值范围[0~360)，单位为°。	预留为厂家自定义可选字段。
     * |	35	|	-	|	32字节	|	预留	-	预留		根据存储数据类型t获取数据的存储方式。实例1：t是0x02，d表示uint8数组，k=1；实例2：t是0x04，d表示int32数组，k=4；实例3：t是0x06，d表示float数组，k=4。依次存储每个数据点的飞行时间T和信号幅值Q，存储顺序：T[0]，Q[0]；T[1]，Q[1]；……；T[n-1]，Q[n-1]。
     * |	36	|	int32	|	4字节	|	CRC32	-	数据校验，使用CRC32算法		预留
     * ---------------------------------------------------------------------------------------
     */
    public Object AA_TJ(int i, byte[] dataBytes) throws Exception {
        Object data = null;
        switch (i + 1) {
            case 6:// uint8 1字节
            case 8:// int8 1字节
            case 17:// uint8 1字节
            case 20:// uint8 1字节
            case 26:// uint8 1字节
            case 27:// uint8 1字节
            case 30:// uint8 1字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 14:// int16 2字节
            case 25:// int16 2字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 2:// uint8 4字节
            case 11:// uint8 4字节
                data = JSON.toJSONString(new Integer[] {ByteConverterUtil.byte2int(new byte[] {dataBytes[0]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[1]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[2]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[3]})});
                break;
            case 1:// int32 4字节
            case 18:// int32 4字节
            case 31:// int32 4字节
            case 36:// int32 4字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 7:// float 4字节
            case 13:// float 4字节
            case 15:// float 4字节
            case 28:// float 4字节
            case 29:// float 4字节
                data = ByteConverterUtil.byte2float(dataBytes);
                break;
            case 32:// uint8 8字节
                data = JSON.toJSONString(new Integer[] {ByteConverterUtil.byte2int(new byte[] {dataBytes[0]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[1]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[2]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[3]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[4]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[5]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[6]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[7]})});
                break;
            case 3:// int64 8字节
            case 19:// int64 8字节
                data = ByteConverterUtil.byte2long(dataBytes);
                break;
            case 5:// char 32字节
            case 12:// char 32字节
            case 22:// char 32字节
            case 24:// char 32字节
                data = ByteConverterUtil.byte2string(dataBytes);
                break;
            case 9:// char 32字节
            case 10:// char 32字节
                data = ByteConverterUtil.byte2unicode(dataBytes);
                break;
            case 4:// char 128字节
            case 21:// char 128字节
            case 23:// char 128字节
                data = ByteConverterUtil.byte2unicode(dataBytes);
                break;
            case 34:// d[2*n] k*2*n字节
                int offset = 0, xlen = 50, ylen = dataBytes.length / 4 / xlen;
                Float[][] byteArr = new Float[xlen][ylen];
                byte[] destBytes = new byte[4];
                for (int x = 0; x < xlen; x++) {
                    for (int y = 0; y < ylen; y++) {
                        System.arraycopy(dataBytes, offset * destBytes.length, destBytes, 0, destBytes.length);
                        byteArr[x][y] = ByteConverterUtil.byte2float(destBytes);
                        offset++;
                    }
                }
                data = JSON.toJSONString(byteArr);
                break;
            case 16:// 自定义 220字节
            case 33:// 自定义 153字节
            case 35:// - 32字节
                data = ByteConverterUtil.byte2string(dataBytes);
                break;
            default:
        }
        return data;
    }

    /**
     * 江苏_超声_相位图数据(噪声)
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
     * |	30	|	uint8	|	1字节	|	超声传感器类型	-	0xFF：未记录0x01：空声0x02：SF6气体绝缘电力设备检测用接触式0x03：充油电力设备检测用接触式0x04：其他类型
     * |	31	|	int32	|	4字节	|	数据点数n	-	数据点个数，默认为1000。
     * |	32	|	uint8	|	8字节	|	放电类型概率	-	表示仪器诊断结果的放电类型概率。参见放电类型概率定义。
     * |	33	|	自定义	|	153字节	|	预留	-	预留为厂家自定义可选字段。
     * |	34	|	d[2*n]	|	k*2*n字节	|	超声相位图数据	-	根据存储数据类型t获取数据的存储方式。实例1：t是0x02，d表示uint8数组，k=1；实例2：t是0x04，d表示int32数组，k=4；实例3：t是0x06，d表示float数组，k=4。依次存储每个数据点的相位Phase和幅值Q，存储顺序：	Phase[0]，Q[0]；Phase[1]，Q[1]；……；Phase[n-1]，Q[n-1]。相位值范围[0~360)，单位为°。
     * |	35	|	-	|	32字节	|	预留	-	预留
     * |	36	|	int32	|	4字节	|	CRC32	-	数据校验，使用CRC32算法
     * ---------------------------------------------------------------------------------------
     */
    public Object AA_TJZS(int i, byte[] dataBytes) throws Exception {
        Object data = null;
        switch (i + 1) {
            case 6:// uint8 1字节
            case 8:// int8 1字节
            case 17:// uint8 1字节
            case 20:// uint8 1字节
            case 26:// uint8 1字节
            case 27:// uint8 1字节
            case 30:// uint8 1字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 14:// int16 2字节
            case 25:// int16 2字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 2:// uint8 4字节
            case 11:// uint8 4字节
                data = JSON.toJSONString(new Integer[] {ByteConverterUtil.byte2int(new byte[] {dataBytes[0]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[1]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[2]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[3]})});
                break;
            case 1:// int32 4字节
            case 18:// int32 4字节
            case 31:// int32 4字节
            case 36:// int32 4字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 7:// float 4字节
            case 13:// float 4字节
            case 15:// float 4字节
            case 28:// float 4字节
            case 29:// float 4字节
                data = ByteConverterUtil.byte2float(dataBytes);
                break;
            case 32:// uint8 8字节
                data = JSON.toJSONString(new Integer[] {ByteConverterUtil.byte2int(new byte[] {dataBytes[0]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[1]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[2]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[3]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[4]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[5]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[6]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[7]})});
                break;
            case 3:// int64 8字节
            case 19:// int64 8字节
                data = ByteConverterUtil.byte2long(dataBytes);
                break;
            case 5:// char 32字节
            case 12:// char 32字节
            case 22:// char 32字节
            case 24:// char 32字节
                data = ByteConverterUtil.byte2string(dataBytes);
                break;
            case 9:// char 32字节
            case 10:// char 32字节
                data = ByteConverterUtil.byte2unicode(dataBytes);
                break;
            case 4:// char 128字节
            case 21:// char 128字节
            case 23:// char 128字节
                data = ByteConverterUtil.byte2unicode(dataBytes);
                break;
            case 34:// d[2*n] k*2*n字节
                int offset = 0, xlen = 50, ylen = dataBytes.length / 4 / xlen;
                Float[][] byteArr = new Float[xlen][ylen];
                byte[] destBytes = new byte[4];
                for (int x = 0; x < xlen; x++) {
                    for (int y = 0; y < ylen; y++) {
                        System.arraycopy(dataBytes, offset * destBytes.length, destBytes, 0, destBytes.length);
                        byteArr[x][y] = ByteConverterUtil.byte2float(destBytes);
                        offset++;
                    }
                }
                data = JSON.toJSONString(byteArr);
                break;
            case 16:// 自定义 220字节
            case 33:// 自定义 153字节
            case 35:// - 32字节
                data = ByteConverterUtil.byte2string(dataBytes);
                break;
            default:
        }
        return data;
    }

    /**
     * 江苏_超声_特征图数据
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
     * |	30	|	uint8	|	1字节	|	超声传感器类型	-	0xFF：未记录0x01：空声0x02：SF6气体绝缘电力设备检测用接触式0x03：充油电力设备检测用接触式0x04：其他类型
     * |	31	|	int32	|	4字节	|	数据点数n	-	数据点个数，默认为8；前4个测量值，后4个背景值。
     * |	32	|	float	|	4字节	|	同步频率	-	采集装置的同步频率，单位Hz。
     * |	33	|	uint8	|	8字节	|	放电类型概率	-	表示仪器诊断结果的放电类型概率。参见放电类型概率定义。
     * |	34	|	float	|	4字节	|	信号最大值	-	信号的最大值。
     * |	35	|	float	|	4字节	|	信号有效值	-	信号的均方根值。
     * |	36	|	float	|	4字节	|	频率分量1相关性	-	超声信号中被测系统频率分量的幅值。
     * |	37	|	float	|	4字节	|	频率分量2相关性	-	超声信号中两倍被测系统频率分量的幅值。
     * |	38	|	float	|	4字节	|	背景信号最大值	-	背景信号的最大值。
     * |	39	|	float	|	4字节	|	背景信号有效值	-	背景信号的均方根值。
     * |	40	|	float	|	4字节	|	背景频率分量1相关性	-	背景超声信号中被测系统频率分量的幅值。
     * |	41	|	float	|	4字节	|	背景频率分量2相关性	-	背景超声信号中两倍被测系统频率分量的幅值。
     * |	42	|	自定义	|	117字节	|	预留	-	预留为厂家自定义可选字段。
     * |	43	|	-	|	32字节	|	预留	-	预留
     * |	44	|	int32	|	4字节	|	CRC32	-	数据校验，使用CRC32算法
     * ---------------------------------------------------------------------------------------
     */
    public Object AA_XJ(int i, byte[] dataBytes) throws Exception {
        Object data = null;
        switch (i + 1) {
            case 6:// uint8 1字节
            case 8:// int8 1字节
            case 17:// uint8 1字节
            case 20:// uint8 1字节
            case 26:// uint8 1字节
            case 27:// uint8 1字节
            case 30:// uint8 1字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 14:// int16 2字节
            case 25:// int16 2字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 2:// uint8 4字节
            case 11:// uint8 4字节
                data = JSON.toJSONString(new Integer[] {ByteConverterUtil.byte2int(new byte[] {dataBytes[0]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[1]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[2]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[3]})});
                break;
            case 1:// int32 4字节
            case 18:// int32 4字节
            case 31:// int32 4字节
            case 44:// int32 4字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 7:// float 4字节
            case 13:// float 4字节
            case 15:// float 4字节
            case 28:// float 4字节
            case 29:// float 4字节
            case 32:// float 4字节
            case 34:// float 4字节
            case 35:// float 4字节
            case 36:// float 4字节
            case 37:// float 4字节
            case 38:// float 4字节
            case 39:// float 4字节
            case 40:// float 4字节
            case 41:// float 4字节
                data = ByteConverterUtil.byte2float(dataBytes);
                break;
            case 33:// uint8 8字节
                data = JSON.toJSONString(new Integer[] {ByteConverterUtil.byte2int(new byte[] {dataBytes[0]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[1]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[2]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[3]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[4]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[5]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[6]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[7]})});
                break;
            case 3:// int64 8字节
            case 19:// int64 8字节
                data = ByteConverterUtil.byte2long(dataBytes);
                break;
            case 5:// char 32字节
            case 12:// char 32字节
            case 22:// char 32字节
            case 24:// char 32字节
                data = ByteConverterUtil.byte2string(dataBytes);
                break;
            case 9:// char 32字节
            case 10:// char 32字节
                data = ByteConverterUtil.byte2unicode(dataBytes);
                break;
            case 4:// char 128字节
            case 21:// char 128字节
            case 23:// char 128字节
                data = ByteConverterUtil.byte2unicode(dataBytes);
                break;
            case 16:// 自定义 220字节
            case 42:// 自定义 117字节
            case 43:// - 32字节
                data = ByteConverterUtil.byte2string(dataBytes);
                break;
            default:
        }
        return data;
    }

    /**
     * 江苏_超声_特征图数据(噪声)
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
     * |	30	|	uint8	|	1字节	|	超声传感器类型	-	0xFF：未记录0x01：空声0x02：SF6气体绝缘电力设备检测用接触式0x03：充油电力设备检测用接触式0x04：其他类型
     * |	31	|	int32	|	4字节	|	数据点数n	-	数据点个数，默认为8；前4个测量值，后4个背景值。
     * |	32	|	float	|	4字节	|	同步频率	-	采集装置的同步频率，单位Hz。
     * |	33	|	uint8	|	8字节	|	放电类型概率	-	表示仪器诊断结果的放电类型概率。参见放电类型概率定义。
     * |	34	|	float	|	4字节	|	信号最大值	-	信号的最大值。
     * |	35	|	float	|	4字节	|	信号有效值	-	信号的均方根值。
     * |	36	|	float	|	4字节	|	频率分量1相关性	-	超声信号中被测系统频率分量的幅值。
     * |	37	|	float	|	4字节	|	频率分量2相关性	-	超声信号中两倍被测系统频率分量的幅值。
     * |	38	|	float	|	4字节	|	背景信号最大值	-	背景信号的最大值。
     * |	39	|	float	|	4字节	|	背景信号有效值	-	背景信号的均方根值。
     * |	40	|	float	|	4字节	|	背景频率分量1相关性	-	背景超声信号中被测系统频率分量的幅值。
     * |	41	|	float	|	4字节	|	背景频率分量2相关性	-	背景超声信号中两倍被测系统频率分量的幅值。
     * |	42	|	自定义	|	117字节	|	预留	-	预留为厂家自定义可选字段。
     * |	43	|	-	|	32字节	|	预留	-	预留
     * |	44	|	int32	|	4字节	|	CRC32	-	数据校验，使用CRC32算法
     * ---------------------------------------------------------------------------------------
     */
    public Object AA_XJZS(int i, byte[] dataBytes) throws Exception {
        Object data = null;
        switch (i + 1) {
            case 6:// uint8 1字节
            case 8:// int8 1字节
            case 17:// uint8 1字节
            case 20:// uint8 1字节
            case 26:// uint8 1字节
            case 27:// uint8 1字节
            case 30:// uint8 1字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 14:// int16 2字节
            case 25:// int16 2字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 2:// uint8 4字节
            case 11:// uint8 4字节
                data = JSON.toJSONString(new Integer[] {ByteConverterUtil.byte2int(new byte[] {dataBytes[0]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[1]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[2]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[3]})});
                break;
            case 1:// int32 4字节
            case 18:// int32 4字节
            case 31:// int32 4字节
            case 44:// int32 4字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 7:// float 4字节
            case 13:// float 4字节
            case 15:// float 4字节
            case 28:// float 4字节
            case 29:// float 4字节
            case 32:// float 4字节
            case 34:// float 4字节
            case 35:// float 4字节
            case 36:// float 4字节
            case 37:// float 4字节
            case 38:// float 4字节
            case 39:// float 4字节
            case 40:// float 4字节
            case 41:// float 4字节
                data = ByteConverterUtil.byte2float(dataBytes);
                break;
            case 33:// uint8 8字节
                data = JSON.toJSONString(new Integer[] {ByteConverterUtil.byte2int(new byte[] {dataBytes[0]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[1]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[2]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[3]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[4]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[5]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[6]}),
                        ByteConverterUtil.byte2int(new byte[] {dataBytes[7]})});
                break;
            case 3:// int64 8字节
            case 19:// int64 8字节
                data = ByteConverterUtil.byte2long(dataBytes);
                break;
            case 5:// char 32字节
            case 12:// char 32字节
            case 22:// char 32字节
            case 24:// char 32字节
                data = ByteConverterUtil.byte2string(dataBytes);
                break;
            case 9:// char 32字节
            case 10:// char 32字节
                data = ByteConverterUtil.byte2unicode(dataBytes);
                break;
            case 4:// char 128字节
            case 21:// char 128字节
            case 23:// char 128字节
                data = ByteConverterUtil.byte2unicode(dataBytes);
                break;
            case 16:// 自定义 220字节
            case 42:// 自定义 117字节
            case 43:// - 32字节
                data = ByteConverterUtil.byte2string(dataBytes);
                break;
            default:
        }
        return data;
    }
}
