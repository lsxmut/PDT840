package com.redphase.service.user;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.user.ILogLoginService;
import com.redphase.dao.master.user.ILogLoginDao;
import com.redphase.dto.user.LogLoginDto;
import com.redphase.entity.user.LogLogin;
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

import java.util.List;

@Service
@Slf4j
public class LogLoginService extends BaseService implements ILogLoginService {
    @Autowired
    private ILogLoginDao loginDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(LogLoginDto dto) {
        Response result = new Response(0, "success");
        try {
            //删除N天以上的日志
            loginDao.deleteByExpireDay(60);
            LogLogin entity = copyTo(dto, LogLogin.class);
            entity.setId(IdUtil.nextId());
            //新增
            loginDao.insert(entity);
            dto.setId(entity.getId());
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return result;
    }

    @Override
    public PageInfo findDataIsPage(LogLoginDto dto) {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            LogLogin entity = copyTo(dto, LogLogin.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = loginDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), LogLoginDto.class));
        } catch (Exception e) {
            log.error("信息查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return pageInfo;
    }

    @Override
    public List<LogLoginDto> findDataIsList(LogLoginDto dto) {
        List<LogLoginDto> results = null;
        try {
            LogLogin entity = copyTo(dto, LogLogin.class);
            results = copyTo(loginDao.findDataIsList(entity), LogLoginDto.class);
        } catch (Exception e) {
            log.error("信息查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return results;
    }

    @Override
    public LogLoginDto findDataById(LogLoginDto dto) {
        LogLoginDto result = null;
        try {
            LogLogin entity = copyTo(dto, LogLogin.class);
            result = copyTo(loginDao.selectByPrimaryKey(entity), LogLoginDto.class);
        } catch (Exception e) {
            log.error("信息查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return result;
    }

}