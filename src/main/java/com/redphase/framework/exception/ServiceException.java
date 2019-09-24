package com.redphase.framework.exception;

import com.redphase.framework.util.CommonConstant;
import org.springframework.dao.*;

public class ServiceException extends BaseException {
    private static final long serialVersionUID = 1690624130527369260L;

    public ServiceException() {
        super();
    }

    public ServiceException(ErrorCode error, Throwable cause, String msg) {
        super(error, cause, msg);
    }

    public ServiceException(ErrorCode error, Throwable cause) {
        super(error, cause, error.getCode() + CommonConstant.FEIGN_ERROR_SYMBOL_STRING + (
                cause instanceof DataIntegrityViolationException
                        ? "违反数据库完整性约束" + (cause.getMessage().indexOf("for key") != -1 ? cause.getMessage().substring(0, cause.getMessage().indexOf("for key")) : cause.getMessage())
                        : cause instanceof CleanupFailureDataAccessException
                        ? "操作成功地执行，但在释放数据库资源时发生异常"
                        : cause instanceof DataAccessResourceFailureException
                        ? "数据访问资源彻底失败，例如不能连接数据库"
                        : cause instanceof DataRetrievalFailureException
                        ? "某些数据不能被检测到"
                        : cause instanceof DeadlockLoserDataAccessException
                        ? "当前的操作因为死锁而失败"
                        : cause instanceof IncorrectUpdateSemanticsDataAccessException
                        ? "Update时发生某些没有预料到的情况"
                        : cause instanceof DataAccessException
                        ? "数据库操作异常"
                        : cause.getMessage()
        ));
    }

    public ServiceException(ErrorCode error, String msg) {
        super(error, msg);
    }

    public ServiceException(ErrorCode error) {
        super(error, CommonConstant.FEIGN_ERROR_SYMBOL_STRING);
    }
}
