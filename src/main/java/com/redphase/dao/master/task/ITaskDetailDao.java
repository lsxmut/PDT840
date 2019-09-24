package com.redphase.dao.master.task;

import com.redphase.dto.task.TaskDto;
import com.redphase.dto.task.TaskHistoryConfigDto;
import com.redphase.framework.IBaseDao;
import com.redphase.framework.IEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>task_detail 数据库处理接口类。
 */
@Mapper
public interface ITaskDetailDao extends IBaseDao {

    /**
     * 判断是否存在
     */
    @Select("select IFNULL(count(0),0) as count from task_detail where  id = #{id} ")
    int isDataYN(IEntity entity) throws Exception;


    /**
     * 根据主键 物理删除
     */
    @Delete("delete from task_detail where  id = #{id} ")
    int deleteByPrimaryKey(IEntity entity) throws Exception;

    List<TaskHistoryConfigDto> findDataIsListByTask(TaskDto task);
}