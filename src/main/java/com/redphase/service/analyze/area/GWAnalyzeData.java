package com.redphase.service.analyze.area;

import com.alibaba.fastjson.JSON;
import com.redphase.framework.util.ByteConverterUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 地区-国网数据
 */
@Slf4j
public class GWAnalyzeData {

    /**
     * 高频统计数据
     * 2.数据格式要求。
     * ---------------------------------------------------------------------------------------
     * |    序号  |  数据类型   |  长度        |  备注
     * ---------------------------------------------------------------------------------------
     * 1	数据长度	     int	 4字节	数据包长度，含CRC校验。
     * 2	规范版本号	 float	 4字节	所使用的数据格式规范版本号，例如1.0。
     * 3	图谱类型编码	 char	 1字节	标识该文件是PRPD图谱还是PRPS图谱。PRPD:0x00 PRPS: 0x01
     * 4	相位窗数m	 int	 4字节	工频周期被等分成m个相位窗，每个相位窗跨360/m度。
     * 5	量化幅值n	 int	 4字节	幅值范围的等分区间数。如果该文件是PRPS图谱，则该4个字节清零，赋值为：0x00000000。
     * 6	幅值单位	     char	 1字节	表示幅值的单位 dBm: 0x01 mV: 0x02 %: 0x03
     * 7	幅值下限	     float	 4字节	仪器所能检测到的放电信号幅值的下限。
     * 8	幅值上限	     float	 4字节	仪器所能检测到的放电信号幅值的上限。
     * 9	工频周期数p	 int	 4字节	图谱工频周期的个数。如果该文件是PRPD图谱，则该4个字节清零，赋值为：0x00000000。
     * 10	放电类型概率	 char[8] 8字节	表示仪器诊断结果的放电类型概率。数组元素[0]至[7]分别存储正常、尖端放电、悬浮放电、沿面放电、内部放电、颗粒放电、外部干扰和其它共8种情况的概率，每个元素可以是整数0至100中的某数。实例：数组元素[3]为69，表示沿面放电的概率为69%。诊断结果为正常，则为数组元素[0]赋值100，数组元素[1]至[7]赋值0。如果仪器不具备放电类型诊断功能，则为数组元素[0]至[7]赋值0x00。
     * 11	局部放电图谱数据 int[m][n] 或 float[p][m]	 4*m*n字节或4*p*m 字节	如果该文件是PRPD图谱，则为int[m][n]，m为相位窗数，n为量化幅值，数组元素[x][y]的值表示在对应第x相位窗和第y幅值处发生的放电次数。如果该文件是PRPS图谱，则为float[p][m]，p为工频周期数，m为相位窗数，数组元素[x][y]的值表示在对应第x个周期的第y相位窗处发生的放电的幅值。
     * 12	CRC32	      int	 4字节	数据校验，使用CRC32算法。
     * ---------------------------------------------------------------------------------------
     */
    public Object HFCT_TJ(int i, byte[] dataBytes) throws Exception {
        Object data = null;
        switch (i + 1) {
            case 3:// char 1字节
            case 6:// char 1字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 1:// int 4字节
            case 4:// int 4字节
            case 5:// int 4字节
            case 9:// int 4字节
            case 12:// int 4字节
                data = ByteConverterUtil.byte2int(dataBytes);
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
            case 7:// float 4字节
            case 8:// float 4字节
                data = ByteConverterUtil.byte2float(dataBytes);
                break;
            case 10:// char 8字节
                data = JSON.toJSONString(new Integer[]{
                        ByteConverterUtil.byte2int(new byte[]{dataBytes[0]}),
                        ByteConverterUtil.byte2int(new byte[]{dataBytes[1]}),
                        ByteConverterUtil.byte2int(new byte[]{dataBytes[2]}),
                        ByteConverterUtil.byte2int(new byte[]{dataBytes[3]}),
                        ByteConverterUtil.byte2int(new byte[]{dataBytes[4]}),
                        ByteConverterUtil.byte2int(new byte[]{dataBytes[5]}),
                        ByteConverterUtil.byte2int(new byte[]{dataBytes[6]}),
                        ByteConverterUtil.byte2int(new byte[]{dataBytes[7]})
                });
                break;
            default:
        }
        return data;
    }

    /**
     * 特高频统计数据
     * 2.数据格式要求。
     * ---------------------------------------------------------------------------------------
     * |    序号  |  数据类型   |  长度        |  备注
     * ---------------------------------------------------------------------------------------
     * 1	数据长度	     int	 4字节	数据包长度，含CRC校验。
     * 2	规范版本号	 float	 4字节	所使用的数据格式规范版本号，例如1.0。
     * 3	图谱类型编码	 char	 1字节	标识该文件是PRPD图谱还是PRPS图谱。PRPD:0x00 PRPS: 0x01
     * 4	相位窗数m	 int	 4字节	工频周期被等分成m个相位窗，每个相位窗跨360/m度。
     * 5	量化幅值n	 int	 4字节	幅值范围的等分区间数。如果该文件是PRPS图谱，则该4个字节清零，赋值为：0x00000000。
     * 6	幅值单位	     char	 1字节	表示幅值的单位 dBm: 0x01 mV: 0x02 %: 0x03
     * 7	幅值下限	     float	 4字节	仪器所能检测到的放电信号幅值的下限。
     * 8	幅值上限	     float	 4字节	仪器所能检测到的放电信号幅值的上限。
     * 9	工频周期数p	 int	 4字节	图谱工频周期的个数。如果该文件是PRPD图谱，则该4个字节清零，赋值为：0x00000000。
     * 10	放电类型概率	 char[8] 8字节	表示仪器诊断结果的放电类型概率。数组元素[0]至[7]分别存储正常、尖端放电、悬浮放电、沿面放电、内部放电、颗粒放电、外部干扰和其它共8种情况的概率，每个元素可以是整数0至100中的某数。实例：数组元素[3]为69，表示沿面放电的概率为69%。诊断结果为正常，则为数组元素[0]赋值100，数组元素[1]至[7]赋值0。如果仪器不具备放电类型诊断功能，则为数组元素[0]至[7]赋值0x00。
     * 11	局部放电图谱数据 int[m][n] 或 float[p][m]	 4*m*n字节或4*p*m 字节	如果该文件是PRPD图谱，则为int[m][n]，m为相位窗数，n为量化幅值，数组元素[x][y]的值表示在对应第x相位窗和第y幅值处发生的放电次数。如果该文件是PRPS图谱，则为float[p][m]，p为工频周期数，m为相位窗数，数组元素[x][y]的值表示在对应第x个周期的第y相位窗处发生的放电的幅值。
     * 12	CRC32	      int	 4字节	数据校验，使用CRC32算法。
     * ---------------------------------------------------------------------------------------
     */
    public Object UHF_TJ(int i, byte[] dataBytes) throws Exception {
        Object data = null;
        switch (i + 1) {
            case 3:// char 1字节
            case 6:// char 1字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 1:// int 4字节
            case 4:// int 4字节
            case 5:// int 4字节
            case 9:// int 4字节
            case 12:// int 4字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 11:// float[p][m] 50*60*4字节
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
            case 7:// float 4字节
            case 8:// float 4字节
                data = ByteConverterUtil.byte2float(dataBytes);
                break;
            case 10:// char 8字节
                data = JSON.toJSONString(new Integer[]{
                        ByteConverterUtil.byte2int(new byte[]{dataBytes[0]}),
                        ByteConverterUtil.byte2int(new byte[]{dataBytes[1]}),
                        ByteConverterUtil.byte2int(new byte[]{dataBytes[2]}),
                        ByteConverterUtil.byte2int(new byte[]{dataBytes[3]}),
                        ByteConverterUtil.byte2int(new byte[]{dataBytes[4]}),
                        ByteConverterUtil.byte2int(new byte[]{dataBytes[5]}),
                        ByteConverterUtil.byte2int(new byte[]{dataBytes[6]}),
                        ByteConverterUtil.byte2int(new byte[]{dataBytes[7]})
                });
                break;
            default:
        }
        return data;
    }
}
