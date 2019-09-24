package com.redphase.framework;

import lombok.Data;

@Data
public class Response<T> {

    /**
     * code 0 - 正确； 非0为错误码
     */

    public int code = 0;


    public String message = "";

    public T data;

    public Response(T data) {
        this.data = data;
    }

    public Response() {
    }

    public Response(int code, String message) {
        this(code, message, null);
    }

    public Response(int code, String message, T data) {
        super();
        this.code = code;
        this.message = message;
        this.data = data == null ? null : data;
    }

    public Response(int code, T data) {
        this(code, "", data);
    }

    /**
     * 根据code返回错误编码
     *
     * @param code
     * @return
     */
    public static Response error(int code) {
        return new Response(code);
    }

    public static Response error(String message) {
        Response response = new Response(500, message);
        return response;
    }

    public static Response error(Throwable t) {
        String message = t.getMessage();
        int index = message.lastIndexOf(":");
        if (index > 0) {
            message = message.substring(index + 1);
        }
        return new Response(500, message);
    }

    public static Response badRequest() {
        Response response = new Response(400, "");
        return response;
    }

    public static Response error(int code, String message) {
        return new Response(code, message);
    }
}
