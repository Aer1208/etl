package com.mongohua.etl.controller;

import com.mongohua.etl.model.DsDef;
import com.mongohua.etl.schd.DsSchedule;
import com.mongohua.etl.schd.common.InitDataBase;
import com.mongohua.etl.service.DsDefService;
import com.mongohua.etl.utils.Constant;
import com.mongohua.etl.utils.SecurityUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 数据源Controller
 * @author xiaohf
 */
@Controller
public class DsDefController {
    private final Logger logger = LoggerFactory.getLogger(DsDefController.class);
    @Autowired
    private DsDefService dsDefService;

    @Autowired
    private DsSchedule dsSchedule;

    @Autowired
    private InitDataBase initDataBase;

    @RequestMapping(value = "/monitor/ds_inst_list", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object getList(String key,int page, int rows) {
        return dsDefService.getListForPage(key,page, rows);
    }

    @RequestMapping(value = "/monitor/ds_inst_index")
    @RequiresPermissions("monitor:ds_inst_index")
    public String dsInstIndex() {
        return "monitor/ds_inst_index";
    }

    @RequestMapping(value = "/manager/ds_index")
    @RequiresPermissions("manager:ds_index")
    public String dsIndex() {
        return "manager/ds_index";
    }

    @RequestMapping(value = "/manager/ds_list", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object dsList(int page, int rows) {
        return dsDefService.getListForPage2(page, rows);
    }


    @ResponseBody
    @RequestMapping(value = "/manager/ds_stop",produces = "application/json;charset=utf-8")
    public String stopSchedule(int dsId) {
        DsDef dsDef = InitDataBase.dsDefMap.get(dsId);
        dsSchedule.cancel(dsDef);
        return "{\"ret\":1}";
    }

    @ResponseBody
    @RequestMapping(value = "/manager/ds_start",produces = "application/json;charset=utf-8")
    public String startSchedue(int dsId) {
        DsDef dsDef = InitDataBase.dsDefMap.get(dsId);
        dsSchedule.add(dsDef);
        return "{\"ret\":1}";
    }

    @ResponseBody
    @RequestMapping(value = "/manager/ds_refresh",produces = "application/json;charset=utf-8")
    public String refreshDs() {
        logger.info("开始刷新调度，刷新之前调度数={}", InitDataBase.dsDefMap.size());
        initDataBase.initDsDef();
        logger.info("刷新调度成功，刷新之后调度数={}", InitDataBase.dsDefMap.size());
        return "{\"dsSize\":" + InitDataBase.dsDefMap.size() + "}";
    }

    @ResponseBody
    @RequestMapping(value = "/manager/ds_change_valid",produces = "application/json;charset=utf-8")
    public String changeDsValid(String ids, int valid) {
        int ret = dsDefService.changeDsValid(ids.split(","),valid);
        return "{\"ret\":" + ret + "}";
    }

    @ResponseBody
    @RequestMapping(value = "/manager/ds_add",produces = "application/json;charset=utf-8")
    public String dsAdd(DsDef dsDef, String type) {
        int ret = 0;
        dsDef.setUserId(SecurityUtil.getCurrentUserId());
        if (Constant.ADD.equalsIgnoreCase(type)) {
            ret = dsDefService.add(dsDef);
        } else if (Constant.UPDATE.equalsIgnoreCase(type)) {
            ret = dsDefService.update(dsDef);
        }
        return "{\"ret\":" + ret + "}";
    }
}
