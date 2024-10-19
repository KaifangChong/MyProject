package com.kc.lesson.common;


import lombok.Data;

import java.io.Serializable;

/**
 * 响应报文，统一封装类
 */
@Data
public class KCResponse<T> implements Serializable {
    private int code;
    private String msg;
    private T data;

    public KCResponse(ResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.msg = responseCode.getMsg();
    }

    public KCResponse(ResponseCode responseCode, String msg) {
        this(responseCode);
        this.msg = msg;
    }

    public KCResponse(ResponseCode responseCode, T data) {
        this(responseCode);
        this.data = data;
    }

    public KCResponse(ResponseCode responseCode, String msg, T data) {
        this(responseCode);
        this.msg = msg;
        this.data = data;
    }
}
