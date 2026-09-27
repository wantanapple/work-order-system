package com.acme.workorder.base.service;

import com.acme.workorder.base.dto.CreateRoleDTO;
import com.acme.workorder.base.vo.RolePageVO;
import com.acme.workorder.common.dto.PageQuery;
import com.acme.workorder.common.dto.PageResult;

public interface RoleService {

    /**
     * 分页查询角色，并解析角色权限点列表。
     *
     * @param pageQuery 通用分页查询参数
     * @return 角色分页结果
     */
    PageResult<RolePageVO> page(PageQuery pageQuery);

    /**
     * 创建角色，成功返回新角色主键 id。
     * 角色编码重复、权限点格式错误时抛出 BizException。
     */
    String create(CreateRoleDTO createRoleDTO);
}
