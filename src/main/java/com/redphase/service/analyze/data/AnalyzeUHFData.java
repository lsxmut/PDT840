package com.redphase.service.analyze.data;

import com.alibaba.fastjson.JSON;
import com.redphase.framework.util.ByteConverterUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 特高频（UHF）数据
 */
@Slf4j
public class AnalyzeUHFData {
    /**
     * 特高频统计模式1噪声数据
     * 2.数据格式要求。
     * ---------------------------------------------------------------------------------------
     * | 序号 |  数据类型    |  长度   |  备注
     * ---------------------------------------------------------------------------------------
     * |    1   |   int     |  4字节   | 数据长度 数据项1到12的字节长度。
     * |    2   |   float   |  4字节   | 规范版本号 所使用的数据格式规范版本号，例如1.0。
     * |    3   |   char    |  1字节   | 图谱类型编码 标识该文件是PRPD图谱还是PRPS图谱。 PRPS: 0x01
     * |    4   |   int     |  4字节   | 相位窗数m 工频周期被等分成m个相位窗，每个相位窗跨 360/m 度。 m = dataBytes.length / 4 / xlen（暂定）
     * |    5   |   int     |  4字节   | 量化幅值n 赋值为：0x00000000。
     * |    6   |   char    |  1字节   | 幅值单位 表示幅值的单位 dBm: 0x01 mV: 0x02 %: 0x03
     * |    7   |   float   |  4字节   | 幅值下限 仪器所能检测到的放电信号幅值的下限。暂定：-75
     * |    8   |   float   |  4字节   | 幅值上限 仪器所能检测到的放电信号幅值的上限。暂定：-10
     * |    9   |   int     |  4字节   | 工频周期数p 图谱工频周期的个数。 工频周期数=50.
     * |   10   |  char[8]  |  8字节   | 放电类型概率 表示仪器诊断结果的放电类型概率。数组元素[0]至[7]分别存储正常、尖端放电、悬浮放电、沿面放电、内部放电、颗粒放电、外部干扰和其它共8种情况的概率，每个元素可以是整数0至100中的某数。实例：数组元素[3]为69，表示沿面放电的概率为69%。 如果诊断结果为正常，则为数组元素[0]赋值100，数组元素[1]至[7]赋值0。 先按照以下规则处理 如果仪器不具备放电类型诊断功能，则为数组元素[0]至[7]赋值0x00。
     * |   11   |float[p][m]|4*50*120字节| 局部放电图谱数据 该文件是PRPS图谱，则为float[p][m]，p为工频周期数——50，m为相位窗数——120，数组元素[x][y]的值表示在对应第x个周期的第y相位窗处发生的放电的幅值。
     * |   12   |   int     |  4字节   | CRC32 数据校验，使用CRC32算法。
     * |   13   |   int     |  4字节   | 数据长度 数据项13到校验和的字节长度。
     * |   14   |   int64   |  8字节   | 图谱生成时间 生成图谱的时间，格式为YYYYMMDDhhmmssfff，例如20100818151010001。该时间与文件名的时间相同，只是文件名中的时间到秒，这边的到毫秒。
     * |   15   |   uint8   |  1字节   | 检测技术类型编码 标识该文件是采用什么技术获得。 TEV: 0x01 AA: 0x02 AE: 0x03 HFCT: 0x04 UHF: 0x05
     * |   16   |   float   |  4字节   | 幅值 表示统计模式下当前1秒的幅值
     * |   17   |   int32   |  4字节   | 放电频度，即脉冲个数 表示统计模式下当前1秒的脉冲个数
     * |   18   |   float   |  4字节   | 参考阈值
     * |   19   |   float   |  4字节   | 相位
     * |   20   |   Char    |  1字节   | 同步类型 外同步: 0x01 内同步: 0x02
     * |   21   |   float   |  4字节   | 同步频率
     * |   22   |   float   |  4字节   | 系统频率
     * |   23   |   float   |  4字节   | 警戒值1 如-60
     * |   24   |   float   |  4字节   | 警戒值2 如-40
     * |   25   |   float   |  4字节   | 警戒值3 如-20
     * |   26   |   uint8   |  1字节   | 频带
     * |   27   |   float   |  4字节   | 下限频率
     * |   28   |   float   |  4字节   | 上限频率
     * |   29   |   char    |  32字节  | 仪器厂家 从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：RedPhase。
     * |   30   |   char    |  32字节  | 仪器型号 从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：PDT-400。
     * |   31   |   uint8   |  4字节   | 仪器版本号 从系统设置中提取，所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。 实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。
     * |   32   |   char    |  32字节  | 仪器序列号 从系统设置中提取，即仪器出厂编号，使用ASCII编码。以\0结尾，例如：S12300000000000000。
     * |   33   |   int     |  4字节   | 检验和 13到校验和前一项的数据校验。
     * ---------------------------------------------------------------------------------------
     */
    public Object TJZS_1(int i, byte[] dataBytes) throws Exception {
        Object data = null;
        switch (i + 1) {
            case 3:// char 1字节
            case 6:// char 1字节
            case 15:// uint8 1字节
            case 20:// Char 1字节
            case 26:// uint8 1字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 14:// int64 8字节
                data = ByteConverterUtil.byte2long(dataBytes);
                break;
            case 1:// int 4字节
            case 4:// int 4字节
            case 5:// int 4字节
            case 9:// int 4字节
            case 12:// int 4字节
            case 13:// int 4字节
            case 17:// int32 4字节
            case 33:// int 4字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 10:// char[8] 8字节
                data = JSON.toJSONString(new Integer[]{ByteConverterUtil.byte2int(new byte[]{dataBytes[0]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[1]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[2]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[3]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[4]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[5]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[6]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[7]})}
                );
                break;
            case 11:// float[p][m] 4*50*120 字节
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
            case 2:// float 4字节
            case 7:// float 4字节
            case 8:// float 4字节
            case 16:// float 4字节
            case 18:// float 4字节
            case 19:// float 4字节
            case 21:// float 4字节
            case 22:// float 4字节
            case 23:// float 4字节
            case 24:// float 4字节
            case 25:// float 4字节
            case 27:// float 4字节
            case 28:// float 4字节
                data = ByteConverterUtil.byte2float(dataBytes);
                break;
            case 31:// uint8 4字节
                data = JSON.toJSONString(new Integer[]{ByteConverterUtil.byte2int(new byte[]{dataBytes[0]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[1]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[2]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[3]})});
                break;
            case 29:// char 32字节
            case 30:// char 32字节
            case 32:// char 32字节
                data = ByteConverterUtil.byte2string(dataBytes);
                break;
            default:
        }
        return data;
    }

    /**
     * 特高频统计模式1数据
     * 2.数据格式要求。
     * ---------------------------------------------------------------------------------------
     * | 序号 |  数据类型    |  长度     |  备注
     * ---------------------------------------------------------------------------------------
     * |    1  |   int       |    4字节    | 数据长度 数据项1到12的字节长度。
     * |    2  |   float     |    4字节    | 规范版本号 所使用的数据格式规范版本号，例如1.0。
     * |    3  |   char      |    1字节    | 图谱类型编码 标识该文件是PRPD图谱还是PRPS图谱。 PRPS: 0x01
     * |    4  |   int       |    4字节    | 相位窗数m 工频周期被等分成m个相位窗，每个相位窗跨 360/m 度。 m = dataBytes.length / 4 / xlen（暂定）
     * |    5  |   int       |    4字节    | 量化幅值n 赋值为：0x00000000。
     * |    6  |   char      |    1字节    | 幅值单位 表示幅值的单位 dBm: 0x01 mV: 0x02 %: 0x03
     * |    7  |   float     |    4字节    | 幅值下限 仪器所能检测到的放电信号幅值的下限。暂定：-75
     * |    8  |   float     |    4字节    | 幅值上限 仪器所能检测到的放电信号幅值的上限。暂定：-10
     * |    9  |   int       |    4字节    | 工频周期数p 图谱工频周期的个数。 工频周期数=50.
     * |   10  |   char[8]   |    8字节    | 放电类型概率 表示仪器诊断结果的放电类型概率。数组元素[0]至[7]分别存储正常、尖端放电、悬浮放电、沿面放电、内部放电、颗粒放电、外部干扰和其它共8种情况的概率，每个元素可以是整数0至100中的某数。实例：数组元素[3]为69，表示沿面放电的概率为69%。 如果诊断结果为正常，则为数组元素[0]赋值100，数组元素[1]至[7]赋值0。 先按照以下规则处理 如果仪器不具备放电类型诊断功能，则为数组元素[0]至[7]赋值0x00。
     * |   11  | float[p][m] | 4*50*120字节| 局部放电图谱数据 该文件是PRPS图谱，则为float[p][m]，p为工频周期数——50，m为相位窗数——120，数组元素[x][y]的值表示在对应第x个周期的第y相位窗处发生的放电的幅值。
     * |   12  |   int       |    4字节    | CRC32 数据校验，使用CRC32算法。
     * |   13  |   int       |    4字节    | 数据长度 数据项13到校验和的字节长度。
     * |   14  |   int64     |    8字节    | 图谱生成时间 生成图谱的时间，格式为YYYYMMDDhhmmssfff，例如20100818151010001。该时间与文件名的时间相同，只是文件名中的时间到秒，这边的到毫秒。
     * |   15  |   char      |    1字节    | 检测技术类型编码 标识该文件是采用什么技术获得。 TEV: 0x01 AA: 0x02 AE: 0x03 HFCT: 0x04 UHF: 0x05
     * |   16  |   float     |    4字节    | 幅值 表示统计模式下当前1秒的幅值
     * |   17  |   int32     |    4字节    | 放电频度，即脉冲个数 表示统计模式下当前1秒的脉冲个数
     * |   18  |   float     |    4字节    | 参考阈值
     * |   19  |   float     |    4字节    | 相位
     * |   20  |   Char      |    1字节    | 同步类型 外同步: 0x01 内同步: 0x02
     * |   21  |   float     |    4字节    | 同步频率
     * |   22  |   float     |    4字节    | 系统频率
     * |   23  |   float     |    4字节    | 警戒值1 如-60
     * |   24  |   float     |    4字节    | 警戒值2 如-40
     * |   25  |   float     |    4字节    | 警戒值3 如-20
     * |   26  |   uint8     |    1字节    | 频带
     * |   27  |   float     |    4字节    | 下限频率
     * |   28  |   float     |    4字节    | 上限频率
     * |   29  |   char      |    32字节   | 仪器厂家 从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：RedPhase。
     * |   30  |   char      |    32字节   | 仪器型号 从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：PDT-400。
     * |   31  |   uint8     |    4字节    | 仪器版本号 从系统设置中提取，所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。 实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。
     * |   32  |   char      |    32字节   | 仪器序列号 从系统设置中提取，即仪器出厂编号，使用ASCII编码。以\0结尾，例如：S12300000000000000。
     * |   33  |   int       |    4字节    | 检验和 13到校验和前一项的数据校验。
     * ---------------------------------------------------------------------------------------
     */
    public Object TJ_1(int i, byte[] dataBytes) throws Exception {
        Object data = null;
        switch (i + 1) {
            case 3:// char 1字节
            case 6:// char 1字节
            case 15:// char 1字节
            case 20:// Char 1字节
            case 26:// uint8 1字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 1:// int 4字节
            case 4:// int 4字节
            case 5:// int 4字节
            case 9:// int 4字节
            case 12:// int 4字节
            case 13:// int 4字节
            case 17:// int32 4字节
            case 33:// int 4字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 14:// int64 8字节
                data = ByteConverterUtil.byte2long(dataBytes);
                break;
            case 10:// char[8] 8字节
                data = JSON.toJSONString(new Integer[]{ByteConverterUtil.byte2int(new byte[]{dataBytes[0]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[1]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[2]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[3]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[4]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[5]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[6]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[7]})}
                );
                break;
            case 11:// float[p][m] 4*50*120字节
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
            case 2:// float 4字节
            case 7:// float 4字节
            case 8:// float 4字节
            case 16:// float 4字节
            case 18:// float 4字节
            case 19:// float 4字节
            case 21:// float 4字节
            case 22:// float 4字节
            case 23:// float 4字节
            case 24:// float 4字节
            case 25:// float 4字节
            case 27:// float 4字节
            case 28:// float 4字节
                data = ByteConverterUtil.byte2float(dataBytes);
                break;
            case 31:// uint8 4字节
                data = JSON.toJSONString(new Integer[]{ByteConverterUtil.byte2int(new byte[]{dataBytes[0]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[1]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[2]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[3]})});
                break;
            case 29:// char 32字节
            case 30:// char 32字节
            case 32:// char 32字节
                data = ByteConverterUtil.byte2string(dataBytes);
                break;
            default:
        }
        return data;
    }

    /**
     * 特高频统计模式2录波数据
     * 2.数据格式要求。
     * ---------------------------------------------------------------------------------------
     * |    序号  |  数据类型   |  长度        |  备注
     * ---------------------------------------------------------------------------------------
     * |     1    |    int      |    4字节     |    数据长度 数据项1到校验和的字节长度。
     * |     2    |    float    |    4字节     |    规范版本号 所使用的数据格式规范版本号，例如1.0。
     * |     3    |    int64    |    8字节     |    图谱生成时间 生成图谱的时间，格式为YYYYMMDDhhmmssfff，例如20100818151010001。该时间与文件名的时间相同，只是文件名中的时间到秒，这边的到毫秒。
     * |     4    |    char     |    1字节     |    检测技术类型编码 标识该文件是采用什么技术获得。 TEV: 0x01 AA: 0x02 AE: 0x03 HFCT: 0x04 UHF: 0x05
     * |     5    |    char     |    1字节     |    图谱类型编码 标识该文件是PRPD图谱还是PRPS图谱。 PRPS: 0x01
     * |     6    |    int      |    4字节     |    相位窗数m 工频周期被等分成m个相位窗，每个相位窗跨 360/m 度。 m = dataBytes.length / 4 / xlen（暂定）
     * |     7    |    char     |    1字节     |    幅值单位 表示幅值的单位 dBm: 0x01 mV: 0x02 %: 0x03
     * |     8    |    float    |    4字节     |    幅值下限 仪器所能检测到的放电信号幅值的下限。暂定：-75
     * |     9    |    float    |    4字节     |    幅值上限 仪器所能检测到的放电信号幅值的上限。暂定：-10
     * |    10    |    int      |    4字节     |    工频周期数p 图谱工频周期的个数。 工频周期数=500.
     * |    11    |float[p][m]  |4*500*120 字节|    局部放电图谱数据 该文件是PRPS图谱，则为float[p][m]，p为工频周期数——500（表示10秒，每秒50个），m为相位窗数——120，数组元素[x][y]的值表示在对应第x个周期的第y相位窗处发生的放电的幅值。
     * |    12    |    float    |    4字节     |    参考阈值
     * |    13    |    float    |    4字节     |    相位
     * |    14    |    Char     |    1字节     |    同步类型 外同步: 0x01 内同步: 0x02
     * |    15    |    float    |    4字节     |    同步频率
     * |    16    |    float    |    4字节     |    系统频率
     * |    17    |    float    |    4字节     |    警戒值1 如-60
     * |    18    |    float    |    4字节     |    警戒值2 如-40
     * |    19    |    float    |    4字节     |    警戒值3 如-20
     * |    20    |    uint8    |    1字节     |    频带
     * |    21    |    float    |    4字节     |    下限频率
     * |    22    |    float    |    4字节     |    上限频率
     * |    23    |    char     |    32字节    |    仪器厂家 从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：RedPhase。
     * |    24    |    char     |    32字节    |    仪器型号 从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：PDT-400。
     * |    25    |    uint8    |    4字节     |    仪器版本号 从系统设置中提取，所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。 实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。
     * |    26    |    char     |    32字节    |    仪器序列号 从系统设置中提取，即仪器出厂编号，使用ASCII编码。以\0结尾，例如：S12300000000000000。
     * |    27    |    int      |    4字节     |    检验和 1到校验和前一项的数据校验。
     * ---------------------------------------------------------------------------------------
     */
    public Object TJLB_1(int i, byte[] dataBytes) throws Exception {
        Object data = null;
        switch (i + 1) {
            case 4:// char 1字节
            case 5:// char 1字节
            case 7:// char 1字节
            case 14:// Char 1字节
            case 20:// uint8 1字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 1:// int 4字节
            case 6:// int 4字节
            case 10:// int 4字节
            case 27:// int 4字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 3:// int64 8字节
                data = ByteConverterUtil.byte2long(dataBytes);
                break;
            case 11:// float[p][m] 4*500*120字节
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
            case 2:// float 4字节
            case 8:// float 4字节
            case 9:// float 4字节
            case 12:// float 4字节
            case 13:// float 4字节
            case 15:// float 4字节
            case 16:// float 4字节
            case 17:// float 4字节
            case 18:// float 4字节
            case 19:// float 4字节
            case 21:// float 4字节
            case 22:// float 4字节
                data = ByteConverterUtil.byte2float(dataBytes);
                break;
            case 25:// uint8 4字节
                data = JSON.toJSONString(new Integer[]{ByteConverterUtil.byte2int(new byte[]{dataBytes[0]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[1]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[2]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[3]})});
                break;
            case 23:// char 32字节
            case 24:// char 32字节
            case 26:// char 32字节
                data = ByteConverterUtil.byte2string(dataBytes);
                break;
            default:
        }
        return data;
    }

    /**
     * 特高频统计模式2噪声数据
     * 2.数据格式要求。
     * ---------------------------------------------------------------------------------------
     * | 序号 |  数据类型    |  长度      |  备注
     * ---------------------------------------------------------------------------------------
     * |    1  |   int       |   4字节    | 数据长度 数据项1到12的字节长度。
     * |    2  |   float     |   4字节    | 规范版本号 所使用的数据格式规范版本号，例如1.0。
     * |    3  |   char      |   1字节    | 图谱类型编码 标识该文件是PRPD图谱还是PRPS图谱。 PRPD:0x00
     * |    4  |   int       |   4字节    | 相位窗数m 工频周期被等分成m个相位窗，每个相位窗跨360/m度。 m = dataBytes.length / 4 / xlen
     * |    5  |   int       |   4字节    | 量化幅值n 幅值范围的等分区间数。n=66. -10-（-75）+1=66
     * |    6  |   char      |   1字节    | 幅值单位 表示幅值的单位 dBm: 0x01
     * |    7  |   float     |   4字节    | 幅值下限 仪器所能检测到的放电信号幅值的下限。下限=-75
     * |    8  |   float     |   4字节    | 幅值上限 仪器所能检测到的放电信号幅值的上限。上限=-10
     * |    9  |   int       |   4字节    | 工频周期数p 如果该文件是PRPD图谱，则该4个字节清零，赋值为：0x00000000。
     * |   10  |   char[8]   |   8字节    | 放电类型概率 表示仪器诊断结果的放电类型概率。数组元素[0]至[7]分别存储正常、尖端放电、悬浮放电、沿面放电、内部放电、颗粒放电、外部干扰和其它共8种情况的概率，每个元素可以是整数0至100中的某数。实例：数组元素[3]为69，表示沿面放电的概率为69%。 如果诊断结果为正常，则为数组元素[0]赋值100，数组元素[1]至[7]赋值0。 先按照以下规则处理 如果仪器不具备放电类型诊断功能，则为数组元素[0]至[7]赋值0x00。
     * |   11  |   int[m][n] | 4*50*66字节| 局部放电图谱数据 如果该文件是PRPD图谱，则为int[m][n]，m=50为相位窗数，n=66为量化幅值，数组元素[x][y]的值表示在对应第x相位窗和第y幅值处发生的放电次数。
     * |   12  |   int       |    4字节   | CRC32 数据校验，使用CRC32算法。
     * |   13  |   int       |    4字节   | 数据长度 数据项13到校验和的字节长度。
     * |   14  |   int64     |    8字节   | 图谱生成时间 生成图谱的时间，格式为YYYYMMDDhhmmssfff，例如20100818151010001。该时间与文件名的时间相同，只是文件名中的时间到秒，这边的到毫秒。
     * |   15  |   char      |    1字节   | 检测技术类型编码 标识该文件是采用什么技术获得。 TEV: 0x01 AA: 0x02 AE: 0x03 HFCT: 0x04 UHF: 0x05
     * |   16  |   float     |    4字节   | 幅值 表示统计模式下当前1秒的幅值
     * |   17  |   int32     |    4字节   | 放电频度，即脉冲个数 表示统计模式下当前1秒的脉冲个数
     * |   18  |   float     |    4字节   | 参考阈值
     * |   19  |   float     |    4字节   | 相位
     * |   20  |   Char      |    1字节   | 同步类型 外同步: 0x01 内同步: 0x02
     * |   21  |   float     |    4字节   | 同步频率
     * |   22  |   float     |    4字节   | 系统频率
     * |   23  |   float     |    4字节   | 警戒值1 如-60
     * |   24  |   float     |    4字节   | 警戒值2 如-40
     * |   25  |   float     |    4字节   | 警戒值3 如-20
     * |   26  |   uint8     |    1字节   | 频带
     * |   27  |   float     |    4字节   | 下限频率
     * |   28  |   float     |    4字节   | 上限频率
     * |   29  |   char      |    32字节  | 仪器厂家 从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：RedPhase。
     * |   30  |   char      |    32字节  | 仪器型号 从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：PDT-400。
     * |   31  |   uint8     |    4字节   | 仪器版本号 从系统设置中提取，所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。 实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。
     * |   32  |   char      |    32字节  | 仪器序列号 从系统设置中提取，即仪器出厂编号，使用ASCII编码。以\0结尾，例如：S12300000000000000。
     * |   33  |   int       |    4字节   | 检验和 13到校验和前一项的数据校验。
     * ---------------------------------------------------------------------------------------
     */
    public Object TJZS_2(int i, byte[] dataBytes) throws Exception {
        Object data = null;
        switch (i + 1) {
            case 3:// char 1字节
            case 6:// char 1字节
            case 15:// char 1字节
            case 20:// Char 1字节
            case 26:// uint8 1字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 1:// int 4字节
            case 4:// int 4字节
            case 5:// int 4字节
            case 17:// int32 4字节
            case 9:// int 4字节
            case 12:// int 4字节
            case 13:// int 4字节
            case 33:// int 4字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 14:// int64 8字节
                data = ByteConverterUtil.byte2long(dataBytes);
                break;
            case 10:// char[8] 8字节
                data = JSON.toJSONString(new Integer[]{ByteConverterUtil.byte2int(new byte[]{dataBytes[0]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[1]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[2]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[3]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[4]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[5]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[6]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[7]})}
                );
                break;
            case 11:// int[m][n]  4*50*66字节
                int offset = 0, xlen = 50, ylen = 66;
                Integer[][] byteArr = new Integer[xlen][ylen];
                byte[] destBytes = new byte[4];
                for (int x = 0; x < xlen; x++) {
                    for (int y = 0; y < ylen; y++) {
                        System.arraycopy(dataBytes, offset * destBytes.length, destBytes, 0, destBytes.length);
                        byteArr[x][y] = ByteConverterUtil.byte2int(destBytes);
                        offset++;
                    }
                }
                data = JSON.toJSONString(byteArr);
                break;
            case 2:// float 4字节
            case 7:// float 4字节
            case 8:// float 4字节
            case 16:// float 4字节
            case 18:// float 4字节
            case 19:// float 4字节
            case 21:// float 4字节
            case 22:// float 4字节
            case 23:// float 4字节
            case 24:// float 4字节
            case 25:// float 4字节
            case 27:// float 4字节
            case 28:// float 4字节
                data = ByteConverterUtil.byte2float(dataBytes);
                break;
            case 31:// uint8 4字节
                data = JSON.toJSONString(new Integer[]{ByteConverterUtil.byte2int(new byte[]{dataBytes[0]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[1]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[2]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[3]})});
                break;
            case 29:// char 32字节
            case 30:// char 32字节
            case 32:// char 32字节
                data = ByteConverterUtil.byte2string(dataBytes);
                break;
            default:
        }
        return data;
    }

    /**
     * 特高频统计模式2数据
     * 2.数据格式要求。
     * ---------------------------------------------------------------------------------------
     * | 序号 |  数据类型    |  长度     |  备注
     * ---------------------------------------------------------------------------------------
     * |    1  | int         | 4字节     | 数据长度 数据项1到12的字节长度。
     * |    2  | float       | 4字节     | 规范版本号 所使用的数据格式规范版本号，例如1.0。
     * |    3  | char        | 1字节     | 图谱类型编码 标识该文件是PRPD图谱还是PRPS图谱。 PRPD:0x00
     * |    4  | int         | 4字节     | 相位窗数m 工频周期被等分成m个相位窗，每个相位窗跨360/m度。 m = dataBytes.length / 4 / xlen
     * |    5  | int         | 4字节     | 量化幅值n 幅值范围的等分区间数。n=66. -10-（-75）+1=66
     * |    6  | char        | 1字节     | 幅值单位 表示幅值的单位 dBm: 0x01
     * |    7  | float       | 4字节     | 幅值下限 仪器所能检测到的放电信号幅值的下限。下限=-75
     * |    8  | float       | 4字节     | 幅值上限 仪器所能检测到的放电信号幅值的上限。上限=-10
     * |    9  | int         | 4字节     | 工频周期数p 如果该文件是PRPD图谱，则该4个字节清零，赋值为：0x00000000。
     * |   10  | char[8]     | 8字节     | 放电类型概率 表示仪器诊断结果的放电类型概率。数组元素[0]至[7]分别存储正常、尖端放电、悬浮放电、沿面放电、内部放电、颗粒放电、外部干扰和其它共8种情况的概率，每个元素可以是整数0至100中的某数。实例：数组元素[3]为69，表示沿面放电的概率为69%。 如果诊断结果为正常，则为数组元素[0]赋值100，数组元素[1]至[7]赋值0。 先按照以下规则处理 如果仪器不具备放电类型诊断功能，则为数组元素[0]至[7]赋值0x00。
     * |   11  | int[m][n]   |4*50*66字节| 局部放电图谱数据 如果该文件是PRPD图谱，则为int[m][n]，m=50为相位窗数，n=66为量化幅值，数组元素[x][y]的值表示在对应第x相位窗和第y幅值处发生的放电次数。
     * |   12  | int         | 4字节     | CRC32 数据校验，使用CRC32算法。
     * |   13  | int         | 4字节     | 数据长度 数据项13到校验和的字节长度。
     * |   14  | int64       | 8字节     | 图谱生成时间 生成图谱的时间，格式为YYYYMMDDhhmmssfff，例如20100818151010001。该时间与文件名的时间相同，只是文件名中的时间到秒，这边的到毫秒。
     * |   15  | char        | 1字节     | 检测技术类型编码 标识该文件是采用什么技术获得。 TEV: 0x01 AA: 0x02 AE: 0x03 HFCT: 0x04 UHF: 0x05
     * |   16  | float       | 4字节     | 幅值 表示统计模式下当前1秒的幅值
     * |   17  | int32       | 4字节     | 放电频度，即脉冲个数 表示统计模式下当前1秒的脉冲个数
     * |   18  | float       | 4字节     | 参考阈值
     * |   19  | float       | 4字节     | 相位
     * |   20  | Char        | 1字节     | 同步类型 外同步: 0x01 内同步: 0x02
     * |   21  | float       | 4字节     | 同步频率
     * |   22  | float       | 4字节     | 系统频率
     * |   23  | float       | 4字节     | 警戒值1 如-60
     * |   24  | float       | 4字节     | 警戒值2 如-40
     * |   25  | float       | 4字节     | 警戒值3 如-20
     * |   26  | uint8       | 1字节     | 频带
     * |   27  | float       | 4字节     | 下限频率
     * |   28  | float       | 4字节     | 上限频率
     * |   29  | char        | 32字节    | 仪器厂家 从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：RedPhase。
     * |   30  | char        | 32字节    | 仪器型号 从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：PDT-400。
     * |   31  | uint8       | 4字节     | 仪器版本号 从系统设置中提取，所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。 实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。
     * |   32  | char        | 32字节    | 仪器序列号 从系统设置中提取，即仪器出厂编号，使用ASCII编码。以\0结尾，例如：S12300000000000000。
     * |   33  | int         | 4字节     | 检验和 13到校验和前一项的数据校验。
     * ---------------------------------------------------------------------------------------
     */
    public Object TJ_2(int i, byte[] dataBytes) throws Exception {
        Object data = null;
        switch (i + 1) {
            case 3:// char  1字节
            case 6:// char  1字节
            case 15:// char  1字节
            case 20:// Char  1字节
            case 26:// uint8  1字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 14:// int64  8字节
                data = ByteConverterUtil.byte2long(dataBytes);
                break;
            case 1:// int  4字节
            case 4:// int  4字节
            case 5:// int  4字节
            case 9:// int  4字节
            case 12:// int  4字节
            case 13:// int  4字节
            case 17:// int32  4字节
            case 33:// int  4字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 10:// char[8]  8字节
                data = new Integer[]{ByteConverterUtil.byte2int(new byte[]{dataBytes[0]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[1]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[2]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[3]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[4]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[5]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[6]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[7]})};
                break;
            case 11:// int[m][n]  4*50*66字节
                int offset = 0, xlen = 50, ylen = 66;
                Integer[][] byteArr = new Integer[xlen][ylen];
                byte[] destBytes = new byte[4];
                for (int x = 0; x < xlen; x++) {
                    for (int y = 0; y < ylen; y++) {
                        System.arraycopy(dataBytes, offset * destBytes.length, destBytes, 0, destBytes.length);
                        byteArr[x][y] = ByteConverterUtil.byte2int(destBytes);
                        offset++;
                    }
                }
                data = JSON.toJSONString(byteArr);
                break;
            case 2:// float  4字节
            case 7:// float  4字节
            case 8:// float  4字节
            case 16:// float  4字节
            case 18:// float  4字节
            case 19:// float  4字节
            case 21:// float  4字节
            case 22:// float  4字节
            case 23:// float  4字节
            case 24:// float  4字节
            case 25:// float  4字节
            case 27:// float  4字节
            case 28:// float  4字节
                data = ByteConverterUtil.byte2float(dataBytes);
                break;
            case 31:// uint8  4字节
                data = new Integer[]{ByteConverterUtil.byte2int(new byte[]{dataBytes[0]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[1]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[2]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[3]})};
                break;
            case 29:// char  32字节
            case 30:// char  32字节
            case 32:// char  32字节
                data = ByteConverterUtil.byte2string(dataBytes);
                break;
            default:
        }
        return data;
    }

    /**
     * 特高频统计模式2录波数据
     * 2.数据格式要求。
     * ---------------------------------------------------------------------------------------
     * |    序号  |  数据类型   |  长度        |  备注
     * ---------------------------------------------------------------------------------------
     * |     1    |    int      |    4字节     |    数据长度 数据项1到校验和的字节长度。
     * |     2    |    float    |    4字节     |    规范版本号 所使用的数据格式规范版本号，例如1.0。
     * |     3    |    int64    |    8字节     |    图谱生成时间 生成图谱的时间，格式为YYYYMMDDhhmmssfff，例如20100818151010001。该时间与文件名的时间相同，只是文件名中的时间到秒，这边的到毫秒。
     * |     4    |    char     |    1字节     |    检测技术类型编码 标识该文件是采用什么技术获得。 TEV: 0x01 AA: 0x02 AE: 0x03 HFCT: 0x04 UHF: 0x05
     * |     5    |    char     |    1字节     |    图谱类型编码 标识该文件是PRPD图谱还是PRPS图谱。 PRPS: 0x01
     * |     6    |    int      |    4字节     |    相位窗数m 工频周期被等分成m个相位窗，每个相位窗跨 360/m 度。 m = dataBytes.length / 4 / xlen（暂定）
     * |     7    |    char     |    1字节     |    幅值单位 表示幅值的单位 dBm: 0x01 mV: 0x02 %: 0x03
     * |     8    |    float    |    4字节     |    幅值下限 仪器所能检测到的放电信号幅值的下限。暂定：-75
     * |     9    |    float    |    4字节     |    幅值上限 仪器所能检测到的放电信号幅值的上限。暂定：-10
     * |    10    |    int      |    4字节     |    工频周期数p 图谱工频周期的个数。 工频周期数=500.
     * |    11    |float[p][m]  |4*500*120 字节|    局部放电图谱数据 该文件是PRPS图谱，则为float[p][m]，p为工频周期数——500（表示10秒，每秒50个），m为相位窗数——120，数组元素[x][y]的值表示在对应第x个周期的第y相位窗处发生的放电的幅值。
     * |    12    |    float    |    4字节     |    参考阈值
     * |    13    |    float    |    4字节     |    相位
     * |    14    |    Char     |    1字节     |    同步类型 外同步: 0x01 内同步: 0x02
     * |    15    |    float    |    4字节     |    同步频率
     * |    16    |    float    |    4字节     |    系统频率
     * |    17    |    float    |    4字节     |    警戒值1 如-60
     * |    18    |    float    |    4字节     |    警戒值2 如-40
     * |    19    |    float    |    4字节     |    警戒值3 如-20
     * |    20    |    uint8    |    1字节     |    频带
     * |    21    |    float    |    4字节     |    下限频率
     * |    22    |    float    |    4字节     |    上限频率
     * |    23    |    char     |    32字节    |    仪器厂家 从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：RedPhase。
     * |    24    |    char     |    32字节    |    仪器型号 从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：PDT-400。
     * |    25    |    uint8    |    4字节     |    仪器版本号 从系统设置中提取，所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。 实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。
     * |    26    |    char     |    32字节    |    仪器序列号 从系统设置中提取，即仪器出厂编号，使用ASCII编码。以\0结尾，例如：S12300000000000000。
     * |    27    |    int      |    4字节     |    检验和 1到校验和前一项的数据校验。
     * ---------------------------------------------------------------------------------------
     */
    public Object TJLB_2(int i, byte[] dataBytes) throws Exception {
        Object data = null;
        switch (i + 1) {
            case 4:// char 1字节
            case 5:// char 1字节
            case 7:// char 1字节
            case 14:// Char 1字节
            case 20:// uint8 1字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 1:// int 4字节
            case 6:// int 4字节
            case 10:// int 4字节
            case 27:// int 4字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 3:// int64 8字节
                data = ByteConverterUtil.byte2long(dataBytes);
                break;
            case 11:// float[p][m] 4*500*120字节
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
            case 2:// float 4字节
            case 8:// float 4字节
            case 9:// float 4字节
            case 12:// float 4字节
            case 13:// float 4字节
            case 15:// float 4字节
            case 16:// float 4字节
            case 17:// float 4字节
            case 18:// float 4字节
            case 19:// float 4字节
            case 21:// float 4字节
            case 22:// float 4字节
                data = ByteConverterUtil.byte2float(dataBytes);
                break;
            case 25:// uint8 4字节
                data = JSON.toJSONString(new Integer[]{ByteConverterUtil.byte2int(new byte[]{dataBytes[0]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[1]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[2]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[3]})});
                break;
            case 23:// char 32字节
            case 24:// char 32字节
            case 26:// char 32字节
                data = ByteConverterUtil.byte2string(dataBytes);
                break;
            default:
        }
        return data;
    }
}
