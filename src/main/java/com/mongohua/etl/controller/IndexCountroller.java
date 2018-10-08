package com.mongohua.etl.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongohua.etl.mapper.EchartsMapper;
import com.mongohua.etl.model.Echarts;
import com.mongohua.etl.model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 首页相关的Controller
 * @author xiaohf
 */
@Controller
public class IndexCountroller {

    @Autowired
    private EchartsMapper echartsMapper;

    @RequestMapping(value = "/index")
    public String index(Model model) {
        Subject sub = SecurityUtils.getSubject();
        User user = (User)sub.getPrincipal();
        model.addAttribute("username", user.getUserName());
        return "index";
    }

    @RequestMapping("/")
    public String defaultIndex() {
        return "forward:/index";
    }

    @ResponseBody
    @RequestMapping(value = "/getServerTime")
    public String getServerTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    @ResponseBody
    @RequestMapping(value = "/topn", produces = "application/json;charset=utf-8")
    public Object getTopN(int dataDate, int top) {
        return echartsMapper.topN(dataDate, top);
    }

    @ResponseBody
    @RequestMapping(value = "/getRunTime", produces = "application/json;charset=utf-8")
    public Object getRunTime() {
        return echartsMapper.getRunTime();
    }

    @ResponseBody
    @RequestMapping(value = "/allJobs", produces = "application/json;charset=utf-8")
    public Object allJobs() {
        Echarts echarts = echartsMapper.allJobMonitor();
        return toArray(echarts);
    }

    @ResponseBody
    @RequestMapping(value = "/ds", produces = "application/json;charset=utf-8")
    public Object ds() {
        Echarts echarts = echartsMapper.dsMonitor();
        return toArray(echarts);
    }

    @ResponseBody
    @RequestMapping(value = "/job", produces = "application/json;charset=utf-8")
    public Object job() {
        Echarts echarts = echartsMapper.jobMonitor();
        return  toArray(echarts);
    }

    private Object toArray(Echarts echarts) {
        JSONArray array = new JSONArray();
        JSONObject allJob = new JSONObject();
        allJob.put("name","应完成实例数");
        allJob.put("value", echarts.getTotJobCnt());
        JSONObject succJob = new JSONObject();
        succJob.put("name","已完成实例数");
        succJob.put("value", echarts.getSuccJobCnt());
        JSONObject errJob = new JSONObject();
        errJob.put("name","已失败实例数");
        errJob.put("value",echarts.getErrJobCnt());

        array.add(allJob);
        array.add(succJob);
        array.add(errJob);
        return array;
    }
}
