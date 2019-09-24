package com.redphase.dao.master.atlas;

import com.redphase.entity.atlas.DataAnalyze;
import com.redphase.framework.IBaseDao;
import com.redphase.framework.IEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>检测文件 数据库处理接口类。
 */
@Mapper
public interface IDataAnalyzeDao extends IBaseDao {

    /**
     * 判断是否存在
     */
    @Select("select IFNULL(count(0),0) as count from data_analyze where  id = #{id} ")
    int isDataYN(IEntity entity) throws Exception;


    /**
     * 根据主键 物理删除
     */
    @Delete("delete from data_analyze where  id = #{id} ")
    int deleteByPrimaryKey(IEntity entity) throws Exception;

    Object findDataByDataSource(IEntity entity) throws Exception;

    /**
     * 查询多个设备的所有检测文件
     *
     * @param ids 检测设备ID，逗号分隔
     * @return
     * @throws Exception
     */
    List<DataAnalyze> findDataByDataDeviceIds(@Param("ids") String ids) throws Exception;

    /**
     * 趋势查询
     *
     * @return
     * @throws Exception
     */
    List<DataAnalyze> thendSearch(Map map) throws Exception;

    List<DataAnalyze> findListByDevice(Map map) throws Exception;
}