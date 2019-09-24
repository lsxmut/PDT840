package com.redphase.service.nda.ae;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.nda.ae.INdaAeTjlbService;
import com.redphase.dao.slave.nda.ae.INdaAeTjlbDao;
import com.redphase.dto.atlas.ae.DataAeTjlbDto;
import com.redphase.entity.atlas.ae.DataAeTjlb;
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
 * <p>接触式超声波_统计录波 业务处理实现类。
 */
@Service
@Slf4j
public class NdaAeTjlbService extends BaseService implements INdaAeTjlbService {
    @Autowired
    private INdaAeTjlbDao dataAeTjlbDao;

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(DataAeTjlbDto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAeTjlb entity = copyTo(dto, DataAeTjlb.class);
            //判断数据是否存在
            if (dataAeTjlbDao.isDataYN(entity) != 0) {
                //数据存在
                dataAeTjlbDao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                dataAeTjlbDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(DataAeTjlbDto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAeTjlb entity = copyTo(dto, DataAeTjlb.class);
            if (dataAeTjlbDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public PageInfo findDataIsPage(DataAeTjlbDto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAeTjlb entity = copyTo(dto, DataAeTjlb.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = dataAeTjlbDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), DataAeTjlbDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<DataAeTjlbDto> findDataIsList(DataAeTjlbDto dto) throws Exception {
        List<DataAeTjlbDto> results = null;
        try {
            DataAeTjlb entity = copyTo(dto, DataAeTjlb.class);
            results = copyTo(dataAeTjlbDao.findDataIsList(entity), DataAeTjlbDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public DataAeTjlbDto findDataById(DataAeTjlbDto dto) throws Exception {
        DataAeTjlbDto result = null;
        try {
            DataAeTjlb entity = copyTo(dto, DataAeTjlb.class);
            result = copyTo(dataAeTjlbDao.selectByPrimaryKey(entity), DataAeTjlbDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


}