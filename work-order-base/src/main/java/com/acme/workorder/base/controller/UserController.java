package com.acme.workorder.base.controller;

import com.acme.workorder.base.dto.CreateUserDTO;
import com.acme.workorder.base.dto.LoginDTO;
import com.acme.workorder.base.service.UserService;
import com.acme.workorder.base.vo.LoginVO;
import com.acme.workorder.base.vo.UserPageVO;
import com.acme.workorder.common.dto.PageQuery;
import com.acme.workorder.common.dto.PageResult;
import com.acme.workorder.common.dto.Result;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户管理接口：提供创建用户和登录能力。
 * 登录成功后返回 token 与当前用户权限点，供前端保存并在后续请求中携带。
 */
@Tag(name = "用户管理", description = "提供用户管理基础信息能力")
@RestController
@RequestMapping("/api/work_order_system/base/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 分页查询用户：按通用分页参数查询，并补充用户所属公司详情。
     */
    @PostMapping("/page")
    public Result<PageResult<UserPageVO>> page(
            @Parameter(description = "分页查询用户请求参数", required = true)
            @Valid @RequestBody PageQuery pageQuery
    ) {
        PageResult<UserPageVO> result = userService.page(pageQuery);
        return Result.ok(result);
    }

    /**
     * 创建用户：校验用户名唯一、公司存在、角色编码存在，成功后返回新用户主键 id。
     * 密码使用 BCrypt 加密后入库；业务异常由全局异常处理器统一转成 Result。
     */
    @PostMapping("/create")
    public Result<String> create(
            @Parameter(required = true, description = "创建用户请求参数")
            @Valid @RequestBody CreateUserDTO createUserDTO
    ) {
        String id = userService.create(createUserDTO);
        return Result.ok(id);
    }

    /**
     * 用户登录：校验账号状态与密码，成功后签发 JWT，并返回用户信息、权限点列表。
     * 前端保存 token 后，在后续请求头中携带 Authorization: Bearer {token}。
     */
    @PostMapping("/login")
    public Result<LoginVO> login(
            @Parameter(required = true, description = "登录请求参数")
            @Valid @RequestBody LoginDTO loginDTO
    ) {
        LoginVO loginVO = userService.login(loginDTO);
        return Result.ok(loginVO);
    }


}
