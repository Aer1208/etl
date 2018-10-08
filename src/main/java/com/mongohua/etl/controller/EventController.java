package com.mongohua.etl.controller;

import com.alibaba.fastjson.JSONObject;
import com.mongohua.etl.model.JobEvent;
import com.mongohua.etl.service.EventService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 事件Controller类
 * @author xiaohf
 */
@Controller
@RequestMapping(value = "/monitor")
public class EventController {

    @Autowired
    private EventService eventService;

    @RequestMapping(value = "/event_index")
    @RequiresPermissions("monitor:event_index")
    public String eventIndex() {
        return "monitor/event_index";
    }

    @ResponseBody
    @RequestMapping(value = "/event_list",produces = "application/json;charset=utf-8")
    public Object eventList() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows", eventService.getList());
        return jsonObject;
    }

    @ResponseBody
    @RequestMapping(value = "/event_add" , produces = "application/json;charset=utf-8")
    public Object addOrUpdate(JobEvent jobEvent) {
        JSONObject jsonObject = new JSONObject();
        if (jobEvent.getEventId() > 0) {
            int ret = eventService.update(jobEvent);
            jsonObject.put("type","update");
            jsonObject.put("ret", ret);
        }else {
            int ret = eventService.add(jobEvent);
            jsonObject.put("type","add");
            jsonObject.put("ret",ret);
        }
        return  jsonObject;
    }

    @ResponseBody
    @RequestMapping(value = "/event_delete",produces = "application/json;charset=utf-8")
    public String delete(String ids) {
        int ret = eventService.delete(ids.split(","));
        return "{\"ret\":"+ ret +"}";
    }

}
