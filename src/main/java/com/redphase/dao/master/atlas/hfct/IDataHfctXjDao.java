package com.redphase.dao.master.atlas.hfct;

import com.redphase.entity.atlas.hfct.DataHfctXj;
import com.redphase.framework.IBaseDao;
import com.redphase.framework.IEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>高频电流_巡检 数据库处理接口类。
 */
@Mapper
public interface IDataHfctXjDao extends IBaseDao {

    /**
     * 判断是否存在
     */
    @Select("select IFNULL(count(0),0) as count from data_hfct_xj where  data_analyze_id = #{dataAnalyzeId} ")
    int isDataYN(IEntity entity) throws Exception;


    /**
     * 根据主键 物理删除
     */
    @Delete("delete from data_hfct_xj where  data_analyze_id = #{dataAnalyzeId} ")
    int deleteByPrimaryKey(IEntity entity) throws Exception;

    List<DataHfctXj> findDataByIds(@Param("ids") String ids) throws Exception;
}