package com.mongohua.etl.controller;

import com.alibaba.fastjson.JSONObject;
import com.mongohua.etl.model.Menu;
import com.mongohua.etl.model.User;
import com.mongohua.etl.service.MenuService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 菜单控制视图
 * @author xiaohf
 */
@Controller
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 获取用户的菜单
     * @return
     */
    @RequestMapping(value = "/system/get_menus")
    @ResponseBody
    public Object getMenus() {
        Subject sub = SecurityUtils.getSubject();
        User user = (User)sub.getPrincipal();
        if (user== null) {
            return null;
        }
        return menuService.getMenus(user.getUserId());
    }

    /**
     * 获取所有权限
     * @param roleId
     * @return
     */
    @RequestMapping(value = "/system/get_all_menus",produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object getMenusByUserId(int roleId) {
        return menuService.getAllMenusByUserId(roleId,0);
    }

    /**
     * 菜单管理首页
     * @return
     */
    @RequiresPermissions("system:menu_index")
    @RequestMapping(value = "/system/menu_index")
    public String menuIndex() {
        return "system/menu_index";
    }

    @RequestMapping(value = "/system/menu_list")
    @ResponseBody
    public Object menuList() {
        return menuService.getAllMenu(0);
    }

    /**
     * 获取所有最顶级菜单列表
     * @return
     */
    @RequestMapping(value = "/system/get_root_menus",produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object getRootMenus() {
        return menuService.getMenusByPid(0);
    }

    @RequestMapping(value = "/system/menu_add",produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object addOrUpdate(Menu menu) {
        JSONObject jsonObject = new JSONObject();
        if (menu.getMenuId() ==0) {
            menuService.add(menu);
            jsonObject.put("type","add");
        } else if (menu.getMenuId() > 0) {
            menuService.update(menu);
            jsonObject.put("type","update");
        }
        return jsonObject;
    }
}
