package com.redphase.service.convert.tofile;


import com.alibaba.fastjson.JSONObject;
import com.redphase.api.atlas.aa.IDataAaBxService;
import com.redphase.dto.atlas.DataAnalyzeDto;
import com.redphase.dto.atlas.DataHjDto;
import com.redphase.dto.atlas.aa.DataAaBxDto;
import com.redphase.entity.atlas.DataDevice;
import com.redphase.entity.atlas.DataDeviceSite;
import com.redphase.framework.util.ByteConverterUtil;
import com.redphase.service.atlas.aa.DataAaBxService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.zip.CRC32;


/**
 * <p> 江苏_超声_波形图数据
 */
@Data
@Slf4j
public class JSDataAaBX2File extends BaseData2File {
    public static void toFile(Map paramMap) throws Exception {
        FileOutputStream out = null;
        try {
            //AA波形业务类
            IDataAaBxService dataAaBxService = (DataAaBxService) paramMap.get("dataAaBxService");
            //资源记录
            DataAnalyzeDto dataAnalyzeDto = (DataAnalyzeDto) paramMap.get("dataAnalyzeDto");
            //环境数据
            DataHjDto dataHjDto = (DataHjDto) paramMap.get("dataHjDto");
            //检测设备
            DataDevice dataDevice = dataAnalyzeDto.getDataDevice();
            //检测点
            DataDeviceSite dataDeviceSite = dataAnalyzeDto.getDataDeviceSite();
            //AA波形数据
            DataAaBxDto dataAaBxDto = dataAaBxService.findDataById(new DataAaBxDto() {{
                setDataAnalyzeId(dataAnalyzeDto.getId());
            }});
            String dataFilePath = getPathAndMkdirs(paramMap.get("outFilePath") + File.separator + "AA") + File.separator + "AA_BX_" + dataAaBxDto.getX3() + ".dat";
            //数据存储
            out = new FileOutputStream(dataFilePath);
            //图谱数据
            int[] atlasArr = JSONObject.parseObject(dataAaBxDto.getX19(), int[].class);
            //文件长度计算
            int dataRowLength = 0;
            int[] dataRowBytes = JSDataType.AA_BX.getDataRowBytes();
            for (int i = 0; i < dataRowBytes.length; i++) {
                dataRowLength += dataRowBytes[i];
            }
            //二进制文件缓存集合
            byte[] bytes = new byte[dataRowLength + atlasArr.length * 4];
            //文件存储起始位
            int destPost = 0;
//     ************************************************************************************************************************************************************************************************************************************
//     1     * 必备 int32 4字节 文件长度L - 文件长度，含CRC校验。
            byte[] x1buffer = ByteConverterUtil.int2byte(bytes.length);
            System.arraycopy(x1buffer, 0, bytes, destPost, x1buffer.length);
            destPost += 4;
//     2     * 必备 uint8 4字节 规范版本号 - 所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。建议：版本号采用“内部主版本号.内部子版本号.省公司编码.国家单位编码”的形式，以便于版本维护。
            byte[] x2buffer = new byte[]{1, 0, 0, 0};
            System.arraycopy(x2buffer, 0, bytes, destPost, x2buffer.length);
            destPost += 4;
//     3     * 必备 int64 8字节 文件生成时间 - 生成文件的时间，格式为YYYYMMDDhhmmssfff，例如20100818151010001。
            byte[] x3buffer = ByteConverterUtil.long2bytes(dataAaBxDto.getX3());
            if (x3buffer != null) {
                System.arraycopy(x3buffer, 0, bytes, destPost, x3buffer.length);
            }
            destPost += 8;
//     4     * 必备 char 128字节 站点名称 - 使用UNICODE编码。以0x0000结尾，例如：110kV枫泾变电站。
            byte[] x4buffer = ByteConverterUtil.unicode2byte(dataDevice.getSubstation());
            if (x4buffer != null) {
                System.arraycopy(x4buffer, 0, bytes, destPost, x4buffer.length);
            }
            destPost += 128;
//     5     * 必备 char 32字节 站点编码 - 使用ASCII编码。以\0结尾，例如：A12300000000000000。
            destPost += 32;
//     6     * 必备 uint8 1字节 天气 - 表示天气。0xFF：未记录0x01：晴0x02：阴0x03：雨0x04：雪0x05：雾0x06：雷雨0x07：多云
            bytes[destPost] = hjTQ2byte(dataHjDto);
            destPost += 1;
//     7     * 必备 float 4字节 温度 - 环境温度，单位摄氏度。
            byte[] x7buffer = hjWD2byte(dataHjDto);
            if (x7buffer != null) {
                System.arraycopy(x7buffer, 0, bytes, destPost, x7buffer.length);
            }
            destPost += 4;
//     8     * 必备 int8 1字节 湿度 - 环境湿度，单位%。
            bytes[destPost] = hjSD2byte(dataHjDto);
            destPost += 1;
//     9     * 必备 char 32字节 仪器厂家 - 使用UNICODE编码。以0x0000结尾，例如：HuaCheng。
            byte[] x9buffer = ByteConverterUtil.unicode2byte(dataAaBxDto.getX20());
            if (x9buffer != null) {
                System.arraycopy(x9buffer, 0, bytes, destPost, x9buffer.length);
            }
            destPost += 32;
//     10     * 必备 char 32字节 仪器型号 - 使用UNICODE编码。以0x0000结尾，例如：PDS-T95。
            byte[] x10buffer = ByteConverterUtil.unicode2byte(dataAaBxDto.getX21());
            if (x10buffer != null) {
                System.arraycopy(x10buffer, 0, bytes, destPost, x10buffer.length);
            }
            destPost += 32;
//     11     * 可选 uint8 4字节 仪器版本号 - 所使用的数据格式规范版本号。版本号有4个部分，形如X.X.X.X。实例：版本号为1.0.0.0，数组元素[0]到[3]分别存储1、0、0、0四个数字。每个元素可以是整数0至255中的某数。
            System.arraycopy(JSONObject.parseObject(dataAaBxDto.getX22(), byte[].class), 0, bytes, destPost, 4);
            destPost += 4;
//     12     * 必备 char 32字节 仪器序列号 - 即仪器出厂编号，使用ASCII编码。以\0结尾，例如：S12300000000000000。
            byte[] x12buffer = ByteConverterUtil.string2byte(dataAaBxDto.getX23());
            if (x12buffer != null) {
                System.arraycopy(x12buffer, 0, bytes, destPost, x12buffer.length);
            }
            destPost += 32;
//     13     * 必备 float 4字节 系统频率 - 被测设备电压频率，单位Hz。例如50Hz。
            System.arraycopy(ByteConverterUtil.float2byte(dataAaBxDto.getX15()), 0, bytes, destPost, 4);
            destPost += 4;
//     14     * 必备 int16 2字节 图谱数量N - 文件中包含的图谱数量。
            short atlasTotal = 1;
            System.arraycopy(ByteConverterUtil.short2byte(atlasTotal), 0, bytes, destPost, 2);
            destPost += 2;
//     15     * 必备 float 4字节 同步频率 - 采集装置的同步频率，单位Hz。
            System.arraycopy(ByteConverterUtil.float2byte(dataAaBxDto.getX14()), 0, bytes, destPost, 4);
            destPost += 4;
//     16     * 可选 自定义 220字节 预留 - 预留为厂家自定义可选字段。
            destPost += 220;
//     17     * 必备 uint8 1字节 图谱类型编码 - 标识该文件的图谱类型。参见图谱类型编码表。
            bytes[destPost] = JSAtlasType.AA_BX_超声波形图.value;
            destPost += 1;
//     18     * 必备 int32 4字节 图谱数据长度 - 图谱总长度，指从图谱类型编码到图谱数据结束的长度。
            System.arraycopy(ByteConverterUtil.int2byte(atlasArr.length), 0, bytes, destPost, 4);
            destPost += 4;
//     19     * 必备 int64 8字节 图谱生成时间 - 生成图谱的时间，格式为YYYYMMDDhhmmssfff，例如20100818151010001。
            System.arraycopy(ByteConverterUtil.long2bytes(dataAaBxDto.getX3()), 0, bytes, destPost, 8);
            destPost += 8;
//     20     * 必备 uint8 1字节 图谱性质 - 0xFF：未记录0x01：检测图谱0x02：背景噪声0x03：检测图谱及背景噪声
            bytes[destPost] = 0x01;
            destPost += 1;
//     21     * 必备 char 128字节 电力设备名称 - 使用UNICODE编码。以0x0000结尾，例如：10kV万达线#3开关柜。
            byte[] x21buffer = ByteConverterUtil.unicode2byte(dataDevice.getDeviceName());
            if (x21buffer != null) {
                System.arraycopy(x21buffer, 0, bytes, destPost, x21buffer.length);
            }
            destPost += 128;
//     22     * 必备 char 32字节 电力设备编码 - 使用ASCII编码。以\0结尾，例如：B12300000000000000。
            byte[] x22buffer = ByteConverterUtil.string2byte("B12300000000000000");
            if (x22buffer != null) {
                System.arraycopy(x22buffer, 0, bytes, destPost, x22buffer.length);
            }
            destPost += 32;
//     23     * 可选 char 128字节 测点名称 - 使用UNICODE编码。以0x0000结尾，例如：电缆仓。
            byte[] x23buffer = siteName2byte(dataDeviceSite);
            System.arraycopy(x23buffer, 0, bytes, destPost, x23buffer.length);
            destPost += 128;
//     24     * 可选 char 32字节 测点编码 - 使用ASCII编码。以\0结尾，例如：C12300000000000000。
            byte[] x24buffer = ByteConverterUtil.string2byte("C12300000000000000");
            if (x24buffer != null) {
                System.arraycopy(x24buffer, 0, bytes, destPost, x24buffer.length);
            }
            destPost += 32;
//     25     * 必备 int16 2字节 检测通道标志 - 仪器的检测通道标识，例如：1。
            short detectionChannel = 1;
            System.arraycopy(ByteConverterUtil.short2byte(detectionChannel), 0, bytes, destPost, 2);
            destPost += 2;
//     26     * 必备 uint8 1字节 存储数据类型t - 表示图谱数据的存储数据类型。参见存储类型编码表。
            bytes[destPost] = JSAtlasType.DATATYPE_INT32.value;
            destPost += 1;
//     27     * 必备 uint8 1字节 幅值单位 - 表示幅值的单位。参见幅值单位表。
            bytes[destPost] = 0x06;
            destPost += 1;
//     28     * 必备 float 4字节 幅值下限 - 仪器所能检测到的信号幅值的下限。
            System.arraycopy(ByteConverterUtil.float2byte(dataAaBxDto.getX7()), 0, bytes, destPost, 4);
            destPost += 4;
//     29     * 必备 float 4字节 幅值上限 - 仪器所能检测到的信号幅值的上限。
            System.arraycopy(ByteConverterUtil.float2byte(dataAaBxDto.getX8()), 0, bytes, destPost, 4);
            destPost += 4;
//     30     * 必备 uint8 1字节 超声传感器类型 - 0xFF：未记录0x01：空声0x02：SF6气体绝缘电力设备检测用接触式0x03：充油电力设备检测用接触式0x04：其他类型
            bytes[destPost] = 0x01;
            destPost += 1;
//     31     * 必备 int32 4字节 数据点数n - 数据点个数。
            System.arraycopy(ByteConverterUtil.int2byte(dataAaBxDto.getX9()), 0, bytes, destPost, 4);
            destPost += 4;
//     32     * 必备 int64 8字节 采样率 - 单位SPS。
            System.arraycopy(ByteConverterUtil.long2bytes(1000000/*dataAaBxDto.getX10()*/), 0, bytes, destPost, 8);
            destPost += 8;
//     33     * 必备 uint8 8字节 放电类型概率 - 表示仪器诊断结果的放电类型概率。参见放电类型概率定义。
            byte[] x33buffer = JSONObject.parseObject(dataAaBxDto.getX11(), byte[].class);
            if (x33buffer != null) {
                System.arraycopy(x33buffer, 0, bytes, destPost, x33buffer.length);
            }
            destPost += 8;
//     34     * 可选 自定义 145字节 预留 - 预留为厂家自定义可选字段。
            destPost += 145;
//     35     * 必备 d[n] k*n字节 超声波形图数据 - 根据存储数据类型t获取数据的存储方式。实例1：t是0x02，d表示uint8数组，k=1；实例2：t是0x04，d表示int32数组，k=4；实例3：t是0x06，d表示float数组，k=4。依次存储每个数据点的信号幅值Q，存储顺序：Q[0]；Q[1]；……；Q[n-1]。
            for (int x = 0; x < atlasArr.length; x++) {
                System.arraycopy(ByteConverterUtil.float2byte(atlasArr[x]), 0, bytes, destPost, 4);
                destPost += 4;
            }
//     36     * 必备 - 32字节 预留 - 预留
            destPost += 32;
//     37     * 必备 int32 4字节 CRC32 - 数据校验，使用CRC32算法
            CRC32 crc32 = new CRC32();
            crc32.update(bytes);
            System.arraycopy(ByteConverterUtil.int2byte((int) crc32.getValue()), 0, bytes, destPost, 4);
//     ************************************************************************************************************************************************************************************************************************************
            out.write(bytes);
            out.flush();
            //debug
            debugAnalyzeData(dataFilePath);
        } catch (Exception e) {
            log.error("AA波形,文件输出异常!", e);
            throw e;
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}