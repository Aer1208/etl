package com.mongohua.etl.controller;

import com.mongohua.etl.mapper.HostMapper;
import com.mongohua.etl.model.Host;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 服务器请求控制层
 * @author xiaohf
 */

@Controller
public class HostController {

    @Autowired
    private HostMapper hostMapper;

    @ResponseBody
    @RequestMapping(value = "/host/list", produces = "application/json;charset=utf-8")
    public List<Host> getList() {
        return hostMapper.getList();
    }
}
