package com.acme.workorder.common.dto;

import com.acme.workorder.common.enums.ConditionOperator;
import lombok.Data;

/**
 * 单个动态查询条件。
 * field 为实体属性名(camelCase)，QueryWrapperUtil 会自动映射为数据库列名。
 * value 约定：IN/NOT_IN 传 Collection；BETWEEN 传长度为2的 Object[] 或 Collection(start,end)；IS_NULL/IS_NOT_NULL 无需 value。
 */
@Data
public class QueryCondition {

    /**
     * 查询字段（实体属性名，camelCase），由 QueryWrapperUtil 映射为数据库列名
     */
    private String field;

    /**
     * 比较操作符
     */
    private ConditionOperator operator;

    /**
     * 条件值：IN/NOT_IN 传 Collection；BETWEEN 传 [start, end]；IS_NULL/IS_NOT_NULL 无需传
     */
    private Object value;

    /**
     * 构建指定操作符的查询条件
     */
    public static QueryCondition of(String field, ConditionOperator operator, Object value) {
        QueryCondition c = new QueryCondition();
        c.setField(field);
        c.setOperator(operator);
        c.setValue(value);
        return c;
    }

    /**
     * 等值查询
     */
    public static QueryCondition eq(String field, Object value) {
        return of(field, ConditionOperator.EQ, value);
    }

    /**
     * 不等查询
     */
    public static QueryCondition ne(String field, Object value) {
        return of(field, ConditionOperator.NE, value);
    }

    /**
     * 大于
     */
    public static QueryCondition gt(String field, Object value) {
        return of(field, ConditionOperator.GT, value);
    }

    /**
     * 大于等于
     */
    public static QueryCondition gte(String field, Object value) {
        return of(field, ConditionOperator.GTE, value);
    }

    /**
     * 小于
     */
    public static QueryCondition lt(String field, Object value) {
        return of(field, ConditionOperator.LT, value);
    }

    /**
     * 小于等于
     */
    public static QueryCondition lte(String field, Object value) {
        return of(field, ConditionOperator.LTE, value);
    }

    /**
     * 模糊查询（%value%）
     */
    public static QueryCondition like(String field, Object value) {
        return of(field, ConditionOperator.LIKE, value);
    }

    /**
     * 包含查询（value 传 Collection）
     */
    public static QueryCondition in(String field, Object value) {
        return of(field, ConditionOperator.IN, value);
    }

    /**
     * 区间查询（value 传 [start, end]）
     */
    public static QueryCondition between(String field, Object value) {
        return of(field, ConditionOperator.BETWEEN, value);
    }
}