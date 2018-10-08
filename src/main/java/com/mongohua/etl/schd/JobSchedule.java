package com.mongohua.etl.schd;

import com.mongohua.etl.model.JobDef;
import com.mongohua.etl.schd.common.InitDataBase;
import com.mongohua.etl.schd.common.InitJdbc;
import com.mongohua.etl.schd.common.JobReadWriterLock;
import com.mongohua.etl.schd.job.ShellJob;
import com.mongohua.etl.schd.job.SqoopJob;
import com.mongohua.etl.utils.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 队列生成调度和作业运行调度
 * @author xiaohf
 */
@SuppressWarnings("ALL")
@Component
public class JobSchedule extends InitJdbc{
    private final static Logger logger = LoggerFactory.getLogger(JobSchedule.class);
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private ThreadPoolExecutor jobThreadPool;

    public static final Object RUNNING_LOCK = new Object();
    public static final Object QUEUE_LOCK = new Object();

    @Autowired
    private ShellJob shellJob;

    @Autowired
    private SqoopJob sqoopJob;

    @Autowired
    private Environment environment;

    @SuppressWarnings("AlibabaThreadShouldSetName")
    public void schedule() {

        start();

        threadPoolTaskScheduler.schedule(new QueueRunnable(), new CronTrigger("*/10 * * * * *"));
        threadPoolTaskScheduler.schedule(new JobRunnable(), new CronTrigger("*/10 * * * * *"));

    }

    /**
     * 停止调度
     */
    public void stop() {
        logger.info("shutdown jobThreadPool....");
        jobThreadPool.shutdown();
    }

    /**
     * 重新创建线程池
     */
    public void start() {
        logger.info("creating jobThreadPool...");
        ThreadFactory factory = new ThreadFactory() {
            private final AtomicInteger integer = new AtomicInteger();

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "ThreadPool thread: " + integer.getAndIncrement());
            }
        };
        jobThreadPool = new ThreadPoolExecutor(Integer.parseInt(environment.getProperty(Constant.MAX_RUN_CNT)),
                Integer.parseInt(environment.getProperty(Constant.MAX_RUN_CNT)),
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                factory);
    }

    class QueueRunnable implements Runnable {

        @Override
        public void run() {
            synchronized (QUEUE_LOCK) {
                logger.debug("scan events....");
                List<Map<String, Object>> result = jdbcTemplate.queryForList("select a.job_id, b.data_date, count(distinct a.ref_job_id) ref_job_cnt " +
                        "  from t_job_ref a " +
                        "        join t_job_event b on a.job_id=b.job_id and a.ref_job_id=b.ref_job_id and a.ref_type=b.ref_type " +
                        " group by a.job_id, b.data_date");
                for (Map<String, Object> m : result) {
                    int jobId = Integer.parseInt(m.get("job_id").toString());
                    int vDate = Integer.parseInt(m.get("data_date").toString());
                    int refJobCnt = Integer.parseInt(m.get("ref_job_cnt").toString());
                    if (InitDataBase.jobDefMap.get(jobId) == null) {
                        // 如果内存数据库里面没有作业定义信息表，则跳过
                        continue;
                    }
                    if (refJobCnt == InitDataBase.jobRefMap.get(jobId).size()) {
                        // 如果触发事件满足依赖关系，则生成队列，并删除事件
                        JobDef jobDef = InitDataBase.jobDefMap.get(jobId);
                        // 插入队列
                        jdbcTemplate.update("insert into t_job_queue(job_id,data_date,priorty) values(?,?,?)", jobId, vDate, jobDef.getPriorty());
                        // 删除事件
                        jdbcTemplate.update("DELETE  from t_job_event where job_id=? and data_date=? ", jobId, vDate);
                        logger.info("generate queue info [job_id={}, data_date={}]", jobId, vDate);
                    }
                }
            }
        }

    }

    class JobRunnable implements  Runnable {

        @Override
        public void run() {
            if (jobThreadPool.getActiveCount() >= Integer.parseInt(environment.getProperty(Constant.MAX_RUN_CNT))) {
                // 如果在线进程大于最大则跳过
                return ;
            }
            if (jobThreadPool.isShutdown() || jobThreadPool.isTerminated() || jobThreadPool.isTerminating()) {
                // 如果调度程序处于不正常状态，则不调用作业
                logger.info("jobThreadPool is shutdown or terminated or terminating ,please access /manger/start start jobThreadPool");
                return ;
            }
            synchronized (RUNNING_LOCK) {
                // 需要从队列中取出的作业数
                int runningCnt = Integer.parseInt(environment.getProperty(Constant.MAX_RUN_CNT)) - jobThreadPool.getActiveCount() ;
                logger.debug("scan queues ....");
                // 将重调的作业插入队列中，优先级默认为100
                jdbcTemplate.update("INSERT INTO T_JOB_QUEUE (JOB_ID,DATA_DATE,PRIORTY) " +
                        "                   SELECT b.JOB_ID,B.DATA_DATE,10000 " +
                        "                     FROM T_ERR_INST A" +
                        "                           JOIN T_JOB_INST B ON A.INST_ID=B.INST_ID" +
                        "                    WHERE A.STATUS=1");
                // 删除重调的作业
                jdbcTemplate.update("delete from T_ERR_INST WHERE STATUS=1");
                List<Map<String, Object>> queues = jdbcTemplate.queryForList("select QUEUE_ID, JOB_ID, DATA_DATE FROM t_JOB_QUEUE ORDER BY DATA_DATE, PRIORTY DESC LIMIT " + runningCnt);
                for (Map<String, Object> queue: queues) {
                    final int jobId = Integer.parseInt(queue.get("JOB_ID").toString());
                    final int queueId = Integer.parseInt(queue.get("QUEUE_ID").toString());
                    final String vDate = queue.get("DATA_DATE").toString();
                    //删除队列
                    jdbcTemplate.update("delete from t_job_queue where queue_id=?", queueId);

                    jobThreadPool.execute(new Runnable(){
                        // 开始启动执行作业线程
                        @Override
                        public void run() {

                            try {
                                // 获取锁
                                JobReadWriterLock.getInstance().lock(jobId, vDate);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            if (jobId < Constant.MIN_JOB_ID) {
                                // 如果作业号小于20000000，则认为是数据源重调
                                sqoopJob.run(jobId, vDate);
                            } else {
                                // 执行作业
                                shellJob.run(jobId, vDate);
                            }
                        }
                    });
                }
            }
        }
    }
}
