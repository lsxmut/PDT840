package com.redphase.service.user;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.user.IAuthPermService;
import com.redphase.dao.master.user.IAuthPermDao;
import com.redphase.dto.user.AuthPermDto;
import com.redphase.entity.user.AuthPerm;
import com.redphase.framework.NodeTree;
import com.redphase.framework.Response;
import com.redphase.framework.SysErrorCode;
import com.redphase.framework.exception.ServiceException;
import com.redphase.framework.service.BaseService;
import com.redphase.framework.util.CommonConstant;
import com.redphase.framework.util.I18nUtil;
import com.redphase.framework.util.IdUtil;
import com.redphase.framework.util.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AuthPermService extends BaseService implements IAuthPermService {
    @Autowired
    private IAuthPermDao authPermDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(AuthPermDto dto) {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramNotFound"));
            }
            AuthPerm entity = copyTo(dto, AuthPerm.class);
            if (authPermDao.isDataYN(entity) != 0) { //判断数据是否存在
                //数据存在
                authPermDao.update(entity);
            } else {
                //新增
                if (ValidatorUtil.isEmpty(entity.getId())) {
                    entity.setId(IdUtil.createUUID(22));
                }
                authPermDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存失败!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return result;
    }

    @Override
    public String deleteData(AuthPermDto dto) {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramNotFound"));
            }
            authPermDao.deleteByPrimaryKey(copyTo(dto, AuthPerm.class));
        } catch (Exception e) {
            log.error("信息删除失败!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {
            Exception.class, RuntimeException.class})
    public String deleteDataById(AuthPermDto dto) {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramNotFound"));
            }
            AuthPerm entity = copyTo(dto, AuthPerm.class);
            if (authPermDao.isNoDelFlag(entity) > 0) {
                throw new RuntimeException("当前数据为系统保留数据，不能删除！");
            }
            authPermDao.deleteById(entity);
        } catch (Exception e) {
            log.error("信息删除失败", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return result;
    }

    @Override
    public PageInfo findDataIsPage(AuthPermDto dto) {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            AuthPerm entity = copyTo(dto, AuthPerm.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = authPermDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), AuthPermDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询失败!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return pageInfo;
    }

    @Override
    public List<AuthPermDto> findDataIsList(AuthPermDto dto) {
        List<AuthPermDto> results = null;
        try {
            results = copyTo(authPermDao.findDataIsList(copyTo(dto, AuthPerm.class)), AuthPermDto.class);
        } catch (Exception e) {
            log.error("查询失败!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return results;
    }

    @Override
    public AuthPermDto findDataById(AuthPermDto dto) {
        AuthPermDto result = null;
        try {
            result = copyTo(authPermDao.selectByPrimaryKey(copyTo(dto, AuthPerm.class)), AuthPermDto.class);
        } catch (Exception e) {
            log.error("信息详情查询失败!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return result;
    }

    @Override
    public String recoveryDataById(AuthPermDto dto) {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramNotFound"));
            }
            authPermDao.recoveryDataById(copyTo(dto, AuthPerm.class));
        } catch (Exception e) {
            log.error("信息恢复失败!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return result;
    }

    @Override
    public List<AuthPermDto> findDataIsTree(AuthPermDto dto) {
        try {
            List<AuthPermDto> results = findDataIsList(dto);
            if (results == null) {
                return null;
            }
            NodeTree<AuthPermDto> tree = new NodeTree(results, "id", "parentId", "nodes");
            return tree.buildTree();
        } catch (Exception e) {
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
    }

    @Override
    public List<AuthPermDto> findPermDataIsListByRoleId(Map dto) {
        List<AuthPermDto> results = null;
        try {
            results = (List<AuthPermDto>) authPermDao.findPermDataIsListByRoleId(dto);
        } catch (Exception e) {
            log.error("信息查询失败!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return results;
    }

}