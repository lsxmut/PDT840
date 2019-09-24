package com.redphase.dto.world.tev;


import com.redphase.framework.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
/** <p> WORD LIST DTO */
public class TevListDto extends BaseDto {
    //三、检测数据
    private Integer isNew;//0:旧,1:新
    private String xh;// 序号
    private String bh;// 开关柜编号
    private String qz;// 前中
    private String qx;// 前下
    private String hs;// 后上
    private String hz;// 后中
    private String hx;// 后下
    private String cs;// 侧上
    private String cz;// 侧中
    private String cx;// 侧下
    private String fh;// 负荷
    private String kjg;// 备注（可见光照片）
    private String jl;// 结论
}