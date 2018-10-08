package com.mongohua.etl.mapper;

import com.mongohua.etl.model.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色Mybatis层
 * @author xiaohf
 */
public interface RoleMapper {

    /**
     * 获取所有的角色
     * @return
     */
    public List<Role> getRoles();

    /**
     * 根据用户ID获取角色信息
     * @param userId
     * @return
     */
    public List<Role> getRolesByUserId(@Param("userId") int userId);

    /**
     * 新增一个角色
     * @param role
     * @return
     */
    public int add(Role role);

    /**
     * 给角色ID赋权
     * @param roleId 角色ID
     * @param permIds 权限ID列表
     * @return
     */
    public int grantPerms(@Param("roleId") int roleId, @Param("permIds") String[] permIds);

    /**
     * 回收角色的所有权限
     * @param roleId
     * @return
     */
    public int deletePerms(@Param("roleId") int roleId);

    /**
     * 更新一个角色
     * @param role
     * @return
     */
    public int update(Role role);
}
