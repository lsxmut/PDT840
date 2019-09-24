package com.redphase.service.nda.tev;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.nda.tev.INdaTevTjlbService;
import com.redphase.dao.slave.nda.tev.INdaTevTjlbDao;
import com.redphase.dto.atlas.tev.DataTevTjlbDto;
import com.redphase.entity.atlas.tev.DataTevTjlb;
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
 * <p>地电波_统计录波 业务处理实现类。
 */
@Service
@Slf4j
public class NdaTevTjlbService extends BaseService implements INdaTevTjlbService {
    @Autowired
    private INdaTevTjlbDao dataTevTjlbDao;

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(DataTevTjlbDto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataTevTjlb entity = copyTo(dto, DataTevTjlb.class);
            //判断数据是否存在
            if (dataTevTjlbDao.isDataYN(entity) != 0) {
                //数据存在
                dataTevTjlbDao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                dataTevTjlbDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(DataTevTjlbDto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataTevTjlb entity = copyTo(dto, DataTevTjlb.class);
            if (dataTevTjlbDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public PageInfo findDataIsPage(DataTevTjlbDto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataTevTjlb entity = copyTo(dto, DataTevTjlb.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = dataTevTjlbDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), DataTevTjlbDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<DataTevTjlbDto> findDataIsList(DataTevTjlbDto dto) throws Exception {
        List<DataTevTjlbDto> results = null;
        try {
            DataTevTjlb entity = copyTo(dto, DataTevTjlb.class);
            results = copyTo(dataTevTjlbDao.findDataIsList(entity), DataTevTjlbDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public DataTevTjlbDto findDataById(DataTevTjlbDto dto) throws Exception {
        DataTevTjlbDto result = null;
        try {
            DataTevTjlb entity = copyTo(dto, DataTevTjlb.class);
            result = copyTo(dataTevTjlbDao.selectByPrimaryKey(entity), DataTevTjlbDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


}