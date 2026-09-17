package com.acme.workorder.common.config;

import com.acme.workorder.common.exception.GlobalExceptionHandler;
import com.acme.workorder.common.mybatis.CommonMetaObjectHandler;
import com.acme.workorder.common.snowflake.IdGenerator;
import com.acme.workorder.common.snowflake.SnowflakeIdGenerator;
import com.acme.workorder.common.snowflake.SnowflakeProperties;
import com.acme.workorder.common.web.CorsConfig;
import com.acme.workorder.common.web.CorsProperties;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 公共模块自动装配：引入 common 即自动注册
 * 「全局异常处理器」「全局 CORS 配置」「雪花 ID 生成器」「MyBatis-Plus 公共插件（分页拦截器等）」。
 */
@AutoConfiguration
@EnableConfigurationProperties({CorsProperties.class, SnowflakeProperties.class})
public class CommonAutoConfiguration {

    /**
     * 全局异常处理器
     */
    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    /**
     * 全局 CORS 配置
     */
    @Bean
    public CorsConfig corsConfig(CorsProperties corsProperties) {
        return new CorsConfig(corsProperties);
    }

    /**
     * 雪花 ID 生成器（workorder.snowflake.enabled=false 时不注册）
     */
    @Bean
    @ConditionalOnProperty(prefix = "workorder.snowflake", name = "enabled", havingValue = "true", matchIfMissing = true)
    public IdGenerator idGenerator(SnowflakeProperties snowflakeProperties) {
        return new SnowflakeIdGenerator(
                snowflakeProperties.getEpoch(),
                snowflakeProperties.getWorkerId(),
                snowflakeProperties.getDatacenterId());
    }

    /**
     * MyBatis-Plus 公共字段填充器。
     * 所有依赖 common 的服务都会自动注册，业务实体只需声明 fill 策略即可。
     */
    @Bean
    public MetaObjectHandler commonMetaObjectHandler() {
        return new CommonMetaObjectHandler();
    }

    /**
     * MyBatis-Plus 公共拦截器：注册分页拦截器（PaginationInnerInterceptor），
     * 使 this.page() / selectPage() 真正执行 count + LIMIT，填充 total/pages。
     * 同时注册 BlockAttackInnerInterceptor 防止全表 UPDATE/DELETE 误操作。
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
        // 设置最大单页限制，-1 表示不限制（如需限制可改为 500 等）
        paginationInterceptor.setMaxLimit(-1L);
        interceptor.addInnerInterceptor(paginationInterceptor);

        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return interceptor;
    }
}
