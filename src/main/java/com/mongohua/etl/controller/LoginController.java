package com.mongohua.etl.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 登录Controller
 * @author xiaohf
 */
@Controller
public class LoginController {

    @RequestMapping(value = "/login")
    public String login() {
        return "login";
    }

    /**
     * 验证用户名和密码
     * @param username
     * @Param password
     * @return
     */
    @RequestMapping(value="/checkLogin",produces = "application/json;charset=utf-8")
    @ResponseBody
    public String checkLogin(String username,String password) {
        JSONObject result = new JSONObject();
        try{
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            Subject currentUser = SecurityUtils.getSubject();
            if (!currentUser.isAuthenticated()){
                //使用shiro来验证
                token.setRememberMe(true);
                //验证角色和权限
                currentUser.login(token);
            }
            result.put("ret", true);
        }catch(Exception ex){
            result.put("msg", ex.getCause().getMessage());
            result.put("ret", false);
        }

        return result.toString();
    }
}
