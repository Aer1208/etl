package com.mongohua.etl.config;

import com.mongohua.etl.model.Menu;
import com.mongohua.etl.model.Role;
import com.mongohua.etl.model.User;
import com.mongohua.etl.service.MenuService;
import com.mongohua.etl.service.RoleService;
import com.mongohua.etl.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * shiro认证
 * @author xiaohf
 */
public class EtlShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        User user = (User)principalCollection.getPrimaryPrincipal();
        if (user == null) {
            return null;
        }
        List<Menu> menuList = menuService.getPermissions(user.getUserId());
        for (Menu menu : menuList) {
            if (menu.getPermission() != null && !"".equalsIgnoreCase(menu.getPermission())) {
                authorizationInfo.addStringPermission(menu.getPermission());
            }
        }

        List<Role> roles =roleService.getRolesByUserId(user.getUserId());
        for (Role r : roles) {
            if(r.getRoleCode() != null && !"".equalsIgnoreCase(r.getRoleCode()) ) {
                authorizationInfo.addRole(r.getRoleCode());
            }
        }
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取用户的输入的账号.
        String username = (String)token.getPrincipal();
        //通过username从数据库中查找 User对象，如果找到，没找到.
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        User user = userService.findByUsername(username);
        if(user == null){
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user,
                user.getPassword(),
                null,
                getName()
        );
        return authenticationInfo;
    }
}
