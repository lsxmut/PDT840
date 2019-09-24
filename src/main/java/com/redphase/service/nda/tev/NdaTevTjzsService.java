package com.redphase.service.nda.tev;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.nda.tev.INdaTevTjzsService;
import com.redphase.dao.slave.nda.tev.INdaTevTjzsDao;
import com.redphase.dto.atlas.tev.DataTevTjzsDto;
import com.redphase.entity.atlas.tev.DataTevTjzs;
import com.redphase.framework.Response;
import com.redphase.framework.SysErrorCode;
import com.redphase.framework.exception.ServiceException;
import com.redphase.framework.service.BaseService;
import com.redphase.framework.util.I18nUtil;
import com.redphase.framework.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>地电波_统计噪声 业务处理实现类。
 */
@Service
@Slf4j
public class NdaTevTjzsService extends BaseService implements INdaTevTjzsService {
    @Autowired
    private INdaTevTjzsDao dataTevTjzsDao;

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(DataTevTjzsDto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataTevTjzs entity = copyTo(dto, DataTevTjzs.class);
            //判断数据是否存在
            if (dataTevTjzsDao.isDataYN(entity) != 0) {
                //数据存在
                dataTevTjzsDao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                dataTevTjzsDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(DataTevTjzsDto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataTevTjzs entity = copyTo(dto, DataTevTjzs.class);
            if (dataTevTjzsDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public PageInfo findDataIsPage(DataTevTjzsDto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataTevTjzs entity = copyTo(dto, DataTevTjzs.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = dataTevTjzsDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), DataTevTjzsDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<DataTevTjzsDto> findDataIsList(DataTevTjzsDto dto) throws Exception {
        List<DataTevTjzsDto> results = null;
        try {
            DataTevTjzs entity = copyTo(dto, DataTevTjzs.class);
            results = copyTo(dataTevTjzsDao.findDataIsList(entity), DataTevTjzsDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public DataTevTjzsDto findDataById(DataTevTjzsDto dto) throws Exception {
        DataTevTjzsDto result = null;
        try {
            DataTevTjzs entity = copyTo(dto, DataTevTjzs.class);
            result = copyTo(dataTevTjzsDao.selectByPrimaryKey(entity), DataTevTjzsDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


}