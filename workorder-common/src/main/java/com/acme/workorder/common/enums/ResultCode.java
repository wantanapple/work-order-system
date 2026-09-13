package com.acme.workorder.common.enums;

/**
 * 统一错误码。
 */
public enum ResultCode {
    SUCCESS(0, "success"),
    BAD_REQUEST(400, "bad request"),
    UNAUTHORIZED(401, "unauthorized"),
    FORBIDDEN(403, "forbidden"),
    NOT_FOUND(404, "not found"),
    INTERNAL_ERROR(500, "internal error"),
    BIZ_ERROR(1000, "business error");

    /** 错误码 */
    private final int code;

    /** 错误信息 */
    private final String message;

    /** 构造错误码枚举 */
    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
