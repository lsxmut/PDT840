package com.redphase.framework;

import java.util.List;

public interface IBaseDao {
    /**
     * 新增
     */
    int insert(IEntity dto) throws Exception;

    /**
     * 新增 批量
     */
    int insertBatch(List<?> dtos) throws Exception;

    /**
     * 详情
     */
    Object selectByPrimaryKey(IEntity dto) throws Exception;

    /**
     * 更新
     */
    int update(IEntity dto) throws Exception;

    /**
     * <p>信息列表 分页。
     */
    List<?> findDataIsPage(IEntity dto) throws Exception;

    /**
     * <p>信息列表。
     */
    List<?> findDataIsList(IEntity dto) throws Exception;


}