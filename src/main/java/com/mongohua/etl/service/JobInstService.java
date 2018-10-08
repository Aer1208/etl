package com.mongohua.etl.service;

import com.mongohua.etl.model.JobInst;
import com.mongohua.etl.utils.PageModel;

import java.util.List;

/**
 * 作业运行实例服务层
 * @author xiaohf
 */
public interface JobInstService {

    /**
     * 按页获取作业运行实例列表
     * @param jobInst
     * @param pageNo
     * @param pageSize
     * @return
     */
    public PageModel<JobInst> getListForPage(JobInst jobInst, int pageNo, int pageSize);

    /**
     * 获取作业实例的运行日志
     * @param instId
     * @return
     */
    public String getInstLog (int instId);

    /**
     * 批量重调出错的实例
     * @param ids
     * @return
     */
    public int redoInsts(String[] ids);

    /**
     * 获取一个作业的全部运行实例
     * @param jobId
     * @return
     */
    public List<JobInst> instJob(int jobId);
}
