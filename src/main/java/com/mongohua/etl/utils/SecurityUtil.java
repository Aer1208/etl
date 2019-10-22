package com.mongohua.etl.utils;

import com.mongohua.etl.model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * 安全相关的类
 */
public class SecurityUtil {

    /**
     * 获取用户登录id
     * @return
     */
    public static Integer getLoginUserId() {
        try {
            Subject subject = SecurityUtils.getSubject();
            User user = (User) subject.getPrincipal();
            if (user != null) {
                return user.getUserId();
            }
            return null;
        }catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 获取用户登录id，管理员角色用户id，统一赋值为0
     * @return
     */
    public static Integer getCurrentUserId() {
        try {
            Subject subject = SecurityUtils.getSubject();
            if (subject.hasRole("ADMIN")) {
                return 0;
            }
            return getLoginUserId();
        }catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }
}
