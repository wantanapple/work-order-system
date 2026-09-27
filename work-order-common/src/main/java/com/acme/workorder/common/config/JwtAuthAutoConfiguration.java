package com.acme.workorder.common.config;

import com.acme.workorder.common.config.jwt.JwtProperties;
import com.acme.workorder.common.util.JwtUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * JWT 自动装配：注册 JwtUtil Bean 并启用 JwtProperties 配置绑定。
 * 通过 AutoConfiguration.imports 声明，依赖 common 的服务启动时自动生效。
 */
@AutoConfiguration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtAuthAutoConfiguration {

    /**
     * 注册 JWT 工具类，业务代码直接注入使用。
     */
    @Bean
    public JwtUtil jwtUtil(JwtProperties jwtProperties) {
        return new JwtUtil(jwtProperties);
    }
}
