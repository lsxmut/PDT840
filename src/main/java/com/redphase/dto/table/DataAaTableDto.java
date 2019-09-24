package com.redphase.dto.table;

import com.redphase.dto.atlas.DataDeviceDto;
import lombok.Data;

@Data
public class DataAaTableDto extends DataTableDto {
    /**
     * 设备
     */
    private DataDeviceDto dataDeviceDto;

    private Float qzfd;//前中幅度

    private Integer qzcs;//前中次数

    private Float qxfd;//前下幅度

    private Integer qxcs;//前下次数

    private Float hsfd;//后上幅度

    private Integer hscs;//后上次数

    private Float hzfd;//后中幅度

    private Integer hzcs;//后中次数

    private Float hxfd;//后下幅度

    private Integer hxcs;//后下次数

    private Float csfd;//侧上幅度

    private Integer cscs;//侧上次数

    private Float czfd;//侧中幅度

    private Integer czcs;//侧中次数

    private Float cxfd;//侧下幅度

    private Integer cxcs;//侧下次数

    private String csbjtjg;//超声波监听结果

    private Float csbcsz;//超声波测试值

    private Float fh;//负荷电流

    private String kggyxzt;//开关柜运行状态
}
