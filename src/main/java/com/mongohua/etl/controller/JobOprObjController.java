package com.mongohua.etl.controller;

import com.alibaba.fastjson.JSONObject;
import com.mongohua.etl.model.JobLockObj;
import com.mongohua.etl.service.JobLockObjService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 获取作业锁数据的控制层
 * @author xiaohf
 */
@Controller
public class JobOprObjController {

    @Autowired
    private JobLockObjService jobLockObjService;

    /**
     * 获取所有写锁的作业
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/manager/get_writer_lock",produces = "application/json;charset=utf-8")
    public List<JobLockObj> getWriterLock() {
        return  jobLockObjService.getJobByLockObj();
    }

    /**
     * 根据一个作业获取作业的锁列表
     * @param jobId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/manager/get_lock_byjob",produces = "application/json;charset=utf-8")
    public Object getLockObjsByJobId(int jobId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows",jobLockObjService.getLockObjsByJobId(jobId));
        return jsonObject;
    }
}
