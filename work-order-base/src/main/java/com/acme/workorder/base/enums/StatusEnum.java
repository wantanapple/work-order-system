package com.acme.workorder.base.enums;

/**
 * 通用启用/禁用状态枚举。
 * 与数据库 tinyint 字段对应：1 启用，0 禁用。
 */
public enum StatusEnum {

    ACTIVE(1, "启用"),
    INACTIVE(0, "禁用");

    private final int code;
    private final String description;

    StatusEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static StatusEnum valueOfCode(int code) {
        for (StatusEnum status : StatusEnum.values()) {
            if (status.code == code) {
                return status;
            }
        }
        return null;
    }
}
