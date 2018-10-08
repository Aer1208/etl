package com.mongohua.etl.service;

import com.mongohua.etl.model.Role;

import java.util.List;

/**
 * 角色服务层
 * @author xiaohf
 */
public interface RoleService {

    /**
     * 获取所有的角色列表
     * @return
     */
    public List<Role> getRoles();

    /**
     * 新增一个角色
     * @param role 新增角色对象
     * @param permIds  新增角色的权限
     * @return
     */
    public Object addRole(Role role, String permIds);

    /**
     * 根据用户ID获取角色信息
     * @param userId
     * @return
     */
    public List<Role> getRolesByUserId(int userId);
}
