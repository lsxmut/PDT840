package com.redphase.service.analyze.data;

import com.alibaba.fastjson.JSON;
import com.redphase.framework.util.ByteConverterUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * 空气超声（AA）数据
 */
@Slf4j
public class AnalyzeAAData {
    /**
     * AA巡检噪声数据
     * 2.数据格式要求。
     * ---------------------------------------------------------------------------------------
     * | 序号 |   数据类型    |  长度   |  备注
     * ---------------------------------------------------------------------------------------
     * |   1  |      int32    | 4字节  | 数据长度 数据项1到校验和的字节长度。
     * |   2  |      float    | 4字节  | 规范版本号 所使用的数据格式规范版本号，例如1.0。
     * |   3  |      int64    | 8字节  | 图谱生成时间 生成图谱的时间，格式为YYYYMMDDhhmmssfff，例如20100818151010001。该时间与文件名的时间相同，只是文件名中的时间到秒，这边的到毫秒。
     * |   4  |      uint8    | 1字节  | 检测技术类型编码 标识该文件是采用什么技术获得。 TEV: 0x01 AA: 0x02 AA: 0x03 HFCT: 0x04 UHF: 0x05
     * |   5  |      float    | 4字节  | 幅值m2
     * |   6  |      int32    | 4字节  | 放电频率n2
     * |   7  |      float    | 4字节  | 有效值 计算结果
     * |   8  |      float    | 4字节  | f1分量 计算结果
     * |   9  |      float    | 4字节  | f2分量 计算结果
     * |  10  |      char[8]  | 8字节  | 放电类型概率 噪声文件对应的数组元素[0]至[7]赋值0x00。
     * |  11  |      uint8    | 1字节  | 空气超声监听结果 表示空气超声的监听结果 正常: 0x01 异常: 0x02 未监听: 0x03
     * |  12  |      uint8    | 1字节  | 空气超声幅值单位 表示幅值的单位dBuv: 0x04
     * |  13  |      float    | 4字节  | 空气超声幅值下限 仪器所能检测到的放电信号幅值的下限。暂定：-10
     * |  14  |      float    | 4字节  | 空气超声幅值上限 仪器所能检测到的放电信号幅值的上限。暂定：70
     * |  15  |      float    | 4字节  | 空气超声参考阈值
     * |  16  |      uint8    | 1字节  | 空气超声增益 60dB: 0x01 80dB: 0x02 100dB: 0x03
     * |  17  |      float    | 4字节  | f1分量 检测参数，用户人工录入或点击获取的值。
     * |  18  |      float    | 4字节  | f2分量 检测参数，f2=f1×2；
     * |  19  |      float    | 4字节  | 系统频率
     * |  20  |      float    | 4字节  | 同步频率 同步频率=f1分量
     * |  21  |      float    | 4字节  | 空气超声警戒值1 如6
     * |  22  |      float    | 4字节  | 空气超声警戒值2 如15
     * |  23  |      float    | 4字节  | 空气超声警戒值3 如20
     * |  24  |      char     | 32字节 | 仪器厂家 从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：RedPhase。
     * |  25  |      char     | 32字节 | 仪器型号 从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：PDT-400。
     * |  26  |      uint8    | 4字节  | 仪器版本号 从系统设置中提取，所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。 实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。
     * |  27  |      char     | 32字节 | 仪器序列号 从系统设置中提取，即仪器出厂编号，使用ASCII编码。以\0结尾，例如：S12300000000000000。
     * |  28  |      int32    | 4字节  | 校验和 1到校验和前一项的数据校验。
     * ---------------------------------------------------------------------------------------
     */
    public Object XJZS(int i, byte[] dataBytes) throws Exception {
        Object data = null;
        switch (i + 1) {
            case 4:// uint8 1字节
            case 11:// uint8 1字节
            case 12:// uint8 1字节
            case 16:// uint8 1字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 1:// int32 4字节
            case 6:// int32 4字节
            case 28:// int32 4字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 3:// int64 8字节
                data = ByteConverterUtil.byte2long(dataBytes);
                break;
            case 2:// float 4字节
            case 5:// float 4字节
            case 7:// float 4字节
            case 8:// float 4字节
            case 9:// float 4字节
            case 13:// float 4字节
            case 14:// float 4字节
            case 15:// float 4字节
            case 17:// float 4字节
            case 18:// float 4字节
            case 19:// float 4字节
            case 20:// float 4字节
            case 21:// float 4字节
            case 22:// float 4字节
            case 23:// float 4字节
                data = ByteConverterUtil.byte2float(dataBytes);
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
            case 26:// uint8 4字
                data = JSON.toJSONString(new Integer[]{ByteConverterUtil.byte2int(new byte[]{dataBytes[0]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[1]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[2]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[3]})});
                break;
            case 24:// char 32字节
            case 25:// char 32字节
            case 27:// char 32字节
                data = ByteConverterUtil.byte2string(dataBytes);
                break;
            default:
        }
        return data;
    }

    /**
     * AA巡检数据
     * 2.数据格式要求。
     * ---------------------------------------------------------------------------------------
     * | 序号 |  数据类型    |  长度   |  备注
     * ---------------------------------------------------------------------------------------
     * |    1  |    int32    | 4字节   | 数据长度 数据项1到校验和的字节长度。
     * |    2  |    float    | 4字节   | 规范版本号 所使用的数据格式规范版本号，例如1.0。
     * |    3  |    int64    | 8字节   | 图谱生成时间 生成图谱的时间，格式为YYYYMMDDhhmmssfff，例如20100818151010001。该时间与文件名的时间相同，只是文件名中的时间到秒，这边的到毫秒。
     * |    4  |    uint8    | 1字节   | 检测技术类型编码 标识该文件是采用什么技术获得。,TEV: 0x01,AA: 0x02,AA: 0x03,HFCT: 0x04,UHF: 0x05
     * |    5  |    float    | 4字节   | 幅值m2
     * |    6  |    int32    | 4字节   | 放电频率n2
     * |    7  |    float    | 4字节   | 有效值 计算结果
     * |    8  |    float    | 4字节   | f1分量 计算结果
     * |    9  |    float    | 4字节   | f2分量 计算结果
     * |   10  |    char[8]  | 8字节   | 放电类型概率 表示仪器诊断结果的放电类型概率。数组元素[0]至[7]分别存储正常、尖端放电、悬浮放电、沿面放电、内部放电、颗粒放电、外部干扰和其它共8种情况的概率，每个元素可以是整数0至100中的某数。实例：数组元素[3]为69，表示沿面放电的概率为69%。,如果诊断结果为正常，则为数组元素[0]赋值100，数组元素[1]至[7]赋值0。,先按照以下规则处理,如果仪器不具备放电类型诊断功能，则为数组元素[0]至[7]赋值0x00。
     * |   11  |    uint8    | 1字节   | 空气超声监听结果 表示空气超声的监听结果,正常: 0x01,异常: 0x02,未监听: 0x03
     * |   12  |    uint8    | 1字节   | AA幅值单位 表示幅值的单位,dBuV: 0x04
     * |   13  |    float    | 4字节   | AA幅值下限 仪器所能检测到的放电信号幅值的下限。暂定：-10
     * |   14  |    float    | 4字节   | AA幅值上限 仪器所能检测到的放电信号幅值的上限。暂定：70
     * |   15  |    float    | 4字节   | AA参考阈值
     * |   16  |    uint8    | 1字节   | 空气超声增益 60dB: 0x01,80dB: 0x02,100dB: 0x03
     * |   17  |    float    | 4字节   | f1分量 检测参数，用户人工录入或点击获取的值。
     * |   18  |    float    | 4字节   | f2分量 检测参数，f2=f1×2；
     * |   19  |    float    | 4字节   | 系统频率
     * |   20  |    float    | 4字节   | 同步频率 同步频率=f1分量
     * |   21  |    float    | 4字节   | 空气超声警戒值1 如6
     * |   22  |    float    | 4字节   | 空气超声警戒值2 如15
     * |   23  |    float    | 4字节   | 空气超声警戒值3 如20
     * |   24  |    int32    | 4字节   | 前置增益
     * |   25  |    char     | 32字节  | 仪器厂家 从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：RedPhase。
     * |   26  |    char     | 32字节  | 仪器型号 从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：PDT-400。
     * |   27  |    uint8    | 4字节   | 仪器版本号 从系统设置中提取，所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。,实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。
     * |   28  |    char     | 32字节  | 仪器序列号 从系统设置中提取，即仪器出厂编号，使用ASCII编码。以\0结尾，例如：S12300000000000000。
     * |   29  |    int32    | 4字节   | 校验和 1到校验和前一项的数据校验。
     * ---------------------------------------------------------------------------------------
     */
    public Object XJ(int i, byte[] dataBytes) throws Exception {
        Object data = null;
        switch (i + 1) {
            case 4:// uint8 1字节
            case 11:// uint8 1字节
            case 12:// uint8 1字节
            case 16:// uint8 1字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 1:// int32 4字节
            case 6:// int32 4字节
            case 24:// int32 4字节
            case 29:// int32 4字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 3:// int64 8字节
                data = ByteConverterUtil.byte2long(dataBytes);
                break;
            case 27:// uint8 4字节
                data = JSON.toJSONString(new Integer[]{ByteConverterUtil.byte2int(new byte[]{dataBytes[0]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[1]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[2]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[3]})});
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
            case 2:// float 4字节
            case 5:// float 4字节
            case 7:// float 4字节
            case 8:// float 4字节
            case 9:// float 4字节
            case 13:// float 4字节
            case 14:// float 4字节
            case 15:// float 4字节
            case 17:// float 4字节
            case 18:// float 4字节
            case 19:// float 4字节
            case 20:// float 4字节
            case 21:// float 4字节
            case 22:// float 4字节
            case 23:// float 4字节
                data = ByteConverterUtil.byte2float(dataBytes);
                break;
            case 25:// char 32字节
            case 26:// char 32字节
            case 28:// char 32字节
                data = ByteConverterUtil.byte2string(dataBytes);
                break;
            default:
        }
        return data;
    }

    /**
     * AA统计噪声数据
     * 2.数据格式要求。
     * ---------------------------------------------------------------------------------------
     * | 序号 |  数据类型    |  长度   |  备注
     * ---------------------------------------------------------------------------------------
     * |    1  |   int32     |  4字节  | 数据长度 数据项1到12的字节长度。
     * |    2  |   float     |  4字节  | 规范版本号 所使用的数据格式规范版本号，例如1.0。
     * |    3  |   uint8     |  1字节  | 图谱类型编码 标识该文件是PRPD图谱还是PRPS图谱。,PRPS: 0x01
     * |    4  |   int32     |  4字节  | 相位窗数m 工频周期被等分成m个相位窗，每个相位窗跨 360/m 度。,m = 120（暂定）
     * |    5  |   int32     |  4字节  | 量化幅值n 赋值为：0x00000000。
     * |    6  |   uint8     |  1字节  | 幅值单位 表示幅值的单位,dBuV: 0x04
     * |    7  |   float     |  4字节  | 幅值下限 仪器所能检测到的放电信号幅值的下限。暂定：-10
     * |    8  |   float     |  4字节  | 幅值上限 仪器所能检测到的放电信号幅值的上限。暂定：70
     * |    9  |   int32     |  4字节  | 工频周期数p 图谱工频周期的个数。,工频周期数=50.
     * |   10  |   uint8     |  8字节  | 放电类型概率 表示仪器诊断结果的放电类型概率。数组元素[0]至[7]分别存储正常、尖端放电、悬浮放电、沿面放电、内部放电、颗粒放电、外部干扰和其它共8种情况的概率，每个元素可以是整数0至100中的某数。实例：数组元素[3]为69，表示沿面放电的概率为69%。,如果诊断结果为正常，则为数组元素[0]赋值100，数组元素[1]至[7]赋值0。,先按照以下规则处理,如果仪器不具备放电类型诊断功能，则为数组元素[0]至[7]赋值0x00。
     * |   11  | float[p][m] |4*50*120字节| 局部放电图谱数据 该文件是PRPS图谱，则为float[p][m]，p为工频周期数——50，m为相位窗数——120，数组元素[x][y]的值表示在对应第x个周期的第y相位窗处发生的放电的幅值。
     * |   12  |   int       |  4字节  | CRC32 1到11项的数据校验，使用CRC32算法。
     * |   13  |   int       |  4字节  | 数据长度 数据项13到校验和的字节长度。
     * |   14  |   int64     |  8字节  | 图谱生成时间 生成图谱的时间，格式为YYYYMMDDhhmmssfff，例如20100818151010001。该时间与文件名的时间相同，只是文件名中的时间到秒，这边的到毫秒。
     * |   15  |   uint8     |  1字节  | 检测技术类型编码 标识该文件是采用什么技术获得。,TEV: 0x01,AA: 0x02,AA: 0x03,HFCT: 0x04,UHF: 0x05
     * |   16  |   float     |  4字节  | 超声波幅值 表示统计模式下当前1秒的幅值
     * |   17  |   int32     |  4字节  | 超声波放电频度，即脉冲个数 表示统计模式下当前1秒的脉冲个数
     * |   18  |   float     |  4字节  | 空气超声参考阈值
     * |   19  |   float     |  4字节  | 相位
     * |   20  |   uint8     |  1字节  | 同步类型 外同步: 0x01,内同步: 0x02
     * |   21  |   float     |  4字节  | 同步频率
     * |   22  |   float     |  4字节  | 系统频率
     * |   23  |   uint8     |  1字节  | 放大增益 60dB: 0x01,80dB: 0x02,100dB: 0x03
     * |   24  |   float     |  4字节  | 空气超声警戒值1 如6
     * |   25  |   float     |  4字节  | 空气超声警戒值2 如15
     * |   26  |   float     |  4字节  | 空气超声警戒值3 如20
     * |   27  |   char      |  32字节 | 仪器厂家 从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：RedPhase。
     * |   28  |   char      |  32字节 | 仪器型号 从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：PDT-400。
     * |   29  |   uint8     |  4字节  | 仪器版本号 从系统设置中提取，所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。,实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。
     * |   30  |   char      |  32字节 | 仪器序列号 从系统设置中提取，即仪器出厂编号，使用ASCII编码。以\0结尾，例如：S12300000000000000。
     * |   31  |   int32     |  4字节  | 检验和 13到校验和前一项的数据校验。
     * ---------------------------------------------------------------------------------------
     */
    public Object TJZS(int i, byte[] dataBytes) throws Exception {
        Object data = null;
        switch (i + 1) {
            case 3:// uint8 1字节
            case 6:// uint8 1字节
            case 15:// uint8 1字节
            case 20:// uint8 1字节
            case 23:// uint8 1字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 29:// uint8 4字节
                data = JSON.toJSONString(new Integer[]{ByteConverterUtil.byte2int(new byte[]{dataBytes[0]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[1]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[2]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[3]})});
                break;
            case 10:// uint8 8字节
                data = new Integer[]{ByteConverterUtil.byte2int(new byte[]{dataBytes[0]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[1]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[2]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[3]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[4]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[5]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[6]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[7]})};
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
            case 1:// int32 4字节
            case 4:// int32 4字节
            case 5:// int32 4字节
            case 9:// int32 4字节
            case 12:// int 4字节
            case 13:// int 4字节
            case 17:// int32 4字节
            case 31:// int32 4字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 14:// int64 8字节
                data = ByteConverterUtil.byte2long(dataBytes);
                break;
            case 2:// float 4字节
            case 7:// float 4字节
            case 8:// float 4字节
            case 16:// float 4字节
            case 18:// float 4字节
            case 19:// float 4字节
            case 21:// float 4字节
            case 22:// float 4字节
            case 24:// float 4字节
            case 25:// float 4字节
            case 26:// float 4字节
                data = ByteConverterUtil.byte2float(dataBytes);
                break;
            case 27:// char 32字节
            case 28:// char 32字节
            case 30:// char 32字节
                data = ByteConverterUtil.byte2string(dataBytes);
                break;
            default:
        }
        return data;
    }

    /**
     * AA统计数据
     * 2.数据格式要求。
     * ---------------------------------------------------------------------------------------
     * | 序号 |  数据类型    |  长度     |  备注
     * ---------------------------------------------------------------------------------------
     * |    1   |   int32    |    4字节   | 数据长度 数据项1到12的字节长度。
     * |    2   |   float    |    4字节   | 规范版本号 所使用的数据格式规范版本号，例如1.0。
     * |    3   |   uint8    |    1字节   | 图谱类型编码 标识该文件是PRPD图谱还是PRPS图谱。 PRPS: 0x01
     * |    4   |   int32    |    4字节   | 相位窗数m 工频周期被等分成m个相位窗，每个相位窗跨 360/m 度。 m = 120（暂定）
     * |    5   |   int32    |    4字节   | 量化幅值n 赋值为：0x00000000。
     * |    6   |   uint8    |    1字节   | 幅值单位 表示幅值的单位 dBuV: 0x04
     * |    7   |   float    |    4字节   | 幅值下限 仪器所能检测到的放电信号幅值的下限。暂定：-10
     * |    8   |   float    |    4字节   | 幅值上限 仪器所能检测到的放电信号幅值的上限。暂定：70
     * |    9   |   int32    |    4字节   | 工频周期数p 图谱工频周期的个数。 工频周期数=50.
     * |   10   |   uint8    |    8字节   | 放电类型概率 表示仪器诊断结果的放电类型概率。数组元素[0]至[7]分别存储正常、尖端放电、悬浮放电、沿面放电、内部放电、颗粒放电、外部干扰和其它共8种情况的概率，每个元素可以是整数0至100中的某数。实例：数组元素[3]为69，表示沿面放电的概率为69%。 如果诊断结果为正常，则为数组元素[0]赋值100，数组元素[1]至[7]赋值0。 先按照以下规则处理 如果仪器不具备放电类型诊断功能，则为数组元素[0]至[7]赋值0x00。
     * |   11   |float[p][m ]|4*50*120字节| 局部放电图谱数据 该文件是PRPS图谱，则为float[p][m]，p为工频周期数——50，m为相位窗数——120，数组元素[x][y]的值表示在对应第x个周期的第y相位窗处发生的放电的幅值。
     * |   12   |   int      |    4字节   | CRC32 1到11项的数据校验，使用CRC32算法。
     * |   13   |   int      |    4字节   | 数据长度 数据项13到校验和的字节长度。
     * |   14   |   int64    |    8字节   | 图谱生成时间 生成图谱的时间，格式为YYYYMMDDhhmmssfff，例如20100818151010001。该时间与文件名的时间相同，只是文件名中的时间到秒，这边的到毫秒。
     * |   15   |   uint8    |    1字节   | 检测技术类型编码 标识该文件是采用什么技术获得。 TEV: 0x01 AA: 0x02 AA: 0x03 HFCT: 0x04 UHF: 0x05
     * |   16   |   float    |    4字节   | 超声波幅值 表示统计模式下当前1秒的幅值
     * |   17   |   int32    |    4字节   | 超声波放电频度，即脉冲个数 表示统计模式下当前1秒的脉冲个数
     * |   18   |   float    |    4字节   | 空气超声参考阈值
     * |   19   |   float    |    4字节   | 相位
     * |   20   |   uint8    |    1字节   | 同步类型 外同步: 0x01 内同步: 0x02
     * |   21   |   float    |    4字节   | 同步频率
     * |   22   |   float    |    4字节   | 系统频率
     * |   23   |   uint8    |    1字节   | 放大增益 60dB: 0x01 80dB: 0x02 100dB: 0x03
     * |   24   |   float    |    4字节   | 空气超声警戒值1 如6
     * |   25   |   float    |    4字节   | 空气超声警戒值2 如15
     * |   26   |   float    |    4字节   | 空气超声警戒值3 如20
     * |   27   |   char     |    32字节  | 仪器厂家 从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：RedPhase。
     * |   28   |   char     |    32字节  | 仪器型号 从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：PDT-400。
     * |   29   |   uint8    |    4字节   | 仪器版本号 从系统设置中提取，所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。 实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。
     * |   30   |   char     |    32字节  | 仪器序列号 从系统设置中提取，即仪器出厂编号，使用ASCII编码。以\0结尾，例如：S12300000000000000。
     * |   31   |   int32    |    4字节   | 检验和 13到校验和前一项的数据校验。
     * ---------------------------------------------------------------------------------------
     */
    public Object TJ(int i, byte[] dataBytes) throws Exception {
        Object data = null;
        switch (i + 1) {
            case 3:// uint8 1字节
            case 6:// uint8 1字节
            case 15:// uint8 1字节
            case 20:// uint8 1字节
            case 23:// uint8 1字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 1:// int32 4字节
            case 4:// int32 4字节
            case 5:// int32 4字节
            case 9:// int32 4字节
            case 12:// int 4字节
            case 13:// int 4字节
            case 17:// int32 4字节
            case 31:// int32 4字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 29:// uint8 4字节
                data = JSON.toJSONString(new Integer[]{ByteConverterUtil.byte2int(new byte[]{dataBytes[0]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[1]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[2]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[3]})});
                break;
            case 14:// int64 8字节
                data = ByteConverterUtil.byte2long(dataBytes);
                break;
            case 10:// uint8 8字节
                data = new Integer[]{ByteConverterUtil.byte2int(new byte[]{dataBytes[0]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[1]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[2]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[3]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[4]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[5]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[6]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[7]})};
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
            case 24:// float 4字节
            case 25:// float 4字节
            case 26:// float 4字节
                data = ByteConverterUtil.byte2float(dataBytes);
                break;
            case 27:// char 32字节
            case 28:// char 32字节
            case 30:// char 32字节
                data = ByteConverterUtil.byte2string(dataBytes);
                break;
            default:
        }
        return data;
    }

    /**
     * AA统计录波数据
     * 2.数据格式要求。
     * ---------------------------------------------------------------------------------------
     * | 序号 |  数据类型    |  长度   |  备注
     * ---------------------------------------------------------------------------------------
     * |    1  |    int32    |  4字节  | 数据长度 数据项1到校验和的字节长度。
     * |    2  |    float    |  4字节  | 规范版本号 所使用的数据格式规范版本号，例如1.0。
     * |    3  |    int64    |  8字节  | 图谱生成时间 生成图谱的时间，格式为YYYYMMDDhhmmssfff，例如20100818151010001。该时间与文件名的时间相同，只是文件名中的时间到秒，这边的到毫秒。
     * |    4  |    uint8    |  1字节  | 检测技术类型编码 标识该文件是采用什么技术获得。TEV: 0x01,AA: 0x02,AA: 0x03,HFCT: 0x04,UHF: 0x05
     * |    5  |    uint8    |  1字节  | 图谱类型编码 标识该文件是PRPD图谱还是PRPS图谱。PRPS: 0x01
     * |    6  |    int32    |  4字节  | 相位窗数m 工频周期被等分成m个相位窗，每个相位窗跨 360/m 度。,m = 120（暂定）
     * |    7  |    uint8    |  1字节  | 幅值单位 表示幅值的单位,dBuV: 0x04
     * |    8  |    float    |  4字节  | 幅值下限 仪器所能检测到的放电信号幅值的下限。暂定：-10
     * |    9  |    float    |  4字节  | 幅值上限 仪器所能检测到的放电信号幅值的上限。暂定：70
     * |   10  |    int32    |  4字节  | 工频周期数p 图谱工频周期的个数。,工频周期数=500.
     * |   11  |float[p][m]  |4*500*120字节| 局部放电图谱数据 该文件是PRPS图谱，则为float[p][m]，p为工频周期数——500（表示10秒，每秒50个），m为相位窗数——120，数组元素[x][y]的值表示在对应第x个周期的第y相位窗处发生的放电的幅值。
     * |   12  |    float    |  4字节  | AA参考阈值
     * |   13  |    float    |  4字节  | AA相位
     * |   14  |    uint8    |  1字节  | 同步类型 外同步: 0x01,内同步: 0x02
     * |   15  |    float    |  4字节  | 同步频率
     * |   16  |    float    |  4字节  | 系统频率
     * |   17  |    uint8    |  1字节  | 放大增益 60dB: 0x01,80dB: 0x02,100dB: 0x03
     * |   18  |    float    |  4字节  | 空气超声警戒值1 如6
     * |   19  |    float    |  4字节  | 空气超声警戒值2 如15
     * |   20  |    float    |  4字节  | 空气超声警戒值3 如20
     * |   21  |    char     |  32字节 | 仪器厂家 从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：RedPhase。
     * |   22  |    char     |  32字节 | 仪器型号 从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：PDT-400。
     * |   23  |    uint8    |  4字节  | 仪器版本号 从系统设置中提取，所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。,实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。
     * |   24  |    char     |  32字节 | 仪器序列号 从系统设置中提取，即仪器出厂编号，使用ASCII编码。以\0结尾，例如：S12300000000000000。
     * |   25  |    int      |  4字节  | 检验和 1到校验和前一项的数据校验。
     * ---------------------------------------------------------------------------------------
     */
    public Object TJLB(int i, byte[] dataBytes) throws Exception {
        Object data = null;
        switch (i + 1) {
            case 4:// uint8 1字节
            case 5:// uint8 1字节
            case 7:// uint8 1字节
            case 17:// uint8 1字节
            case 14:// uint8 1字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 1:// int32 4字节
            case 6:// int32 4字节
            case 10:// int32 4字节
            case 25:// int 4字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 3:// int64 8字节
                data = ByteConverterUtil.byte2long(dataBytes);
                break;
            case 23:// uint8 4字节
                data = JSON.toJSONString(new Integer[]{ByteConverterUtil.byte2int(new byte[]{dataBytes[0]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[1]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[2]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[3]})});
                break;
            case 11:// float[p][m] 4*500*120字节
                int offset = 0, ylen = 60, xlen = dataBytes.length / 4 / ylen;
                Float[][] byteArr = new Float[xlen][ylen];
                byte[] destBytes = new byte[4];
                for (int x = 0; x < xlen; x++) {
                    for (int y = 0; y < ylen; y++) {
//                        log.debug("================="+offset * destBytes.length);
                        System.arraycopy(dataBytes, offset, destBytes, 0, destBytes.length);
                        byteArr[x][y] = ByteConverterUtil.byte2float(destBytes);
                        offset += destBytes.length;
//                        log.debug(x+":"+y+"=="+offset*destBytes.length+"=="+byteArr[x][y]+",");
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
            case 18:// float 4字节
            case 19:// float 4字节
            case 20:// float 4字节
                data = ByteConverterUtil.byte2float(dataBytes);
                break;
            case 21:// char 32字节
            case 22:// char 32字节
            case 24:// char 32字节
                data = ByteConverterUtil.byte2string(dataBytes);
                break;
            default:
        }
        return data;
    }

    /**
     * AA飞行图谱数据
     * 2.数据格式要求。
     * ---------------------------------------------------------------------------------------
     * | 序号 |  数据类型   |  长度     |  备注
     * ---------------------------------------------------------------------------------------
     * |  1   |   int     |   4字节    |   数据长度 数据项1到校验和的字节长度。
     * |  2   |   float   |   4字节    |   规范版本号 所使用的数据格式规范版本号，例如1.0。
     * |  3   |   int64   |   8字节    |   图谱生成时间 生成图谱的时间，格式为YYYYMMDDhhmmssfff，例如20100818151010001。该时间与文件名的时间相同，只是文件名中的时间到秒，这边的到毫秒。
     * |  4   |   char    |   1字节    |   检测技术类型编码 标识该文件是采用什么技术获得。 TEV: 0x01 AA: 0x02 AA: 0x03 HFCT: 0x04 UHF: 0x05
     * |  5   |   uint8   |   1字节    |   图谱类型编码 标识该文件是飞行图谱。 编码: 0x33
     * |  6   |   char    |   1字节    |   幅值单位 表示幅值的单位 mV: 0x06
     * |  7   |   float   |   4字节    |   幅值下限 仪器所能检测到的放电信号幅值的下限。暂定：0
     * |  8   |   float   |   4字节    |   幅值上限 仪器所能检测到的放电信号幅值的上限。暂定：300
     * |  9   |   uint8   |   1字节    |   脉冲间隔时间单位 表示脉冲间隔时间的单位。 微秒: 0x01 毫秒: 0x02 秒: 0x03
     * | 10   |   int32   |   4字节    |   数据点数n 数据点个数，默认为1000。
     * | 11   |   uint8   |   8字节    |   放电类型概率 表示仪器诊断结果的放电类型概率。参见放电类型概率定义。 先按照以下规则处理 如果仪器不具备放电类型诊断功能，则为数组元素[0]至[7]赋值0x00。
     * | 12   |   float   |   4字节    |   参考阈值
     * | 13   |   char    |   1字节    |   增益 20dB: 0x01 40dB: 0x02 60dB: 0x03
     * | 14   |   int     |   4字节    |   开门时间 以us为单位
     * | 15   |   int     |   4字节    |   闭锁时间 以ms为单位
     * | 16   |   int     |   4字节    |   等待时间 以秒为单位
     * | 17   |   int     |   4字节    |   最大时间间隔 以ms为单位
     * | 18   |   int     |   4字节    |   前置增益 单位dB
     * | 19   |   float   |   4字节    |   系统频率
     * | 20   |   uint8   |   1字节    |   同步类型 外同步: 0x01 内同步: 0x02 默认写入内同步。
     * | 21   |   float   |   4字节    |   同步频率 同步频率=系统频率
     * | 22   |   float   |   4*2*n字节|   超声脉冲图数据 根据存储数据类型t获取数据的存储方式。 实例1： uint8数组，k=1； 实例2： int32数组，k=4； 实例3： float数组，k=4。依次存储每个数据点的飞行时间T和信号幅值Q，存储顺序：T[0]，Q[0]；T[1]，Q[1]；……；T[n-1]，Q[n-1]。
     * | 23   |   char    |   32字节   |   仪器厂家 从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：RedPhase。
     * | 24   |   char    |   32字节   |   仪器型号 从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：PDT-400。
     * | 25   |   uint8   |   4字节    |   仪器版本号 从系统设置中提取，所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。 实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。
     * | 26   |   char    |   32字节   |   仪器序列号 从系统设置中提取，即仪器出厂编号，使用ASCII编码。以\0结尾，例如：S12300000000000000。
     * | 27   |   int     |   4字节    |   检验和 1到校验和前一项的数据校验。
     * ---------------------------------------------------------------------------------------
     */
    public Object FX(int i, byte[] dataBytes) throws Exception {
        Object data = null;
        switch (i + 1) {
            case 5:// uint8 1字节
            case 4:// char 1字节
            case 6:// char 1字节
            case 9:// uint8 1字节
            case 13:// char 1字节
            case 20:// uint8 1字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 1:// int 4字节
            case 14:// int 4字节
            case 15:// int 4字节
            case 16:// int 4字节
            case 17:// int 4字节
            case 18:// int 4字节
            case 10:// int32 4字节
            case 27:// int 4字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 3:// int64 8字节
                data = ByteConverterUtil.byte2long(dataBytes);
                break;
            case 11:// uint8 8字节
                data = new Integer[]{ByteConverterUtil.byte2int(new byte[]{dataBytes[0]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[1]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[2]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[3]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[4]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[5]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[6]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[7]})};
                break;
            case 2:// float 4字节
            case 7:// float 4字节
            case 8:// float 4字节
            case 12:// float 4字节
            case 19:// float 4字节
            case 21:// float 4字节
                data = ByteConverterUtil.byte2float(dataBytes);
                break;
            case 22://TODO Integer 4*2*n字节
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
                data = JSON.toJSONString(new HashMap() {{
                    put("t", tbyteArr);
                    put("q", qbyteArr);
                }});
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
     * AA波形图谱数据
     * 2.数据格式要求。
     * ---------------------------------------------------------------------------------------
     * | 序号 |  数据类型    |  长度   |  备注
     * ---------------------------------------------------------------------------------------
     * |  1   |   int     |   4字节   |   数据长度 数据项1到校验和的字节长度。
     * |  2   |   float   |   4字节   |   规范版本号 所使用的数据格式规范版本号，例如1.0。
     * |  3   |   int64   |   8字节   |   图谱生成时间 生成图谱的时间，格式为YYYYMMDDhhmmssfff，例如20100818151010001。该时间与文件名的时间相同，只是文件名中的时间到秒，这边的到毫秒。
     * |  4   |   char    |   1字节   |   检测技术类型编码 标识该文件是采用什么技术获得。 TEV: 0x01 AA: 0x02 AA: 0x03 HFCT: 0x04 UHF: 0x05
     * |  5   |   uint8   |   1字节   |   图谱类型编码 标识该文件是飞行图谱。 编码: 0x34
     * |  6   |   char    |   1字节   |   幅值单位 表示幅值的单位 mV: 0x06
     * |  7   |   float   |   4字节   |   幅值下限 仪器所能检测到的放电信号幅值的下限。暂定：0
     * |  8   |   float   |   4字节   |   幅值上限 仪器所能检测到的放电信号幅值的上限。暂定：300
     * |  9   |   int32   |   4字节   |   数据点数n 数据点个数
     * | 10   |   int64   |   8字节   |   采样率 单位SPS。
     * | 11   |   uint8   |   8字节   |   放电类型概率 表示仪器诊断结果的放电类型概率。参见放电类型概率定义。 先按照以下规则处理 如果仪器不具备放电类型诊断功能，则为数组元素[0]至[7]赋值0x00。
     * | 12   |   char    |   1字节   |   增益 20dB: 0x01 40dB: 0x02 60dB: 0x03
     * | 13   |   Char    |   1字节   |   同步类型 外同步: 0x01 内同步: 0x02
     * | 14   |   float   |   4字节   |   同步频率
     * | 15   |   float   |   4字节   |   系统频率
     * | 16   |   int     |   4字节   |   采样时间 以ms为单位
     * | 17   |   Char    |   1字节   |   运行方式 连续: 0x01 单次: 0x02
     * | 18   |   int     |   4字节   |   前置增益 单位dB
     * | 19   |   float   |   4*n字节 |   超声波形图数据 根据存储数据类型t获取数据的存储方式。 实例1： uint8数组，k=1； 实例2： int32数组，k=4； 实例3： float数组，k=4。依次存储每个数据点的信号幅值Q，存储顺序：Q[0]；Q[1]；……；Q[n-1]。
     * | 20   |   char    |   32字节  |   仪器厂家 从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：RedPhase。
     * | 21   |   char    |   32字节  |   仪器型号 从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：PDT-400。
     * | 22   |   uint8   |   4字节   |   仪器版本号 从系统设置中提取，所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。 实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。
     * | 23   |   char    |   32字节  |   仪器序列号 从系统设置中提取，即仪器出厂编号，使用ASCII编码。以\0结尾，例如：S12300000000000000。
     * | 24   |   int     |   4字节   |   检验和 1到校验和前一项的数据校验。
     * ---------------------------------------------------------------------------------------
     */
    public Object BX(int i, byte[] dataBytes) throws Exception {
        Object data = null;
        switch (i + 1) {
            case 4:// char 1字节
            case 5:// uint8 1字节
            case 6:// char 1字节
            case 12:// char 1字节
            case 13:// Char 1字节
            case 17:// Char 1字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 16:// int 4字节
            case 18:// int 4字节
            case 1:// int 4字节
            case 9:// int32 4字节
            case 24:// int 4字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 3:// int64 8字节
            case 10:// int64 8字节
                data = ByteConverterUtil.byte2long(dataBytes);
                break;
            case 11:// uint8 8字节
                data = new Integer[]{ByteConverterUtil.byte2int(new byte[]{dataBytes[0]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[1]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[2]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[3]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[4]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[5]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[6]})
                        , ByteConverterUtil.byte2int(new byte[]{dataBytes[7]})};
                break;
            case 2:// float 4字节
            case 7:// float 4字节
            case 8:// float 4字节
            case 14:// float 4字节
            case 15:// float 4字节
                data = ByteConverterUtil.byte2float(dataBytes);
                break;
            case 19://TODO Integer 4*n字节
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
            case 22:// uint8 4字节
                data = JSON.toJSONString(new Integer[]{ByteConverterUtil.byte2int(new byte[]{dataBytes[0]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[1]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[2]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[3]})});
                break;
            case 20:// char 32字节
            case 21:// char 32字节
            case 23:// char 32字节
                data = ByteConverterUtil.byte2string(dataBytes);
                break;
            default:
        }
        return data;
    }
}
