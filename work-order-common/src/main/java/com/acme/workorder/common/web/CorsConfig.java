package com.acme.workorder.common.web;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 全局 CORS 配置：由 workorder-common 自动装配注册，对该服务所有 Controller 生效。
 */
public class CorsConfig implements WebMvcConfigurer {

    /**
     * CORS 配置项
     */
    private final CorsProperties corsProperties;

    /**
     * 注入 CORS 配置项
     */
    public CorsConfig(CorsProperties corsProperties) {
        this.corsProperties = corsProperties;
    }

    /**
     * 未启用 CORS 时不注册映射；否则对所有路径注册全局跨域规则
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (!corsProperties.isEnabled()) {
            return;
        }
        registry.addMapping("/**")
                .allowedOrigins(corsProperties.getAllowedOrigins().toArray(String[]::new))
                .allowedMethods(corsProperties.getAllowedMethods().toArray(String[]::new))
                .allowedHeaders(corsProperties.getAllowedHeaders().toArray(String[]::new))
                .allowCredentials(corsProperties.isAllowCredentials())
                .maxAge(corsProperties.getMaxAge());
    }
}
