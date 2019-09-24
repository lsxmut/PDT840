package com.redphase.service.task;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.task.ITaskHistoryService;
import com.redphase.dao.master.task.ITaskHistoryDao;
import com.redphase.dto.task.TaskHistoryDto;
import com.redphase.entity.task.TaskHistory;
import com.redphase.framework.Response;
import com.redphase.framework.SysErrorCode;
import com.redphase.framework.exception.ServiceException;
import com.redphase.framework.service.BaseService;
import com.redphase.framework.util.I18nUtil;
import com.redphase.framework.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>历史任务单 业务处理实现类。
 */
@Service
@Slf4j
public class TaskHistoryService extends BaseService implements ITaskHistoryService {
    @Autowired
    private ITaskHistoryDao taskHistoryDao;

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(TaskHistoryDto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            TaskHistory entity = copyTo(dto, TaskHistory.class);
            //判断数据是否存在
            if (taskHistoryDao.isDataYN(entity) != 0) {
                //数据存在
                taskHistoryDao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                taskHistoryDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(TaskHistoryDto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            TaskHistory entity = copyTo(dto, TaskHistory.class);
            if (taskHistoryDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public TaskHistoryDto findDataById(TaskHistoryDto dto) throws Exception {
        TaskHistoryDto result = null;
        try {
            TaskHistory entity = copyTo(dto, TaskHistory.class);
            result = copyTo(taskHistoryDao.selectByPrimaryKey(entity), TaskHistoryDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public PageInfo findDataIsPage(TaskHistoryDto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) throw new RuntimeException(I18nUtil.get("error.paramError"));
            TaskHistory entity = copyTo(dto, TaskHistory.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = taskHistoryDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), TaskHistoryDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<TaskHistoryDto> findDataIsList(TaskHistoryDto dto) throws Exception {
        List<TaskHistoryDto> results = null;
        try {
            TaskHistory entity = copyTo(dto, TaskHistory.class);
            results = copyTo(taskHistoryDao.findDataIsList(entity), TaskHistoryDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }
}