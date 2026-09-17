package com.acme.workorder.common.snowflake;

/**
 * 全局唯一 ID 生成器。
 * 用于生成“非数据库主键”的唯一 id（业务编号、traceId、幂等键等），
 * 也可被 MyBatis-Plus 复用作为主键雪花算法。
 */
public interface IdGenerator {

    /**
     * 生成一个全局唯一、趋势递增的 long 型 ID
     */
    long nextId();

    /**
     * 生成字符串形式 ID
     */
    default String nextIdStr() {
        return Long.toString(nextId());
    }
}
