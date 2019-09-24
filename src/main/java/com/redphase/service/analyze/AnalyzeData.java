package com.redphase.service.analyze;

import com.alibaba.fastjson.JSON;
import com.redphase.dto.atlas.DataHjDto;
import com.redphase.dto.atlas.DataLcDto;
import com.redphase.dto.atlas.aa.*;
import com.redphase.dto.atlas.ae.*;
import com.redphase.dto.convert.*;
import com.redphase.dto.atlas.hfct.*;
import com.redphase.dto.atlas.tev.*;
import com.redphase.dto.atlas.uhf.*;
import com.redphase.framework.util.CommonConstant;
import com.redphase.framework.util.ReflectUtil;
import com.redphase.service.analyze.area.*;
import com.redphase.service.analyze.data.*;
import com.redphase.service.convert.tofile.JSDataType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;

/**
 * 解析数据
 */
@Service
@Slf4j
public class AnalyzeData {
    private AnalyzeTEVData analyzeTEVData = new AnalyzeTEVData();
    private AnalyzeAAData analyzeAAData = new AnalyzeAAData();
    private AnalyzeHFCTData analyzeHFCTData = new AnalyzeHFCTData();
    private AnalyzeAEData analyzeAEData = new AnalyzeAEData();
    private AnalyzeUHFData analyzeUHFData = new AnalyzeUHFData();
    private AnalyzeLCData analyzeLCData = new AnalyzeLCData();
    private AnalyzeHJData analyzeHJData = new AnalyzeHJData();
    private GWAnalyzeData gwAnalyzeData = new GWAnalyzeData();
    private JSAnalyzeAAData jsAnalyzeAAData = new JSAnalyzeAAData();
    private JSAnalyzeAEData jsAnalyzeAEData = new JSAnalyzeAEData();
    private JSAnalyzeHFCTData jsAnalyzeHFCTData = new JSAnalyzeHFCTData();
    private JSAnalyzeUHFData jsAnalyzeUHFData = new JSAnalyzeUHFData();
    private JSAnalyzeTEVData jsAnalyzeTEVData = new JSAnalyzeTEVData();

    public static void main(String[] args) throws Exception {
        AnalyzeData analyzeData = new AnalyzeData();
//        Object obj = analyzeData.parse("D:\\PDT840\\前中_BX_20100101085122.dat", DataType.AA_BX);
//        Object obj = analyzeData.parse("E:\\AA\\红相高压实验室_10kV开关柜_AA_20180820\\#01开关柜\\前中\\RedPhasePDT400_1_20180825082811.AA", DataType.AA_TJ);
//        Object objlb = analyzeData.parse("E:\\AA\\红相高压实验室_10kV开关柜_AA_20180820\\#01开关柜\\前中\\RedPhasePDT400_1_20180825090521.TJLB", DataType.AA_TJLB);
//        Object objlb = analyzeData.parse("D:\\documents\\WeChat Files\\wu1g119\\Files\\RedPhasePDT840_1_20160526094210.dat", DataType.GW_UHF_TJ);
//        Object objlb = analyzeData.parse("D:\\PDT840\\data\\红相车间\\局部放电测试任务2018-09-27\\车间实验室\\开关柜\\检测数据\\车间实验室_10kV开关柜_TEV_20180926\\车间实验室_10kV开关柜_XJZS_0_20180926170014.dat", DataType.TEV_XJZS);
//        Object objlb = analyzeData.parse("D:\\PDT840\\data\\红相车间\\局部放电测试任务2018-09-27\\车间实验室\\开关柜\\检测数据\\车间实验室_10kV开关柜_TEV_20180926\\#001号开关柜\\前中\\前中_XJ_20180926170931.dat", DataType.TEV_XJ);
//        analyzeData.parse(rootDir + "/TEV/110kV夏河变_10kV开关柜_TEV_20180409/10kV母分900断路器/前中/前中_XJ_20180409154447.dat", DataType.TEV_XJ);
//        analyzeData.parse(rootDir + "/TEV/110kV夏河变_10kV开关柜_TEV_20180409/10kV母分900断路器/10kV母分900断路器_LC_20180409.dat", DataType.LC);
//        analyzeData.parse(rootDir + "/TEV/110kV夏河变_10kV开关柜_TEV_20180409/110kV夏河变_10kV开关柜_TEV_HJ_20180409154436.dat", DataType.HJ);
//        System.out.println(JSON.toJSONString(obj));
        Object data = analyzeData.parse("D:\\PDT840\\江苏\\UHF\\UHF_TJ_20100101011418009.dat", DataType.JS_UHF_TJ);

        System.out.println(JSON.toJSONString(data));
    }

    public Object parse(String url, DataType dataType) throws Exception {
        log.debug("parse data==>{},{}", dataType.modeType, dataType.dataName);
//        List<Object> objectList=new ArrayList<>();
        File dataFile = new File(url);
        int[] dataRowBytes = new int[dataType.dataRowBytes.length];
        System.arraycopy(dataType.dataRowBytes, 0, dataRowBytes, 0, dataType.dataRowBytes.length);
        int dataRowLength = 0;
        int bigByteIndex = -1;
        for (int i = 0; i < dataRowBytes.length; i++) {
            int databyte = dataRowBytes[i];
            if (databyte == CommonConstant.BIG_DATA_LENGTH) {
                bigByteIndex = i;
            } else {
                dataRowLength += databyte;
            }
        }

        if (bigByteIndex != -1) {
            dataRowBytes[bigByteIndex] = (int) (dataFile.length() - dataRowLength);
            dataRowLength += dataRowBytes[bigByteIndex];
        }

        log.debug("length==>{}", dataRowLength);
        log.debug("fileSize==>{}", dataFile.length());
//        if (log.isDebugEnabled() && dataRowLength != dataFile.length()) {
//            FileWriter fw = new FileWriter("d:/123.txt", true);
//            fw.append(url + "==>" + dataType.dataName + "=>" + dataRowLength + ":" + dataFile.length());
//            fw.append("");
//            fw.flush();
//            fw.close();
//        }

        FileInputStream inputStream = new FileInputStream(dataFile);
        byte[] dataRowBuffer = new byte[dataRowLength];

//        StringBuilder sb = new StringBuilder("\n");
        if (inputStream.read(dataRowBuffer) > 0) {// == dataRowBuffer.length
            int offset = 0;
            Object dataObj = dataType.dataObject.newInstance();
            for (int i = 0; i < dataRowBytes.length; i++) {
                byte[] destBytes = new byte[dataRowBytes[i]];
                System.arraycopy(dataRowBuffer, offset, destBytes, 0, destBytes.length);
                offset += destBytes.length;
                int dataNum = i + 1;
                Object data = null;
                switch (dataType.modeType) {
                    case "TEV": {
                        data = parseTEV(dataType, i, destBytes);
                        break;
                    }
                    case "AA": {
                        data = parseAA(dataType, i, destBytes);
                        break;
                    }
                    case "HFCT": {
                        data = parseHFCT(dataType, i, destBytes);
                        break;
                    }
                    case "AE": {
                        data = parseAE(dataType, i, destBytes);
                        break;
                    }
                    case "UHF": {
                        data = parseUHF(dataType, i, destBytes);
                        break;
                    }
                    case "LC": {
                        data = parseLC(dataType, i, destBytes);
                        break;
                    }
                    case "HJ": {
                        data = parseHJ(dataType, i, destBytes);
                        break;
                    }
                    case "GW": {
                        data = parseGW(dataType, i, destBytes);
                        break;
                    }
                    case "JS": {
                        data = parseJS(dataType, i, destBytes);
                        break;
                    }
                    default:
                }
                ReflectUtil.setValueByFieldName(dataObj, "x" + dataNum, data);
//
//                sb.append("" + (i + 1) + ",");
//                sb.append("" + destBytes.length + "字节,{");
//                for (int y = 0; y < destBytes.length; y++) {
//                    if (y > 0) {
//                        sb.append(",");
//                    }
//                    sb.append(destBytes[y]);
//                }
//                sb.append("}==>");
//                sb.append(data);
//                sb.append("\n");
            }
//            objectList.add(dataObj);
//            log.debug(sb.toString());
//            log.debug(dataType.dataObject + "={}", JSON.toJSONString(dataObj));
//            log.debug("\n");
            return dataObj;
        }
        return null;
    }

    private Object parseTEV(DataType dataType, int i, byte[] destBytes) throws Exception {
        switch (dataType) {
            case TEV_XJZS:
                return analyzeTEVData.XJZS(i, destBytes);
            case TEV_XJ:
            case TEV_XJL:
                return analyzeTEVData.XJ(i, destBytes);
            case TEV_TJZS:
                return analyzeTEVData.TJZS(i, destBytes);
            case TEV_TJ:
                return analyzeTEVData.TJ(i, destBytes);
            case TEV_TJLB:
                return analyzeTEVData.TJLB(i, destBytes);
            default:
        }
        return null;
    }

    private Object parseAA(DataType dataType, int i, byte[] destBytes) throws Exception {
        switch (dataType) {
            case AA_BX:
                return analyzeAAData.BX(i, destBytes);
            case AA_FX:
                return analyzeAAData.FX(i, destBytes);
            case AA_XJZS:
                return analyzeAAData.XJZS(i, destBytes);
            case AA_XJ:
                return analyzeAAData.XJ(i, destBytes);
            case AA_TJZS:
                return analyzeAAData.TJZS(i, destBytes);
            case AA_TJ:
                return analyzeAAData.TJ(i, destBytes);
            case AA_TJLB:
                return analyzeAAData.TJLB(i, destBytes);
            default:
        }
        return null;
    }

    private Object parseHFCT(DataType dataType, int i, byte[] destBytes) throws Exception {
        switch (dataType) {
            case HFCT_TJ:
                return analyzeHFCTData.TJ(i, destBytes);
            case HFCT_TJLB:
                return analyzeHFCTData.TJLB(i, destBytes);
            case HFCT_TJZS:
                return analyzeHFCTData.TJZS(i, destBytes);
            case HFCT_XJ:
                return analyzeHFCTData.XJ(i, destBytes);
            case HFCT_XJZS:
                return analyzeHFCTData.XJZS(i, destBytes);
            default:
        }
        return null;
    }

    private Object parseAE(DataType dataType, int i, byte[] destBytes) throws Exception {
        switch (dataType) {
            case AE_BX:
                return analyzeAEData.BX(i, destBytes);
            case AE_FX:
                return analyzeAEData.FX(i, destBytes);
            case AE_TJ:
                return analyzeAEData.TJ(i, destBytes);
            case AE_TJLB:
                return analyzeAEData.TJLB(i, destBytes);
            case AE_TJZS:
                return analyzeAEData.TJZS(i, destBytes);
            case AE_XJ:
                return analyzeAEData.XJ(i, destBytes);
            case AE_XJZS:
                return analyzeAEData.XJZS(i, destBytes);
            default:
        }
        return null;
    }

    private Object parseUHF(DataType dataType, int i, byte[] destBytes) throws Exception {
        switch (dataType) {
            case UHF_TJ_1:
                return analyzeUHFData.TJ_1(i, destBytes);
            case UHF_TJLB_1:
                return analyzeUHFData.TJLB_1(i, destBytes);
            case UHF_TJZS_1:
                return analyzeUHFData.TJZS_1(i, destBytes);
            case UHF_TJ_2:
                return analyzeUHFData.TJ_2(i, destBytes);
            case UHF_TJLB_2:
                return analyzeUHFData.TJLB_2(i, destBytes);
            case UHF_TJZS_2:
                return analyzeUHFData.TJZS_2(i, destBytes);
            default:
        }
        return null;
    }

    private Object parseLC(DataType dataType, int i, byte[] destBytes) throws Exception {
        switch (dataType) {
            case LC:
                return analyzeLCData.LC(i, destBytes);
            default:
        }
        return null;
    }

    private Object parseHJ(DataType dataType, int i, byte[] destBytes) throws Exception {
        switch (dataType) {
            case HJ:
                return analyzeHJData.HJ(i, destBytes);
            default:
        }
        return null;
    }

    private Object parseGW(DataType dataType, int i, byte[] destBytes) throws Exception {
        switch (dataType) {
            case GW_HFCT_TJ:
                return gwAnalyzeData.HFCT_TJ(i, destBytes);
            case GW_UHF_TJ:
                return gwAnalyzeData.UHF_TJ(i, destBytes);
            default:
        }
        return null;
    }

    private Object parseJS(DataType dataType, int i, byte[] destBytes) throws Exception {
        switch (dataType) {
            case JS_AA_BX:
                return jsAnalyzeAAData.AA_BX(i, destBytes);
            case JS_AA_FX:
                return jsAnalyzeAAData.AA_FX(i, destBytes);
            case JS_AA_TJ:
                return jsAnalyzeAAData.AA_TJ(i, destBytes);
            case JS_AA_TJZS:
                return jsAnalyzeAAData.AA_TJZS(i, destBytes);
            case JS_AA_XJ:
                return jsAnalyzeAAData.AA_XJ(i, destBytes);
            case JS_AA_XJZS:
                return jsAnalyzeAAData.AA_XJZS(i, destBytes);
            case JS_AE_BX:
                return jsAnalyzeAEData.AE_BX(i, destBytes);
            case JS_AE_FX:
                return jsAnalyzeAEData.AE_FX(i, destBytes);
            case JS_AE_TJ:
                return jsAnalyzeAEData.AE_TJ(i, destBytes);
            case JS_AE_TJZS:
                return jsAnalyzeAEData.AE_TJZS(i, destBytes);
            case JS_AE_XJ:
                return jsAnalyzeAEData.AE_XJ(i, destBytes);
            case JS_AE_XJZS:
                return jsAnalyzeAEData.AE_XJZS(i, destBytes);
            case JS_HFCT_TJ:
                return jsAnalyzeHFCTData.HFCT_TJ(i, destBytes);
            case JS_HFCT_TJZS:
                return jsAnalyzeHFCTData.HFCT_TJZS(i, destBytes);
            case JS_TEV_XJ:
                return jsAnalyzeTEVData.TEV_XJ(i, destBytes);
            case JS_TEV_XJZS:
                return jsAnalyzeTEVData.TEV_XJZS(i, destBytes);
            case JS_UHF_TJ:
                return jsAnalyzeUHFData.UHF_TJ(i, destBytes);
            case JS_UHF_TJZS:
                return jsAnalyzeUHFData.UHF_TJZS(i, destBytes);
            default:
        }
        return null;
    }

    public enum DataType {
        //------------------地电波
        TEV_XJZS("TEV", "地电波_巡检噪声", new int[]{4, 4, 8, 1, 4, 4, 4, 4, 1, 4, 4, 4, 4, 4, 4, 4, 1, 4, 4, 4, 1, 4, 4, 4, 1, 4, 4, 4, 32, 32, 4, 32, 4}
                , DataTevXjzsDto.class
        ),
        TEV_XJ("TEV", "地电波_巡检", new int[]{4, 4, 8, 1, 4, 4, 4, 4, 1, 4, 4, 4, 4, 4, 4, 4, 1, 4, 4, 4, 1, 1, 4, 4, 4, 1, 4, 4, 4, 32, 32, 4, 32, 4}
                , DataTevXjDto.class
        ),
        TEV_XJL("TEV", "地电波_联合巡检", new int[]{4, 4, 8, 1, 4, 4, 4, 4, 1, 4, 4, 4, 4, 4, 4, 4, 1, 4, 4, 4, 1, 1, 4, 4, 4, 1, 4, 4, 4, 32, 32, 4, 32, 4}
                , DataTevXjlhDto.class
        ),
        TEV_TJZS("TEV", "地电波_统计噪声", new int[]{4, 4, 1, 4, 4, 1, 4, 4, 4, 8, CommonConstant.BIG_DATA_LENGTH, 4, 4, 8, 1, 4, 4, 4, 4, 1, 4, 4, 4, 4, 4, 32, 32, 4, 32, 4}
                , DataTevTjzsDto.class
        ),
        TEV_TJ("TEV", "地电波_统计数据", new int[]{4, 4, 1, 4, 4, 1, 4, 4, 4, 8, CommonConstant.BIG_DATA_LENGTH, 4, 4, 8, 1, 4, 4, 4, 4, 1, 4, 4, 4, 4, 4, 32, 32, 4, 32, 4,}
                , DataTevTjDto.class
        ),
        TEV_TJLB("TEV", "地电波_统计录波", new int[]{4, 4, 8, 1, 1, 4, 1, 4, 4, 4, CommonConstant.BIG_DATA_LENGTH, 4, 4, 1, 4, 4, 4, 4, 4, 32, 32, 4, 32, 4}
                , DataTevTjlbDto.class
        ),

        //------------------空气式超声波
        AA_XJZS("AA", "空气式超声波_巡检噪声", new int[]{4, 4, 8, 1, 4, 4, 4, 4, 4, 8, 1, 1, 4, 4, 4, 1, 4, 4, 4, 4, 4, 4, 4, 32, 32, 4, 32, 4}
                , DataAaXjzsDto.class
        ),
        AA_XJ("AA", "空气式超声波_巡检", new int[]{4, 4, 8, 1, 4, 4, 4, 4, 4, 8, 1, 1, 4, 4, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 32, 32, 4, 32, 4}
                , DataAaXjDto.class
        ),
        AA_TJZS("AA", "空气式超声波_统计噪声", new int[]{4, 4, 1, 4, 4, 1, 4, 4, 4, 8, CommonConstant.BIG_DATA_LENGTH, 4, 4, 8, 1, 4, 4, 4, 4, 1, 4, 4, 1, 4, 4, 4, 32, 32, 4, 32, 4}
                , DataAaTjzsDto.class
        ),
        AA_TJ("AA", "空气式超声波_统计数据", new int[]{4, 4, 1, 4, 4, 1, 4, 4, 4, 8, CommonConstant.BIG_DATA_LENGTH, 4, 4, 8, 1, 4, 4, 4, 4, 1, 4, 4, 1, 4, 4, 4, 32, 32, 4, 32, 4}
                , DataAaTjDto.class
        ),
        AA_TJLB("AA", "空气式超声波_统计录波", new int[]{4, 4, 8, 1, 1, 4, 1, 4, 4, 4, CommonConstant.BIG_DATA_LENGTH, 4, 4, 1, 4, 4, 1, 4, 4, 4, 32, 32, 4, 32, 4}
                , DataAaTjlbDto.class
        ),
        AA_FX("AA", "空气式超声波_飞行图谱", new int[]{4, 4, 8, 1, 1, 1, 4, 4, 1, 4, 8, 4, 1, 4, 4, 4, 4, 4, 4, 1, 4, CommonConstant.BIG_DATA_LENGTH, 32, 32, 4, 32, 4}
                , DataAaFxDto.class
        ),
        AA_BX("AA", "空气式超声波_波形图谱", new int[]{4, 4, 8, 1, 1, 1, 4, 4, 4, 8, 8, 1, 1, 4, 4, 4, 1, 4, CommonConstant.BIG_DATA_LENGTH, 32, 32, 4, 32, 4}
                , DataAaBxDto.class
        ),

        //------------------高频电流
        HFCT_XJZS("HFCT", "高频电流_巡检噪声", new int[]{4, 4, 8, 1, 4, 4, 1, 4, 4, 4, 1, 4, 4, 4, 4, 1, 4, 4, 4, 1, 4, 32, 32, 4, 32, 4}
                , DataHfctXjzsDto.class
        ),
        HFCT_XJ("HFCT", "高频电流_巡检", new int[]{4, 4, 8, 1, 4, 4, 1, 4, 4, 4, 1, 4, 4, 4, 4, 1, 4, 4, 4, 1, 4, 32, 32, 4, 32, 4}
                , DataHfctXjDto.class
        ),
        HFCT_TJZS("HFCT", "高频电流_统计噪声", new int[]{4, 4, 1, 4, 4, 1, 4, 4, 4, 8, CommonConstant.BIG_DATA_LENGTH, 4, 4, 8, 1, 4, 4, 4, 4, 1, 4, 4, 1, 4, 4, 4, 4, 1, 4, 4, 32, 32, 4, 32, 4}
                , DataHfctTjzsDto.class
        ),
        HFCT_TJ("HFCT", "高频电流_统计数据", new int[]{4, 4, 1, 4, 4, 1, 4, 4, 4, 8, CommonConstant.BIG_DATA_LENGTH, 4, 4, 8, 1, 4, 4, 4, 4, 1, 4, 4, 1, 4, 4, 4, 4, 1, 4, 4, 32, 32, 4, 32, 4}
                , DataHfctTjDto.class
        ),
        HFCT_TJLB("HFCT", "高频电流_统计录波", new int[]{4, 4, 8, 1, 1, 4, 1, 4, 4, 4, CommonConstant.BIG_DATA_LENGTH, 4, 4, 1, 4, 4, 1, 4, 4, 4, 4, 1, 4, 4, 32, 32, 4, 32, 4}
                , DataHfctTjlbDto.class
        ),

        //------------------特高频
        UHF_TJZS_1("UHF", "特高频_模式1_统计噪声", new int[]{4, 4, 1, 4, 4, 1, 4, 4, 4, 8, CommonConstant.BIG_DATA_LENGTH, 4, 4, 8, 1, 4, 4, 4, 4, 1, 4, 4, 4, 4, 4, 1, 4, 4, 32, 32, 4, 32, 4}
                , DataUhfTjzs1Dto.class
        ),
        UHF_TJ_1("UHF", "特高频_模式1_统计数据", new int[]{4, 4, 1, 4, 4, 1, 4, 4, 4, 8, CommonConstant.BIG_DATA_LENGTH, 4, 4, 8, 1, 4, 4, 4, 4, 1, 4, 4, 4, 4, 4, 1, 4, 4, 32, 32, 4, 32, 4}
                , DataUhfTj1Dto.class
        ),
        UHF_TJLB_1("UHF", "特高频_模式1_统计录波", new int[]{4, 4, 8, 1, 1, 4, 1, 4, 4, 4, CommonConstant.BIG_DATA_LENGTH, 4, 4, 1, 4, 4, 4, 4, 4, 1, 4, 4, 32, 32, 4, 32, 4}
                , DataUhfTjlb1Dto.class
        ),
        UHF_TJZS_2("UHF", "特高频_模式2_统计噪声", new int[]{4, 4, 1, 4, 4, 1, 4, 4, 4, 8, CommonConstant.BIG_DATA_LENGTH, 4, 4, 8, 1, 4, 4, 4, 4, 1, 4, 4, 4, 4, 4, 1, 4, 4, 32, 32, 4, 32, 4}
                , DataUhfTjzs2Dto.class
        ),
        UHF_TJ_2("UHF", "特高频_模式2_统计数据", new int[]{4, 4, 1, 4, 4, 1, 4, 4, 4, 8, CommonConstant.BIG_DATA_LENGTH, 4, 4, 8, 1, 4, 4, 4, 4, 1, 4, 4, 4, 4, 4, 1, 4, 4, 32, 32, 4, 32, 4}
                , DataUhfTj2Dto.class
        ),
        UHF_TJLB_2("UHF", "特高频_模式2_统计录波", new int[]{4, 4, 8, 1, 1, 4, 1, 4, 4, 4, CommonConstant.BIG_DATA_LENGTH, 4, 4, 1, 4, 4, 4, 4, 4, 1, 4, 4, 32, 32, 4, 32, 4}
                , DataUhfTjlb2Dto.class
        ),

        //------------------接触式超声波
        AE_XJZS("AE", "接触式超声波_巡检噪声", new int[]{4, 4, 8, 1, 4, 4, 4, 4, 4, 8, 1, 4, 4, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 32, 32, 4, 32, 4}
                , DataAeXjzsDto.class
        ),
        AE_XJ("AE", "接触式超声波_巡检", new int[]{4, 4, 8, 1, 4, 4, 4, 4, 4, 8, 1, 4, 4, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 32, 32, 4, 32, 4}
                , DataAeXjDto.class
        ),
        AE_TJZS("AE", "接触式超声波_统计噪声", new int[]{4, 4, 1, 4, 4, 1, 4, 4, 4, 8, CommonConstant.BIG_DATA_LENGTH, 4, 4, 8, 1, 4, 4, 4, 4, 1, 4, 4, 1, 4, 4, 4, 4, 32, 32, 4, 32, 4}
                , DataAeTjzsDto.class
        ),
        AE_TJ("AE", "接触式超声波_统计数据", new int[]{4, 4, 1, 4, 4, 1, 4, 4, 4, 8, CommonConstant.BIG_DATA_LENGTH, 4, 4, 8, 1, 4, 4, 4, 4, 1, 4, 4, 1, 4, 4, 4, 4, 32, 32, 4, 32, 4}
                , DataAeTjDto.class
        ),
        AE_TJLB("AE", "接触式超声波_统计录波", new int[]{4, 4, 8, 1, 1, 4, 1, 4, 4, 4, CommonConstant.BIG_DATA_LENGTH, 4, 4, 1, 4, 4, 1, 4, 4, 4, 4, 32, 32, 4, 32, 4}
                , DataAeTjlbDto.class
        ),
        AE_FX("AE", "接触式超声波_飞行图谱", new int[]{4, 4, 8, 1, 1, 1, 4, 4, 1, 4, 8, 4, 1, 4, 4, 4, 4, 4, 4, 1, 4, CommonConstant.BIG_DATA_LENGTH, 32, 32, 4, 32, 4}
                , DataAeFxDto.class
        ),
        AE_BX("AE", "接触式超声波_波形图谱", new int[]{4, 4, 8, 1, 1, 1, 4, 4, 4, 8, 8, 1, 1, 4, 4, 4, 1, 4, CommonConstant.BIG_DATA_LENGTH, 32, 32, 4, 32, 4}
                , DataAeBxDto.class
        ),


        //------------------负荷电流数据
        LC("LC", "负荷电流数据", new int[]{4, 4, 1, 4, 4}
                , DataLcDto.class
        ),

        //------------------环境参数数据
        HJ("HJ", "环境参数数据", new int[]{4, 4, 1, 4, 1, 20, 20, 20, 20, 20, 4}
                , DataHjDto.class
        ),

        //------------------地区-国网数据
        GW_UHF_TJ("GW", "国网_特高频_统计数据", new int[]{4, 4, 1, 4, 4, 1, 4, 4, 4, 8, CommonConstant.BIG_DATA_LENGTH, 4}
                , GWDataUhfTjDto.class
        ),
        GW_HFCT_TJ("GW", "国网_高频电流_统计", new int[]{4, 4, 1, 4, 4, 1, 4, 4, 4, 8, CommonConstant.BIG_DATA_LENGTH, 4}
                , GWDataHfctTjDto.class
        ),
        //------------------地区-江苏数据
        JS_AA_BX("JS", "江苏_超声_波形图数据", JSDataType.AA_BX.getDataRowBytes()
                , JSDataAaBXDto.class
        ),
        JS_AA_FX("JS", "江苏_超声_脉冲图数据", JSDataType.AA_FX.getDataRowBytes()
                , JSDataAaFXDto.class
        ),
        JS_AA_TJ("JS", "江苏_超声_相位图数据", JSDataType.AA_TJ.getDataRowBytes()
                , JSDataAaTJDto.class
        ),
        JS_AA_TJZS("JS", "江苏_超声_相位图数据(噪声)", JSDataType.AA_TJZS.getDataRowBytes()
                , JSDataAaTJZSDto.class
        ),
        JS_AA_XJ("JS", "江苏_超声_特征图数据", JSDataType.AA_XJ.getDataRowBytes()
                , JSDataAaXJDto.class
        ),
        JS_AA_XJZS("JS", "江苏_超声_特征图数据(噪声)", JSDataType.AA_XJZS.getDataRowBytes()
                , JSDataAaXJZSDto.class
        ),
        JS_AE_BX("JS", "江苏_超声_波形图数据", JSDataType.AE_BX.getDataRowBytes()
                , JSDataAeBXDto.class
        ),
        JS_AE_FX("JS", "江苏_超声_脉冲图数据", JSDataType.AE_FX.getDataRowBytes()
                , JSDataAeFXDto.class
        ),
        JS_AE_TJ("JS", "江苏_超声_相位图数据", JSDataType.AE_TJ.getDataRowBytes()
                , JSDataAeTJDto.class
        ),
        JS_AE_TJZS("JS", "江苏_超声_相位图数据(噪声)", JSDataType.AE_TJZS.getDataRowBytes()
                , JSDataAeTJZSDto.class
        ),
        JS_AE_XJ("JS", "江苏_超声_特征图数据", JSDataType.AE_XJ.getDataRowBytes()
                , JSDataAeXJDto.class
        ),
        JS_AE_XJZS("JS", "江苏_超声_特征图数据(噪声)", JSDataType.AE_XJZS.getDataRowBytes()
                , JSDataAeXJZSDto.class
        ),
        JS_HFCT_TJ("JS", "江苏_高频电流_PRPD图和PRPS图数据", JSDataType.HFCT_TJ.getDataRowBytes()
                , JSDataHfctTJDto.class
        ),
        JS_HFCT_TJZS("JS", "江苏_高频电流_PRPD图和PRPS图数据(噪声)", JSDataType.HFCT_TJZS.getDataRowBytes()
                , JSDataHfctTJZSDto.class
        ),
        JS_TEV_XJ("JS", "江苏_暂态地电压_电压幅值数据", JSDataType.TEV_XJ.getDataRowBytes()
                , JSDataTevXJDto.class
        ),
        JS_TEV_XJZS("JS", "江苏_暂态地电压_电压幅值数据(噪声)", JSDataType.TEV_XJZS.getDataRowBytes()
                , JSDataTevXJZSDto.class
        ),
        JS_UHF_TJ("JS", "江苏_特高频电流_PRPD图和PRPS图数据", JSDataType.UHF_TJ.getDataRowBytes()
                , JSDataUhfTJDto.class
        ),
        JS_UHF_TJZS("JS", "江苏_特高频电流_PRPD图和PRPS图数据(噪声)", JSDataType.UHF_TJZS.getDataRowBytes()
                , JSDataUhfTJZSDto.class
        ),
        ;

        private String modeType;
        private String dataName;
        private int[] dataRowBytes;
        private Class dataObject;

        DataType(String modeType, String dataName, int[] dataRowBytes, Class dataObject) {
            this.modeType = modeType;
            this.dataName = dataName;
            this.dataRowBytes = dataRowBytes;
            this.dataObject = dataObject;
        }

        public int[] getDataRowBytes() {
            return dataRowBytes;
        }

        public Class getDataObject() {
            return dataObject;
        }
    }
}
