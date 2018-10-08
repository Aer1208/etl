package com.mongohua.etl.service.impl;

import com.mongohua.etl.mapper.QueueMapper;
import com.mongohua.etl.model.JobQueue;
import com.mongohua.etl.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 队列服务类实现
 * @author xiaohf
 */
@Service
public class QueueServiceImpl implements QueueService {

    @Autowired
    private QueueMapper queueMapper;

    @Override
    public List<JobQueue> getList() {
        return queueMapper.getList();
    }

    @Override
    public int add(JobQueue jobQueue) {
        return queueMapper.add(jobQueue);
    }

    @Override
    public int update(JobQueue jobQueue) {
        return queueMapper.update(jobQueue);
    }

    @Override
    public int delete(String[] queueIds) {
        return queueMapper.delete(queueIds);
    }
}
