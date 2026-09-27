package com.acme.workorder.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.acme.workorder.base.dto.CreateRoleDTO;
import com.acme.workorder.base.enums.StatusEnum;
import com.acme.workorder.base.mapper.RoleMapper;
import com.acme.workorder.base.model.RolePO;
import com.acme.workorder.base.service.RoleService;
import com.acme.workorder.base.vo.RolePageVO;
import com.acme.workorder.common.dto.PageQuery;
import com.acme.workorder.common.dto.PageResult;
import com.acme.workorder.common.enums.ResultCode;
import com.acme.workorder.common.util.AssertUtil;
import com.acme.workorder.common.util.QueryWrapperUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RolePO> implements RoleService {


    /**
     * 分页查询角色。
     * <p>按通用分页参数执行角色分页查询，并将角色权限点字符串解析为去空格、去空项、去重后的列表。</p>
     *
     * @param pageQuery 通用分页查询参数，支持动态查询条件、排序字段和分页参数归一化
     * @return 角色分页结果，permissions 为解析后的权限点列表；无权限点时保持为空
     */
    @Override
    public PageResult<RolePageVO> page(PageQuery pageQuery) {
        AssertUtil.notNull(pageQuery, ResultCode.BAD_REQUEST, "请求参数不能为空");
        pageQuery.normalize();
        QueryWrapper<RolePO> wrapper = QueryWrapperUtil.buildWrapper(RolePO.class, pageQuery);
        Page<RolePO> page = Page.of(pageQuery.getPageNum(), pageQuery.getPageSize());
        Page<RolePO> rolePOPage = this.page(page, wrapper);
        List<RolePageVO> rolePageVOS = new ArrayList<>();
        rolePOPage.getRecords().forEach(rolePO -> {
            RolePageVO rolePageVO = BeanUtil.copyProperties(rolePO, RolePageVO.class, "permissions");
            String permissions = rolePO.getPermissions();
            if (StrUtil.isNotBlank(permissions)) {
                List<String> permissionList = Arrays.stream(permissions.split(","))
                        .map(String::trim)
                        .filter(StrUtil::isNotBlank)
                        .distinct()
                        .toList();
                rolePageVO.setPermissions(permissionList);
            }
            rolePageVOS.add(rolePageVO);
        });
        PageResult<RolePageVO> result = PageResult.of(
                rolePOPage.getTotal(),
                rolePOPage.getCurrent(),
                rolePOPage.getSize(),
                rolePOPage.getPages(),
                rolePOPage.hasNext(),
                rolePOPage.hasPrevious(),
                rolePageVOS
        );
        return result;
    }

    /**
     * 创建角色，成功返回新角色主键 id。
     * 角色编码重复、权限点格式错误时抛出 BizException。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String create(CreateRoleDTO createRoleDTO) {
        checkCreateRoleDTO(createRoleDTO);
        RolePO rolePO = BeanUtil.copyProperties(createRoleDTO, RolePO.class);
        boolean save = this.save(rolePO);
        AssertUtil.isTrue(save, ResultCode.BIZ_ERROR, "添加失败");
        return String.valueOf(rolePO.getId());
    }

    private void checkCreateRoleDTO(CreateRoleDTO createRoleDTO) {
        AssertUtil.notNull(createRoleDTO, ResultCode.BAD_REQUEST, "参数不能为空");
        StatusEnum statusEnum = StatusEnum.valueOfCode(createRoleDTO.getStatus());
        AssertUtil.notNull(statusEnum, ResultCode.BAD_REQUEST, "状态码不存在");
        String permissions = createRoleDTO.getPermissions();
        if (StrUtil.isNotBlank(permissions)) {
            String[] permissionArr = permissions.split(",");
            for (String permission : permissionArr) {
                String[] strings = permission.split(":");
                AssertUtil.isTrue(strings.length == 2, ResultCode.BAD_REQUEST, "权限点格式错误");
            }
        }
        LambdaQueryWrapper<RolePO> wrapper = new LambdaQueryWrapper<RolePO>()
                .eq(RolePO :: getRoleCode, createRoleDTO.getRoleCode());
        AssertUtil.isFalse(this.exists(wrapper), ResultCode.BIZ_ERROR, "角色代号已存在");
    }
}
