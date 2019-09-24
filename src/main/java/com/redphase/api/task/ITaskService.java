package com.redphase.api.task;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.task.TaskDto;
import com.redphase.framework.Response;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * <p>task 业务处理接口类。
 */
public interface ITaskService {
    String acPrefix = "/feign//ITaskService";

    /**
     * <p>批量保存。
     */
    public Response batchSave(List<TaskDto> taskDtos) throws Exception;

    /**
     * <p>批量保存。
     */

    public Response createFile(List<TaskDto> taskDtos) throws Exception;

    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(TaskDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(TaskDto dto) throws Exception;

    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(TaskDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<TaskDto> findDataIsList(TaskDto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public TaskDto findDataById(TaskDto dto) throws Exception;

    /**
     * <p>导入历史任务单
     */
    HashMap<String, Object> importTask(File file) throws Exception;


}