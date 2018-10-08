package com.mongohua.etl.service;

import com.mongohua.etl.model.JobQueue;

import java.util.List;

/**
 * 队列服务接口类
 * @author xiaohf
 */
public interface QueueService {

    /**
     * 读取所有的队列
     * @return
     */
    public List<JobQueue> getList();

    /**
     * 新增一个队列
     * @param jobQueue
     * @return
     */
    public int add(JobQueue jobQueue);

    /**
     * 更新一个队列
     * @param jobQueue
     * @return
     */
    public int update(JobQueue jobQueue);

    /**
     * 删除一组队列
     * @param queueIds
     * @return
     */
    public int delete(String[] queueIds);
}
