package com.redphase.service.analyze.data;

import com.alibaba.fastjson.JSON;
import com.redphase.framework.util.ByteConverterUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 地电波（TEV）数据
 */
@Slf4j
public class AnalyzeTEVData {
    /**
     * 3.1.1、地电波巡检噪声数据
     * 1、文件格式要求
     * 	同时保存噪声数据文件和界面的截屏图片，二者文件名相同；
     * 	文件名：变电站_检测对象_XJZS_噪声序号_日期时间；
     * 	噪声数据文件采用二进制编码，后缀为.dat，图片为JPG文件，噪声序号为0/1/2，共3个。如110kV夏河变_10kV开关柜_XJZS_0_20171112121212.dat。
     * 2.数据格式要求。
     * ---------------------------------------------------------------------------------------
     * | 序号 |  数据类型    |  长度   |  备注
     * ---------------------------------------------------------------------------------------
     * |   1  |  int32     |  4字节  | 数据长度   数据项1到校验和的字节长度。
     * |   2  |  float     |  4字节  | 规范版本号   所使用的数据格式规范版本号，例如1.0。
     * |   3  |  int64     |  8字节  | 图谱生成时间   生成图谱的时间，格式为YYYYMMDDhhmmssfff，例如20100818151010001。该时间与文件名的时间相同，只是文件名中的时间到秒，这边的到毫秒。
     * |   4  |  uint8     |  1字节  | 检测技术类型编码   标识该文件是采用什么技术获得。,TEV: 0x01,AA: 0x02,AE: 0x03,HFCT: 0x04,UHF: 0x05,TEV+AA：0x06
     * |   5  |  float     |  4字节  | 地电波幅值m1
     * |   6  |  float     |  4字节  | 地电波幅值最大值
     * |   7  |  int32     |  4字节  | 地电波放电频率n1
     * |   8  |  float     |  4字节  | 地电波烈度
     * |   9  |  uint8     |  1字节  | 地电波幅值单位   表示幅值的单位,
     * |  10  |  float     |  4字节  | 地电波幅值下限   仪器所能检测到的放电信号幅值的下限。暂定：0
     * |  11  |  float     |  4字节  | 地电波幅值上限   仪器所能检测到的放电信号幅值的上限。暂定：70
     * |  12  |  float     |  4字节  | 地电波参考阈值
     * |  13  |  float     |  4字节  | 地电波警戒值1   如20
     * |  14  |  float     |  4字节  | 地电波警戒值2   如25
     * |  15  |  float     |  4字节  | 地电波警戒值3   如30
     * |  16  |  float     |  4字节  | 系统频率
     * |  17  |  uint8     |  1字节  | 同步类型   外同步: 0x01,内同步: 0x02,默认写入内同步。
     * |  18  |  float     |  4字节  | 同步频率   此模式无法检测同步频率，因此将同步频率默认与系统频率相同。
     * |  19  |  float     |  4字节  | 空气超声幅值m2   当仅选择地电波检测时，AA幅值为某一个特定的数，如-100
     * |  20  |  int32     |  4字节  | 空气超声放电频率n2  当仅选择地电波检测时，AA放电频度值为某一个特定的数，如-100，
     * |  21  |  uint8     |  1字节  | 空气超声幅值单位   表示幅值的单位,dBuv: 0x04
     * |  22  |  float     |  4字节  | 空气超声幅值下限   仪器所能检测到的放电信号幅值的下限。暂定：-10
     * |  23  |  float     |  4字节  | 空气超声幅值上限   仪器所能检测到的放电信号幅值的上限。暂定：70
     * |  24  |  float     |  4字节  | 空气超声参考阈值
     * |  25  |  uint8     |  1字节  | 空气超声增益   60dB: 0x01,80dB: 0x02,100dB: 0x03
     * |  26  |  float     |  4字节  | 空气超声警戒值1   如6
     * |  27  |  float     |  4字节  | 空气超声警戒值2   如15
     * |  28  |  float     |  4字节  | 空气超声警戒值3   如20
     * |  29  |  char      |  32字节 | 仪器厂家   从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：RedPhase。
     * |  30  |  char      |  32字节 | 仪器型号   从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：PDT-400。
     * |  31  |  uint8     |  4字节  | 仪器版本号   从系统设置中提取，所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。,实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。
     * |  32  |  char      |  32字节 | 仪器序列号   从系统设置中提取，即仪器出厂编号，使用ASCII编码。以\0结尾，例如：S12300000000000000。
     * |  33  |  int32     |  4字节  | 校验和   1到校验和项前一项的数据校验。
     * ---------------------------------------------------------------------------------------
     */
    public Object XJZS(int i, byte[] dataBytes) throws Exception {
        Object data = null;
        switch (i + 1) {
            case 4: // uint8 1字节
            case 9: // uint8 1字节
            case 17: // uint8 1字节
            case 21: // uint8 1字节
            case 25: // uint8 1字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 31: // uint8 4字节
                data = JSON.toJSONString(new Integer[]{ByteConverterUtil.byte2int(new byte[]{dataBytes[0]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[1]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[2]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[3]})});
                break;
            case 1: // int32 4字节
            case 7: // int32 4字节
            case 20: // int32 4字节
            case 33: // int32 4字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 3: // int64 8字节
                data = ByteConverterUtil.byte2long(dataBytes);
                break;
            case 2: // float 4字节
            case 5: // float 4字节
            case 6: // float 4字节
            case 8: // float 4字节
            case 10: // float 4字节
            case 11: // float 4字节
            case 12: // float 4字节
            case 13: // float 4字节
            case 14: // float 4字节
            case 15: // float 4字节
            case 16: // float 4字节
            case 18: // float 4字节
            case 19: // float 4字节
            case 22: // float 4字节
            case 23: // float 4字节
            case 24: // float 4字节
            case 26: // float 4字节
            case 27: // float 4字节
            case 28: // float 4字节
                data = ByteConverterUtil.byte2float(dataBytes);
                break;
            case 29: // char 32字节
            case 30: // char 32字节
            case 32: // char 32字节
                data = ByteConverterUtil.byte2string(dataBytes);
                break;
            default:
        }
        return data;
    }

    /**
     * 地电波巡检数据
     * 1、文件格式要求
     * 	同时保存巡检数据文件和界面的截屏图片，二者文件名相同；
     * 	文件名：被测部位+测试点_XJ_日期时间；
     * 	巡检数据文件采用二进制编码，后缀为.dat，图片为JPG文件。如前下_XJ_20171112121212.dat。
     * 2.数据格式要求。
     * ---------------------------------------------------------------------------------------
     * | 序号 |  数据类型    |  长度   |  备注
     * ---------------------------------------------------------------------------------------
     * |   1  |  int32     |  4字节  |  数据长度 数据项1到校验和的字节长度。
     * |   2  |  float     |  4字节  |  规范版本号 所使用的数据格式规范版本号，例如1.0。
     * |   3  |  int64     |  8字节  |  图谱生成时间 生成图谱的时间，格式为YYYYMMDDhhmmssfff，例如20100818151010001。该时间与文件名的时间相同，只是文件名中的时间到秒，这边的到毫秒。
     * |   4  |  uint8     |  1字节  |  检测技术类型编码 标识该文件是采用什么技术获得。 TEV: 0x01 AA: 0x02 AE: 0x03 HFCT: 0x04 UHF: 0x05 TEV+AA：0x06
     * |   5  |  float     |  4字节  |  地电波幅值m1
     * |   6  |  float     |  4字节  |  地电波幅值最大值
     * |   7  |  int32     |  4字节  |  地电波放电频率n1
     * |   8  |  float     |  4字节  |  地电波烈度
     * |   9  |  uint8     |  1字节  |  地电波幅值单位 表示幅值的单位 dBmv: 0x03
     * |  10  |  float     |  4字节  |  地电波幅值下限 仪器所能检测到的放电信号幅值的下限。暂定：0
     * |  11  |  float     |  4字节  |  地电波幅值上限 仪器所能检测到的放电信号幅值的上限。暂定：70
     * |  12  |  float     |  4字节  |  地电波参考阈值
     * |  13  |  float     |  4字节  |  地电波警戒值1 如20
     * |  14  |  float     |  4字节  |  地电波警戒值2 如25
     * |  15  |  float     |  4字节  |  地电波警戒值3 如30
     * |  16  |  float     |  4字节  |  系统频率
     * |  17  |  uint8     |  1字节  |  同步类型 外同步: 0x01 内同步: 0x02默认写入内同步。
     * |  18  |  float     |  4字节  |  同步频率 此模式无法检测同步频率，因此将同步频率默认与系统频率相同。
     * |  19  |  float     |  4字节  |  空气超声幅值m2 当仅选择地电波检测时，AA幅值为某一个特定的数，如-100
     * |  20  |  int32     |  4字节  |  空气超声放电频率n2 当仅选择地电波检测时，AA放电频度值为某一个特定的数，如-100，
     * |  21  |  uint8     |  1字节  |  空气超声监听结果 表示空气超声的监听结果 正常: 0x01 异常: 0x02 未监听: 0x03 未检测: 0x04——当仅选择地电波检测时
     * |  22  |  uint8     |  1字节  |  空气超声幅值单位 表示幅值的单位 dBuv: 0x04
     * |  23  |  float     |  4字节  |  空气超声幅值下限 仪器所能检测到的放电信号幅值的下限。暂定：-10
     * |  24  |  float     |  4字节  |  空气超声幅值上限 仪器所能检测到的放电信号幅值的上限。暂定：70
     * |  25  |  float     |  4字节  |  空气超声参考阈值
     * |  26  |  uint8     |  1字节  |  空气超声增益 60dB: 0x01 80dB: 0x02 100dB: 0x03
     * |  27  |  float     |  4字节  |  空气超声警戒值1 如6
     * |  28  |  float     |  4字节  |  空气超声警戒值2 如15
     * |  29  |  float     |  4字节  |  空气超声警戒值3 如20
     * |  30  |  char      |  32字节 |  仪器厂家 从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：RedPhase。
     * |  31  |  char      |  32字节 |  仪器型号 从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：PDT-400。
     * |  32  |  uint8     |  4字节  |  仪器版本号  从系统设置中提取，所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。
     * |  33  |  char      |  32字节 |  仪器序列号 从系统设置中提取，即仪器出厂编号，使用ASCII编码。以\0结尾，例如：S12300000000000000。
     * |  34  |  int32     |  4字节  |  校验和 1到校验和项前一项的数据校验。
     * ---------------------------------------------------------------------------------------
     */
    public Object XJ(int i, byte[] dataBytes) throws Exception {
        Object data = null;
        switch (i + 1) {
            case 4: // uint8 1字节
            case 9: // uint8 1字节
            case 17: // uint8 1字节
            case 21: // uint8 1字节
            case 22: // uint8 1字节
            case 26: // uint8 1字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 3: // int64 8字节
                data = ByteConverterUtil.byte2long(dataBytes);
                break;
            case 1: // int32 4字节
            case 7: // int32 4字节
            case 20: // int32 4字节
            case 34: // int32 4字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 2: // float 4字节
            case 5: // float 4字节
            case 6: // float 4字节
            case 8: // float 4字节
            case 10: // float 4字节
            case 11: // float 4字节
            case 12: // float 4字节
            case 13: // float 4字节
            case 14: // float 4字节
            case 15: // float 4字节
            case 16: // float 4字节
            case 18: // float 4字节
            case 19: // float 4字节
            case 23: // float 4字节
            case 24: // float 4字节
            case 25: // float 4字节
            case 27: // float 4字节
            case 28: // float 4字节
            case 29: // float 4字节
                data = ByteConverterUtil.byte2float(dataBytes);
                break;
            case 30: // char 32字节
            case 31: // char 32字节
            case 33: // char 32字节
                data = ByteConverterUtil.byte2string(dataBytes);
                break;
            case 32: // uint8 4字节
                data = JSON.toJSONString(new Integer[]{ByteConverterUtil.byte2int(new byte[]{dataBytes[0]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[1]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[2]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[3]})});
                break;
            default:
        }
        return data;
    }

    /**
     * 统计噪声
     * 文件格式要求
     * 	同时保存噪声数据文件和界面的截屏图片，二者文件名相同；
     * 	文件名：变电站_检测对象_TJZS_噪声序号_日期时间；
     * 	噪声数据文件采用二进制编码，后缀为.dat，图片为JPG文件，噪声序号为0/1/2，共3个。如110kV夏河变_10kV开关柜_TJZS_0_20171112121212.dat。
     * 2.数据格式要求。
     * ---------------------------------------------------------------------------------------
     * | 序号 |  数据类型    |  长度   |  备注
     * ---------------------------------------------------------------------------------------
     * |   1  |  int32     |  4字节  |  数据长度  数据项1到12的字节长度。
     * |   2  |  float     |  4字节  |  规范版本号  所使用的数据格式规范版本号，例如1.0。
     * |   3  |  uint8     |  1字节  |  图谱类型编码  标识该文件是PRPD图谱还是PRPS图谱。,PRPS: 0x01
     * |   4  |  int32     |  4字节  |  相位窗数m  工频周期被等分成m个相位窗，每个相位窗跨 360/m 度。,m = dataBytes.length / 4 / xlen（暂定）
     * |   5  |  int32     |  4字节  |  量化幅值n  赋值为：0x00000000。
     * |   6  |  uint8     |  1字节  |  幅值单位  表示幅值的单位,dBmv: 0x03
     * |   7  |  float     |  4字节  |  幅值下限  仪器所能检测到的放电信号幅值的下限。暂定：0
     * |   8  |  float     |  4字节  |  幅值上限  仪器所能检测到的放电信号幅值的上限。暂定：70
     * |   9  |  int32     |  4字节  |  工频周期数p  图谱工频周期的个数。,工频周期数=50.
     * |  10  |  uint8     |  8字节  |  放电类型概率  表示仪器诊断结果的放电类型概率。数组元素[0]至[7]分别存储正常、尖端放电、悬浮放电、沿面放电、内部放电、颗粒放电、外部干扰和其它共8种情况的概率，每个元素可以是整数0至100中的某数。实例：数组元素[3]为69，表示沿面放电的概率为69%。,如果诊断结果为正常，则为数组元素[0]赋值100，数组元素[1]至[7]赋值0。,先按照以下规则处理,如果仪器不具备放电类型诊断功能，则为数组元素[0]至[7]赋值0x00。
     * |  11  |float[p][m] |4*50*120字节|  局部放电图谱数据  该文件是PRPS图谱，则为float[p][m]，p为工频周期数——50，m为相位窗数——120，数组元素[x][y]的值表示在对应第x个周期的第y相位窗处发生的放电的幅值。
     * |  12  |  int32     |  4字节  |  CRC32  数据校验，使用CRC32算法。针对1到11项
     * |  13  |  int32     |  4字节  |  数据长度  数据从本项开始一直到校验和的字节长度。
     * |  14  |  int64     |  8字节  |  图谱生成时间  生成图谱的时间，格式为YYYYMMDDhhmmssfff，例如20100818151010001。该时间与文件名的时间相同，只是文件名中的时间到秒，这边的到毫秒。
     * |  15  |  uint8     |  1字节  |  检测技术类型编码  标识该文件是采用什么技术获得。,TEV: 0x01,AA: 0x02,AE: 0x03,HFCT: 0x04,UHF: 0x05
     * |  16  |  float     |  4字节  |  地电波幅值  表示统计模式下当前1秒的幅值
     * |  17  |  int32     |  4字节  |  地电波放电频度，即脉冲个数  表示统计模式下当前1秒的脉冲个数
     * |  18  |  float     |  4字节  |  地电波参考阈值
     * |  19  |  float     |  4字节  |  地电波相位
     * |  20  |  uint8     |  1字节  |  同步类型  外同步: 0x01,内同步: 0x02
     * |  21  |  float     |  4字节  |  同步频率
     * |  22  |  float     |  4字节  |  系统频率
     * |  23  |  float     |  4字节  |  地电波警戒值1  如20
     * |  24  |  float     |  4字节  |  地电波警戒值2  如25
     * |  25  |  float     |  4字节  |  地电波警戒值3  如30
     * |  26  |  char      |  32字节 |  仪器厂家  从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：RedPhase。
     * |  27  |  char      |  32字节 |  仪器型号  从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：PDT-400。
     * |  28  |  uint8     |  4字节  |  仪器版本号  从系统设置中提取，所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。,实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。
     * |  29  |  char      |  32字节 |  仪器序列号  从系统设置中提取，即仪器出厂编号，使用ASCII编码。以\0结尾，例如：S12300000000000000。
     * |  30  |  int32     |  4字节  |  检验和  数据长度到校验和项之前一项的数据校验。
     * ---------------------------------------------------------------------------------------
     */
    public Object TJZS(int i, byte[] dataBytes) throws Exception {
        Object data = null;
        switch (i + 1) {
            case 3://uint8 1字节
            case 6://uint8 1字节
            case 15://uint8 1字节
            case 20://uint8 1字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 10://uint8 8字节
                data = ByteConverterUtil.byte2int(new byte[]{dataBytes[0]})
                        + ByteConverterUtil.byte2int(new byte[]{dataBytes[1]})
                        + ByteConverterUtil.byte2int(new byte[]{dataBytes[2]})
                        + ByteConverterUtil.byte2int(new byte[]{dataBytes[3]})
                        + ByteConverterUtil.byte2int(new byte[]{dataBytes[4]})
                        + ByteConverterUtil.byte2int(new byte[]{dataBytes[5]})
                        + ByteConverterUtil.byte2int(new byte[]{dataBytes[6]})
                        + ByteConverterUtil.byte2int(new byte[]{dataBytes[7]}
                );
                break;
            case 1://int32 4字节
            case 4://int32 4字节
            case 5://int32 4字节
            case 9://int32 4字节
            case 12://int32 4字节
            case 13://int32 4字节
            case 17://int32 4字节
            case 30://int32 4字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 11://float[p][m]4*50*120字节
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
            case 14://int64 8字节
                data = ByteConverterUtil.byte2long(dataBytes);
                break;
            case 28://uint8 4字节
                data = JSON.toJSONString(new Integer[]{ByteConverterUtil.byte2int(new byte[]{dataBytes[0]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[1]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[2]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[3]})});
                break;
            case 2://float 4字节
            case 7://float 4字节
            case 8://float 4字节
            case 16://float 4字节
            case 18://float 4字节
            case 19://float 4字节
            case 21://float 4字节
            case 22://float 4字节
            case 23://float 4字节
            case 24://float 4字节
            case 25://float 4字节
                data = ByteConverterUtil.byte2float(dataBytes);
                break;
            case 26://char  32字节
            case 27://char  32字节
            case 29://char  32字节
                data = ByteConverterUtil.byte2string(dataBytes);
                break;
            default:
        }
        return data;
    }

    /**
     * 地电波统计数据
     * 1.文件格式要求
     * 文件采用扩展名为.TEV的二进制数据格式进行存储，每个.TEV文件存储一张PRPS图谱的数据。
     * 文件名规则为：仪器厂家及型号编码_图谱类型_图谱生成时间.TEV。仪器厂家及型号编码为数字、字母或两者的组合，最多为32个字符。图谱类型： PRPS图谱为 1。图谱生成日期格式为：YYYYMMDDhhmmss。
     * 仪器厂家及型号编码、图谱类型、图谱生成时间之间用下划线分隔。
     * 示例：RedPhasePDT400_1_20140823185809.TEV
     * 每个文件大小不超过500KB。
     * 2.数据格式要求。
     * ---------------------------------------------------------------------------------------
     * | 序号 |  数据类型    |  长度     |  备注
     * ---------------------------------------------------------------------------------------
     * |   1  |   int32     |   4字节  | 数据长度   数据项1到12的字节长度。
     * |   2  |   float     |   4字节  | 规范版本号   所使用的数据格式规范版本号，例如1.0。
     * |   3  |   uint8     |   1字节  | 图谱类型编码   标识该文件是PRPD图谱还是PRPS图谱。,PRPS: 0x01
     * |   4  |   int32     |   4字节  | 相位窗数m   工频周期被等分成m个相位窗，每个相位窗跨 360/m 度。,m = dataBytes.length / 4 / xlen（暂定）
     * |   5  |   int32     |   4字节  | 量化幅值n   赋值为：0x00000000。
     * |   6  |   uint8     |   1字节  | 幅值单位   表示幅值的单位,dBmv: 0x03
     * |   7  |   float     |   4字节  | 幅值下限   仪器所能检测到的放电信号幅值的下限。暂定：0
     * |   8  |   float     |   4字节  | 幅值上限   仪器所能检测到的放电信号幅值的上限。暂定：70
     * |   9  |   int32     |   4字节  | 工频周期数p   图谱工频周期的个数。,工频周期数=50.
     * |  10  |   uint8     |   8字节  | 放电类型概率   表示仪器诊断结果的放电类型概率。数组元素[0]至[7]分别存储正常、尖端放电、悬浮放电、沿面放电、内部放电、颗粒放电、外部干扰和其它共8种情况的概率，每个元素可以是整数0至100中的某数。实例：数组元素[3]为69，表示沿面放电的概率为69%。,如果诊断结果为正常，则为数组元素[0]赋值100，数组元素[1]至[7]赋值0。,先按照以下规则处理,如果仪器不具备放电类型诊断功能，则为数组元素[0]至[7]赋值0x00。
     * |  11  | float[p][m] |4*50*120 字节| 局部放电图谱数据   该文件是PRPS图谱，则为float[p][m]，p为工频周期数——50，m为相位窗数——120，数组元素[x][y]的值表示在对应第x个周期的第y相位窗处发生的放电的幅值。
     * |  12  |   int32     |   4字节  | CRC32   数据校验，使用CRC32算法。针对1到11项
     * |  13  |   int32     |   4字节  | 数据长度   数据从本项开始一直到校验和的字节长度。
     * |  14  |   int64     |   8字节  | 图谱生成时间   生成图谱的时间，格式为YYYYMMDDhhmmssfff，例如20100818151010001。该时间与文件名的时间相同，只是文件名中的时间到秒，这边的到毫秒。
     * |  15  |   uint8     |   1字节  | 检测技术类型编码   标识该文件是采用什么技术获得。,TEV: 0x01,AA: 0x02,AE: 0x03,HFCT: 0x04,UHF: 0x05
     * |  16  |   float     |   4字节  | 地电波幅值   表示统计模式下当前1秒的幅值
     * |  17  |   int32     |   4字节  | 地电波放电频度，即脉冲个数   表示统计模式下当前1秒的脉冲个数
     * |  18  |   float     |   4字节  | 地电波参考阈值
     * |  19  |   float     |   4字节  | 地电波相位
     * |  20  |   uint8     |   1字节  | 同步类型   外同步: 0x01,内同步: 0x02
     * |  21  |   float     |   4字节  | 同步频率
     * |  22  |   float     |   4字节  | 系统频率
     * |  23  |   float     |   4字节  | 地电波警戒值1   如20
     * |  24  |   float     |   4字节  | 地电波警戒值2   如25
     * |  25  |   float     |   4字节  | 地电波警戒值3   如30
     * |  26  |   char      |   32字节 | 仪器厂家   从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：RedPhase。
     * |  27  |   char      |   32字节 | 仪器型号   从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：PDT-400。
     * |  28  |   uint8     |   4字节  | 仪器版本号   从系统设置中提取，所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。,实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。
     * |  29  |   char      |   32字节 | 仪器序列号   从系统设置中提取，即仪器出厂编号，使用ASCII编码。以\0结尾，例如：S12300000000000000。
     * |  30  |   int32     |   4字节  | 检验和   数据长度到校验和项之前一项的数据校验。
     * ---------------------------------------------------------------------------------------
     */
    public Object TJ(int i, byte[] dataBytes) throws Exception {
        Object data = null;
        switch (i + 1) {
            case 3: // uint8 1字节
            case 6: // uint8 1字节
            case 15: // uint8 1字节
            case 20: // uint8 1字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 10: // uint8 8字节
                data = ByteConverterUtil.byte2int(new byte[]{dataBytes[0]})
                        + ByteConverterUtil.byte2int(new byte[]{dataBytes[1]})
                        + ByteConverterUtil.byte2int(new byte[]{dataBytes[2]})
                        + ByteConverterUtil.byte2int(new byte[]{dataBytes[3]})
                        + ByteConverterUtil.byte2int(new byte[]{dataBytes[4]})
                        + ByteConverterUtil.byte2int(new byte[]{dataBytes[5]})
                        + ByteConverterUtil.byte2int(new byte[]{dataBytes[6]})
                        + ByteConverterUtil.byte2int(new byte[]{dataBytes[7]}
                );
                break;
            case 14: // int64 8字节
                data = ByteConverterUtil.byte2long(dataBytes);
                break;
            case 28: // uint8 4字节
                data = JSON.toJSONString(new Integer[]{ByteConverterUtil.byte2int(new byte[]{dataBytes[0]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[1]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[2]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[3]})});
                break;
            case 1: // int32 4字节
            case 4: // int32 4字节
            case 5: // int32 4字节
            case 9: // int32 4字节
            case 12: // int32 4字节
            case 13: // int32 4字节
            case 17: // int32 4字节
            case 30: // int32 4字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 11: // float[p][m] 4*50*120字节
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
            case 2: // float 4字节
            case 7: // float 4字节
            case 8: // float 4字节
            case 16: // float 4字节
            case 18: // float 4字节
            case 19: // float 4字节
            case 21: // float 4字节
            case 22: // float 4字节
            case 23: // float 4字节
            case 24: // float 4字节
            case 25: // float 4字节
                data = ByteConverterUtil.byte2float(dataBytes);
                break;
            case 26: // char 32字节
            case 27: // char 32字节
            case 29: // char 32字节
                data = ByteConverterUtil.byte2string(dataBytes);
                break;
            default:
        }
        return data;
    }

    /**
     * 地电波统计录波数据
     * 1.文件格式要求
     * 文件采用扩展名为.TJLB的二进制数据格式进行存储，每个.TJLB文件存储录波数据。
     * 文件名规则为：仪器厂家及型号编码_图谱类型_图谱生成时间. TJLB。仪器厂家及型号编码为数字、字母或两者的组合，最多为32个字符。图谱类型： PRPS图谱为 1。图谱生成日期格式为：YYYYMMDDhhmmss。
     * 仪器厂家及型号编码、图谱类型、图谱生成时间之间用下划线分隔。
     * 录波数据文件的文件名与统计数据文件名相同。
     * 示例：RedPhasePDT400_1_20140823185809.TJLB
     * 2.数据格式要求。
     * ---------------------------------------------------------------------------------------
     * | 序号 |  数据类型    |  长度   |  备注
     * ---------------------------------------------------------------------------------------
     * |   1  |  int32      |  4字节  | 数据长度  数据项1到最后一项的字节长度。
     * |   2  |  float      |  4字节  | 规范版本号  所使用的数据格式规范版本号，例如1.0。
     * |   3  |  int64      |  8字节  | 图谱生成时间  生成图谱的时间，格式为YYYYMMDDhhmmssfff，例如20100818151010001。该时间与文件名的时间相同，只是文件名中的时间到秒，这边的到毫秒。
     * |   4  |  uint8      |  1字节  | 检测技术类型编码  标识该文件是采用什么技术获得。,TEV: 0x01,AA: 0x02,AE: 0x03,HFCT: 0x04,UHF: 0x05
     * |   5  |  uint8      |  1字节  | 图谱类型编码  标识该文件是PRPD图谱还是PRPS图谱。,PRPS: 0x01
     * |   6  |  int32      |  4字节  | 相位窗数m  工频周期被等分成m个相位窗，每个相位窗跨 360/m 度。,m = dataBytes.length / 4 / xlen（暂定）
     * |   7  |  uint8      |  1字节  | 幅值单位  表示幅值的单位,dBmv: 0x03
     * |   8  |  float      |  4字节  | 幅值下限  仪器所能检测到的放电信号幅值的下限。暂定：0
     * |   9  |  float      |  4字节  | 幅值上限  仪器所能检测到的放电信号幅值的上限。暂定：70
     * |  10  |  int32      |  4字节  | 工频周期数p  图谱工频周期的个数。,工频周期数=500.
     * |  11  | float[p][m] |4*500*120字节| 局部放电图谱数据  该文件是PRPS图谱，则为float[p][m]，p为工频周期数——500（表示10秒，每秒50个），m为相位窗数——120，数组元素[x][y]的值表示在对应第x个周期的第y相位窗处发生的放电的幅值。
     * |  12  |  float      |  4字节  | 地电波参考阈值
     * |  13  |  float      |  4字节  | 地电波相位
     * |  14  |  char       |  1字节  | 同步类型  外同步: 0x01,内同步: 0x02
     * |  15  |  float      |  4字节  | 同步频率
     * |  16  |  float      |  4字节  | 系统频率
     * |  17  |  float      |  4字节  | 地电波警戒值1  如20
     * |  18  |  float      |  4字节  | 地电波警戒值2  如25
     * |  19  |  float      |  4字节  | 地电波警戒值3  如30
     * |  20  |  char       |  32字节 | 仪器厂家  从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：RedPhase。
     * |  21  |  char       |  32字节 | 仪器型号  从系统设置中提取，使用UNICODE编码。以0x0000结尾，例如：PDT-400。
     * |  22  |  uint8      |  4字节  | 仪器版本号  从系统设置中提取，所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。,实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。
     * |  23  |  char       |  32字节 | 仪器序列号  从系统设置中提取，即仪器出厂编号，使用ASCII编码。以\0结尾，例如：S12300000000000000。
     * |  24  |  int        |  4字节  | 检验和  1到校验和项之前一项的数据校验。
     * ---------------------------------------------------------------------------------------
     */
    public Object TJLB(int i, byte[] dataBytes) throws Exception {
        Object data = null;
        switch (i + 1) {
            case 4://uint8 1字节
            case 5://uint8 1字节
            case 7://uint8 1字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 3://int64 8字节
                data = ByteConverterUtil.byte2long(dataBytes);
                break;
            case 1://int32 4字节
            case 6://int32 4字节
            case 10://int32 4字节
            case 24://int32 4字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 11://float[p][m]4*500*120字节
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
            case 22://uint8 4字节
                data = JSON.toJSONString(new Integer[]{ByteConverterUtil.byte2int(new byte[]{dataBytes[0]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[1]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[2]}), ByteConverterUtil.byte2int(new byte[]{dataBytes[3]})});
                break;
            case 2://float 4字节
            case 8://float 4字节
            case 9://float 4字节
            case 12://float 4字节
            case 13://float 4字节
            case 15://float 4字节
            case 16://float 4字节
            case 17://float 4字节
            case 18://float 4字节
            case 19://float 4字节
                data = ByteConverterUtil.byte2float(dataBytes);
                break;
            case 14://char  1字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 20://char  32字节
                data = ByteConverterUtil.byte2unicode(dataBytes);
                break;
            case 21://char  32字节
            case 23://char  32字节
                data = ByteConverterUtil.byte2string(dataBytes);
                break;
            default:
        }
        return data;
    }
}
