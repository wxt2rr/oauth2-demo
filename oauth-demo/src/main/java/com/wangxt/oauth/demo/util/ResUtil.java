package com.wangxt.oauth.demo.util;

import com.wangxt.oauth.demo.pojo.resp.ErrorCode;
import com.wangxt.oauth.demo.pojo.resp.Result;

public class ResUtil {
    // 返回成功
    public static <T> Result<T> success(T data) {
        return new Result<>(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage(), data);
    }

    // 返回失败
    public static <T> Result<T> fail(ErrorCode errorCode) {
        return new Result<>(errorCode.getCode(), errorCode.getMessage(), null);
    }
}

