package com.redphase.framework.dto;

import com.redphase.framework.AbstractEntity;
import com.redphase.framework.IDto;
import lombok.Data;

@Data
public class BaseDto extends AbstractEntity implements IDto {
    private String token;
    /**
     * 新增标记0否1是
     */
    private Integer newFlag;
    /**
     * 起始日期
     */
    private String dateBegin;
    /**
     * 截止日期
     */
    private String dateEnd;
    /**
     * 页码
     */
    private Integer pageNum;
    /**
     * 每页显示条数
     */
    private Integer pageSize;
    /**
     * 关键字
     */
    private String keyword;
    /**
     * 用户名
     */
    private String account;
    /**
     * 建立者ID
     */
    private Long createId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 鉴权token
     */
    private String Authorization;

    /**
     * 客户端登录ip
     */
    private String ip;
}
