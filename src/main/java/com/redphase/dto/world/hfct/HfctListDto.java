package com.redphase.dto.world.hfct;


import com.redphase.framework.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
/** <p> WORD LIST DTO */
public class HfctListDto extends BaseDto {
    //三、检测数据
    private String xh;// 序号
    private String jgmc;// 间隔名称
    private String sbmc;// 设备名称
    private String xw;// 相位
    private String tpwj;// 图谱文件
    private String fd;// 是否存在放电信号（打勾）
    private String fz;// 测试值（峰值）（mV）
}