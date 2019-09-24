package com.redphase.dto.table;

import com.redphase.dto.atlas.DataDeviceDto;
import com.redphase.dto.atlas.DataDeviceSiteDto;
import com.redphase.dto.atlas.DataHjDto;
import com.redphase.dto.atlas.DataLcDto;
import com.redphase.dto.atlas.hfct.DataHfctTjDto;
import com.redphase.dto.atlas.uhf.DataUhfTj1Dto;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Data
public class DataTableDto {
    private Integer seqNo;//序号
    private DataDeviceDto dataDeviceDto;
    private DataDeviceSiteDto dataDeviceSiteDto;
    private DataHjDto dataHjDto;
    private DataLcDto dataLcDto;
    private String site;//检测位置  被测部位和测试点；
    private String bcsb; //被测设备；
    private String cssj;//测试时间；
    private String fddj;//电压等级；
    private Float fh;//负荷电流
    private String xwwy;//相位位移；
    private String ckyz;//参考阈值；
    private String fdzy;//放大增益；
    private String qzsj;//前置衰减；
    private String qzzy;//前置增益单位dB
    private String tbfs;//同步方式
    private String tbpl;//同步频率；
    private String xtpl;//系统频率；
    private String fdlx;//放电类型。——预留位置，先不显示

    private Float fz;//幅值；最大值
    private Float zdz;//最大值
    private String mc;//脉冲
    private String ld;//烈度
    private String yz;//阈值

    private Float yxz;//--有效值，
    private Float f1;//--F1分量，
    private Float f2;//--F2分量

    public Float getFh() {
        return setScale(fh);
    }

    public Float getFz() {
        return setScale(fz);
    }

    public Float getZdz() {
        return setScale(zdz);
    }

    public Float getYxz() {
        return setScale(yxz);
    }

    public Float getF1() {
        return setScale(f1);
    }

    public Float getF2() {
        return setScale(f2);
    }

    private Float setScale(Float ff) {
        try {
            if (ff != null) {
                return new BigDecimal(ff).setScale(1, RoundingMode.DOWN).floatValue();
            } else {
                return ff;
            }
        } catch (Exception e) {
            return ff;
        }
    }

    private Integer kmsj;//开门时间以us为单位
    private Integer bssj;//闭锁时间
    private Integer ddsj;//等待时间
//    private Integer cyds;//采样点数

    private List<DataTableDto> kjgzpList;//可见光照片列表
    private List<DataTableDto> xjList;//统计文件列表
    private List<DataTableDto> lhxjList;//联合文件列表
    private List<DataTableDto> tjList;//统计文件列表
    private List<DataTableDto> tjlbList;//录波文件列表
    private List<DataTableDto> fxList;
    private List<DataTableDto> bxList;
    private Float[][] atlasArr;//图谱数据

    /**
     * 文件路径
     */
    private String fileUrl;
    /**
     * 截图路径,逗号分割
     */
    private String screenshots;
    /**
     * 照片路径,逗号分割
     */
    private String photos;
    /**
     * 录音路径,逗号分割
     */
    private String audios;


    private DataHfctTjDto dataHfctTjDto;//高频电流_统计数据
    private DataUhfTj1Dto dataUhfTj1Dto;//特高频_模式1_统计数据

    public static void main(String[] args) {
        System.out.println(new BigDecimal(1.496).setScale(1, RoundingMode.DOWN));
    }
}
