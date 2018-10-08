package com.mongohua.etl.mapper;

import com.mongohua.etl.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统用户Mybatis接口
 * @author xiaohf
 */
public interface UserMapper {

    /**
     * 根据用户登录名获取用户信息
     * @param username
     * @return
     */
    public User findByUsername(@Param("username") String username);

    /**
     * 分页获取用户信息
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public List<User> getListForPage(@Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize);

    /**
     * 获取用户总记录数
     * @return
     */
    public int getCount();

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
     * @return
     */
    public int changePwd(User user);
}
