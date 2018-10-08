package com.mongohua.etl.controller;

import com.alibaba.fastjson.JSONObject;
import com.mongohua.etl.model.JobQueue;
import com.mongohua.etl.service.QueueService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 队列Controller
 * @author xiaohf
 */
@Controller
@RequestMapping(value = "/monitor")
public class QueueController {

    @Autowired
    private QueueService queueService;

    @RequestMapping(value = "/queue_index")
    @RequiresPermissions("monitor:queue_index")
    public String queueIndex() {
        return "monitor/queue_index";
    }

    @ResponseBody
    @RequestMapping(value = "/queue_list",produces = "application/json;charset=utf-8")
    public Object queueList() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows", queueService.getList());
        return jsonObject;
    }

    @ResponseBody
    @RequestMapping(value = "/queue_add" , produces = "application/json;charset=utf-8")
    public Object addOrUpdate(JobQueue jobQueue) {
        JSONObject jsonObject = new JSONObject();
        if (jobQueue.getQueueId() > 0) {
            int ret = queueService.update(jobQueue);
            jsonObject.put("type","update");
            jsonObject.put("ret", ret);
        }else {
            int ret = queueService.add(jobQueue);
            jsonObject.put("type","add");
            jsonObject.put("ret",ret);
        }
        return  jsonObject;
    }

    @ResponseBody
    @RequestMapping(value = "/queue_delete",produces = "application/json;charset=utf-8")
    public String delete(String ids) {
        int ret = queueService.delete(ids.split(","));
        return "{\"ret\":"+ ret +"}";
    }

}
