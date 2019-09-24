package com.redphase.service.analyze.area;

import com.alibaba.fastjson.JSON;
import com.redphase.framework.util.ByteConverterUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 地区-江苏数据
 */
@Slf4j
public class JSAnalyzeHFCTData {
    /**
     * 江苏_高频电流_PRPD图和PRPS图数据
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
     * |	30	|	uint8	|	1字节	|	频带	-	表示滤波类型0xFF:未记录0x01:全通0x02:低通0x03:高通0x04:带通0x05:带阻0x06:可扩充
     * |	31	|	float	|	4字节	|	下限频率	-	表示检测仪器的下限频率，单位Hz。
     * |	32	|	float	|	4字节	|	上限频率	-	表示检测仪器的上限频率，单位Hz。
     * |	33	|	int32	|	4字节	|	相位窗数m	-	工频周期被等分成m个相位窗，每个相位窗跨360/m度。
     * |	34	|	int32	|	4字节	|	量化幅值n	-	幅值范围的等分区间数。如果该文件是PRPS图谱，则该4个字节清零，赋值为：0x00000000。
     * |	35	|	int32	|	4字节	|	工频周期数p	-	图谱工频周期的个数。如果该文件是PRPD图谱，则该4个字节清零，赋值为：0x00000000。
     * |	36	|	uint8	|	8字节	|	放电类型概率	-	表示仪器诊断结果的放电类型概率。参见放电类型概率定义。
     * |	37	|	自定义	|	137字节	|	预留	-	预留为厂家自定义可选字段。
     * |	38	|	d[m][n]或d[p][m]	|	k*m*n字节或k*p*m	|	字节	局部放电图谱数据	-	根据存储数据类型t获取数据的存储方式。实例1：t是0x02，d表示uint8数组，k=1；实例2：t是0x04，d表示int32数组，k=4；实例3：t是0x06，d表示float数组，k=4。如果该文件是PRPD图谱，则为d[m][n]，m为相位窗数，n为量化幅值，数组元素[x][y]的值表示在对应第x相位窗和第y幅值处发生的脉冲信号的次数。如果该文件是PRPS图谱，则为d[p][m]，p为工频周期数，m为相位窗数，数组元素[x][y]的值表示在对应第x个周期的第y相位窗处发生的脉冲信号的幅值。
     * |	39	|	自定义	|	32字节	|	预留	-	预留
     * |	40	|	int32	|	4字节	|	CRC32	-	数据校验，使用CRC32算法
     * ---------------------------------------------------------------------------------------
     */
    public Object HFCT_TJ(int i, byte[] dataBytes) throws Exception {
        Object data = null;
        switch (i + 1) {
            case 6://	uint8	1字节
            case 8://	int8	1字节
            case 17://	uint8	1字节
            case 20://	uint8	1字节
            case 26://	uint8	1字节
            case 27://	uint8	1字节
            case 30://	uint8	1字节
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
            case 36://	uint8	8字节
                data = JSON.toJSONString(new Integer[]{
                        ByteConverterUtil.byte2int(new byte[]{dataBytes[0]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[1]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[2]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[3]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[4]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[5]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[6]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[7]})
                });
                break;
            case 14://	int16	2字节
            case 25://	int16	2字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 1://	int32	4字节
            case 18://	int32	4字节
            case 33://	int32	4字节
            case 34://	int32	4字节
            case 35://	int32	4字节
            case 40://	int32	4字节
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
            case 31://	float	4字节
            case 32://	float	4字节
                data = ByteConverterUtil.byte2float(dataBytes);
                break;
            case 38://	d[m][n]或d[p][m]	k*m*n字节或k*p*m
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
            case 37://	自定义	137字节
            case 39://	自定义	32字节
                data = ByteConverterUtil.byte2string(dataBytes);
                break;
            default:
        }
        return data;
    }

    /**
     * 江苏_高频电流_PRPD图和PRPS图数据(噪声)
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
     * |	30	|	uint8	|	1字节	|	频带	-	表示滤波类型0xFF:未记录0x01:全通0x02:低通0x03:高通0x04:带通0x05:带阻0x06:可扩充
     * |	31	|	float	|	4字节	|	下限频率	-	表示检测仪器的下限频率，单位Hz。
     * |	32	|	float	|	4字节	|	上限频率	-	表示检测仪器的上限频率，单位Hz。
     * |	33	|	int32	|	4字节	|	相位窗数m	-	工频周期被等分成m个相位窗，每个相位窗跨360/m度。
     * |	34	|	int32	|	4字节	|	量化幅值n	-	幅值范围的等分区间数。如果该文件是PRPS图谱，则该4个字节清零，赋值为：0x00000000。
     * |	35	|	int32	|	4字节	|	工频周期数p	-	图谱工频周期的个数。如果该文件是PRPD图谱，则该4个字节清零，赋值为：0x00000000。
     * |	36	|	uint8	|	8字节	|	放电类型概率	-	表示仪器诊断结果的放电类型概率。参见放电类型概率定义。
     * |	37	|	自定义	|	137字节	|	预留	-	预留为厂家自定义可选字段。
     * |	38	|	d[m][n]或d[p][m]	|	k*m*n字节或k*p*m	|	字节	局部放电图谱数据	-	根据存储数据类型t获取数据的存储方式。实例1：t是0x02，d表示uint8数组，k=1；实例2：t是0x04，d表示int32数组，k=4；实例3：t是0x06，d表示float数组，k=4。如果该文件是PRPD图谱，则为d[m][n]，m为相位窗数，n为量化幅值，数组元素[x][y]的值表示在对应第x相位窗和第y幅值处发生的脉冲信号的次数。如果该文件是PRPS图谱，则为d[p][m]，p为工频周期数，m为相位窗数，数组元素[x][y]的值表示在对应第x个周期的第y相位窗处发生的脉冲信号的幅值。
     * |	39	|	自定义	|	32字节	|	预留	-	预留
     * |	40	|	int32	|	4字节	|	CRC32	-	数据校验，使用CRC32算法
     * ---------------------------------------------------------------------------------------
     */
    public Object HFCT_TJZS(int i, byte[] dataBytes) throws Exception {
        Object data = null;
        switch (i + 1) {
            case 6://	uint8	1字节
            case 8://	int8	1字节
            case 17://	uint8	1字节
            case 20://	uint8	1字节
            case 26://	uint8	1字节
            case 27://	uint8	1字节
            case 30://	uint8	1字节
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
            case 36://	uint8	8字节
                data = JSON.toJSONString(new Integer[]{
                        ByteConverterUtil.byte2int(new byte[]{dataBytes[0]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[1]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[2]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[3]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[4]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[5]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[6]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[7]})
                });
                break;
            case 14://	int16	2字节
            case 25://	int16	2字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 1://	int32	4字节
            case 18://	int32	4字节
            case 33://	int32	4字节
            case 34://	int32	4字节
            case 35://	int32	4字节
            case 40://	int32	4字节
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
            case 31://	float	4字节
            case 32://	float	4字节
                data = ByteConverterUtil.byte2float(dataBytes);
                break;
            case 38://	d[m][n]或d[p][m]	k*m*n字节或k*p*m
                int offset = 0, ylen = 60, xlen = dataBytes.length / 4 / ylen;
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
            case 37://	自定义	137字节
            case 39://	自定义	32字节
                data = ByteConverterUtil.byte2string(dataBytes);
                break;
            default:
        }
        return data;
    }
}

