package com.mongohua.etl.controller;

import com.alibaba.fastjson.JSONObject;
import com.mongohua.etl.model.User;
import com.mongohua.etl.service.UserService;
import com.mongohua.etl.utils.Constant;
import com.mongohua.etl.utils.Md5Utils;
import com.mongohua.etl.utils.PageModel;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户管理实体类
 * @author xiaohf
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/system/user_index")
    @RequiresPermissions("system:user_index")
    public String userIndex() {
        return "system/user_index";
    }

    /**
     * 查询用户列表
     * @param page
     * @param rows
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/system/user_list",produces = "application/json;charset=utf-8")
    public Object queueList(int page, int rows) {
        PageModel<User> userPageModel = userService.getUsers(page, rows);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows",userPageModel.getRows());
        jsonObject.put("total",userPageModel.getTotal());
        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/system/user_add",produces = "application/json;charset=utf-8")
    public Object addOrUpdate(User user) {
        JSONObject jsonObject = new JSONObject();
        if (user.getUserId() > 0) {
            int ret = userService.update(user);
            jsonObject.put("type","update");
            jsonObject.put("ret", ret);
        }else {
            user.setPassword(Md5Utils.encode("123456",2));
            int ret = userService.add(user);
            jsonObject.put("type","add");
            jsonObject.put("ret",ret);
        }
        return  jsonObject;
    }

    @ResponseBody
    @RequestMapping(value = "/system/change_pwd",produces = "application/json;charset=utf-8")
    public Object changePwd(String newPassword, String oldPassword) {
        JSONObject jsonObject = new JSONObject();
        Subject sub = SecurityUtils.getSubject();
        User user = (User)sub.getPrincipal();
        if (user== null) {
            jsonObject.put("ret",-1);
            jsonObject.put("msg","用户回话过期，请重新登录");
        } else if (Md5Utils.encode(newPassword, Constant.MD5_CNT).equals(user.getPassword())) {
            jsonObject.put("ret",-2);
            jsonObject.put("msg","输入的原始密码不正确");
        } else if (newPassword.equals(oldPassword)) {
            jsonObject.put("ret",-3);
            jsonObject.put("msg","新老密码一致，不需要修改");
        } else {
            user.setPassword(Md5Utils.encode(newPassword,Constant.MD5_CNT));
            int ret = userService.chagePwd(user);
            jsonObject.put("ret",ret);
            jsonObject.put("msg","密码修改成功");
        }
        return jsonObject;
    }

    @ResponseBody
    @RequestMapping(value = "/system/reset_pwd",produces = "application/json;charset=utf-8")
    public Object resetPwd(int userId) {
        JSONObject jsonObject = new JSONObject();

        User user = new User();
        user.setUserId(userId);
        user.setPassword(Md5Utils.encode(userId == 1 ? "admin":"123456",2));
        int ret = userService.chagePwd(user);
        jsonObject.put("ret",ret);
        jsonObject.put("msg","重置密码成功");
        return jsonObject;
    }

}
