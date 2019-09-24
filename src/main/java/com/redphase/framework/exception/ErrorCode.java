package com.redphase.framework.exception;

/**
 * @Description: 错误返回码接口
 */
public interface ErrorCode {

    /**
     * 返回码
     *
     * @return
     */
    int getCode();

    /**
     * 异常信息, 源自{@link Throwable#getMessage()}
     */
    String getMessage();

    void setMessage(String msg);

}
