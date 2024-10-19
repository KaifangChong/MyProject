package com.kc.lesson.permission;

public enum RolePermissionLevel {

    // 此处与user表的role字段对应起来
    LOGIN(1, "all"), // 登录用户
    ADMIN(3, "admin"); // 管理员

    int code;
    String msg;

    RolePermissionLevel(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
