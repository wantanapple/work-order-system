package com.acme.workorder.common.util;

import com.acme.workorder.common.enums.ResultCode;
import com.acme.workorder.common.exception.BizException;

import java.util.Collection;
import java.util.Map;

/**
 * 参数校验工具：校验失败直接抛 BizException，由 GlobalExceptionHandler 统一转成 Result。
 * 例：AssertUtil.notNull(ticket, ResultCode.NOT_FOUND, "工单不存在");
 */
public final class AssertUtil {

    private AssertUtil() {
    }

    /** 对象非空，否则抛 BizException（使用 ResultCode 默认信息） */
    public static void notNull(Object value, ResultCode code) {
        notNull(value, code, code.getMessage());
    }

    /** 对象非空，否则抛带自定义信息的 BizException */
    public static void notNull(Object value, ResultCode code, String message) {
        if (value == null) {
            throw new BizException(code, message);
        }
    }

    /** 对象非空，否则抛带自定义错误码的 BizException */
    public static void notNull(Object value, int code, String message) {
        if (value == null) {
            throw new BizException(code, message);
        }
    }

    /** 字符串非空（非 null 且 trim 后非空） */
    public static void notBlank(String value, ResultCode code, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new BizException(code, message);
        }
    }

    /** 字符串非空（非 null 且 trim 后非空），否则抛 BizException（使用 ResultCode 默认信息） */
    public static void notBlank(String value, ResultCode code) {
        notBlank(value, code, code.getMessage());
    }

    /** 集合非空 */
    public static void notEmpty(Collection<?> value, ResultCode code, String message) {
        if (value == null || value.isEmpty()) {
            throw new BizException(code, message);
        }
    }

    /** 集合非空，否则抛 BizException（使用 ResultCode 默认信息） */
    public static void notEmpty(Collection<?> value, ResultCode code) {
        notEmpty(value, code, code.getMessage());
    }

    /** Map 非空 */
    public static void notEmpty(Map<?, ?> value, ResultCode code, String message) {
        if (value == null || value.isEmpty()) {
            throw new BizException(code, message);
        }
    }

    /** Map 非空，否则抛 BizException（使用 ResultCode 默认信息） */
    public static void notEmpty(Map<?, ?> value, ResultCode code) {
        notEmpty(value, code, code.getMessage());
    }

    /** 数组非空 */
    public static void notEmpty(Object[] value, ResultCode code, String message) {
        if (value == null || value.length == 0) {
            throw new BizException(code, message);
        }
    }

    /** 条件必须为 true，否则抛异常 */
    public static void isTrue(boolean expression, ResultCode code, String message) {
        if (!expression) {
            throw new BizException(code, message);
        }
    }

    /** 条件必须为 true，否则抛 BizException（使用 ResultCode 默认信息） */
    public static void isTrue(boolean expression, ResultCode code) {
        isTrue(expression, code, code.getMessage());
    }

    /** 条件必须为 false，否则抛异常 */
    public static void isFalse(boolean expression, ResultCode code, String message) {
        if (expression) {
            throw new BizException(code, message);
        }
    }

    /** 两个对象必须相等，否则抛异常 */
    public static void isEquals(Object a, Object b, ResultCode code, String message) {
        if (a == null ? b != null : !a.equals(b)) {
            throw new BizException(code, message);
        }
    }
}
