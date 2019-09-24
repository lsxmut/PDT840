package com.redphase.service.user;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.user.IUserService;
import com.redphase.dao.master.sys.ISysVariableDao;
import com.redphase.dao.master.user.IAuthRoleDao;
import com.redphase.dao.master.user.IUserDao;
import com.redphase.dto.user.UserDto;
import com.redphase.entity.user.User;
import com.redphase.framework.Response;
import com.redphase.framework.SysErrorCode;
import com.redphase.framework.annotation.ALogOperation;
import com.redphase.framework.annotation.RfAccount2Bean;
import com.redphase.framework.exception.ServiceException;
import com.redphase.framework.security.MD5;
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

@Service
@Slf4j
public class UserService extends BaseService implements IUserService {
    @Autowired
    private IUserDao userDao;
    @Autowired
    private IAuthRoleDao authRoleDao;
    @Autowired
    private ISysVariableDao sysVariableDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
//    @RfAccount2Bean
    @ALogOperation(type = "修改", desc = "用户管理")
    public Response saveOrUpdateData(UserDto dto) {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramNotFound"));
            }
            User entity = copyTo(dto, User.class);
            // 判断数据是否存在
            if (userDao.isDataYN(entity) > 0) {
                // 数据存在
                userDao.update(entity);
            } else {
                if (!jwtUtil.isPermitted("user:add")) {
                    throw new RuntimeException("没用新增员工的权限!");
                }
                if (userDao.isAccountYN(dto.getAccount()) > 0) {
                    throw new RuntimeException("账号已存在!");
                }
                entity.setPwd(MD5.pwdMd5Hex(entity.getPwd()));
                // 新增
                entity.setId(IdUtil.nextId());
                userDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存失败!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    @RfAccount2Bean
    public Response insert(UserDto dto) {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramNotFound"));
            }
            User entity = copyTo(dto, User.class);
            // 判断数据是否存在
            if (userDao.isDataYN(entity) != 0) {
                // 数据存在
            } else {
                if (!jwtUtil.isPermitted("user:add")) {
                    throw new RuntimeException("没用新增员工的权限!");
                }
                if (userDao.isAccountYN(dto.getAccount()) > 0) {
                    throw new RuntimeException("账号已存在!");
                }
                entity.setPwd(MD5.pwdMd5Hex(entity.getPwd()));
                // 新增
                userDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存失败!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    @RfAccount2Bean
    public String updateData(UserDto dto) {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramNotFound"));
            }
            User entity = copyTo(dto, User.class);
            // 判断数据是否存在
            if (userDao.isDataYN(entity) != 0) {
                // 数据存在
                userDao.update(entity);
            }
        } catch (Exception e) {
            log.error("信息更新失败!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return result;
    }

    @Override
    public String deleteData(UserDto dto) {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramNotFound"));
            }
            userDao.deleteByPrimaryKey(copyTo(dto, User.class));
        } catch (Exception e) {
            log.error("信息删除失败!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    @RfAccount2Bean
    @ALogOperation(type = "删除", desc = "用户管理")
    public String deleteDataById(UserDto dto) {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramNotFound"));
            }
            userDao.deleteById(copyTo(dto, User.class));
        } catch (Exception e) {
            log.error("信息删除失败!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return result;
    }

    @Override
    public PageInfo findDataIsPage(UserDto dto) {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            User entity = copyTo(dto, User.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = userDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), UserDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return pageInfo;
    }

    @Override
    public List<UserDto> findDataIsList(UserDto dto) {
        List<UserDto> results = null;
        try {
            results = copyTo(userDao.findDataIsList(copyTo(dto, User.class)), UserDto.class);
        } catch (Exception e) {
            log.error("信息查询失败!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return results;
    }

    @Override
    public UserDto findDataById(UserDto dto) {
        UserDto result = null;
        try {
            result = copyTo(userDao.selectByPrimaryKey(copyTo(dto, User.class)), UserDto.class);
        } catch (Exception e) {
            log.error("信息详情byid查询失败!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return result;
    }

    @Override
    public String recoveryDataById(UserDto dto) {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramNotFound"));
            }
            userDao.recoveryDataById(copyTo(dto, User.class));
        } catch (Exception e) {
            result = "信息恢复失败!";
            log.error(result, e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return result;
    }


    @Override
    public String isAccountYN(String account) {
        String result = "success";
        try {
            if (ValidatorUtil.notEmpty(account)) {
                if (userDao.isAccountYN(account) > 0) {
                    throw new RuntimeException("账号[" + account + "]已存在!");
                }
            }
        } catch (Exception e) {
            log.error("判断员工account是否存在,数据库处理异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return result;
    }

    @Override
    @RfAccount2Bean
    @ALogOperation(type = "修改", desc = "员工密码")
    public String updatePwd(UserDto dto) {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramNotFound"));
            }
            if (!dto.getNewpwd().equals(dto.getConfirmpwd())) {
                throw new RuntimeException("两次密码不一致!");
            }
            UserDto userDto = copyTo(userDao.selectUserPwdByPrimaryKey(copyTo(dto, User.class)), UserDto.class);
            if (userDto == null) {
                throw new RuntimeException("账号不存在!");
            }

            userDto.setOldpwd(MD5.pwdMd5Hex(dto.getOldpwd()));
            userDto.setConfirmpwd(MD5.pwdMd5Hex(dto.getConfirmpwd()));

            if (!userDto.getPwd().equals(userDto.getOldpwd())) {
                throw new RuntimeException("原密码错误!");
            }
            if (userDao.updatePwd(copyTo(userDto, User.class)) == 0) {
                throw new RuntimeException("密码修改失败,请重试!");
            }
        } catch (Exception e) {
            log.error("修改密码异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return result;
    }

    @Override
    @RfAccount2Bean
    @ALogOperation(type = "修改", desc = "员工密码重置")
    public String resetPwd(UserDto dto) {
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramNotFound"));
            }
            String newPwd = IdUtil.createUUID(8);
            dto.setPwd(MD5.pwdMd5Hex(MD5.md5Hex(newPwd)));
            if (userDao.resetPwd(copyTo(dto, User.class)) == 0) {
                throw new RuntimeException("密码重置失败,请重试!");
            }
            return newPwd;
        } catch (Exception e) {
            log.error("密码重置失败!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
    }
}