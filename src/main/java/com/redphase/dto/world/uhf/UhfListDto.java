package com.redphase.dto.world.uhf;


import com.redphase.framework.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
/** <p> WORD LIST DTO */
public class UhfListDto extends BaseDto {
    //三、检测数据
    private String xh;// 序号
    private String jcwz;// 检测位置
    private String fh;// 负荷电流（A）
    private String tpwj;// 图谱文件
}