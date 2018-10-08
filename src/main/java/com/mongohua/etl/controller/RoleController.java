package com.mongohua.etl.controller;

import com.alibaba.fastjson.JSONObject;
import com.mongohua.etl.model.Role;
import com.mongohua.etl.service.RoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 角色Controller
 * @author xiaohf
 */
@Controller
@RequestMapping(value = "/system")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 获取所有的角色列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/role_list",produces = "application/json;charset=utf-8")
    public List<Role> getRoles() {
        return roleService.getRoles();
    }

    @ResponseBody
    @RequestMapping(value = "/role_list2",produces = "application/json;charset=utf-8")
    public Object getRoles2() {
        JSONObject object = new JSONObject();
        object.put("rows", roleService.getRoles());
        return object;
    }

    /**
     * 访问角色列表首页
     * @return
     */
    @RequestMapping(value = "/role_index")
    @RequiresPermissions("system:role_index")
    public String roleIndex() {
        return "system/role_index";
    }

    /**
     * 新增一个角色
     * @param role 新增角色对象
     * @param permIds  角色的权限ID列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/role_add",produces = "application/json;charset=utf-8")
    public Object addRole(Role role, String permIds){
        return roleService.addRole(role, permIds);
    }
}
