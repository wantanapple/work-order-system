package com.acme.workorder.common.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * MyBatis-Plus 公共字段自动填充器。
 * <p>
 * 该类本身不声明 {@code @Component}，而是由 {@code CommonAutoConfiguration#commonMetaObjectHandler}
 * 以 {@code @Bean} 方式注册；再通过 common 模块
 * {@code META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports}
 * 里的 {@code com.acme.workorder.common.config.CommonAutoConfiguration} 实现自动装配。
 * 所以只要业务模块依赖了 common（不需要再写 {@code @Component} 或 {@code @MapperScan}），
 * 实体上声明了 {@code fill = FieldFill.INSERT / INSERT_UPDATE} 的字段就会在
 * 插入 / 更新时自动填充。
 */
public class CommonMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入时自动填充：
     * 仅当字段当前为 {@code null} 时才填充（{@code strictInsertFill} 语义），
     * 避免覆盖业务侧显式设置的值。当前支持 {@code createdTime} 与 {@code updatedTime}，
     * 字段名不是这两个的实体可继续在此扩展。
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Date now = new Date();
        Object createdTime = metaObject.getValue("createdTime");
        Object updatedTime = metaObject.getValue("updatedTime");

        if (createdTime == null) {
            this.strictInsertFill(metaObject, "createdTime", Date.class, now);
        }
        if (updatedTime == null) {
            this.strictInsertFill(metaObject, "updatedTime", Date.class, now);
        }
    }

    /**
     * 更新时自动填充：仅当 {@code updatedTime} 当前为 {@code null} 时才刷新，
     * 避免覆盖业务侧显式设置的值。
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        Object updatedTime = metaObject.getValue("updatedTime");
        if (updatedTime == null) {
            this.strictUpdateFill(metaObject, "updatedTime", Date.class, new Date());
        }
    }
}
