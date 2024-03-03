package com.wangxt.oauth.demo.pojo.resp;

import lombok.Getter;

@Getter
public enum ErrorCode {
    SUCCESS(200, "resp success"),
    PARAM_FAIL(-100, "request param error"),
    CLIENT_ID_ERROR(-101, "client_id not found"),
    CLIENT_SECRET_ERROR(-102, "client_secret not match"),
    CODE_ERROR(-103, "code not match"),
    UN_SUPPORT_TYPE(-200, "un support type"),
    CODE_EXPIRED(-104, "code expired"),
    USER_NOT_EXISTS(-105, "username or password error");
    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
