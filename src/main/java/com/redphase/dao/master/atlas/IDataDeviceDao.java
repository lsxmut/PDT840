package com.redphase.dao.master.atlas;

import com.redphase.entity.atlas.DataDevice;
import com.redphase.framework.IBaseDao;
import com.redphase.framework.IEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>电力设备 数据库处理接口类。
 */
@Mapper
public interface IDataDeviceDao extends IBaseDao {
    /**
     * 判断是否存在
     */
    @Select("select " +
            "IFNULL(count(0),0) as count " +
            "from data_device " +
            "where " +
            "date_detection=#{dateDetection} " +
            "and test_technology=#{testTechnology} " +
            "and electric_bureau=#{electricBureau} " +
            "and substation=#{substation} " +
            "and device_type=#{deviceType} " +
            "and device_name=#{deviceName} " +
            "and space_name=#{spaceName} " +
            "and folder_path=#{folderPath} "
    )
    int isDataYN(IEntity entity) throws Exception;


    /**
     * 根据主键 物理删除
     */
    @Delete("delete from data_device where  id = #{id} ")
    int deleteByPrimaryKey(IEntity entity) throws Exception;

    Object findDataByTestTechnology(IEntity entity) throws Exception;

    List<?> findListByDevice(IEntity entity) throws Exception;

    Object findDataIsDevice(IEntity entity) throws Exception;

    @Select("select substation from data_device WHERE substation!='#' GROUP BY substation")
    List<String> findSubstationIsList() throws Exception;

    @Select("select  t1.id as \"id\"\n" +
            "        ,t1.date_detection as \"dateDetection\"\n" +
            "        ,t1.test_technology as \"testTechnology\"\n" +
            "        ,t1.electric_bureau as \"electricBureau\"\n" +
            "        ,t1.substation as \"substation\"\n" +
            "        ,t1.voltage_level as \"voltageLevel\"\n" +
            "        ,t1.device_type as \"deviceType\"\n" +
            "        ,t1.device_name as \"deviceName\"\n" +
            "        ,t1.space_name as \"spaceName\"\n" +
            "        ,t1.folder_path as \"folderPath\"\n" +
            "        ,t1.create_id as \"createId\"\n" +
            "        ,t1.date_created as \"dateCreated\"\n" +
            "        ,t1.date_updated as \"dateUpdated\"" +
            " from data_device t1" +
            " where t1.device_name!='#'  " +
            " and t1.substation=#{substation}" +
            " GROUP BY t1.device_name")
    List<DataDevice> findDeviceNameIsList(IEntity entity) throws Exception;

    @Select("select test_technology from data_device where device_name!='#' and substation=#{substation} and device_name=#{deviceName} GROUP BY device_name,test_technology")
    List<String> findTestTechnologyIsList(IEntity entity) throws Exception;
    List<?> findListByMap(Map entity) throws Exception;
}