package com.mongohua.etl.mapper;

import com.mongohua.etl.model.JobLockObj;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 作业锁对象
 */
public interface JobLockObjMapper {

    /**
     * 根据作业ID和锁类型获取作业的锁取对象
     * @param jobId
     * @return
     */
    public List<JobLockObj> getLockObjsByJobId(@Param("jobId") int jobId);

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
     * @return
     */
    public int add(JobLockObj jobLockObj);

    /**
     * 插入依赖作业的读锁
     * @param jobId
     * @param refJobIds
     * @return
     */
    public int insertLock(@Param("jobId") int jobId, @Param("refJobIds") int[] refJobIds);

    /**
     * 删除依赖作业的读锁
     * @param jobId
     * @param refJobIds
     * @return
     */
    public int deleteLock(@Param("jobId") int jobId, @Param("refJobIds") int[] refJobIds);

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
    public List<JobLockObj> getJobLockObjByLockObj(JobLockObj jobLockObj);
}
