package com.redphase.service.analyze.data;

import com.redphase.framework.util.ByteConverterUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 环境参数数据
 */
@Slf4j
public class AnalyzeHJData {

    /**
     * 环境参数数据
     * 2.数据格式要求。
     * ---------------------------------------------------------------------------------------
     * | 序号 |  数据类型 |  长度   |  备注
     * ---------------------------------------------------------------------------------------
     * |   1  |  int32  |  4字节  |  数据长度  数据项1到11的字节长度。
     * |   2  |  float  |  4字节  |  规范版本号  所使用的数据格式规范版本号，例如1.0。
     * |   3  |  uint8  |  1字节  |  天气  表示天气;未记录：0xFF,晴：0x01,阴：0x02,雨：0x03,雪：0x04,雾：0x05,雷雨：0x06,多云：0x07
     * |   4  |  float  |  4字节  |  温度  环境温度，单位摄氏度。
     * |   5  |  int8   |  1字节  |  湿度  环境湿度，单位%。
     * |   6  |  char   |  20字节 |  检测人员1  UNICODE：2个字节;使用UNICODE编码。以0x0000结尾
     * |   7  |  char   |  20字节 |  检测人员2  UNICODE：2个字节;使用UNICODE编码。以0x0000结尾
     * |   8  |  char   |  20字节 |  检测人员3  UNICODE：2个字节;使用UNICODE编码。以0x0000结尾
     * |   9  |  char   |  20字节 |  检测人员4  UNICODE：2个字节;使用UNICODE编码。以0x0000结尾
     * |  10  |  char   |  20字节 |  检测人员5  UNICODE：2个字节;使用UNICODE编码。以0x0000结尾
     * |  11  |  int32  |  4字节  |  检验和  1到10项的数据校验。
     * ---------------------------------------------------------------------------------------
     */
    public Object HJ(int i, byte[] dataBytes) throws Exception {
        Object data = null;
        switch (i + 1) {
            case 3:// uint8 1字节
            case 5:// int8 1字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 1:// int32 4字节
            case 11:// int32 4字节
                data = ByteConverterUtil.byte2int(dataBytes);
                break;
            case 2:// float 4字节
            case 4:// float 4字节
                data = ByteConverterUtil.byte2float(dataBytes);
                break;
            case 6:// char 20字节
            case 7:// char 20字节
            case 8:// char 20字节
            case 9:// char 20字节
            case 10:// char 20字节
                data = ByteConverterUtil.byte2unicode(dataBytes);
                break;
            default:
        }
        return data;
    }
}
