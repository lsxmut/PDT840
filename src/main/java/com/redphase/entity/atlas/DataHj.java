package com.redphase.entity.atlas;


import com.redphase.framework.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
/** <p> 环境参数数据 entity */
public class DataHj extends BaseEntity {
    /**
     * 主键
     */
    private Long id;
    /**
     * 数据分析id
     */
    private Long dataAnalyzeId;
    /**
     * |1|int32|4字节|数据长度数据项1到11的字节长度。
     */
    private Integer x1;
    /**
     * |2|float|4字节|规范版本号所使用的数据格式规范版本号，例如1.0。
     */
    private Float x2;
    /**
     * |3|uint8|1字节|天气表示天气;未记录：0xFF,晴：0x01,阴：0x02,雨：0x03,雪：0x04,雾：0x05,雷雨：0x06,多云：0x07
     */
    private Integer x3;
    /**
     * |4|float|4字节|温度环境温度，单位摄氏度。
     */
    private Float x4;
    /**
     * |5|int8|1字节|湿度环境湿度，单位%。
     */
    private Integer x5;
    /**
     * |6|char|20字节|检测人员1UNICODE：2个字节;使用UNICODE编码。以0x0000结尾
     */
    private String x6;
    /**
     * |7|char|20字节|检测人员2UNICODE：2个字节;使用UNICODE编码。以0x0000结尾
     */
    private String x7;
    /**
     * |8|char|20字节|检测人员3UNICODE：2个字节;使用UNICODE编码。以0x0000结尾
     */
    private String x8;
    /**
     * |9|char|20字节|检测人员4UNICODE：2个字节;使用UNICODE编码。以0x0000结尾
     */
    private String x9;
    /**
     * |10|char|20字节|检测人员5UNICODE：2个字节;使用UNICODE编码。以0x0000结尾
     */
    private String x10;
    /**
     * |11|int32|4字节|检验和1到10项的数据校验。
     */
    private Integer x11;
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