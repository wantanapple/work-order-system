package com.acme.workorder.common.enums;

/**
 * 查询条件操作符。
 */
public enum ConditionOperator {
    /**
     * 等于
     */
    EQ,
    /**
     * 不等于
     */
    NE,
    /**
     * 大于
     */
    GT,
    /**
     * 大于等于
     */
    GTE,
    /**
     * 小于
     */
    LT,
    /**
     * 小于等于
     */
    LTE,
    /**
     * 模糊查询（%value%）
     */
    LIKE,
    /**
     * 左模糊查询（%value）
     */
    LIKE_LEFT,
    /**
     * 右模糊查询（value%）
     */
    LIKE_RIGHT,
    /**
     * 包含（value 传 Collection）
     */
    IN,
    /**
     * 不包含（value 传 Collection）
     */
    NOT_IN,
    /**
     * 区间查询（value 传 [start, end]）
     */
    BETWEEN,
    /**
     * 空值
     */
    IS_NULL,
    /**
     * 非空值
     */
    IS_NOT_NULL
}