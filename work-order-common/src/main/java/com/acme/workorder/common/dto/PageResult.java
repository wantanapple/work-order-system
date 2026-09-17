package com.acme.workorder.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用分页结果。
 * 配合 MyBatis-Plus：IPage page 拿到后 ->
 * PageResult.of(page.getTotal(), page.getCurrent(), page.getSize(), page.getPages(), page.getHasNext(), page.getHasPrevious(), page.getRecords())
 */
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 当前页码（long 类型，与 MyBatis-Plus Page.getCurrent() 保持一致）
     */
    private long pageNum;

    /**
     * 每页大小（long 类型，与 MyBatis-Plus Page.getSize() 保持一致）
     */
    private long pageSize;

    /**
     * 总页数（total=0 时为 0；total>0 时为 ceil(total / pageSize)）
     */
    private long totalPages;

    /**
     * 是否存在下一页（前端可用于判断"加载更多"/下一页按钮是否可用）
     */
    private boolean hasNext;

    /**
     * 是否存在上一页（前端可用于判断"上一页"按钮是否可用）
     */
    private boolean hasPrevious;

    /**
     * 当前页数据
     */
    private List<T> records;

    /**
     * 默认构造（供序列化框架使用）
     */
    public PageResult() {
    }

    /**
     * 全参构造
     */
    public PageResult(long total, long pageNum, long pageSize, long totalPages, boolean hasNext, boolean hasPrevious, List<T> records) {
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
        this.records = records;
    }

    /**
     * 构建分页结果
     */
    public static <T> PageResult<T> of(long total, long pageNum, long pageSize, long totalPages, boolean hasNext, boolean hasPrevious, List<T> records) {
        return new PageResult<>(total, pageNum, pageSize, totalPages, hasNext, hasPrevious, records);
    }

    /**
     * 构建空结果（total 为 0，records 为空列表）
     */
    public static <T> PageResult<T> empty(long pageNum, long pageSize) {
        return new PageResult<>(0, pageNum, pageSize, 0, false, false, new ArrayList<>());
    }
}
