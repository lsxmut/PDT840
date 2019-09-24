package com.redphase.dao.slave.nda.tev;

import com.redphase.entity.atlas.tev.DataTevXjzs;
import com.redphase.framework.IBaseDao;
import com.redphase.framework.IEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>地电波_巡检噪声 数据库处理接口类。
 */
@Mapper
public interface INdaTevXjzsDao extends IBaseDao {

    /**
     * 判断是否存在
     */
    @Select("select IFNULL(count(0),0) as count from data_tev_xjzs where  data_analyze_id = #{dataAnalyzeId} ")
    int isDataYN(IEntity entity) throws Exception;


    /**
     * 根据主键 物理删除
     */
    @Delete("delete from data_tev_xjzs where  data_analyze_id = #{dataAnalyzeId} ")
    int deleteByPrimaryKey(IEntity entity) throws Exception;

    /**
     * 根据检测文件ID查询地电波_巡检噪声数据
     *
     * @param ids 检测文件ID，逗号分隔
     * @return
     * @throws Exception
     */
    List<DataTevXjzs> findDataByIds(@Param("ids") String ids) throws Exception;
}