package com.redphase.service.analyze.data;

import com.redphase.framework.util.ByteConverterUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 负荷电流数据
 */
@Slf4j
public class AnalyzeLCData {

    /**
     * 2.数据格式要求。
     * ---------------------------------------------------------------------------------------
     * | 序号 |  数据类型    |  长度   |  备注
     * ---------------------------------------------------------------------------------------
     * |   1   |    int      |  4字节  | 数据长度 数据项1到5的字节长度。
     * |   2   |    float    |  4字节  | 规范版本号 所使用的数据格式规范版本号，例如1.0。
     * |   3   |    char     |  1字节  | 检测技术类型编码 标识该负荷电流文件是对于什么检测技术。,TEV: 0x01,AA: 0x02,AE: 0x03,HFCT: 0x04,UHF: 0x05
     * |   4   |    float    |  4字节  | 负荷电流
     * |   5   |    int      |  4字节  | 检验和 1到4项的数据校验。
     * ---------------------------------------------------------------------------------------
     */
    public Object LC(int i, byte[] dataBytes) throws Exception {
        Object data = null;
        switch (i + 1) {
            case 1:// int 4字节
            case 5:// int 4字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 2:// float 4字节
            case 4:// float 4字节
                data = ByteConverterUtil.byte2float(dataBytes);
                break;
            case 3:// char 1字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            default:
        }
        return data;
    }
}
