package com.mongohua.etl.mapper;

import com.mongohua.etl.model.JobRefCnt;

import java.util.List;

/**
 * 作业计数依赖Mybatis接口
 * @author xiaohf
 */
public interface JobRefCntMapper {

    /**
     * 获取全部作业计数依赖列表
     * @return
     */
    public List<JobRefCnt> getList();

    /**
     * 获取一个作业的全部依赖
     * @param jobId
     * @return
     */
    public List<JobRefCnt> getById(int jobId);
    /**
     * 新增一个计数依赖
     * @param jobRefCnt
     * @return
     */
    public int add(JobRefCnt jobRefCnt);
    /**
     * 更新一个计数依赖
     * @param jobRefCnt
     * @return
     */
    public int update(JobRefCnt jobRefCnt);

    /**
     * 删除一个计数依赖
     * @param jobRefCnt
     * @return
     */
    public int delete(JobRefCnt jobRefCnt);
}
