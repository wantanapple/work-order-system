package com.acme.workorder.base.controller;

import com.acme.workorder.base.dto.CreateRoleDTO;
import com.acme.workorder.base.service.RoleService;
import com.acme.workorder.base.vo.RolePageVO;
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

/**
 * 角色管理接口：提供创建角色能力。
 * 角色编码用于标识角色，权限点用于后续登录、菜单显隐和接口鉴权。
 */
@Tag(name = "角色管理", description = "提供角色管理基础信息能力")
@RestController
@RequestMapping("/api/work_order_system/base/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    /**
     * 分页查询角色：按通用分页参数查询，并返回角色权限点列表。
     */
    @PostMapping("/page")
    public Result<PageResult<RolePageVO>> page(
            @Parameter(description = "分页查询角色请求参数", required = true)
            @Valid @RequestBody PageQuery pageQuery
    ) {
        PageResult<RolePageVO> result = roleService.page(pageQuery);
        return Result.ok(result);
    }

    /**
     * 创建角色：校验角色编码唯一、权限点格式，成功后返回新角色主键 id。
     */
    @PostMapping("/create")
    public Result<String> create(
            @Parameter(required = true, description = "创建角色dto")
            @Valid @RequestBody CreateRoleDTO createRoleDTO
    ) {
        String id = roleService.create(createRoleDTO);
        return Result.ok(id);
    }
}
