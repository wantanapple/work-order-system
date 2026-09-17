package com.acme.workorder.common.snowflake;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 雪花算法参数，前缀 workorder.snowflake。
 * 微服务部署时建议每个服务在 Nacos/各自 application.yml 配不同的 worker-id。
 */
@Data
@ConfigurationProperties(prefix = "workorder.snowflake")
public class SnowflakeProperties {

    /**
     * 是否启用（自动装配 IdGenerator Bean），默认 true
     */
    private boolean enabled = true;

    /**
     * 起始纪元（毫秒），默认 2024-01-01 00:00:00 UTC
     */
    private long epoch = 1704067200000L;

    /**
     * 机器编号（0-31）
     */
    private long workerId = 0;

    /**
     * 数据中心编号（0-31）
     */
    private long datacenterId = 0;
}
