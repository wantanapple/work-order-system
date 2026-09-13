package com.acme.workorder.common.dto;

import com.acme.workorder.common.constant.CommonConstants;
import lombok.Data;

import java.util.List;

/**
 * 通用分页查询参数。
 * 前端可传 conditions(多个动态查询条件)；为空则做全量分页。
 */
@Data
public class PageQuery {

    /** 页码，从 1 开始 */
    private int pageNum = 1;

    /** 每页大小，默认 10，上限 MAX_PAGE_SIZE */
    private int pageSize = CommonConstants.DEFAULT_PAGE_SIZE;

    /** 动态查询条件（可空） */
    private List<QueryCondition> conditions;

    /** 可选：排序字段（实体属性名） */
    private String orderBy;

    /** 可选：是否升序，默认 false */
    private boolean asc = false;

    /** 归一化分页参数：页码或每页大小不合法时修正为默认值/上限值 */
    public void normalize() {
        if (pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize < 1) {
            pageSize = CommonConstants.DEFAULT_PAGE_SIZE;
        }
        if (pageSize > CommonConstants.MAX_PAGE_SIZE) {
            pageSize = CommonConstants.MAX_PAGE_SIZE;
        }
    }

    /** 数据库偏移量 */
    public int getOffset() {
        return (pageNum - 1) * pageSize;
    }

    /** 是否带查询条件 */
    public boolean hasCondition() {
        return conditions != null && !conditions.isEmpty();
    }
}