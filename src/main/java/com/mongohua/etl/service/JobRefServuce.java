package com.mongohua.etl.service;

import com.mongohua.etl.model.JobRef;
import com.mongohua.etl.model.JobRef2;

import java.util.List;

/**
 * 作业依赖服务类
 * @author xiaohf
 */
public interface JobRefServuce {

    /**
     * 获取所有的依赖关系对象
     * @return
     */
    public List<JobRef> getList();

    /**
     * 获取作业的所有依赖关系对象
     * @param jobId
     * @return
     */
    public List<JobRef> getById(int jobId);

    /**
     * 新增一个依赖关系
     * @param jobRef
     * @return
     */
    public int add(JobRef jobRef);

    /**
     * 批量更新作业依赖
     * @param jobRef2
     * @return
     */
    public String update(JobRef2 jobRef2);
}
