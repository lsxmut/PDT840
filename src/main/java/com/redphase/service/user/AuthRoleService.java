package com.redphase.service.user;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.user.IAuthRoleService;
import com.redphase.dao.master.user.IAuthRoleDao;
import com.redphase.dao.master.user.IAuthRoleVsPermDao;
import com.redphase.dto.user.AuthPermDto;
import com.redphase.dto.user.AuthRoleDto;
import com.redphase.entity.user.AuthRole;
import com.redphase.entity.user.AuthRoleVsPerm;
import com.redphase.framework.Response;
import com.redphase.framework.SysErrorCode;
import com.redphase.framework.exception.ServiceException;
import com.redphase.framework.service.BaseService;
import com.redphase.framework.util.CommonConstant;
import com.redphase.framework.util.I18nUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class AuthRoleService extends BaseService implements IAuthRoleService {
    @Autowired
    private IAuthRoleDao authRoleDao;
    @Autowired
    private IAuthRoleVsPermDao authRoleVsPermDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(AuthRoleDto dto) {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramNotFound"));
            }
            AuthRole entity = copyTo(dto, AuthRole.class);
//            if (JwtUtil.isPermitted("authRole:super")) {
//                entity.setIsSuper(dto.getIsSuper() == null ? 0 : 1);// 超级管理员0否1是
//            } else {
//                entity.setIsSuper(0);// 超级管理员0否1是
//            }
            // 判断数据是否存在
            if (authRoleDao.isDataYN(entity) != 0) {
                // 数据存在
                authRoleDao.update(entity);
//                if (JwtUtil.isPermitted("authRole:edit:perm")) {
//                    // 1.清空当前角色权限关联信息
//                    AuthRoleVsPerm roleVsPerm = new AuthRoleVsPerm();
//                    roleVsPerm.setRoleId(entity.getId());
//                    authRoleVsPermDao.deleteBulkDataByRoleId(roleVsPerm);
//                }
//                if (JwtUtil.isPermitted("authRole:edit:menu")) {
//                    // 1.清空当前角色菜单关联信息
//                    AuthRoleVsMenu roleVsMenu = new AuthRoleVsMenu();
//                    roleVsMenu.setRoleId(entity.getId());
//                    authRoleVsMenuDao.deleteBulkDataByRoleId(roleVsMenu);
//                }
//                if (JwtUtil.isPermitted("authRole:edit:app")) {
//                    // 1.清空当前角色应用关联信息
//                    AuthRoleVsApp roleVsApp = new AuthRoleVsApp();
//                    roleVsApp.setRoleId(entity.getId());
//                    authRoleVsAppDao.deleteBulkDataByRoleId(roleVsApp);
//                }
            } else {
                // 新增
                authRoleDao.insert(entity);
                result.data = entity.getId();
            }
//            if (JwtUtil.isPermitted("authRole:edit:perm")) {
//                // 2.新增角色权限关联信息
//                if (dto.getPermIds() != null) {
//                    List<AuthRoleVsPerm> xdtos = new ArrayList<>();
//                    for (String permId : dto.getPermIds()) {
//                        AuthRoleVsPerm authRoleVsPerm = new AuthRoleVsPerm();
//                        authRoleVsPerm.setRoleId(entity.getId());
//                        authRoleVsPerm.setPermId(permId);
//                        authRoleVsPerm.setCreateId(dto.getCreateId());
//                        xdtos.add(authRoleVsPerm);
//                    }
//                    authRoleVsPermDao.insertBatch(xdtos);
//                }
//            }
        } catch (Exception e) {
            log.error("信息保存失败!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return result;
    }

    @Override
    public String deleteData(AuthRoleDto dto) {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramNotFound"));
            }
            authRoleDao.deleteByPrimaryKey(copyTo(dto, AuthRole.class));
        } catch (Exception e) {
            log.error("信息删除失败!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return result;
    }

    @Override
    public String deleteDataById(AuthRoleDto dto) {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramNotFound"));
            }
            authRoleDao.deleteById(copyTo(dto, AuthRole.class));
        } catch (Exception e) {
            log.error("信息删除失败!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return result;
    }

    @Override
    public PageInfo findDataIsPage(AuthRoleDto dto) {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            AuthRole entity = copyTo(dto, AuthRole.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = authRoleDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), AuthRoleDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询失败!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return pageInfo;
    }

    @Override
    public List<AuthRoleDto> findDataIsList(AuthRoleDto dto) {
        List<AuthRoleDto> results = null;
        try {
            results = copyTo(authRoleDao.findDataIsList(copyTo(dto, AuthRole.class)), AuthRoleDto.class);
        } catch (Exception e) {
            log.error("信息查询失败!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return results;
    }

    @Override
    public AuthRoleDto findDataById(AuthRoleDto dto) {
        AuthRoleDto result = null;
        try {
            result = copyTo(authRoleDao.selectByPrimaryKey(copyTo(dto, AuthRole.class)), AuthRoleDto.class);
        } catch (Exception e) {
            log.error("信息详情查询失败!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return result;
    }

    @Override
    public List<AuthPermDto> findPermIsList(AuthRoleDto dto) {
        List<AuthPermDto> results = null;
        try {
            AuthRoleVsPerm authRoleVsPerm = new AuthRoleVsPerm();
            authRoleVsPerm.setRoleId(dto.getId());
            results = copyTo(authRoleVsPermDao.findPermIsList(authRoleVsPerm), AuthPermDto.class);
        } catch (Exception e) {
            log.error("信息查询失败!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return results;
    }
}