package com.ticket.reservation.exception;

/**
 * 异常枚举
 */
public enum TicketSystemExceptionEnum {

    SYSTEM_EXCEPTION(20001, "系统异常"),
    NEED_USER_NAME(10001, "用户名不能为空"),
    NEED_PASSWORD(10002, "密码不能为空"),
    USERNAME_EXIST(10003, "用户名已存在"),
    LOGIN_ERROR(10004, "用户名或密码错误"),
    NEED_LOGIN(10007, "请登陆后再进行操作"),
    UPDATE_FAILED(10008, "更新失败"),
    ORDER_EXITS(10009, "已经存在相同订单"),
    REQUEST_PARAM_ERROR(10012, "参数异常"),
    ORDER_STATUS_NOT_FOUND(10020, "订单状态未找到");

    Integer code;
    String msg;

    TicketSystemExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
