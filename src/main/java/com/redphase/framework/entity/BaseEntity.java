package com.redphase.framework.entity;

import com.redphase.framework.AbstractEntity;
import com.redphase.framework.IEntity;
import lombok.Data;

@Data
public class BaseEntity extends AbstractEntity implements IEntity {
    /**
     * 起始日期
     */
    private String dateBegin;
    /**
     * 截止日期
     */
    private String dateEnd;
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
}
