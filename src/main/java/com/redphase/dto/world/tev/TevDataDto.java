package com.redphase.dto.world.tev;


import com.redphase.framework.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
/** <p> WORD DATA DTO */
public class TevDataDto extends BaseDto {
    //暂态地电压局部放电检测报告
    //一、基本信息
    private String bdz;// 变电站
    private String wtdw;// 委托单位
    private String sydw;// 试验单位
    private String syxz;// 试验性质
    private String syrq;// 试验日期
    private String syry;// 试验人员
    private String sydd;// 试验地点
    private String bgrq;// 报告日期
    private String bzr;// 编制人
    private String shr;// 审核人
    private String pzr;// 批准人
    private String sytq;// 试验天气
    private String wd;// 温度
    private String sd;// 湿度
    private String bjzs;// 背景噪声
    //二、设备铭牌
    private String sbxh;// 设备型号
    private String sccj;// 生产厂家
    private String eddy;// 额定电压
    private String tyrq;// 投运日期
    private String ccrq;// 出厂日期
    //三、检测数据
    private List<TevListDto> oldDtoList = new ArrayList<>();//三、前次-检测数据
    private List<TevListDto> newDtoList = new ArrayList<>();//三、本次-检测数据
    private String tzfx;// 特征分析
    private String bjz;// 背景值
    private String yqcj;// 仪器厂家
    private String yqxh;// 仪器型号
    private String yqbh;// 仪器编号
    private String bz;// 备注
    // 注：在备注中对停运开关柜进行记录。
}