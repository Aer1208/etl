package com.mongohua.etl.schd;

import com.mongohua.etl.model.DsDef;
import com.mongohua.etl.schd.common.InitDataBase;
import com.mongohua.etl.schd.common.InitJdbc;
import com.mongohua.etl.schd.job.SqoopJob;
import com.mongohua.etl.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * 数据源调度主类，读取t_ds_def表里面的数据源启动数据源定义的调度，从mysql数据库执行导出表数据到hdfs上
 * @author xiaohf
 */
@Component
public class DsSchedule extends InitJdbc{
    private final Logger logger = LoggerFactory.getLogger(DsSchedule.class);
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private Map<Integer, ScheduledFuture<?>> startSchedule = new HashMap<Integer, ScheduledFuture<?>>();

    @Autowired
    private SqoopJob sqoopJob;
    /**
     * 新增一个数据源调度
     * @param dsDef
     */
    public void add(DsDef dsDef) {
        logger.info("开始启动调度[dsId={},dsName={}]...",dsDef.getDsId(), dsDef.getDsName());
        cancel(dsDef);

        ScheduledFuture<?> future = threadPoolTaskScheduler.schedule(new DsRunable(dsDef), new CronTrigger(dsDef.getCronDesc()));
        startSchedule.put(dsDef.getDsId(), future);

        logger.info("启动调度[dsId={},dsName={}]成功,现在有{}个数据源调度启动",dsDef.getDsId(), dsDef.getDsName(),startSchedule.size());
    }

    /**
     * 取消一个数据源调度
     * @param dsDef
     */
    public void cancel(DsDef dsDef) {

        if (dsDef == null) {
            return ;
        }

        // 如果存在则取消调度并删除
        if (startSchedule.containsKey(dsDef.getDsId())) {
            logger.info("开始停止调度[dsId={},dsName={}]...",dsDef.getDsId(), dsDef.getDsName());
            startSchedule.get(dsDef.getDsId()).cancel(true);
            startSchedule.remove(dsDef.getDsId());
            logger.info("启动调度[dsId={},dsName={}]成功,现在有{}个数据源调度启动",dsDef.getDsId(), dsDef.getDsName(),startSchedule.size());
        }
    }

    /**
     * 判断数据源是否已经启动
     * @param dsDef
     * @return
     */
    public int scheduleStauts(DsDef dsDef) {
        if (dsDef == null ) {
            return 0;
        }
        if (startSchedule.containsKey(dsDef.getDsId())) {
            return 1;
        }
        return 0;
    }

    /**
     * 启动数据源作业运行定时任务
     */
    public void schedule() {
        // 启动数据源定时启动任务
        if (InitDataBase.dsDefMap != null ) {
            Iterator<Integer> dsIt =  InitDataBase.dsDefMap.keySet().iterator();
            while (dsIt.hasNext()) {
                int dsId = dsIt.next();
                if (!startSchedule.containsKey(dsId)) {
                    DsDef dsDef = InitDataBase.dsDefMap.get(dsId);
                    ScheduledFuture<?> future = threadPoolTaskScheduler.schedule(new DsRunable(dsDef), new CronTrigger(dsDef.getCronDesc()));
                    startSchedule.put(dsDef.getDsId(), future);
                }
            }
        }
    }

    class DsRunable implements Runnable {

        private DsDef dsDef;

        public DsRunable(DsDef dsDef) {
            this.dsDef = dsDef;
        }

        @Override
        public void run() {
            try {
                String vDate = Util.dateIns(-dsDef.getJobCycle(),dsDef.getCycleUnit());
                // sqoopJob.run(dsDef.getDsId(), vDate); // 数据源调起，修改为插入队列中，通过队列方式调起作业
                jdbcTemplate.update("insert into t_job_queue(job_id,data_date,priorty) values(?,?,?)", dsDef.getDsId(), vDate, dsDef.getPriorty());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

}
