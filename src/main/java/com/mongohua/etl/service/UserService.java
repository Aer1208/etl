package com.mongohua.etl.service;

import com.mongohua.etl.model.User;
import com.mongohua.etl.utils.PageModel;

/**
 * 系统用户服务类
 * @author xiaohf
 */
public interface UserService {

    /**
     * 根据用户名查询用户实体类
     * @param username
     * @return
     */
    public User findByUsername(String username);

    /**
     * 分页获取用户列表
     * @param page
     * @param rows
     * @return
     */
    public PageModel<User> getUsers(int page, int rows);

    /**
     * 更新用户
     * @param user
     * @return
     */
    public int update(User user);

    /**
     * 新增用户
     * @param user
     * @return
     */
    public int add(User user);

    /**
     * 修改密码
     * @param user
     * @return 返回修改状态
     */
    public int chagePwd(User user);
}
