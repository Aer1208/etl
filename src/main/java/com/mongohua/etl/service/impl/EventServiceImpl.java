package com.mongohua.etl.service.impl;

import com.mongohua.etl.mapper.EventMapper;
import com.mongohua.etl.model.JobEvent;
import com.mongohua.etl.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 事件服务类的实现
 * @author xiaohf
 */
@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventMapper eventMapper;

    @Override
    public List<JobEvent> getList() {
        return eventMapper.getList();
    }

    @Override
    public int add(JobEvent jobEvent) {
        return eventMapper.add(jobEvent);
    }

    @Override
    public int update(JobEvent jobEvent) {
        return eventMapper.update(jobEvent);
    }

    @Override
    public int delete(String[] eventIds) {
        return eventMapper.delete(eventIds);
    }
}
