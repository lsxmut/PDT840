package com.redphase.dao.master.atlas;

import com.redphase.entity.atlas.DataDeviceSite;
import com.redphase.framework.IBaseDao;
import com.redphase.framework.IEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>检查点 数据库处理接口类。
 */
@Mapper
public interface IDataDeviceSiteDao extends IBaseDao {

    /**
     * 判断是否存在
     */
    @Select("select IFNULL(count(0),0) as count from data_device_site where data_device_id = #{dataDeviceId} and site_name = #{siteName} ")
    int isDataYN(IEntity entity) throws Exception;


    /**
     * 根据主键 物理删除
     */
    @Delete("delete from data_device_site where  data_device_id = #{dataDeviceId} ")
    int deleteByPrimaryKey(IEntity entity) throws Exception;

    Object findDataByDeviceAndSite(IEntity entity) throws Exception;

    /**
     * 查询多个设备的所有检测位置
     *
     * @param ids 检测设备ID，逗号分隔
     * @return
     * @throws Exception
     */
    List<DataDeviceSite> findDataByDataDeviceIds(@Param("ids") String ids);

    @Select("SELECT site_name from data_device_site where site_name !='#' and data_device_id=#{dataDeviceId} GROUP BY site_name")
    List<String> findSiteNameIsList(IEntity entity);
}