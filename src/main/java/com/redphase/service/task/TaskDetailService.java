package com.redphase.service.task;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.task.ITaskDetailService;
import com.redphase.dao.master.task.ITaskDetailDao;
import com.redphase.dto.task.TaskDetailDto;
import com.redphase.entity.task.TaskDetail;
import com.redphase.framework.Response;
import com.redphase.framework.SysErrorCode;
import com.redphase.framework.exception.ServiceException;
import com.redphase.framework.service.BaseService;
import com.redphase.framework.util.CommonConstant;
import com.redphase.framework.util.I18nUtil;
import com.redphase.framework.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TaskDetailService extends BaseService implements ITaskDetailService {
    @Autowired
    private ITaskDetailDao taskDetailDao;

    /**
     * 批量保存或更新任务明细表
     *
     * @param dtos
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response batchSaveOrUpdateData(List<TaskDetailDto> dtos) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dtos == null || dtos.size() <= 0) throw new RuntimeException(I18nUtil.get("error.paramError"));
            List<TaskDetail> entitys = copyTo(dtos, TaskDetail.class);
            List<String> ids = new ArrayList<>();
            for (TaskDetail entity : entitys) {
                //判断数据是否存在
                if (taskDetailDao.isDataYN(entity) != 0) {
                    //数据存在
                    taskDetailDao.update(entity);
                } else {
                    //新增
                    entity.setId(String.valueOf(IdUtil.nextId()));
                    taskDetailDao.insert(entity);
                    ids.add(entity.getId());
                }
            }
            result.data = ids;
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            result = new Response(SysErrorCode.defaultError.getCode(), e.getMessage());
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(TaskDetailDto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) throw new RuntimeException(I18nUtil.get("error.paramError"));
            TaskDetail entity = copyTo(dto, TaskDetail.class);
            //判断数据是否存在
            if (taskDetailDao.isDataYN(entity) != 0) {
                //数据存在
                taskDetailDao.update(entity);
            } else {
                //新增
                entity.setId(String.valueOf(IdUtil.nextId()));
                taskDetailDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(TaskDetailDto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) throw new RuntimeException(I18nUtil.get("error.paramError"));
            TaskDetail entity = copyTo(dto, TaskDetail.class);
            if (taskDetailDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public PageInfo findDataIsPage(TaskDetailDto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) throw new RuntimeException(I18nUtil.get("error.paramError"));
            TaskDetail entity = copyTo(dto, TaskDetail.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = taskDetailDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), TaskDetailDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<TaskDetailDto> findDataIsList(TaskDetailDto dto) throws Exception {
        List<TaskDetailDto> results = null;
        try {
            TaskDetail entity = copyTo(dto, TaskDetail.class);
            results = copyTo(taskDetailDao.findDataIsList(entity), TaskDetailDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public TaskDetailDto findDataById(TaskDetailDto dto) throws Exception {
        TaskDetailDto result = null;
        try {
            TaskDetail entity = copyTo(dto, TaskDetail.class);
            result = copyTo(taskDetailDao.selectByPrimaryKey(entity), TaskDetailDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


}