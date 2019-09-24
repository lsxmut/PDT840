package com.redphase.dao.master.task;

import com.redphase.framework.IBaseDao;
import com.redphase.framework.IEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>历史任务单 数据库处理接口类。
 */
@Mapper
public interface ITaskHistoryDao extends IBaseDao {

    /**
     * 判断是否存在
     */
    @Select("select IFNULL(count(0),0) as count " +
            "from task_history " +
            "where date_detection=#{dateDetection} " +
            "and electric_bureau=#{electricBureau} " +
            "and substation=#{substation} " +
            "and voltage_level = #{voltageLevel} " +
            "and device_type=#{deviceType} " +
            "and test_technology=#{testTechnology} " +
            "and file_path=#{filePath} "
    )
    int isDataYN(IEntity entity) throws Exception;

    /**
     * 根据主键 物理删除
     */
    @Delete("delete from task_history where  id = #{id} ")
    int deleteByPrimaryKey(IEntity entity) throws Exception;
}