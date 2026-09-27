package com.acme.workorder.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 创建角色请求参数。
 * permissions 使用英文逗号分隔，例如 "ticket:view,ticket:assign"。
 */
@Data
public class CreateRoleDTO {

    @Schema(description = "角色代号")
    @NotBlank
    private String roleCode;

    @Schema(description = "角色名称")
    @NotBlank
    private String roleName;

    @Schema(description = "权限点，多个用英文逗号分隔")
    private String permissions;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "状态：1启用 0禁用")
    @NotNull
    private Integer status;
}
