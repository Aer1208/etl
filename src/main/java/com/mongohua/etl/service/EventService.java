package com.mongohua.etl.service;

import com.mongohua.etl.model.JobEvent;

import java.util.List;

/**
 * 队列服务接口类
 * @author xiaohf
 */
public interface EventService {

    /**
     * 读取所有的事件
     * @return
     */
    public List<JobEvent> getList();

    /**
     * 新增一个事件
     * @param jobEvent
     * @return
     */
    public int add(JobEvent jobEvent);

    /**
     * 更新一个事件
     * @param jobEvent
     * @return
     */
    public int update(JobEvent jobEvent);

    /**
     * 删除一组事件
     * @param eventIds
     * @return
     */
    public int delete(String[] eventIds);
}
