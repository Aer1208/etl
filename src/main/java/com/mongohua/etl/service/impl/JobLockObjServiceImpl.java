package com.mongohua.etl.service.impl;

import com.mongohua.etl.mapper.JobLockObjMapper;
import com.mongohua.etl.model.JobLockObj;
import com.mongohua.etl.service.JobLockObjService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 作业锁服务层实现类
 * @author xiaohf
 */
@Service
public class JobLockObjServiceImpl implements JobLockObjService {

    @Autowired
    private JobLockObjMapper jobLockObjMapper;
    @Override
    public List<JobLockObj> getLockObjsByJobId(int jobId) {
        return jobLockObjMapper.getLockObjsByJobId(jobId);
    }

    @Override
    public List<JobLockObj> getJobByLockObj() {
        return jobLockObjMapper.getJobByLockObj();
    }

    @Override
    public List<JobLockObj> getAllJobLockObjs() {
        return jobLockObjMapper.getAllJobLockObjs();
    }

    @Override
    public int add(JobLockObj jobLockObj) {
        return jobLockObjMapper.add(jobLockObj);
    }

    @Override
    public int delete(JobLockObj jobLockObj) {
        return jobLockObjMapper.delete(jobLockObj);
    }

    @Override
    public List<JobLockObj> getJobLockObjByName(JobLockObj jobLockObj) {
        return jobLockObjMapper.getJobLockObjByLockObj(jobLockObj);
    }
}
