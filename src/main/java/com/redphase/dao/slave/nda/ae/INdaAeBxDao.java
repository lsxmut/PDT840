package com.redphase.dao.slave.nda.ae;

import com.redphase.entity.atlas.ae.DataAeBx;
import com.redphase.framework.IBaseDao;
import com.redphase.framework.IEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * <p>接触式超声波_波形图谱 数据库处理接口类。
 */
@Mapper
public interface INdaAeBxDao extends IBaseDao {

    /**
     * 判断是否存在
     */
    @Select("select IFNULL(count(0),0) as count from data_ae_bx where  data_analyze_id = #{dataAnalyzeId} ")
    int isDataYN(IEntity entity) throws Exception;


    /**
     * 根据主键 物理删除
     */
    @Delete("delete from data_ae_bx where  data_analyze_id = #{dataAnalyzeId} ")
    int deleteByPrimaryKey(IEntity entity) throws Exception;


    DataAeBx findDataByDevice(Map map) throws Exception;
}