package com.acme.workorder.common.config;

import com.acme.workorder.common.exception.GlobalExceptionHandler;
import com.acme.workorder.common.snowflake.IdGenerator;
import com.acme.workorder.common.snowflake.SnowflakeIdGenerator;
import com.acme.workorder.common.snowflake.SnowflakeProperties;
import com.acme.workorder.common.web.CorsConfig;
import com.acme.workorder.common.web.CorsProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 公共模块自动装配：引入 common 即自动注册
 * 「全局异常处理器」「全局 CORS 配置」「雪花 ID 生成器」。
 */
@AutoConfiguration
@EnableConfigurationProperties({CorsProperties.class, SnowflakeProperties.class})
public class CommonAutoConfiguration {

    /** 全局异常处理器 */
    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    /** 全局 CORS 配置 */
    @Bean
    public CorsConfig corsConfig(CorsProperties corsProperties) {
        return new CorsConfig(corsProperties);
    }

    /** 雪花 ID 生成器（workorder.snowflake.enabled=false 时不注册） */
    @Bean
    @ConditionalOnProperty(prefix = "workorder.snowflake", name = "enabled", havingValue = "true", matchIfMissing = true)
    public IdGenerator idGenerator(SnowflakeProperties snowflakeProperties) {
        return new SnowflakeIdGenerator(
                snowflakeProperties.getEpoch(),
                snowflakeProperties.getWorkerId(),
                snowflakeProperties.getDatacenterId());
    }
}
