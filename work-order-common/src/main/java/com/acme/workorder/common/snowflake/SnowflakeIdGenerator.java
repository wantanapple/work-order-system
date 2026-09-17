package com.acme.workorder.common.snowflake;

/**
 * 雪花算法 ID 生成器（线程安全）。
 * 布局：1 符号位 | 41 时间戳 | 5 数据中心 | 5 机器 | 12 序列。
 */
public class SnowflakeIdGenerator implements IdGenerator {

    private static final long WORKER_ID_BITS = 5L;
    private static final long DATACENTER_ID_BITS = 5L;
    private static final long SEQUENCE_BITS = 12L;

    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);            // 31
    private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);    // 31
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);             // 4095

    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;                              // 12
    private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;         // 17
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS; // 22

    /**
     * 起始纪元（毫秒）
     */
    private final long twepoch;

    /**
     * 机器编号（0-31）
     */
    private final long workerId;

    /**
     * 数据中心编号（0-31）
     */
    private final long datacenterId;

    /**
     * 当前毫秒内的序列号
     */
    private long sequence = 0L;

    /**
     * 最近一次生成 ID 的时间戳（毫秒）
     */
    private long lastTimestamp = -1L;

    /**
     * 构造生成器；workerId 或 datacenterId 超出 [0, 31] 时抛 IllegalArgumentException
     */
    public SnowflakeIdGenerator(long twepoch, long workerId, long datacenterId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException("workerId must be in [0," + MAX_WORKER_ID + "], got " + workerId);
        }
        if (datacenterId > MAX_DATACENTER_ID || datacenterId < 0) {
            throw new IllegalArgumentException("datacenterId must be in [0," + MAX_DATACENTER_ID + "], got " + datacenterId);
        }
        this.twepoch = twepoch;
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    /**
     * 生成下一个 ID；时钟回拨超过 5ms 时抛 IllegalStateException
     */
    @Override
    public synchronized long nextId() {
        long timestamp = System.currentTimeMillis();
        if (timestamp < lastTimestamp) {
            long diff = lastTimestamp - timestamp;
            if (diff <= 5) {
                timestamp = tilNextMillis(lastTimestamp);
            } else {
                throw new IllegalStateException("clock moved backwards, refused to generate id for " + diff + " ms");
            }
        }
        if (timestamp == lastTimestamp) {
            long newSeq = (sequence + 1) & SEQUENCE_MASK;
            if (newSeq == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
            sequence = newSeq;
        } else {
            sequence = 0L;
        }
        lastTimestamp = timestamp;
        return ((timestamp - twepoch) << TIMESTAMP_LEFT_SHIFT)
                | (datacenterId << DATACENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    /**
     * 自旋等待，直到当前毫秒大于 last
     */
    private long tilNextMillis(long last) {
        long ts = System.currentTimeMillis();
        while (ts <= last) {
            ts = System.currentTimeMillis();
        }
        return ts;
    }
}
