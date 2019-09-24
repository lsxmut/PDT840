package com.redphase.dto.world.hfct;


import com.redphase.framework.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
/** <p> WORD DATA DTO */
public class HfctDataDto extends BaseDto {
    //高频局部放电检测报告
    //一、基本信息
    private String bdz;// 变电站
    private String wtdw;// 委托单位
    private String sydw;// 试验单位
    private String yxbh;// 运行编号
    private String syxz;// 试验性质
    private String syrq;// 试验日期
    private String syry;// 试验人员
    private String sydd;// 试验地点
    private String bgrq;// 报告日期
    private String bzr;// 编制人
    private String shr;// 审核人
    private String pzr;// 批准人
    private String sytq;// 试验天气
    private String wd;// 环境温度（℃）
    private String sd;// 环境相对湿度（%）
    //二、设备铭牌
    private String sccj;// 生产厂家
    private String ccrq;// 出厂日期
    private String ccbh;// 出厂编号
    private String sbxh;// 设备型号
    private String eddy;// 额定电压(kV)
    //三、检测数据
    private List<HfctListDto> dtoList;
    private String tzfx;// 特征分析
    private String jcyq;// 检测仪器
    private String jl;// 结论
    private String bz;// 备注
}