package com.mongohua.etl.service.impl;

import com.mongohua.etl.mapper.UserMapper;
import com.mongohua.etl.model.User;
import com.mongohua.etl.service.UserService;
import com.mongohua.etl.utils.PageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户服务类实现
 * @author xiaohf
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUsername(String username) {
        try {
            User u = userMapper.findByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userMapper.findByUsername(username);
    }

    @Override
    public PageModel<User> getUsers(int page, int rows) {
        if (page < 0) {
            page = 1;
        }

        PageModel<User> jobPageModel = new PageModel<User>();
        jobPageModel.setPageNo(page);
        jobPageModel.setPageSize(rows);

        int count = userMapper.getCount();
        jobPageModel.setTotal(count);
        int pageIndex = (page - 1) * rows;
        jobPageModel.setRows(userMapper.getListForPage(pageIndex,rows));
        int totalPage = (int)Math.ceil(count*1.0/rows);
        jobPageModel.setTotalPage(totalPage);
        return jobPageModel;
    }

    @Override
    public int update(User user) {
        return userMapper.update(user);
    }

    @Override
    public int add(User user) {
        return userMapper.add(user);
    }

    @Override
    public int chagePwd(User user) {
        return userMapper.changePwd(user);
    }
}
