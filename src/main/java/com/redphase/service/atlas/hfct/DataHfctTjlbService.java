package com.redphase.service.atlas.hfct;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.atlas.hfct.IDataHfctTjlbService;
import com.redphase.dao.master.atlas.hfct.IDataHfctTjlbDao;
import com.redphase.dto.atlas.hfct.DataHfctTjlbDto;
import com.redphase.entity.atlas.hfct.DataHfctTjlb;
import com.redphase.framework.Response;
import com.redphase.framework.SysErrorCode;
import com.redphase.framework.exception.ServiceException;
import com.redphase.framework.util.I18nUtil;
import com.redphase.framework.util.IdUtil;
import com.redphase.service.atlas.AtlasService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>高频电流_统计录波 业务处理实现类。
 */
@Service
@Slf4j
public class DataHfctTjlbService extends AtlasService implements IDataHfctTjlbService {
    @Autowired
    private IDataHfctTjlbDao dataHfctTjlbDao;

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(DataHfctTjlbDto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataHfctTjlb entity = copyTo(dto, DataHfctTjlb.class);
            //判断数据是否存在
            if (dataHfctTjlbDao.isDataYN(entity) != 0) {
                //数据存在
                dataHfctTjlbDao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                dataHfctTjlbDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(DataHfctTjlbDto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataHfctTjlb entity = copyTo(dto, DataHfctTjlb.class);
            if (dataHfctTjlbDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public PageInfo findDataIsPage(DataHfctTjlbDto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataHfctTjlb entity = copyTo(dto, DataHfctTjlb.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = dataHfctTjlbDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), DataHfctTjlbDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<DataHfctTjlbDto> findDataIsList(DataHfctTjlbDto dto) throws Exception {
        List<DataHfctTjlbDto> results = null;
        try {
            DataHfctTjlb entity = copyTo(dto, DataHfctTjlb.class);
            results = copyTo(dataHfctTjlbDao.findDataIsList(entity), DataHfctTjlbDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public DataHfctTjlbDto findDataById(DataHfctTjlbDto dto) throws Exception {
        DataHfctTjlbDto result = null;
        try {
            DataHfctTjlb entity = copyTo(dto, DataHfctTjlb.class);
            result = copyTo(dataHfctTjlbDao.selectByPrimaryKey(entity), DataHfctTjlbDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


}