package com.redphase.service.nda.tev;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.nda.tev.INdaTevTjService;
import com.redphase.dao.slave.nda.tev.INdaTevTjDao;
import com.redphase.dto.atlas.tev.DataTevTjDto;
import com.redphase.entity.atlas.tev.DataTevTj;
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
 * <p>地电波_统计数据 业务处理实现类。
 */
@Service
@Slf4j
public class NdaTevTjService extends BaseService implements INdaTevTjService {
    @Autowired
    private INdaTevTjDao dataTevTjDao;

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(DataTevTjDto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataTevTj entity = copyTo(dto, DataTevTj.class);
            //判断数据是否存在
            if (dataTevTjDao.isDataYN(entity) != 0) {
                //数据存在
                dataTevTjDao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                dataTevTjDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(DataTevTjDto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataTevTj entity = copyTo(dto, DataTevTj.class);
            if (dataTevTjDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public PageInfo findDataIsPage(DataTevTjDto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataTevTj entity = copyTo(dto, DataTevTj.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = dataTevTjDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), DataTevTjDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<DataTevTjDto> findDataIsList(DataTevTjDto dto) throws Exception {
        List<DataTevTjDto> results = null;
        try {
            DataTevTj entity = copyTo(dto, DataTevTj.class);
            results = copyTo(dataTevTjDao.findDataIsList(entity), DataTevTjDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public DataTevTjDto findDataById(DataTevTjDto dto) throws Exception {
        DataTevTjDto result = null;
        try {
            DataTevTj entity = copyTo(dto, DataTevTj.class);
            result = copyTo(dataTevTjDao.selectByPrimaryKey(entity), DataTevTjDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public List<DataTevTjDto> findDataByIds(String ids) throws Exception {
        if (StringUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return copyTo(dataTevTjDao.findDataByIds(ids), DataTevTjDto.class);
    }


}