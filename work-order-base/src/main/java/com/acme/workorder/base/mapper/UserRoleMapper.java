package com.acme.workorder.base.mapper;

import com.acme.workorder.base.model.UserRolePO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户-角色关联表 Mapper。
 * 该表为联合主键（user_id + role_id），不适合直接使用 MyBatis-Plus 的
 * save/updateById 等单主键通用方法，写库统一走自定义 SQL。
 */
public interface UserRoleMapper extends BaseMapper<UserRolePO> {

    /**
     * 绑定用户角色；若该用户已绑定过该角色，则只刷新 updated_by / updated_time。
     * 时间字段在 SQL 中用 NOW() 手动填充，不依赖 MyBatis-Plus 自动填充。
     */
    @Insert("""
            INSERT INTO user_role (
            user_id,
            role_id,
            created_by,
            updated_by,
            created_time,
            updated_time
            )
            VALUES (
            #{userId},
            #{roleId},
            #{operator},
            #{operator},
            NOW(),
            NOW()
            )
            ON DUPLICATE KEY UPDATE
            updated_by = #{operator},
            updated_time = NOW()
            """)
    int bindUserRole(@Param("userId") Long userId,
                     @Param("roleId") Long roleId,
                     @Param("operator") String operator);

    /**
     * 查询用户指定状态角色的权限点字段列表。
     * 一个用户可能有多个角色，返回结果可能是多条 "a:b,c:d" 形式的字符串，
     * 由 Service 层负责拆分、去空格、去重后聚合为权限点集合。
     */
    @Select("""
            SELECT 
            r.permissions 
            FROM 
            role r 
            JOIN user_role ur ON r.id = ur.role_id 
            WHERE 
            ur.user_id = #{userId} 
            AND r.status = #{status}
            """)
    List<String> selectPermissionByUserIdAndStatus(@Param("userId") Long userId,
                                                   @Param("status") Integer status);
}
