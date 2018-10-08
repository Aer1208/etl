package com.mongohua.etl.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongohua.etl.mapper.MenuMapper;
import com.mongohua.etl.model.Menu;
import com.mongohua.etl.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 菜单服务类实现
 * @author xiaohf
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public Object getMenus(int userId) {
        List<Menu> parentMenus = menuMapper.getMenus(userId,0);
        JSONArray jsonArray = new JSONArray();
        for (Menu menu: parentMenus) {
            JSONObject jsonObject = (JSONObject)JSON.toJSON(menu);
            jsonObject.put("menus", menuMapper.getMenus(userId, menu.getMenuId()));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    @Override
    public List<Menu> getPermissions(int userId) {
        return menuMapper.getPerimission(userId);
    }

    @Override
    public Object getAllMenu(int pid) {
        List<Menu> menus = menuMapper.getMenusByPid2(pid);
        JSONArray jsonArray = new JSONArray();
        if (menus == null || menus.size() ==0) {
            return null;
        }else {
            for (Menu menu: menus) {
                JSONObject jobDefJson = (JSONObject) JSON.toJSON(menu);
                jobDefJson.put("children", getAllMenu(menu.getMenuId()));
                jsonArray.add(jobDefJson);
            }
            return jsonArray;
        }
    }

    @Override
    public Object getAllMenusByUserId(int roleId, int parentId) {
        List<Menu> parentMenus = menuMapper.getMenusByPid(roleId,parentId);
        JSONArray jsonArray = new JSONArray();
        for (Menu menu: parentMenus) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",menu.getMenuId());
            jsonObject.put("text",menu.getMenuName());
            jsonObject.put("checked",menu.getChecked());
            jsonObject.put("iconCls",menu.getMenuIcon());
            jsonObject.put("children", getAllMenusByUserId(roleId, menu.getMenuId()));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    @Override
    public List<Menu> getMenusByPid(int pid) {
        return menuMapper.getMenusByPid2(pid);
    }

    @Override
    public int add(Menu menu) {
        return menuMapper.add(menu);
    }

    @Override
    public int update(Menu menu) {
        return menuMapper.update(menu);
    }
}
