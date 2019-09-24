package com.redphase.dto.world.aa;


import com.redphase.framework.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
/** <p> WORD LIST DTO */
public class AaListDto extends BaseDto {
    //三、检测数据
    private String xh;//序号
    private String jcwz;// 检测位置
    private String jcsz;// 检测数值
    private String tpwj;// 图谱文件
    private String fh;// 负荷电流(A)
    private String jl;// 结论
    private String kjg;// 备注（可见光照片）
}