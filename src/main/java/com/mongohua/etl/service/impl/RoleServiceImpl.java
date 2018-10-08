package com.mongohua.etl.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mongohua.etl.mapper.RoleMapper;
import com.mongohua.etl.model.Role;
import com.mongohua.etl.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色服务层实现类
 * @author xiaohf
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> getRoles() {
        return roleMapper.getRoles();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object addRole(Role role, String permIds) {

        JSONObject jsonObject = new JSONObject();

        if (role.getRoleId() == 0 ) {
            roleMapper.add(role);
            jsonObject.put("type","add");
        }else {
            roleMapper.update(role);
            jsonObject.put("type","update");
        }
        // 回收权限
        jsonObject.put("deletePerms",roleMapper.deletePerms(role.getRoleId()));

        // 重新赋权
        if ( permIds != null && !"".equalsIgnoreCase(permIds.trim())) {
            jsonObject.put("grantPerms",roleMapper.grantPerms(role.getRoleId(), permIds.split(",")));
        }
        return jsonObject;
    }

    @Override
    public List<Role> getRolesByUserId(int userId) {
        return roleMapper.getRolesByUserId(userId);
    }
}
