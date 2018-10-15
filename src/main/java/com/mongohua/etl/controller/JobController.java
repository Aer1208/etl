package com.mongohua.etl.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongohua.etl.model.*;
import com.mongohua.etl.schd.JobSchedule;
import com.mongohua.etl.schd.NotRefSchedule;
import com.mongohua.etl.schd.common.InitDataBase;
import com.mongohua.etl.service.JobDefService;
import com.mongohua.etl.service.JobInstService;
import com.mongohua.etl.service.JobParamDefService;
import com.mongohua.etl.service.JobRefServuce;
import com.mongohua.etl.utils.PageModel;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 作业Controller
 * @author xiaohf
 */
@Controller
public class JobController {

    @Autowired
    private JobInstService jobInstService;

    @Autowired
    private JobDefService jobDefService;

    @Autowired
    private JobRefServuce jobRefServuce;

    @Autowired
    private JobParamDefService jobParamDefService;

    @Autowired
    private InitDataBase initDataBase;

    @Autowired
    private JobSchedule jobSchedule;

    @Autowired
    private NotRefSchedule notRefSchedule;

    /**
     * 作业运行实例请求页面
     * @return
     */
    @RequestMapping(value = "/monitor/inst_index")
    @RequiresPermissions("monitor:inst_index")
    public String instIndex() {
        return "monitor/inst_index";
    }

    /**
     * 查询作业运行请求
     * @param jobInst
     * @param page
     * @param rows
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/monitor/inst_list",produces = "application/json;charset=utf-8")
    public String instList(JobInst jobInst, int page, int rows) {
        PageModel<JobInst> listForPage = jobInstService.getListForPage(jobInst, page, rows);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows",listForPage.getRows());
        jsonObject.put("total",listForPage.getTotal());
        return jsonObject.toString();
    }

    /**
     * 查询作业运行日志请求
     * @param instId
     * @param model
     * @return
     */
    @RequestMapping(value = "/monitor/inst_log")
    public String instLog(int instId,Model model) {
        String instLog = jobInstService.getInstLog(instId);
        model.addAttribute("instLog", instLog);
        return "monitor/inst_log";
    }

    /**
     * 重调作业实例
     * @param ids
     * @return
     */
    @RequestMapping(value = "/monitor/inst_redo")
    @ResponseBody
    public String redoInsts(String ids) {
        int ret = jobInstService.redoInsts(ids.split(","));
        return "{\"ret\":" + ret + "}";
    }

    /**
     * 查看某一个作业的全部运行实例
     * @param jobId
     * @return
     */
    @RequestMapping(value = "/monitor/inst_job")
    @ResponseBody
    public Object instJob(int jobId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows",jobInstService.instJob(jobId));
        return jsonObject;
    }

    /**
     * 查看作业的后续作业列表
     * @param jobId
     * @return
     */
    @RequestMapping(value = "/monitor/next_job")
    @ResponseBody
    public Object nextJob(int jobId) {
        Object obj = jobDefService.getNextJob(jobId);
        if (obj == null) {
            return new JSONArray();
        }
        return obj;
    }

    /**
     * 查看作业的后续作业列表
     * @param jobId
     * @return
     */
    @RequestMapping(value = "/monitor/pre_job")
    @ResponseBody
    public Object preJob(int jobId) {
        Object obj = jobDefService.getPreJob(jobId);
        if (obj == null) {
            return new JSONArray();
        }
        return obj;
    }

    /**
     * 普通作业监控首页请求
     * @return
     */
    @RequestMapping(value = "/monitor/job_inst_index")
    @RequiresPermissions("monitor:job_inst_index")
    public String jobIndex() {
        return "monitor/job_inst_index";
    }

    /**
     * 普通作业列表
     * @param page
     * @param rows
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/monitor/job_inst_list",produces = "application/json;charset=utf-8")
    public String jobList(String key,int page, int rows) {
        PageModel<JobDef> listForPage = jobDefService.getListForPage(key,page, rows);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows",listForPage.getRows());
        jsonObject.put("total",listForPage.getTotal());
        return jsonObject.toString();
    }

    /**
     * 作业管理请求首页
     * @return
     */
    @RequestMapping(value = "/manager/job_index")
    @RequiresPermissions("manager:job_index")
    public String jobMngIndex() {
        return "manager/job_index";
    }

    /**
     * 分页返回所有作业列表
     * @param page
     * @param rows
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/manager/job_list",produces = "application/json;charset=utf-8")
    public String jobMngList(JobDef jobDef, int page, int rows) {
        PageModel<JobDef> listForPage = jobDefService.getListForPage2(jobDef,page, rows);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows",listForPage.getRows());
        jsonObject.put("total",listForPage.getTotal());
        return jsonObject.toString();
    }

    /**
     * 查看一个作业的作业定义表信息
     * @param jobId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/manager/job_byid",produces = "application/json;charset=utf-8")
    public String jobMngById(int jobId) {
        JSONObject jsonObject = new JSONObject();
        List<JobDef> jobDefList = new ArrayList<JobDef>();
        jobDefList.add(jobDefService.getById(jobId));
        jsonObject.put("rows", jobDefList);
        return jsonObject.toString();
    }

    /**
     * 查看一个作业的依赖信息
     * @param jobId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/manager/jobref_byid",produces = "application/json;charset=utf-8")
    public String jobRefMngById(int jobId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows", jobRefServuce.getById(jobId));
        return jsonObject.toString();
    }

    /**
     * 查看一个作业的参数定义信息
     * @param jobId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/manager/jobparam_byid",produces = "application/json;charset=utf-8")
    public String jobParamMngById(int jobId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows", jobParamDefService.getById(jobId));
        return jsonObject.toString();
    }

    /**
     * 添加作业页面
     * @return
     */
    @RequestMapping(value = "/manager/add_job",produces = "application/json;charset=utf-8")
    public String addJob() {
        return "manager/add_job";
    }

    /**
     * 提交新增作业请求
     * @param jobDef2
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/manager/submit_job",produces = "application/json;charset=utf-8")
    public String addJob(@RequestBody JobDef2 jobDef2) {
        return jobDefService.addJob(jobDef2);
    }

    /**
     * 更新一个作业
     * @param jobDef
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/manager/update_job",produces = "application/json;charset=utf-8")
    public String updateJob(@RequestBody JobDef jobDef) {
        int ret = jobDefService.update(jobDef);
        return null;
    }

    /**
     * 批量更新参数
     * @param jobParamDef2
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/manager/update_param", produces = "application/json;charset=utf-8")
    public String updateParam(@RequestBody JobParamDef2 jobParamDef2) {
        return  jobParamDefService.update(jobParamDef2);
    }

    /**
     * 批量修改依赖
     * @param jobRef2
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/manager/update_ref", produces = "application/json;charset=utf-8")
    public String updateRef(@RequestBody JobRef2 jobRef2) {
        return jobRefServuce.update(jobRef2);
    }

    /**
     * 刷新数据库中的作业定义数据到内存中
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/manager/refresh_job",produces = "application/json;charset=utf-8")
    public String refreshJob() {
        initDataBase.initJobDef();                  // 刷新作业定义表
        initDataBase.initJobParamDef();             // 刷新作业参数表
        initDataBase.initJobRef();                  // 刷新作业依赖表
        initDataBase.initJobLockObj();             //  刷新作业锁对象数据
        return "{\"ret\":\"success\"}";
    }

    @ResponseBody
    @RequestMapping(value = "/manager/start",produces = "application/json;charset=utf-8")
    public String startJobSchd() {
        jobSchedule.start();
        return "{\"ret\":\"success\"}";
    }

    @ResponseBody
    @RequestMapping(value = "/manager/stop",produces = "application/json;charset=utf-8")
    public String stopJobSchd() {
        jobSchedule.stop();
        return "{\"ret\":\"success\"}";
    }

    /**
     * 启动没有依赖的调度
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/manager/startNotRefSchd",produces = "application/json;charset=utf-8")
    public String startNotRefSchd(int jobId) {
        if (InitDataBase.jobRefMap.containsKey(jobId)) {
            return "{\"ret\":\"fail\",\"msg\":\"不能启动有依赖的作业调度\"}";
        }
        JobDef jobDef = InitDataBase.jobDefMap.get(jobId);
        if (jobDef == null) {
            return "{\"ret\":\"fail\",\"msg\":\"没有找到作业\"}";
        }else if (jobDef.getCronDesc() == null || "".equals(jobDef.getCronDesc())) {
            return "{\"ret\":\"fail\",\"msg\":\"没有配置定时任务表达式\"}";
        }
        notRefSchedule.startNotRefJob(jobDef);
        return "{\"ret\":\"success\"}";
    }

    /**
     * 停止没有依赖的调度
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/manager/stopNotRefSchd",produces = "application/json;charset=utf-8")
    public String stopNotRefSchd(int jobId) {
        JobDef jobDef = InitDataBase.jobDefMap.get(jobId);
        notRefSchedule.stopNotRefJob(jobDef);
        return "{\"ret\":\"success\"}";
    }

    /**
     * 没有依赖的作业列表
     * @return
     */
    @RequestMapping(value = "/manager/not_ref_job_index")
    @RequiresPermissions("manager:not_ref_job_index")
    public String notRefJobIndex() {
        return "manager/not_ref_job_index";
    }

    /**
     * 分页返回所有作业列表
     * @param page
     * @param rows
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/manager/not_ref_job_list",produces = "application/json;charset=utf-8")
    public String notRefJobs(String key, int page, int rows) {
        PageModel<JobDef> listForPage = jobDefService.getListForPage3(key,page, rows);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows",listForPage.getRows());
        jsonObject.put("total",listForPage.getTotal());
        return jsonObject.toString();
    }
}
