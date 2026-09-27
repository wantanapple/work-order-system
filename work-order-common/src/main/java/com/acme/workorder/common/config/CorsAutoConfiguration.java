package com.acme.workorder.common.config;

import com.acme.workorder.common.config.web.CorsConfig;
import com.acme.workorder.common.config.web.CorsProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties({CorsProperties.class})
public class CorsAutoConfiguration {

    /**
     * 全局 CORS 配置
     */
    @Bean
    public CorsConfig corsConfig(CorsProperties corsProperties) {
        return new CorsConfig(corsProperties);
    }
}
