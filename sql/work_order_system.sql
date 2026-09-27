SET
FOREIGN_KEY_CHECKS = 0;
SET NAMES utf8mb4;

-- 公司表
DROP TABLE IF EXISTS `company`;
CREATE TABLE `company`
(
    `id`           bigint      NOT NULL COMMENT '主键id',
    `company_name` varchar(64) NOT NULL COMMENT '公司名称',
    `company_code` varchar(32) NOT NULL COMMENT '公司代号',
    `company_addr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '公司地址',
    `created_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT '' COMMENT '创建人',
    `updated_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT '' COMMENT '更新人',
    `created_time` datetime    NOT NULL COMMENT '创建时间',
    `updated_time` datetime    NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='公司表';

-- 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`           bigint       NOT NULL COMMENT '主键id',
    `username`     varchar(64)  NOT NULL                                        DEFAULT '' COMMENT '登录账号，唯一',
    `password`     varchar(128) NOT NULL                                        DEFAULT '' COMMENT '加密密码，BCrypt',
    `nickname`     varchar(64)  NOT NULL                                        DEFAULT '' COMMENT '展示名称',
    `status`       tinyint      NOT NULL                                        DEFAULT '1' COMMENT '状态：1启用 0禁用',
    `created_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建人',
    `updated_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新人',
    `created_time` datetime     NOT NULL COMMENT '创建时间',
    `updated_time` datetime     NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_username` (`username`) COMMENT '用户名唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

-- 角色表
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`
(
    `id`           bigint      NOT NULL COMMENT '主键id',
    `role_code`    varchar(64) NOT NULL                                         DEFAULT '' COMMENT '角色编码，如 ADMIN、PM、ENGINEER等',
    `role_name`    varchar(64) NOT NULL                                         DEFAULT '' COMMENT '角色名称，如 管理员、项目经理、工程师',
    `permissions`  varchar(1024)                                                DEFAULT '' COMMENT '权限点，多个用英文逗号分隔，如 ticket:create,ticket:assign',
    `remark`       varchar(255)                                                 DEFAULT '' COMMENT '备注',
    `status`       tinyint     NOT NULL                                         DEFAULT '1' COMMENT '状态：1启用 0禁用',
    `created_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建人',
    `updated_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新人',
    `created_time` datetime    NOT NULL COMMENT '创建时间',
    `updated_time` datetime    NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_code` (`role_code`) COMMENT '角色code唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表';

-- 用户角色关联表
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`
(
    `user_id`      bigint   NOT NULL COMMENT '用户id',
    `role_id`      bigint   NOT NULL COMMENT '角色id',
    `created_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建人',
    `updated_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新人',
    `created_time` datetime NOT NULL COMMENT '创建时间',
    `updated_time` datetime NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`user_id`, `role_id`),
    KEY            `idx_user_role_role_id` (`role_id`) COMMENT '普通查询索引role_id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户角色关联表';


SET
FOREIGN_KEY_CHECKS = 1;


