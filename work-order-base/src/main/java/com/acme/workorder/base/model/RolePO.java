package com.acme.workorder.base.model;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@TableName("role")
public class RolePO {

    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "角色代号")
    @TableField("role_code")
    private String roleCode;

    @Schema(description = "角色名")
    @TableField("role_name")
    private String roleName;

    @Schema(description = "权限点，多个用英文逗号分隔")
    @TableField("permissions")
    private String permissions;

    @Schema(description = "备注")
    @TableField("remark")
    private String remark;

    @Schema(description = "状态：1启用 0禁用")
    @TableField("status")
    private Integer status;

    @Schema(description = "创建人")
    @TableField("created_by")
    private String createdBy;

    @Schema(description = "更新人")
    @TableField("updated_by")
    private String updatedBy;

    @Schema(description = "创建时间")
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private Date createdTime;

    @Schema(description = "更新时间")
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private Date updatedTime;
}
