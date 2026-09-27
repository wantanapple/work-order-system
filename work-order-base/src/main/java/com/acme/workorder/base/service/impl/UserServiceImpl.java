package com.acme.workorder.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.acme.workorder.base.dto.CreateUserDTO;
import com.acme.workorder.base.dto.LoginDTO;
import com.acme.workorder.base.enums.StatusEnum;
import com.acme.workorder.base.mapper.CompanyMapper;
import com.acme.workorder.base.mapper.RoleMapper;
import com.acme.workorder.base.mapper.UserMapper;
import com.acme.workorder.base.mapper.UserRoleMapper;
import com.acme.workorder.base.model.CompanyPO;
import com.acme.workorder.base.model.RolePO;
import com.acme.workorder.base.model.UserPO;
import com.acme.workorder.base.service.UserService;
import com.acme.workorder.base.vo.LoginVO;
import com.acme.workorder.base.vo.UserPageVO;
import com.acme.workorder.common.dto.PageQuery;
import com.acme.workorder.common.dto.PageResult;
import com.acme.workorder.common.enums.ResultCode;
import com.acme.workorder.common.util.AssertUtil;
import com.acme.workorder.common.util.JwtUtil;
import com.acme.workorder.common.util.QueryWrapperUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPO> implements UserService {

    @Resource
    private CompanyMapper companyMapper;
    @Resource
    private BCryptPasswordEncoder passwordEncoder;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private JwtUtil jwtUtil;

    @Override
    /**
     * 分页查询用户。
     * <p>按通用分页参数执行用户分页查询，并在分页结果基础上补充当前用户所属公司详情。</p>
     *
     * @param pageQuery 通用分页查询参数，支持动态查询条件、排序字段和分页参数归一化
     * @return 用户分页结果，记录中会尽量回填公司详情；若用户未关联公司或公司不存在，则公司详情为空
     */
    public PageResult<UserPageVO> page(PageQuery pageQuery) {
        AssertUtil.notNull(pageQuery, ResultCode.BAD_REQUEST, "请求参数不能为空");
        pageQuery.normalize();
        QueryWrapper<UserPO> wrapper = QueryWrapperUtil.buildWrapper(UserPO.class, pageQuery);
        Page<UserPO> page = Page.of(pageQuery.getPageNum(), pageQuery.getPageSize());
        Page<UserPO> userPOPage = this.page(page, wrapper);
        List<UserPageVO> userPageVOS = BeanUtil.copyToList(userPOPage.getRecords(), UserPageVO.class);
        PageResult<UserPageVO> result = PageResult.of(
                userPOPage.getTotal(),
                userPOPage.getCurrent(),
                userPOPage.getSize(),
                userPOPage.getPages(),
                userPOPage.hasNext(),
                userPOPage.hasPrevious(),
                userPageVOS
        );
        if (CollectionUtil.isEmpty(userPageVOS)) {
            return result;
        }
        Set<Long> companyIdSet = userPOPage.getRecords().stream().map(UserPO::getCompanyId).collect(Collectors.toSet());
        List<CompanyPO> companyPOS = companyMapper.selectBatchIds(companyIdSet);
        Map<Long, CompanyPO> companyIdMap = companyPOS.stream().collect(Collectors.toMap(CompanyPO::getId, companyPO -> companyPO));
        Map<Long, UserPO> userPOMap = userPOPage.getRecords().stream().collect(Collectors.toMap(UserPO::getId, userPO -> userPO));
        userPageVOS.forEach(userPageVO -> {
            UserPO userPO = userPOMap.get(userPageVO.getId());
            if (userPO == null) {
                return;
            }
            CompanyPO companyPO = companyIdMap.get(userPO.getCompanyId());
            if (companyPO == null) {
                return;
            }
            userPageVO.setCompanyDetail(BeanUtil.copyProperties(companyPO, UserPageVO.CompanyDetail.class));
        });
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String create(CreateUserDTO createUserDTO) {
        checkCreateUserDTO(createUserDTO);
        //使用BCryptPasswordEncoder加密密码
        String encode = passwordEncoder.encode(createUserDTO.getPassword());
        UserPO userPO = BeanUtil.copyProperties(createUserDTO, UserPO.class, "roleCodes");
        userPO.setPassword(encode);
        AssertUtil.isTrue(this.save(userPO), ResultCode.BIZ_ERROR, "用户创建失败");
        //绑定用户角色关联关系
        bindUserRole(createUserDTO, userPO);
        return String.valueOf(userPO.getId());
    }

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        UserPO userPO = checkLoginDTO(loginDTO);
        String token = jwtUtil.createToken(userPO.getId(), userPO.getUsername());
        return buildLoginVO(token, userPO);
    }

    private LoginVO buildLoginVO(String token, UserPO userPO) {
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setUserId(userPO.getId());
        loginVO.setUsername(userPO.getUsername());
        loginVO.setNickname(userPO.getNickname());
        loginVO.setCompanyId(userPO.getCompanyId());
        List<String> permissions = userRoleMapper.selectPermissionByUserIdAndStatus(userPO.getId(), StatusEnum.ACTIVE.getCode());
        List<String> permissionList = CollectionUtil.isEmpty(permissions)
                ? Collections.emptyList()
                : permissions.stream()
                .flatMap(permission -> Arrays.stream(permission.split(",")))
                .map(String::trim)
                .filter(StrUtil::isNotBlank)
                .distinct()
                .toList();
        loginVO.setPermissions(permissionList);
        return loginVO;
    }

    private UserPO checkLoginDTO(LoginDTO loginDTO) {
        AssertUtil.notNull(loginDTO, ResultCode.BAD_REQUEST, "参数不能为空");
        UserPO userPO = this.getOne(new LambdaQueryWrapper<UserPO>()
                .eq(UserPO::getUsername, loginDTO.getUsername()));
        AssertUtil.notNull(userPO, ResultCode.BAD_REQUEST, "账号或密码错误");
        AssertUtil.isTrue(StatusEnum.ACTIVE.getCode() == userPO.getStatus(),
                ResultCode.FORBIDDEN, "用户已被禁用");
        boolean matches = passwordEncoder.matches(loginDTO.getPassword(), userPO.getPassword());
        AssertUtil.isTrue(matches, ResultCode.BAD_REQUEST, "账号或密码错误");
        return userPO;
    }

    private void bindUserRole(CreateUserDTO createUserDTO, UserPO userPO) {
        if (CollectionUtil.isEmpty(createUserDTO.getRoleCodes())) return;
        Set<String> roleSet = new HashSet<>(createUserDTO.getRoleCodes());
        AssertUtil.isTrue(roleSet.size() == createUserDTO.getRoleCodes().size(), ResultCode.BAD_REQUEST, "角色列表包含重复元素");
        LambdaQueryWrapper<RolePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(RolePO::getRoleCode, roleSet);
        List<RolePO> rolePOS = roleMapper.selectList(wrapper);
        AssertUtil.isTrue(CollectionUtil.isNotEmpty(rolePOS) && rolePOS.size() == roleSet.size(),
                ResultCode.BIZ_ERROR, "角色列表包含不存在的元素");
        Map<String, Long> roleCodeToIdMap = rolePOS.stream().collect(Collectors.toMap(RolePO::getRoleCode, RolePO::getId));
        int saveCount = 0;
        for (String roleCode : roleSet) {
            //todo 创建人后续集成jwt时候从令牌中获取，此处先留空
            int count = userRoleMapper.bindUserRole(userPO.getId(), roleCodeToIdMap.get(roleCode), "system");
            saveCount += count;
        }
        AssertUtil.isTrue(saveCount == roleSet.size(), ResultCode.BIZ_ERROR, "角色绑定失败");
    }

    private void checkCreateUserDTO(CreateUserDTO createUserDTO) {
        AssertUtil.notNull(createUserDTO, ResultCode.BAD_REQUEST, "参数不能为空");
        StatusEnum statusEnum = StatusEnum.valueOfCode(createUserDTO.getStatus());
        AssertUtil.notNull(statusEnum, ResultCode.BAD_REQUEST, "状态码不存在");
        CompanyPO companyPO = companyMapper.selectById(createUserDTO.getCompanyId());
        AssertUtil.notNull(companyPO, ResultCode.BAD_REQUEST, "公司不存在");
        LambdaQueryWrapper<UserPO> wrapper = new LambdaQueryWrapper<UserPO>().eq(UserPO::getUsername, createUserDTO.getUsername());
        boolean exists = this.exists(wrapper);
        AssertUtil.isFalse(exists, ResultCode.BAD_REQUEST, "用户名已存在");
    }
}
