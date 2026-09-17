package com.acme.workorder.common.constant;

/**
 * 通用常量。业务领域常量（如工单状态/优先级）后续在工单模块定义。
 */
public final class CommonConstants {

    private CommonConstants() {
    }

    /**
     * 默认分页大小
     */
    public static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 最大分页大小
     */
    public static final int MAX_PAGE_SIZE = 200;

    /**
     * 默认字符集
     */
    public static final String CHARSET_UTF8 = "UTF-8";
}
