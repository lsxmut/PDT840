package com.redphase.api.task;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.task.TaskHistoryDto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>历史任务单 业务处理接口类。
 */
public interface ITaskHistoryService {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(TaskHistoryDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(TaskHistoryDto dto) throws Exception;

    /**
     * <p>信息详情。
     */
    public TaskHistoryDto findDataById(TaskHistoryDto dto) throws Exception;

    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(TaskHistoryDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<TaskHistoryDto> findDataIsList(TaskHistoryDto dto) throws Exception;
}