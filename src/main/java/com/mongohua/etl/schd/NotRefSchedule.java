package com.mongohua.etl.schd;

import com.mongohua.etl.model.JobDef;
import com.mongohua.etl.schd.common.InitDataBase;
import com.mongohua.etl.schd.common.InitJdbc;
import com.mongohua.etl.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * 启动没有依赖的作业运行情况
 * @author xiaohf
 */
@Component
public class NotRefSchedule extends InitJdbc{

    public static final Logger logger = LoggerFactory.getLogger(NotRefSchedule.class);
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private Map<Integer, ScheduledFuture<?>> notRefJobSchd = new HashMap<Integer, ScheduledFuture<?>>();

    public void schedule() {

        for (final JobDef jobDef: InitDataBase.jobDefMap.values()) {
            startNotRefJob(jobDef);
        }
    }

    /**
     * 启动一个没有依赖的作业调度
     * @param jobDef
     */
    public void startNotRefJob(final JobDef jobDef) {
        final int jobId = jobDef.getJobId();
        final String cronDesc = jobDef.getCronDesc();
        final int priorty = jobDef.getPriorty();
        if (!InitDataBase.jobRefMap.keySet().contains(jobId) && cronDesc != null && !"".equals(cronDesc)) {
            if (notRefJobSchd.containsKey(jobId)) {
                // 如果作业的调度已经启动，则选择停止
                logger.info("re schedule job,job_id={}", jobId);
                stopNotRefJob(jobDef);
            }
            // 启动调度
            ScheduledFuture<?> jobSchd = threadPoolTaskScheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    try {
                        String vDate = Util.dateIns(-jobDef.getJobCycle(), jobDef.getCycleUnit());
                        jdbcTemplate.update("INSERT INTO T_JOB_QUEUE (job_id,data_date,PRIORTY) VALUES(?,?,?)"
                                , jobId, vDate, priorty);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }, new CronTrigger(cronDesc));
            // 保存启动的调度
            notRefJobSchd.put(jobId, jobSchd);
        }
    }

    /**
     * 取消一个没有依赖的作业调度
     * @param jobDef
     */
    public void stopNotRefJob(final JobDef jobDef) {
        final int jobId = jobDef.getJobId();
        if (notRefJobSchd.containsKey(jobId)) {
            ScheduledFuture jobSchd = notRefJobSchd.get(jobId);
            jobSchd.cancel(true);
            notRefJobSchd.remove(jobId);
        }
    }

    /**
     * 判断没有依赖的作业调度是否已经启动
     * @param jobDef
     * @return
     */
    public int scheduleStauts(JobDef jobDef) {
        if (jobDef == null ) {
            return 0;
        }
        if (notRefJobSchd.containsKey(jobDef.getJobId())) {
            return 1;
        }
        return 0;
    }

}
