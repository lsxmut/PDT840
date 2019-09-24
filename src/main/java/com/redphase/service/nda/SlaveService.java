package com.redphase.service.nda;

import com.redphase.api.nda.ISlaveService;
import com.redphase.dao.slave.ISlaveDao;
import com.redphase.framework.Response;
import com.redphase.framework.SysErrorCode;
import com.redphase.framework.exception.ServiceException;
import com.redphase.framework.service.BaseService;
import com.redphase.framework.util.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class SlaveService extends BaseService implements ISlaveService {
    @Autowired
    private ISlaveDao slaveDao;

    @Override
    @Transactional(value = "slaveTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response flushDb() throws Exception {
        Response result = new Response(0, "success");
        try {
            long s = System.currentTimeMillis();
            slaveDao.flushDbDataAaBx();
            slaveDao.flushDbDataAaFx();
            slaveDao.flushDbDataAaTj();
            slaveDao.flushDbDataAaTjlb();
            slaveDao.flushDbDataAaTjzs();
            slaveDao.flushDbDataAaXj();
            slaveDao.flushDbDataAaXjzs();
            slaveDao.flushDbDataAeBx();
            slaveDao.flushDbDataAeFx();
            slaveDao.flushDbDataAeTj();
            slaveDao.flushDbDataAeTjlb();
            slaveDao.flushDbDataAeTjzs();
            slaveDao.flushDbDataAeXj();
            slaveDao.flushDbDataAeXjzs();
            slaveDao.flushDbDataAnalyze();
            slaveDao.flushDbDataDevice();
            slaveDao.flushDbDataDeviceSite();
            slaveDao.flushDbDataHfctTj();
            slaveDao.flushDbDataHfctTjlb();
            slaveDao.flushDbDataHfctTjzs();
            slaveDao.flushDbDataHfctXj();
            slaveDao.flushDbDataHfctXjzs();
            slaveDao.flushDbDataHj();
            slaveDao.flushDbDataLc();
            slaveDao.flushDbDataTevTj();
            slaveDao.flushDbDataTevTjlb();
            slaveDao.flushDbDataTevTjzs();
            slaveDao.flushDbDataTevXj();
            slaveDao.flushDbDataTevXjlh();
            slaveDao.flushDbDataTevXjzs();
            slaveDao.flushDbDataUhfTj1();
            slaveDao.flushDbDataUhfTj2();
            slaveDao.flushDbDataUhfTjlb1();
            slaveDao.flushDbDataUhfTjlb2();
            slaveDao.flushDbDataUhfTjzs1();
            slaveDao.flushDbDataUhfTjzs2();
            log.debug("flushDb:" + (System.currentTimeMillis() - s));
        } catch (Exception e) {
            log.error("清空数据库!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }
}