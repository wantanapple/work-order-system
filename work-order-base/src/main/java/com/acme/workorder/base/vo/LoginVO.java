package com.acme.workorder.base.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 登录成功返回体。
 * token 用于后续请求鉴权；permissions 用于前端菜单、按钮显隐，无角色时返回空数组。
 */
@Data
public class LoginVO {

    @Schema(description = "登录token")
    private String token;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "所属公司id")
    private Long companyId;

    @Schema(description = "权限点列表")
    private List<String> permissions;
}
