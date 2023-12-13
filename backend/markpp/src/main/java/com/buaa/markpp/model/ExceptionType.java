package com.buaa.markpp.model;

public enum ExceptionType {
    NOT_IMPLEMENTED("此方法尚未实现"),
    INVALID_PARAM("参数格式非法"),
    UNAUTHORIZED("无权访问"),
    NOT_FOUND("未找到对象"),
    FORWARD_TIMEOUT("请求转发超时"),
    INTERNAL_ERROR("操作失败"),
    REPEATED_OPERATION("操作重复");

    private final String message;

    ExceptionType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
