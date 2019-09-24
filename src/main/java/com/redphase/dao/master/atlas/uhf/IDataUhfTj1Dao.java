package com.redphase.dao.master.atlas.uhf;

import com.redphase.entity.atlas.uhf.DataUhfTj1;
import com.redphase.framework.IBaseDao;
import com.redphase.framework.IEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>特高频_模式1_统计数据 数据库处理接口类。
 */
@Mapper
public interface IDataUhfTj1Dao extends IBaseDao {

    /**
     * 判断是否存在
     */
    @Select("select IFNULL(count(0),0) as count from data_uhf_tj_1 where  data_analyze_id = #{dataAnalyzeId} ")
    int isDataYN(IEntity entity) throws Exception;


    /**
     * 根据主键 物理删除
     */
    @Delete("delete from data_uhf_tj_1 where  data_analyze_id = #{dataAnalyzeId} ")
    int deleteByPrimaryKey(IEntity entity) throws Exception;

    List<DataUhfTj1> findDataByIds(@Param("ids") String ids) throws Exception;
}