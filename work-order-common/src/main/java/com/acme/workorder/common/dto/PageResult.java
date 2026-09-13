package com.acme.workorder.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用分页结果。
 * 配合 MyBatis-Plus：IPage page 拿到后 ->
 * PageResult.of(page.getTotal(), (int) page.getCurrent(), (int) page.getSize(), page.getRecords())
 */
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 总记录数 */
    private long total;

    /** 当前页码 */
    private int pageNum;

    /** 每页大小 */
    private int pageSize;

    /** 当前页数据 */
    private List<T> records;

    /** 默认构造（供序列化框架使用） */
    public PageResult() {
    }

    /** 全参构造 */
    public PageResult(long total, int pageNum, int pageSize, List<T> records) {
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.records = records;
    }

    /** 构建分页结果 */
    public static <T> PageResult<T> of(long total, int pageNum, int pageSize, List<T> records) {
        return new PageResult<>(total, pageNum, pageSize, records);
    }

    /** 构建空结果（total 为 0，records 为空列表） */
    public static <T> PageResult<T> empty(int pageNum, int pageSize) {
        return new PageResult<>(0, pageNum, pageSize, new ArrayList<>());
    }
}
