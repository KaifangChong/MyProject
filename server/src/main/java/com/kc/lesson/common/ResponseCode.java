package com.kc.lesson.common;

import lombok.Getter;

/**
 * 响应报文，统一封装类
 */
@Getter
public enum ResponseCode {
    SUCCESS(200, "成功"),
    FAIL(500, "失败");

    private int code;
    private String msg;

    private ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
