package com.acme.workorder.base.service;

import com.acme.workorder.base.dto.CreateUserDTO;
import com.acme.workorder.base.dto.LoginDTO;
import com.acme.workorder.base.vo.LoginVO;
import com.acme.workorder.base.vo.UserPageVO;
import com.acme.workorder.common.dto.PageQuery;
import com.acme.workorder.common.dto.PageResult;

import java.util.List;

public interface UserService {

    /**
     * 分页查询用户，并在分页结果中补充用户所属公司详情。
     *
     * @param pageQuery 通用分页查询参数
     * @return 用户分页结果
     */
    PageResult<UserPageVO> page(PageQuery pageQuery);

    /**
     * 创建用户并绑定角色，成功返回新用户主键 id。
     * 校验失败或数据库操作失败时抛出 BizException，由全局异常处理器统一处理。
     */
    String create(CreateUserDTO createUserDTO);

    /**
     * 用户登录：校验账号状态和密码，签发 JWT，并聚合当前用户权限点返回前端。
     */
    LoginVO login(LoginDTO loginDTO);
}
