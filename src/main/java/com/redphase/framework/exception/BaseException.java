package com.redphase.framework.exception;

/**
 * @Description: 异常基类
 */
public abstract class BaseException extends RuntimeException {
    private static final long serialVersionUID = 5692243177785821696L;

    private ErrorCode error;
    private Object data = "";
    private Throwable cause;

    public BaseException() {
        super();
    }

    public BaseException(ErrorCode error) {
        this.error = error;
    }

    public BaseException(ErrorCode error, Throwable cause, String msg) {
        super(msg);
        this.error = error;
        this.data = msg;
        this.cause = cause;
    }

    public BaseException(ErrorCode error, String msg) {
        super(msg);
        this.error = error;
        this.data = msg;
    }

    public ErrorCode getError() {
        return error;
    }

    public Object getData() {
        return data;
    }

}
