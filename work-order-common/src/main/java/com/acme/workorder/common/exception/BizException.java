package com.acme.workorder.common.exception;

import com.acme.workorder.common.enums.ResultCode;

/**
 * 业务异常：携带错误码，被 GlobalExceptionHandler 捕获并转成统一 Result。
 */
public class BizException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private final int code;

    /**
     * 使用 ResultCode 自带的错误信息
     */
    public BizException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    /**
     * ResultCode 错误码 + 自定义错误信息
     */
    public BizException(ResultCode resultCode, String message) {
        super(message);
        this.code = resultCode.getCode();
    }

    /**
     * 自定义错误码与错误信息
     */
    public BizException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 获取错误码
     */
    public int getCode() {
        return code;
    }
}
