package com.mongohua.etl.schd.common;

import com.mongohua.etl.schd.DsSchedule;
import com.mongohua.etl.schd.JobSchedule;
import com.mongohua.etl.schd.NotRefSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 调度的主线程类，包含初始化数据库，启动数据源线程，启动作业线程，启动没有依赖的作业
 * @author xiaohf
 */
@Component
public class ScheduleThread implements Runnable {

    public final static Logger logger = LoggerFactory.getLogger(ScheduleThread.class);
    @Autowired
    private InitDataBase initDataBase;

    @Autowired
    private DsSchedule dsSchedule;

    @Autowired
    private JobSchedule jobSchedule;

    @Autowired
    private NotRefSchedule notRefSchedule;
    @Override
    public void run() {
        logger.info("初始化数据库....");
        // 初始化数据库
        initDataBase.init();
        // 启动初始化数据源定时任务进程
        logger.info("启动数据源定时调度....");
        dsSchedule.schedule();

        logger.info("启动队列生成器...");
        jobSchedule.schedule();

        logger.info("启动没有依赖的作业定时执行调度器...");
        notRefSchedule.schedule();

        logger.info("启动完成");
    }
}
