package com.mongohua.etl.service;

import com.mongohua.etl.model.JobLockObj;

import java.util.List;

/**
 * 作业锁服务层
 * @author xiaohf
 */
public interface JobLockObjService {
    /**
     * 根据作业ID和锁类型获取作业的锁取对象
     * @param jobId
     * @return
     */
    public List<JobLockObj> getLockObjsByJobId(int jobId);

    /**
     * 获取作业的写锁作业信息
     * @return
     */
    public List<JobLockObj> getJobByLockObj();

    /**
     * 获取所有的锁对象
     * @return
     */
    public List<JobLockObj> getAllJobLockObjs();

    /**
     * 新增一个作业锁
     * @param jobLockObj
     * @return
     */
    public int add(JobLockObj jobLockObj);

    /**
     * 删除一个作业锁
     * @param jobLockObj
     * @return
     */
    public int delete(JobLockObj jobLockObj);

    /**
     * 根据表名检查表是否存在t_job_lock_obj
     * @param jobLockObj
     * @return
     */
    public List<JobLockObj> getJobLockObjByName(JobLockObj jobLockObj);

}
