package com.acme.workorder.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 创建用户请求参数。
 * roleCodes 可为空，表示创建暂不绑定角色的用户；传入时要求角色编码必须存在。
 */
@Data
public class CreateUserDTO {

    @Schema(description = "用户名")
    @NotBlank
    private String username;

    @Schema(description = "登录密码，明文传输，后端 BCrypt 加密后存储")
    @NotBlank
    private String password;

    @Schema(description = "昵称")
    @NotBlank
    private String nickname;

    @Schema(description = "所属公司")
    @NotNull
    private Long companyId;

    @Schema(description = "状态：1启用 0禁用")
    @NotNull
    private Integer status;

    @Schema(description = "角色code集合")
    private List<String> roleCodes;
}
