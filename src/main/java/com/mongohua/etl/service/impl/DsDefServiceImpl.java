package com.mongohua.etl.service.impl;

import com.mongohua.etl.mapper.DsDefMapper;
import com.mongohua.etl.model.DsDef;
import com.mongohua.etl.schd.DsSchedule;
import com.mongohua.etl.schd.common.InitDataBase;
import com.mongohua.etl.service.DsDefService;
import com.mongohua.etl.utils.PageModel;
import com.mongohua.etl.utils.SecurityUtil;
import groovy.util.logging.Slf4j;
import org.apache.shiro.config.Ini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据源服务类的实现
 * @author xiaohf
 */
@Service
public class DsDefServiceImpl implements DsDefService {

    private final static Logger logger = LoggerFactory.getLogger(DsDefServiceImpl.class);

    @Autowired
    private DsDefMapper dsDefMapper;

    @Autowired
    private DsSchedule dsSchedule;

    @Override
    public List<DsDef> getList() {
        return dsDefMapper.getList();
    }

    @Override
    public int add(DsDef dsDef) {
        return dsDefMapper.add(dsDef);
    }

    @Override
    public PageModel<DsDef> getListForPage(String key,int pageNo, int pageSize) {
        if (pageNo < 0) {
            pageNo = 1;
        }

        PageModel<DsDef> dsDefPageModel = new PageModel<DsDef>();
        dsDefPageModel.setPageNo(pageNo);
        dsDefPageModel.setPageSize(pageSize);

        int count = dsDefMapper.getCount(SecurityUtil.getCurrentUserId(),key);
        dsDefPageModel.setTotal(count);
        int pageIndex = (pageNo - 1) * pageSize;
        dsDefPageModel.setRows(dsDefMapper.getListForPage(SecurityUtil.getCurrentUserId(),key,pageIndex,pageSize));
        int totalPage = (int)Math.ceil(count*1.0/pageSize);
        dsDefPageModel.setTotalPage(totalPage);
        return dsDefPageModel;
    }

    @Override
    public PageModel<DsDef> getListForPage2(int pageNo, int pageSize) {
        if (pageNo < 0) {
            pageNo = 1;
        }

        PageModel<DsDef> dsDefPageModel = new PageModel<DsDef>();
        dsDefPageModel.setPageNo(pageNo);
        dsDefPageModel.setPageSize(pageSize);

        int count = dsDefMapper.getCount2(SecurityUtil.getCurrentUserId());
        dsDefPageModel.setTotal(count);
        int pageIndex = (pageNo - 1) * pageSize;
        List<DsDef> dsDefList2 = new ArrayList<DsDef>();
        List<DsDef> dsDefList = dsDefMapper.getListForPage2(SecurityUtil.getCurrentUserId(),pageIndex,pageSize);
        for (DsDef dsDef: dsDefList) {
            // 更新调度是否启动状态
            dsDef.setScheduleStatus(dsSchedule.scheduleStauts(dsDef));
            dsDefList2.add(dsDef);
        }
        dsDefPageModel.setRows(dsDefList2);
        int totalPage = (int)Math.ceil(count*1.0/pageSize);
        dsDefPageModel.setTotalPage(totalPage);
        return dsDefPageModel;
    }

    @Override
    public int changeDsValid(String[] dsIds, int valid) {
        if (valid != 1 && valid != 0) {
            return 0;
        }
        if (valid == 1) {
            return dsDefMapper.dsValid(dsIds);
        } else {
            for ( String dsId: dsIds ) {
                DsDef dsDef = InitDataBase.dsDefMap.get(Integer.parseInt(dsId));
                logger.info("changeDsValid数据源【ds={}】修改成功，并且置为失效，触发调度停止...", dsDef.getDsId());
                dsSchedule.cancel(dsDef);
            }
            return dsDefMapper.dsInvalid(dsIds);
        }
    }

    @Override
    public int update(DsDef dsDef) {
        int res = dsDefMapper.update(dsDef);
        DsDef memoryDsdef = InitDataBase.dsDefMap.get(dsDef.getDsId());
        if (res > 0 ) {
            if (dsDef.getDsValid() == 0 && memoryDsdef != null && memoryDsdef.getDsValid() == 1 ) {
                // 如果修改成功，并且调度置为失效，则将调度停止
                logger.info("update数据源【ds={}】修改成功，并且置为失效，触发调度停止...", dsDef.getDsId());
                dsSchedule.cancel(dsDef);
            }
        }
        return res;
    }

}
