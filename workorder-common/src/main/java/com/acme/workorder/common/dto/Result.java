package com.acme.workorder.common.dto;

import java.io.Serializable;
import com.acme.workorder.common.enums.ResultCode;

/**
 * 统一响应包装。
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 响应码，0 表示成功 */
    private int code;

    /** 响应信息 */
    private String message;

    /** 响应数据（失败时通常为空） */
    private T data;

    /** 默认构造（供序列化框架使用） */
    public Result() {
    }

    /** 全参构造 */
    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /** 成功（无数据） */
    public static <T> Result<T> ok() {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    /** 成功（携带数据） */
    public static <T> Result<T> ok(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /** 失败（使用错误码默认信息） */
    public static <T> Result<T> fail(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMessage(), null);
    }

    /** 失败（自定义错误码与信息） */
    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message, null);
    }

    /** 是否成功（code 为 0） */
    public boolean isSuccess() {
        return code == ResultCode.SUCCESS.getCode();
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }
}
