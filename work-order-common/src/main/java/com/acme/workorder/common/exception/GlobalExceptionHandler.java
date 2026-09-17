package com.acme.workorder.common.exception;

import com.acme.workorder.common.dto.Result;
import com.acme.workorder.common.enums.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * 全局异常处理器：由 workorder-common 自动装配统一注册，各服务无需重复配置。
 * 参数校验由业务侧自行完成，失败时抛出 BizException 即可。
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常：记录 warn 并返回对应错误码
     */
    @ExceptionHandler(BizException.class)
    public Result<Void> handleBizException(BizException e) {
        log.warn("business error: code={}, msg={}", e.getCode(), e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    /**
     * 请求体不可读（如 JSON 解析失败）：返回 400
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Void> handleNotReadable(HttpMessageNotReadableException e) {
        log.warn("request body not readable", e);
        return Result.fail(ResultCode.BAD_REQUEST.getCode(), "request body invalid");
    }

    /**
     * 参数类型不匹配：返回 400
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Void> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        return Result.fail(ResultCode.BAD_REQUEST.getCode(), "param type invalid: " + e.getName());
    }

    /**
     * 兜底异常：记录 error 并返回 500
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleUnknown(Exception e) {
        log.error("unhandled exception", e);
        return Result.fail(ResultCode.INTERNAL_ERROR);
    }
}
