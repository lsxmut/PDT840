package com.redphase.dto.table;

import lombok.Data;

import java.util.List;

@Data
public class DataUhfTableDto extends DataTableDto {
    private List<DataTableDto> tj1List;//统计1文件列表
    private List<DataTableDto> tj2List;//统计2文件列表
    private List<DataTableDto> tjlb1List;//录波文件列表
    private List<DataTableDto> tjlb2List;//录波文件列表
}
