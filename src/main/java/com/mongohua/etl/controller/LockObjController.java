package com.mongohua.etl.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongohua.etl.schd.common.JobReadWriterLock;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Iterator;
import java.util.Map;

/**
 * 锁对象监控控制层
 * @author xiaohf
 */
@Controller
@RequestMapping(value = "/monitor")
public class LockObjController {

    @RequestMapping(value = "/lock_list", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object lockList() {
        Map<String, Integer> writerLock = JobReadWriterLock.getInstance().getWriterLock();
        Map<String, Integer> readerLock = JobReadWriterLock.getInstance().getReadLock();
        Iterator<String> writerKeys = writerLock.keySet().iterator();
        JSONArray jsonArray = new JSONArray();
        // 写锁
        while (writerKeys.hasNext()) {

            String lockObj = writerKeys.next();
            if (writerLock.get(lockObj) != null && writerLock.get(lockObj) >0) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("lockObj", lockObj);
                jsonObject.put("writeLockCnt", writerLock.get(lockObj));
                jsonObject.put("readLockCnt", readerLock.get(lockObj) == null ? 0 : readerLock.get(lockObj));
                jsonArray.add(jsonObject);
                readerLock.remove(lockObj);
            }
        }
        // 读锁
        Iterator<String> readKeys = readerLock.keySet().iterator();
        while (readKeys.hasNext()) {
            String lockObj = readKeys.next();
            if (readerLock.get(lockObj) != null && readerLock.get(lockObj) > 0) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("lockObj", lockObj);
                jsonObject.put("writeLockCnt", 0);
                jsonObject.put("readLockCnt", readerLock.get(lockObj));
                jsonArray.add(jsonObject);
            }
        }
        JSONObject result = new JSONObject();
        result.put("rows", jsonArray);
        return result;
    }

    @RequestMapping(value = "/lock_index")
    @RequiresPermissions("monitor:lock_index")
    public String dsInstIndex() {
        return "monitor/lock_index";
    }

    @RequestMapping(value = "/delete_lock", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object deleteLock(String lockStr) {
        JSONObject jsonObject = new JSONObject();
        for (String lockObj : lockStr.split(",")) {
            JobReadWriterLock.getInstance().clearLock(lockObj);
        }
        jsonObject.put("ret","success");
        return jsonObject;
    }
}
