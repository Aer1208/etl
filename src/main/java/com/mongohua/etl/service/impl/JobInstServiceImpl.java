package com.mongohua.etl.service.impl;

import com.mongohua.etl.mapper.JobInstMapper;
import com.mongohua.etl.model.JobInst;
import com.mongohua.etl.service.JobInstService;
import com.mongohua.etl.utils.PageModel;
import com.mongohua.etl.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 作业实例服务类实现
 * @author xiaohf
 */
@Service
public class JobInstServiceImpl implements JobInstService {

    @Autowired
    private JobInstMapper jobInstMapper;

    @Override
    public PageModel<JobInst> getListForPage(JobInst jobInst, int pageNo, int pageSize) {

        if (pageNo < 0) {
            pageNo = 1;
        }

        PageModel<JobInst> jobInstPageModel = new PageModel<JobInst>();
        jobInstPageModel.setPageNo(pageNo);
        jobInstPageModel.setPageSize(pageSize);

        int count = jobInstMapper.getCount(SecurityUtil.getCurrentUserId(),jobInst);
        jobInstPageModel.setTotal(count);
        int pageIndex = (pageNo - 1) * pageSize;
        jobInstPageModel.setRows(jobInstMapper.getList(SecurityUtil.getCurrentUserId(),jobInst,pageIndex,pageSize));
        int totalPage = (int)Math.ceil(count*1.0/pageSize);
        jobInstPageModel.setTotalPage(totalPage);
        return jobInstPageModel;
    }

    @Override
    public String getInstLog(int instId) {
        return jobInstMapper.getInstLog(instId);
    }

    @Override
    public int redoInsts(String[] ids) {
        return jobInstMapper.redoInsts(ids);
    }

    @Override
    public List<JobInst> instJob(int jobId) {
        return jobInstMapper.instJob(jobId);
    }
}
