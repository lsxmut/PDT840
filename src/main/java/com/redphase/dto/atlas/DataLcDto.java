package com.redphase.dto.atlas;


import com.redphase.framework.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
/** <p> 负荷电流数据 DTO */
public class DataLcDto extends BaseDto {
    /**
     * 主键
     */
    private Long id;
    /**
     * 数据分析id
     */
    //@NotNull(message = "dataAnalyzeId不能为空")
    private Long dataAnalyzeId;
    /**
     * |1|int|4字节|数据长度数据项1到5的字节长度。
     */
    private Integer x1;
    /**
     * |2|float|4字节|规范版本号所使用的数据格式规范版本号，例如1.0。
     */
    private Float x2;
    /**
     * |3|char|1字节|检测技术类型编码标识该负荷电流文件是对于什么检测技术。,TEV:0x01,AA:0x02,AE:0x03,HFCT:0x04,UHF:0x05
     */
    private Integer x3;
    /**
     * |4|float|4字节|负荷电流
     */
    private Float x4;
    /**
     * |5|int|4字节|检验和1到4项的数据校验。
     */
    private Integer x5;
    /**
     * 建立者ID
     */
    private Long createId;
    /**
     * 创建时间
     */
    private Date dateCreated;
    /**
     * 更新时间
     */
    private Date dateUpdated;

}