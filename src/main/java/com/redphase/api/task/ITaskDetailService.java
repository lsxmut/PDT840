package com.redphase.api.task;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.task.TaskDetailDto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>task_detail 业务处理接口类。
 */
public interface ITaskDetailService {
    String acPrefix = "/feign//ITaskDetailService";

    /**
     * <p>。
     */
    public Response batchSaveOrUpdateData(List<TaskDetailDto> dtos) throws Exception;

    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(TaskDetailDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(TaskDetailDto dto) throws Exception;

    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(TaskDetailDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<TaskDetailDto> findDataIsList(TaskDetailDto dto) throws Exception;

    /**
     * <p>信息详情。
     */
    public TaskDetailDto findDataById(TaskDetailDto dto) throws Exception;
}