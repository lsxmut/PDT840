package com.redphase.service.convert.tofile;

import com.alibaba.fastjson.JSON;
import com.redphase.dto.atlas.DataHjDto;
import com.redphase.entity.atlas.DataDeviceSite;
import com.redphase.framework.util.ByteConverterUtil;
import com.redphase.service.analyze.AnalyzeData;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

@Data
@Slf4j
public abstract class BaseData2File {
    protected static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    public static String getPathAndMkdirs(String dirFilePath) {
        File fileDir = new File(dirFilePath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return fileDir.getPath();
    }

    /**
     * 超声传感器类型
     *
     * @param testTechnology 检测技术
     * @param deviceType     设备类型(1、开关柜；2、变压器；3、组合电器；4、电缆)
     */
    public static byte getSensorsType(String testTechnology, String deviceType) {
        //0xFF：未记录
        //0x01：空声---开关柜，下同
        //0x02：SF6气体绝缘电力设备检测用接触式---组合电器，下同
        //0x03：充油电力设备检测用接触式---变压器，下同
        //0x04：其他类型
        byte value = 0x04;
        switch (deviceType) {
            case "1": {
                value = 0x01;
                break;
            }
            case "2": {
                value = 0x03;
                break;
            }
            case "3": {
                value = 0x02;
                break;
            }
            default: {
                value = 0x04;
                break;
            }
        }
        return value;
    }

    /**
     * 设备位置byte[]
     */
    public static byte[] siteName2byte(DataDeviceSite dataDeviceSite) {
        byte[] value = {};
        if (dataDeviceSite != null) {
            value = ByteConverterUtil.unicode2byte(dataDeviceSite.getSiteName());
        }
        return value;
    }

    /**
     * 天气byte 天气 - 表示天气。0xFF：未记录0x01：晴0x02：阴0x03：雨0x04：雪0x05：雾0x06：雷雨0x07：多云
     */
    public static byte hjTQ2byte(DataHjDto dataHjDto) {
        byte value = (byte) 0xFF;
        if (dataHjDto != null && dataHjDto.getX3() != null) {
            value = dataHjDto.getX3().byteValue();
        }
        return value;
    }

    /**
     * 温度byte[] - 环境温度，单位摄氏度。
     */
    public static byte[] hjWD2byte(DataHjDto dataHjDto) {
        byte value[] = {};
        if (dataHjDto != null && dataHjDto.getX4() != null) {
            value = ByteConverterUtil.float2byte(dataHjDto.getX4());
        }
        return value;
    }

    /**
     * 湿度 - 环境湿度，单位%。
     */
    public static byte hjSD2byte(DataHjDto dataHjDto) {
        byte value = (byte) 0xFF;
        if (dataHjDto != null && dataHjDto.getX5() != null) {
            value = dataHjDto.getX5().byteValue();
        }
        return value;
    }

    /**
     * 幅值单位 - TEV:dBmV,AA:dBuV,AE:mV,HFCT:mV,UHF:dBm
     */
    public static byte fzdw2byte(Object fzdw, byte defaultVal) {
        byte value = (byte) 0xFF;
        if (fzdw != null) {
            if (fzdw instanceof String) {
                value = new Byte(fzdw.toString()).byteValue();
            } else if (fzdw instanceof Integer) {
                value = ((Integer) fzdw).byteValue();
            }
        } else {
            value = defaultVal;
        }
        return value;
    }

    /**
     * 调试解析
     */
    public static void debugAnalyzeData(String dataFilePath) {
        try {
            if (log.isDebugEnabled()) {
                AnalyzeData analyzeData = new AnalyzeData();
                File dataFile = new File(dataFilePath);
                String fileName = dataFile.getName();
                Object data=null;
                if (fileName.startsWith("AA_BX")) {
                    data = analyzeData.parse(dataFilePath, AnalyzeData.DataType.JS_AA_BX);
                } else if (fileName.startsWith("AA_FX")) {
                    data = analyzeData.parse(dataFilePath, AnalyzeData.DataType.JS_AA_FX);
                } else if (fileName.startsWith("AA_TJ")) {
                    data = analyzeData.parse(dataFilePath, AnalyzeData.DataType.JS_AA_TJ);
                } else if (fileName.startsWith("AA_TJZS")) {
                    data = analyzeData.parse(dataFilePath, AnalyzeData.DataType.JS_AA_TJZS);
                } else if (fileName.startsWith("AA_XJ")) {
                    data = analyzeData.parse(dataFilePath, AnalyzeData.DataType.JS_AA_XJ);
                } else if (fileName.startsWith("AA_XJZS")) {
                    data = analyzeData.parse(dataFilePath, AnalyzeData.DataType.JS_AA_XJZS);
                } else if (fileName.startsWith("AE_BX")) {
                    data = analyzeData.parse(dataFilePath, AnalyzeData.DataType.JS_AE_BX);
                } else if (fileName.startsWith("AE_FX")) {
                    data = analyzeData.parse(dataFilePath, AnalyzeData.DataType.JS_AE_FX);
                } else if (fileName.startsWith("AE_TJ")) {
                    data = analyzeData.parse(dataFilePath, AnalyzeData.DataType.JS_AE_TJ);
                } else if (fileName.startsWith("AE_TJZS")) {
                    data = analyzeData.parse(dataFilePath, AnalyzeData.DataType.JS_AE_TJZS);
                } else if (fileName.startsWith("AE_XJ")) {
                    data = analyzeData.parse(dataFilePath, AnalyzeData.DataType.JS_AE_XJ);
                } else if (fileName.startsWith("AE_XJZS")) {
                    data = analyzeData.parse(dataFilePath, AnalyzeData.DataType.JS_AE_XJZS);
                } else if (fileName.startsWith("HFCT_TJ")) {
                    data = analyzeData.parse(dataFilePath, AnalyzeData.DataType.JS_HFCT_TJ);
                } else if (fileName.startsWith("HFCT_TJZS")) {
                    data = analyzeData.parse(dataFilePath, AnalyzeData.DataType.JS_HFCT_TJZS);
                } else if (fileName.startsWith("TEV_XJ")) {
                    data = analyzeData.parse(dataFilePath, AnalyzeData.DataType.JS_TEV_XJ);
                } else if (fileName.startsWith("TEV_XJZS")) {
                    data = analyzeData.parse(dataFilePath, AnalyzeData.DataType.JS_TEV_XJZS);
                } else if (fileName.startsWith("UHF_TJ")) {
                    data = analyzeData.parse(dataFilePath, AnalyzeData.DataType.JS_UHF_TJ);
                } else if (fileName.startsWith("UHF_TJZS")) {
                    data = analyzeData.parse(dataFilePath, AnalyzeData.DataType.JS_UHF_TJZS);
                }
                if(data!=null){
                    PrintWriter pw=new PrintWriter(dataFilePath+".debug");
                    pw.write(JSON.toJSONString(data));
                    pw.flush();
                    pw.close();
                }
            }
        } catch (Exception e) {
            log.error("江苏数据解析异常!" + dataFilePath, e);
        }
    }
}