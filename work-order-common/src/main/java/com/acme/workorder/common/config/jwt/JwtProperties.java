package com.acme.workorder.common.config.jwt;
import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Data;

/**
 * JWT 配置项，前缀 workorder.auth.jwt，建议放在 Nacos 共享配置中统一管理，
 * 各服务共用同一份密钥，保证 token 在所有服务间可互相校验。
 */
@Data
@ConfigurationProperties(prefix = "workorder.auth.jwt")
public class JwtProperties {

    /**
     * JWT 签名密钥，至少 32 位
     */
    private String secret;

    /**
     * 过期时间，单位秒，默认 2 小时
     */
    private long expireSeconds = 7200;

    /**
     * 签发者
     */
    private String issuer = "work-order-system";
}
