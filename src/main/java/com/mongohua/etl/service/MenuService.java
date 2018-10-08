package com.mongohua.etl.service;

import com.mongohua.etl.model.Menu;

import java.util.List;

/**
 * 菜单服务类
 * @author xiaohf
 */
public interface MenuService {

    /**
     * 获取登录用户的菜单列表
     * @param userId
     * @return
     */
    public Object getMenus(int userId);

    /**
     * 获取登录用户的权限列表
     * @param userId
     * @return
     */
    public List<Menu> getPermissions(int userId);

    /**
     * 获取所有菜单列表
     * @param pid  父节点ID
     * @return
     */
    public Object getAllMenu(int pid);

    /**
     * 获取所有菜单，并标明该用户是否有菜单权限
     * @param roleId
     * @param parentId
     * @return
     */
    public Object getAllMenusByUserId(int roleId, int parentId);

    /**
     * 获取父菜单下的子菜单列表，不递归查询
     * @param pid
     * @return
     */
    public List<Menu> getMenusByPid(int pid);

    /**
     * 新增一个菜单
     * @param menu
     * @return
     */
    public int add(Menu menu);

    /**
     * 更新一个菜单
     * @param menu
     * @return
     */
    public int update(Menu menu);
}
