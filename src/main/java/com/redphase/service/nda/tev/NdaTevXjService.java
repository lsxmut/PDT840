package com.redphase.service.nda.tev;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.nda.tev.INdaTevXjService;
import com.redphase.dao.slave.nda.tev.INdaTevXjDao;
import com.redphase.dto.atlas.tev.DataTevXjDto;
import com.redphase.entity.atlas.tev.DataTevXj;
import com.redphase.framework.Response;
import com.redphase.framework.SysErrorCode;
import com.redphase.framework.exception.ServiceException;
import com.redphase.framework.service.BaseService;
import com.redphase.framework.util.I18nUtil;
import com.redphase.framework.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>地电波_巡检 业务处理实现类。
 */
@Service
@Slf4j
public class NdaTevXjService extends BaseService implements INdaTevXjService {
    @Autowired
    private INdaTevXjDao dataTevXjDao;

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(DataTevXjDto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataTevXj entity = copyTo(dto, DataTevXj.class);
            //判断数据是否存在
            if (dataTevXjDao.isDataYN(entity) != 0) {
                //数据存在
                dataTevXjDao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                dataTevXjDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(DataTevXjDto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataTevXj entity = copyTo(dto, DataTevXj.class);
            if (dataTevXjDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public PageInfo findDataIsPage(DataTevXjDto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataTevXj entity = copyTo(dto, DataTevXj.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = dataTevXjDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), DataTevXjDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<DataTevXjDto> findDataIsList(DataTevXjDto dto) throws Exception {
        List<DataTevXjDto> results = null;
        try {
            DataTevXj entity = copyTo(dto, DataTevXj.class);
            results = copyTo(dataTevXjDao.findDataIsList(entity), DataTevXjDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public DataTevXjDto findDataById(DataTevXjDto dto) throws Exception {
        DataTevXjDto result = null;
        try {
            DataTevXj entity = copyTo(dto, DataTevXj.class);
            result = copyTo(dataTevXjDao.selectByPrimaryKey(entity), DataTevXjDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public List<DataTevXjDto> findDataByIds(String ids) throws Exception {
        if (StringUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return copyTo(dataTevXjDao.findDataByIds(ids), DataTevXjDto.class);
    }
}