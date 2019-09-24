package com.redphase.dao.master.atlas.aa;

import com.redphase.entity.atlas.aa.DataAaXj;
import com.redphase.framework.IBaseDao;
import com.redphase.framework.IEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>空气式超声波_巡检 数据库处理接口类。
 */
@Mapper
public interface IDataAaXjDao extends IBaseDao {

    /**
     * 判断是否存在
     */
    @Select("select IFNULL(count(0),0) as count from data_aa_xj where  data_analyze_id = #{dataAnalyzeId} ")
    int isDataYN(IEntity entity) throws Exception;


    /**
     * 根据主键 物理删除
     */
    @Delete("delete from data_aa_xj where  data_analyze_id = #{dataAnalyzeId} ")
    int deleteByPrimaryKey(IEntity entity) throws Exception;

    List<DataAaXj> findDataByIds(@Param("ids") String ids) throws Exception;

    DataAaXj findDataMaxByMap(Map map) throws Exception;
}