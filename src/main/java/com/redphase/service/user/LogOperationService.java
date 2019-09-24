package com.redphase.service.user;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.user.ILogOperationService;
import com.redphase.dao.master.user.ILogOperationDao;
import com.redphase.dto.user.LogOperationDto;
import com.redphase.entity.user.LogOperation;
import com.redphase.framework.SysErrorCode;
import com.redphase.framework.exception.ServiceException;
import com.redphase.framework.service.BaseService;
import com.redphase.framework.util.I18nUtil;
import com.redphase.framework.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class LogOperationService extends BaseService implements ILogOperationService {

    @Autowired
    private ILogOperationDao logOperationDao;

    @Override
    public PageInfo findDataIsPage(LogOperationDto dto) {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            LogOperation entity = copyTo(dto, LogOperation.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = logOperationDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), LogOperationDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return pageInfo;
    }

    @Override
    public void log(Map<String, Object> map) {
        try {
            LogOperation orgLogOperation = new LogOperation() {{
                setId(IdUtil.nextId());
                setType((String) map.get("type"));// 操作类型a增d删u改q查)
                setMemo((String) map.get("memo"));// 描述
                setDetailInfo((String) map.get("detailInfo"));// 具体
                setCreateId(Long.parseLong("" + map.get("createId")));// 操作人id
                setCreateName((String) map.get("createName"));// 操作人姓名
                setCreateIp((String) map.get("createIp"));// 建立者IP
            }};
            // 新增
            logOperationDao.insert(orgLogOperation);
        } catch (Exception e) {
            log.error("操作日志信息保存失败!", e);
        }
    }

    @Override
    public LogOperationDto findDataById(LogOperationDto dto) {
        LogOperationDto result = null;
        try {
            LogOperation entity = copyTo(dto, LogOperation.class);
            result = copyTo(logOperationDao.selectByPrimaryKey(entity), LogOperationDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return result;
    }

    @Override
    public String deleteData(LogOperationDto dto) {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramNotFound"));
            }
            logOperationDao.deleteByPrimaryKey(copyTo(dto, LogOperation.class));
        } catch (Exception e) {
            log.error("信息删除失败!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return result;
    }
}