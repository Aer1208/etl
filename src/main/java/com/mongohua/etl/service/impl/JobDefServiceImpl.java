package com.mongohua.etl.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongohua.etl.mapper.JobDefMapper;
import com.mongohua.etl.mapper.JobLockObjMapper;
import com.mongohua.etl.mapper.JobParamDefMapper;
import com.mongohua.etl.mapper.JobRefMapper;
import com.mongohua.etl.model.*;
import com.mongohua.etl.schd.NotRefSchedule;
import com.mongohua.etl.schd.job.Job;
import com.mongohua.etl.service.JobDefService;
import com.mongohua.etl.utils.PageModel;
import com.mongohua.etl.utils.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 作业定义服务类实现
 * @author xiaohf
 */
@Service
public class JobDefServiceImpl implements JobDefService {

    @Autowired
    private JobDefMapper jobDefMapper;

    @Autowired
    private JobParamDefMapper jobParamDefMapper;

    @Autowired
    private JobRefMapper jobRefMapper;

    @Autowired
    private JobLockObjMapper jobLockObjMapper;

    @Autowired
    private NotRefSchedule notRefSchedule;

    @Override
    public List<JobDef> getList() {
        return jobDefMapper.getList();
    }

    @Override
    public JobDef getById(int jobId) {
        return jobDefMapper.getById(jobId);
    }

    @Override
    public int delete(int jobId) {
        return jobDefMapper.delete(jobId);
    }

    @Override
    public int add(JobDef jobDef) {
        return jobDefMapper.add(jobDef);
    }

    @Override
    public Object getNextJob(int jobId) {
        List<JobDef> jobDefs = jobDefMapper.getNextJob(jobId);
        JSONArray jsonArray = new JSONArray();
        if (jobDefs == null || jobDefs.size() ==0) {
            return null;
        }else {
            for (JobDef jobDef: jobDefs) {
                JSONObject jobDefJson = (JSONObject) JSON.toJSON(jobDef);
                jobDefJson.put("children", getNextJob(jobDef.getJobId()));
                jsonArray.add(jobDefJson);
            }
            return jsonArray;
        }
    }

    @Override
    public Object getPreJob(int jobId) {
        List<JobDef> jobDefs = jobDefMapper.getPreJob(jobId);
        JSONArray jsonArray = new JSONArray();
        if (jobDefs == null || jobDefs.size() ==0) {
            return null;
        }else {
            for (JobDef jobDef: jobDefs) {
                JSONObject jobDefJson = (JSONObject) JSON.toJSON(jobDef);
                jobDefJson.put("children", getPreJob(jobDef.getJobId()));
                jsonArray.add(jobDefJson);
            }
            return jsonArray;
        }
    }

    /**
     * 普通作业监控页面作业列表
     * @param  key 搜索关键字
     * @param page
     * @param rows
     * @return
     */
    @Override
    public PageModel<JobDef> getListForPage(String key, int page, int rows) {
        if (page < 0) {
            page = 1;
        }

        PageModel<JobDef> jobPageModel = new PageModel<JobDef>();
        jobPageModel.setPageNo(page);
        jobPageModel.setPageSize(rows);

        int count = jobDefMapper.getCount(SecurityUtil.getCurrentUserId(),key);
        jobPageModel.setTotal(count);
        int pageIndex = (page - 1) * rows;
        jobPageModel.setRows(jobDefMapper.getListForPage(SecurityUtil.getCurrentUserId(),key,pageIndex,rows));
        int totalPage = (int)Math.ceil(count*1.0/rows);
        jobPageModel.setTotalPage(totalPage);
        return jobPageModel;
    }

    /**
     * 作业管理页面的作业列表
     * @param jobDef
     * @param page
     * @param rows
     * @return
     */
    @Override
    public PageModel<JobDef> getListForPage2(JobDef jobDef,int page, int rows) {
        if (page < 0) {
            page = 1;
        }

        jobDef.setUserId(SecurityUtil.getCurrentUserId());

        PageModel<JobDef> jobPageModel = new PageModel<JobDef>();
        jobPageModel.setPageNo(page);
        jobPageModel.setPageSize(rows);

        int count = jobDefMapper.getCount2(jobDef);
        jobPageModel.setTotal(count);
        int pageIndex = (page - 1) * rows;
        jobPageModel.setRows(jobDefMapper.getListForPage2(jobDef,pageIndex,rows));
        int totalPage = (int)Math.ceil(count*1.0/rows);
        jobPageModel.setTotalPage(totalPage);
        return jobPageModel;
    }

    /**
     * 无依赖作业列表
     * @param  key 搜索关键字
     * @param page
     * @param rows
     * @return
     */
    @Override
    public PageModel<JobDef> getListForPage3(String key, int page, int rows) {
        if (page < 0) {
            page = 1;
        }

        PageModel<JobDef> jobPageModel = new PageModel<JobDef>();
        jobPageModel.setPageNo(page);
        jobPageModel.setPageSize(rows);

        int count = jobDefMapper.getCount3(SecurityUtil.getCurrentUserId(),key);
        jobPageModel.setTotal(count);
        int pageIndex = (page - 1) * rows;
        List<JobDef> jobDefs = jobDefMapper.getListForPage3(SecurityUtil.getCurrentUserId(),key,pageIndex,rows);
        List<JobDef> jobDefList = new ArrayList<JobDef>();
        for (JobDef jobDef: jobDefs) {
            jobDef.setScheduleStatus(notRefSchedule.scheduleStauts(jobDef));
            jobDefList.add(jobDef);
        }
        jobPageModel.setRows(jobDefList);
        int totalPage = (int)Math.ceil(count*1.0/rows);
        jobPageModel.setTotalPage(totalPage);
        return jobPageModel;
    }

    /**
     * 新增作业
     * @param jobDef2
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addJob(JobDef2 jobDef2) {
        try {
            List<JobRef> jobRefs = jobDef2.getJobRefs();
            List<JobParamDef> jobParamDefs = jobDef2.getJobParamDefs();

            User user = (User)SecurityUtils.getSubject().getPrincipal();
            JobDef jobDef = jobDef2.getJobDef();
            jobDef.setUserId(user.getUserId());
            int addJobCnt = jobDefMapper.add(jobDef2.getJobDef());
            int addjobParamCnt = 0;
            int addjobRefCnt = 0;
            if (jobParamDefs != null && jobParamDefs.size() > 0) {
                addjobParamCnt = jobParamDefMapper.addParams(jobDef2.getJobParamDefs());
            }
            if (jobRefs != null && jobRefs.size() > 0) {
                int[] refJobIds = new int[jobRefs.size()];
                for(int i = 0; i < jobRefs.size(); i++) {
                    refJobIds[i] = jobRefs.get(i).getRefJobId();
                }
                // 新加依赖
                addjobRefCnt = jobRefMapper.addRefs(jobDef2.getJobRefs());

                // 插入依赖的写锁
                jobLockObjMapper.insertLock(jobDef2.getJobDef().getJobId(), refJobIds);
            }
            jobLockObjMapper.add(jobDef2.getJobLockObj());

            return "{\"addJobCnt\":" + addJobCnt + ",\"addjobParamCnt\":" + addjobParamCnt + ",\"addjobRefCnt\":" + addjobRefCnt + ",\"status\":0}";
        }catch (Exception e) {
            return "{\"ret\":\"" + e.getMessage() + "\",\"status\":-1}";
        }
    }


    @Override
    public int update(JobDef jobDef) {
        return jobDefMapper.update(jobDef);
    }
}
