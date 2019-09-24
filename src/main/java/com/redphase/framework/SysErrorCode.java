package com.redphase.framework;


import com.redphase.framework.exception.ErrorCode;
import com.redphase.framework.util.I18nUtil;

public enum SysErrorCode implements ErrorCode {

    SUCCESS(0, I18nUtil.get("ialert.success")),
    defaultError(500, I18nUtil.get("error.default")),
    ;
    private Integer code;
    private String message;

    SysErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    SysErrorCode(Integer code) {
        this.code = code;
    }

    public static String getMsg(int code) {
        for (SysErrorCode errorCode : SysErrorCode.values()) {
            if (code == errorCode.getCode()) {
                return errorCode.getMessage();
            }
        }
        return null;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String msg) {
        this.message = msg;
    }

}
