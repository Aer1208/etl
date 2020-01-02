package com.mongohua.etl.mapper;

import com.mongohua.etl.model.Host;

import java.util.List;

/**
 * 服务器Mybatis接口
 * @author xiaohf
 */
public interface HostMapper {


    /**
     * 获取全部服务器列表
     * @return
     */
    public List<Host> getList();
}
