package com.mongohua.etl.service;

import com.alibaba.fastjson.JSONObject;
import com.mongohua.etl.model.JobDef;
import com.mongohua.etl.model.JobDef2;
import com.mongohua.etl.utils.PageModel;

import java.util.List;

/**
 * 作业定义服务接口
 * @author xiaohf
 */
public interface JobDefService {
    /**
     * 获取所有的作业定义表
     * @return
     */
    public List<JobDef> getList();

    /**
     * 获取单个作业定义表
     * @param jobId
     * @return
     */
    public JobDef getById(int jobId);

    /**
     * 删除一个作业定义表
     * @param jobId
     * @return
     */
    public int delete(int jobId);

    /**
     * 添加一个作业定义表
     * @param jobDef
     * @return
     */
    public int add(JobDef jobDef);

    /**
     * 获取jobId的后续作业
     * @param jobId
     * @return
     */
    public Object getNextJob(int jobId);

    /**
     * 获取jobId的前置作业
     * @param jobId
     * @return
     */
    public Object getPreJob(int jobId);

    /**
     * 获取有效作业列表
     * @param  key 搜索关键字
     * @param page
     * @param rows
     * @return
     */
    public PageModel<JobDef> getListForPage(String key,int page, int rows);

    /**
     * 获取全部作业列表
     * @param jobdef
     * @param page
     * @param rows
     * @return
     */
    public PageModel<JobDef> getListForPage2(JobDef jobdef,int page, int rows);

    /**
     * 添加一个作业，包括作业定义表，作业依赖表，作业参数表
     * @param jobDef2
     * @return
     */
    public String addJob(JobDef2 jobDef2);

    /**
     * 更新一个作业
     * @param jobDef
     * @return
     */
    public int update(JobDef jobDef);
}
