package com.acme.workorder.common.config;

import com.acme.workorder.common.config.jackson.JacksonDateFormatCustomizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;


@AutoConfiguration
public class JacksonAutoConfiguration {

    /**
     * 全局 Jackson 日期格式化定制器。
     * 所有依赖 common 的服务自动生效：Date -> "yyyy-MM-dd HH:mm:ss"，
     * LocalDateTime -> "yyyy-MM-dd HH:mm:ss"，LocalDate -> "yyyy-MM-dd"。
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonDateFormatCustomizer() {
        return new JacksonDateFormatCustomizer();
    }
}
