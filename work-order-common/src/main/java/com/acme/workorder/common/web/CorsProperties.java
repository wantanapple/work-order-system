package com.acme.workorder.common.web;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;

/**
 * 全局 CORS（跨域）配置：各微服务引入 common 即生效，
 * 可用 application.yml 中 workorder.cors.* 覆盖默认值。
 * <p>
 * 示例：
 * workorder:
 * cors:
 * enabled: true
 * allowed-origins: http://localhost:5173,https://ops.acme.com
 * allowed-methods: GET,POST,PUT,DELETE,OPTIONS
 * allowed-headers: "*"
 * allow-credentials: false
 * max-age: 3600
 */
@Data
@ConfigurationProperties(prefix = "workorder.cors")
public class CorsProperties {

    /**
     * 是否启用 CORS，默认 true
     */
    private boolean enabled = true;

    /**
     * 允许的来源，默认 *（与 allowCredentials=true 互斥）
     */
    private List<String> allowedOrigins = Arrays.asList("*");

    /**
     * 允许的请求方法
     */
    private List<String> allowedMethods = Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS");

    /**
     * 允许的请求头，默认 *
     */
    private List<String> allowedHeaders = Arrays.asList("*");

    /**
     * 是否允许携带凭证（cookie/Authorization）。为 true 时 allowedOrigins 不能用 *
     */
    private boolean allowCredentials = false;

    /**
     * 预检请求(OPTIONS)结果缓存时间（秒）
     */
    private int maxAge = 3600;
}
