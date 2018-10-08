package com.mongohua.etl.mapper;

import com.mongohua.etl.model.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统菜单Mybatis接口
 * @author xiaohf
 */
public interface MenuMapper {
    /**
     * 根据用户ID获取用户父菜单下的子菜单列表
     * @param userId
     * @param parentId
     * @return
     */
    public List<Menu> getMenus(@Param("userId") int userId, @Param("parentId") int parentId);

    /**
     * 获取所有用户的权限
     * @param userId
     * @return
     */
    public List<Menu> getPerimission(@Param("userId") int userId);

    /**
     * 获取父菜单下所有的子菜单，并标明该用户是否有菜单的权限
     * @param roleId
     * @param parentId
     * @return
     */
    public List<Menu> getMenusByPid(@Param("roleId") int roleId, @Param("parentId") int parentId);

    /**
     * 获取父菜单下所有的子菜单
     * @param parentId
     * @return
     */
    public List<Menu> getMenusByPid2( @Param("parentId") int parentId);

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
