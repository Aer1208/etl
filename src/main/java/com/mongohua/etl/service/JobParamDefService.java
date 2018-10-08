package com.mongohua.etl.service;

import com.mongohua.etl.model.JobParamDef;
import com.mongohua.etl.model.JobParamDef2;

import java.util.List;

/**
 * 作业参数定义服务类
 * @author xiaohf
 */
public interface JobParamDefService {
    /**
     * 获取所有的参数定义表
     * @return
     */
    public List<JobParamDef> getList() ;

    /**
     * 获取单个作业的参数定义表
     * @param jobId
     * @return
     */
    public List<JobParamDef> getById(int jobId);

    /**
     * 删除一个参数
     * @param jobId
     * @param  paramSeq
     * @return
     */
    public int delete(int jobId, int paramSeq);

    /**
     * 新增一个参数
     * @param jobParamDef
     * @return
     */
    public int add(JobParamDef jobParamDef);

    /**
     * 批量修改JobParam
     * @param jobParamDef2
     * @return
     */
    public String update(JobParamDef2 jobParamDef2);
}
