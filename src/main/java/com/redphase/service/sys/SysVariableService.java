package com.redphase.service.sys;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.sys.ISysVariableService;
import com.redphase.dao.master.sys.ISysVariableDao;
import com.redphase.dto.sys.SysVariableDto;
import com.redphase.entity.sys.SysVariable;
import com.redphase.framework.NodeTree;
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
public class SysVariableService extends BaseService implements ISysVariableService {
    @Autowired
    private ISysVariableDao sysVariableDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(SysVariableDto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            SysVariable entity = copyTo(dto, SysVariable.class);
            //判断数据是否存在
            if (sysVariableDao.isDataYN(entity) != 0) {
                //数据存在
                sysVariableDao.update(entity);
            } else {
                //新增
                sysVariableDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return result;
    }

    @Override
    public Response updateByCode(SysVariableDto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            SysVariable entity = copyTo(dto, SysVariable.class);
            //数据存在
            sysVariableDao.updateByCode(entity);
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return result;
    }

    @Override
    public String deleteData(SysVariableDto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            SysVariable entity = copyTo(dto, SysVariable.class);
            if (sysVariableDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public String deleteDataById(SysVariableDto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            SysVariable entity = copyTo(dto, SysVariable.class);
            if (sysVariableDao.deleteById(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("逻辑删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return result;
    }

    @Override
    public PageInfo findDataIsPage(SysVariableDto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            SysVariable entity = copyTo(dto, SysVariable.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = sysVariableDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), SysVariableDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return pageInfo;
    }

    @Override
    public List<SysVariableDto> findDataIsList(SysVariableDto dto) throws Exception {
        List<SysVariableDto> results = null;
        try {
            SysVariable entity = copyTo(dto, SysVariable.class);
            results = copyTo(sysVariableDao.findDataIsList(entity), SysVariableDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return results;
    }

    @Override
    public List<SysVariableDto> findChildDataIsList(SysVariableDto dto) throws Exception {
        List<SysVariableDto> results = null;
        try {
            SysVariable entity = copyTo(dto, SysVariable.class);
            results = copyTo(sysVariableDao.findChildDataIsList(entity), SysVariableDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return results;
    }

    @Override
    public SysVariableDto findDataById(SysVariableDto dto) throws Exception {
        SysVariableDto result = null;
        try {
            SysVariable entity = copyTo(dto, SysVariable.class);
            result = copyTo(sysVariableDao.selectByPrimaryKey(entity), SysVariableDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return result;
    }

    @Override
    public SysVariableDto findDataByCode(SysVariableDto dto) throws Exception {
        SysVariableDto result = null;
        try {
            SysVariable entity = copyTo(dto, SysVariable.class);
            result = copyTo(sysVariableDao.findDataByCode(entity), SysVariableDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
        return result;
    }


    @Override
    public List<SysVariableDto> findDataIsTree(SysVariableDto dto) {
        try {
            List<SysVariableDto> results = findDataIsList(dto);
            if (results == null) {
                return null;
            }
            NodeTree<SysVariableDto> tree = new NodeTree(results, "id", "parentId", "nodes");
            return tree.buildTree();
        } catch (Exception e) {
            log.error("信息树获取失败!", e);
            throw new ServiceException(SysErrorCode.defaultError, e);
        }
    }
}