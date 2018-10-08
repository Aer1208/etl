package com.mongohua.etl.controller;

import com.alibaba.fastjson.JSONObject;
import com.mongohua.etl.model.ErrInst;
import com.mongohua.etl.service.ErrInstService;
import com.mongohua.etl.utils.PageModel;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author xiaohaifang
 * @date 2018/9/30 10:15
 *       错误实例Controller层
 */
@Controller
public class ErrInstController {

    @Autowired
    private ErrInstService errInstService;

    @RequestMapping(value = "/monitor/err_inst_list", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getList(int page, int rows) {
        PageModel<ErrInst> listForPage = errInstService.getListForPage(page, rows);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows",listForPage.getRows());
        jsonObject.put("total",listForPage.getTotal());
        return jsonObject.toString();
    }

    @RequestMapping(value = "/monitor/err_inst_index")
    @RequiresPermissions("monitor:err_inst_index")
    public String errInstIndex() {
        return "monitor/err_inst_index";
    }
}
