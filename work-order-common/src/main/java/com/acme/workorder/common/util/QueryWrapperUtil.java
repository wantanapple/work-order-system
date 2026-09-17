package com.acme.workorder.common.util;

import com.acme.workorder.common.dto.PageQuery;
import com.acme.workorder.common.dto.QueryCondition;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;

import java.util.*;

/**
 * 将 PageQuery 的动态条件封装为 MyBatis-Plus 的 QueryWrapper。
 * - conditions 为空 -> 空 wrapper（全量分页）
 * - 条件 field 为实体属性名，自动映射为数据库列名（依赖 MP 已初始化该实体 TableInfo）
 */
public final class QueryWrapperUtil {

    private QueryWrapperUtil() {
    }

    /**
     * 根据 PageQuery 构建 QueryWrapper；pageQuery 为空或无条件/无排序时返回空 wrapper
     */
    public static <T> QueryWrapper<T> buildWrapper(Class<T> entityClass, PageQuery pageQuery) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        if (pageQuery == null) {
            return wrapper;
        }
        Map<String, String> propToColumn = propertyToColumnMap(entityClass);
        applyConditions(wrapper, propToColumn, pageQuery.getConditions());
        applyOrderBy(wrapper, propToColumn, pageQuery);
        return wrapper;
    }

    /**
     * 逐个追加查询条件；field 或 operator 为空的条件跳过，value 为 null 的取值条件忽略
     */
    private static <T> void applyConditions(QueryWrapper<T> wrapper, Map<String, String> propToColumn, List<QueryCondition> conditions) {
        if (conditions == null || conditions.isEmpty()) {
            return;
        }
        for (QueryCondition c : conditions) {
            if (c == null || c.getField() == null || c.getOperator() == null) {
                continue;
            }
            String column = resolveColumn(c.getField(), propToColumn);
            Object value = c.getValue();
            switch (c.getOperator()) {
                case EQ:
                    if (value != null) wrapper.eq(column, value);
                    break;
                case NE:
                    if (value != null) wrapper.ne(column, value);
                    break;
                case GT:
                    if (value != null) wrapper.gt(column, value);
                    break;
                case GTE:
                    if (value != null) wrapper.ge(column, value);
                    break;
                case LT:
                    if (value != null) wrapper.lt(column, value);
                    break;
                case LTE:
                    if (value != null) wrapper.le(column, value);
                    break;
                case LIKE:
                    if (value != null) wrapper.like(column, value);
                    break;
                case LIKE_LEFT:
                    if (value != null) wrapper.likeLeft(column, value);
                    break;
                case LIKE_RIGHT:
                    if (value != null) wrapper.likeRight(column, value);
                    break;
                case IN:
                    if (value != null) wrapper.in(column, toCollection(value));
                    break;
                case NOT_IN:
                    if (value != null) wrapper.notIn(column, toCollection(value));
                    break;
                case BETWEEN:
                    if (value != null) {
                        wrapper.between(column, rangeStart(value), rangeEnd(value));
                    }
                    break;
                case IS_NULL:
                    wrapper.isNull(column);
                    break;
                case IS_NOT_NULL:
                    wrapper.isNotNull(column);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 追加排序；orderBy 为空则忽略，asc 决定升序/降序
     */
    private static <T> void applyOrderBy(QueryWrapper<T> wrapper, Map<String, String> propToColumn, PageQuery pageQuery) {
        if (pageQuery.getOrderBy() == null || pageQuery.getOrderBy().isBlank()) {
            return;
        }
        String column = resolveColumn(pageQuery.getOrderBy(), propToColumn);
        if (pageQuery.isAsc()) {
            wrapper.orderByAsc(column);
        } else {
            wrapper.orderByDesc(column);
        }
    }

    /**
     * 构建实体属性名到数据库列名的映射（依赖 MyBatis-Plus 已初始化该实体的 TableInfo）
     */
    private static Map<String, String> propertyToColumnMap(Class<?> entityClass) {
        Map<String, String> map = new HashMap<>();
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entityClass);
        if (tableInfo != null && tableInfo.getFieldList() != null) {
            for (TableFieldInfo field : tableInfo.getFieldList()) {
                map.put(field.getProperty(), field.getColumn());
            }
        }
        return map;
    }

    /**
     * 属性名转列名；无映射时原样返回
     */
    private static String resolveColumn(String field, Map<String, String> propToColumn) {
        String column = propToColumn.get(field);
        return column != null ? column : field;
    }

    /**
     * IN/NOT_IN 的值兼容单值，统一转为 Collection
     */
    private static Collection<?> toCollection(Object value) {
        if (value instanceof Collection<?> collection) {
            return collection;
        }
        List<Object> single = new ArrayList<>();
        single.add(value);
        return single;
    }

    /**
     * BETWEEN 起点：Object[]/Collection 取第 1 个元素，否则视为单值
     */
    private static Object rangeStart(Object value) {
        if (value instanceof Object[] arr) {
            return arr.length >= 1 ? arr[0] : null;
        }
        if (value instanceof Collection<?> col) {
            List<Object> list = new ArrayList<>(col);
            return list.size() >= 1 ? list.get(0) : null;
        }
        return value;
    }

    /**
     * BETWEEN 终点：Object[]/Collection 取第 2 个元素，否则为 null
     */
    private static Object rangeEnd(Object value) {
        if (value instanceof Object[] arr) {
            return arr.length >= 2 ? arr[1] : null;
        }
        if (value instanceof Collection<?> col) {
            List<Object> list = new ArrayList<>(col);
            return list.size() >= 2 ? list.get(1) : null;
        }
        return null;
    }
}