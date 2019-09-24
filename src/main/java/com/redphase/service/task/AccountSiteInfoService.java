package com.redphase.service.task;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.task.IAccountSiteInfoService;
import com.redphase.dao.master.task.IAccountSiteInfoDao;
import com.redphase.dto.task.AccountSiteInfoDto;
import com.redphase.entity.task.AccountSiteInfo;
import com.redphase.framework.Response;
import com.redphase.framework.SysErrorCode;
import com.redphase.framework.annotation.ALogOperation;
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
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Slf4j
public class AccountSiteInfoService extends BaseService implements IAccountSiteInfoService {
    @Autowired
    private IAccountSiteInfoDao accountSiteInfoDao;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = {Exception.class, RuntimeException.class})
    @ALogOperation(type = "编辑", desc = "台账建设-检测位置")
    public Response batchSaveByAccountId(String accountId, List<AccountSiteInfoDto> dictDtos) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (StringUtils.isEmpty(accountId)) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            accountSiteInfoDao.deleteByAccountId(Long.valueOf(accountId));       //删除该台帐已经配置好的检测位置字典
            if (null != dictDtos && dictDtos.size() > 0) {
                List<AccountSiteInfo> entitys = copyTo(dictDtos, AccountSiteInfo.class);
                for (AccountSiteInfo entity : entitys) {
                    //判断数据是否存在
                    if (accountSiteInfoDao.isDataYN(entity) != 0) {
                        //数据存在
                        accountSiteInfoDao.update(entity);
                    } else {
                        entity.setId(String.valueOf(IdUtil.nextId()));
                        accountSiteInfoDao.insert(entity);
                    }
                }
            }
        } catch (Exception e) {
            log.error("信息批量保存异常!", e);
            result = new Response(SysErrorCode.defaultError.getCode(), e.getMessage());
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(AccountSiteInfoDto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) throw new RuntimeException(I18nUtil.get("error.paramError"));
            AccountSiteInfo entity = copyTo(dto, AccountSiteInfo.class);
            //判断数据是否存在
            if (accountSiteInfoDao.isDataYN(entity) != 0) {
                //数据存在
                accountSiteInfoDao.update(entity);
            } else {
                //新增
                entity.setId(String.valueOf(IdUtil.nextId()));
                accountSiteInfoDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    public String deleteData(AccountSiteInfoDto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) throw new RuntimeException(I18nUtil.get("error.paramError"));
            AccountSiteInfo entity = copyTo(dto, AccountSiteInfo.class);
            if (accountSiteInfoDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public PageInfo findDataIsPage(AccountSiteInfoDto dto) throws Exception {
        PageInfo pageInfo;
        try {
            if (dto == null) throw new RuntimeException(I18nUtil.get("error.paramError"));
            AccountSiteInfo entity = copyTo(dto, AccountSiteInfo.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = accountSiteInfoDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), AccountSiteInfoDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }


    @Override
    public List<AccountSiteInfoDto> findDataIsList(AccountSiteInfoDto dto) throws Exception {
        List<AccountSiteInfoDto> results = null;
        try {
            AccountSiteInfo entity = copyTo(dto, AccountSiteInfo.class);
            results = copyTo(accountSiteInfoDao.findDataIsList(entity), AccountSiteInfoDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }


    @Override
    public AccountSiteInfoDto findDataById(AccountSiteInfoDto dto) throws Exception {
        AccountSiteInfoDto result = null;
        try {
            AccountSiteInfo entity = copyTo(dto, AccountSiteInfo.class);
            result = copyTo(accountSiteInfoDao.selectByPrimaryKey(entity), AccountSiteInfoDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


}