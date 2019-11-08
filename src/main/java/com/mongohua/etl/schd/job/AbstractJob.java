package com.mongohua.etl.schd.job;

import com.mongohua.etl.model.Cycle;
import com.mongohua.etl.model.DsDef;
import com.mongohua.etl.model.JobDef;
import com.mongohua.etl.schd.common.InitDataBase;
import com.mongohua.etl.schd.common.InitJdbc;
import com.mongohua.etl.schd.common.JobReadWriterLock;
import com.mongohua.etl.schd.executor.Executor;
import com.mongohua.etl.utils.Constant;
import com.mongohua.etl.utils.SpringContextUtil;
import com.mongohua.etl.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.*;

/**
 * 抽象作业运行类，定义一个可运行的作业必须实现<code>getArgs(int jobId,String vDate)</code>
 * @author xiaohf
 */
public abstract class AbstractJob extends InitJdbc implements Job {

    public final static Object LOCK = new Object();

    public final static Logger logger = LoggerFactory.getLogger(Job.class);

    @Autowired
    protected Environment environment;

    /**
     * 根据jobId获取数据源或者普通作业对象
     * @param jobId
     * @return
     */
    public Cycle getCycle(final int jobId) {
        if (jobId >= Constant.MIN_JOB_ID) {
            return InitDataBase.jobDefMap.get(jobId);
        } else {
            return InitDataBase.dsDefMap.get(jobId);
        }
    }

    @Override
    public void run(final int jobId, final String vDate) {

        if (InitDataBase.jobDefMap.containsKey(jobId)) {
            // 普通作业
            JobDef jobDef = InitDataBase.jobDefMap.get(jobId);
            if (jobDef == null || jobDef.getJobValid() != 1) {
                logger.warn("job_id={},data_date={} not found... please check or refresh", jobId,vDate);
                return;
            }
        } else if (InitDataBase.dsDefMap.containsKey(jobId)) {
            // 数据源
            DsDef dsDef = InitDataBase.dsDefMap.get(jobId);
            if (dsDef == null || dsDef.getDsValid() != 1) {
                logger.warn("ds_id={},data_date={} not found... please check or refresh", jobId,vDate);
                return;
            }
        }

        // 执行之前记录日志
        KeyHolder keyHolder = new GeneratedKeyHolder();
        synchronized (LOCK) {
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement("insert into t_job_inst (job_id,data_date,start_time,end_time,status) values (?,?,?,?,?)",
                            Statement.RETURN_GENERATED_KEYS);
                    ps.setInt(1, jobId);
                    ps.setString(2, vDate);
                    ps.setTimestamp(3, new java.sql.Timestamp(Util.getCurrentTime()));
                    ps.setTimestamp(4, new java.sql.Timestamp(Util.getCurrentTime()));
                    ps.setInt(5, 2);
                    return ps;
                }
            }, keyHolder);
        }
        long instPkId = keyHolder.getKey().longValue();
        String[] args = getArgs(jobId,vDate);
        String cmdStr = Util.hidePassword(Arrays.toString(args));
        logger.info("running [job_id={},inst_id={},data_date={},params={}]",jobId,instPkId,vDate, cmdStr);
        try {
            Executor executor = new Executor();
            executor.exec(args,getEnvp());
            logger.info("finished [job_id={},inst_id={},data_date={},params={},ret={}]",jobId,instPkId,vDate,cmdStr,executor.getCode());
            after(executor,instPkId,jobId,vDate);
        } catch (IOException e) {
            e.printStackTrace();
            afterError(instPkId, e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
            afterError(instPkId, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            afterError(instPkId, e.getMessage());
        } finally {
            JobReadWriterLock.getInstance().unlock(jobId, vDate);
        }

    }

    /**
     * 获取运行运行的参数
     * @param jobId 运行作业ID
     * @param vDate 运行数据日期
     * @return
     */
    public abstract String[] getArgs(int jobId,String vDate);

    public String[] getEnvp() {
        return null;
    };


    /**
     * 作业运行之后，需要执行的任务
     */
    public void after(Executor retExecutor, long instId, int jobId,String vDate) {
        if (retExecutor.getCode() == 0) {
            //成功
            afterSucc(instId, retExecutor.getStdoutResult() + "\n" + retExecutor.getErrorResult(),jobId,vDate);
        } else {
            // 失败
            afterError(instId, retExecutor.getStdoutResult() + "\n" + retExecutor.getErrorResult());
        }
    }

    public void afterError(long instId, String ret) {
        insertLog(instId, ret);
        updateJobStatus(instId,0);
        jdbcTemplate.update("insert into t_err_inst (inst_id,status) values(?,?)",instId,0);
    }

    public void afterSucc(long instId, String ret,int jobId,String vDate) {
        insertLog(instId, ret);
        updateJobStatus(instId,1);
        triggerEvent(jobId,instId,vDate);
    }

    /**
     * 插入作业运行日志
     * @param instId
     * @param ret
     */
    private void insertLog(long instId, String ret) {
        if (!StringUtils.isEmpty(ret)) {
            synchronized (LOCK) {
                ret = ret.length() > 4096 ? ret.substring(ret.length() - 4096) : ret;
                // 插入执行日志
                jdbcTemplate.update("insert into t_inst_log (inst_id,job_log) values(?,?)", instId, ret);
            }
        }
    }

    /**
     * 更新作业状态
     * @param instId
     * @param status
     */
    private void updateJobStatus(long instId, int status) {
        //更新状态为失败
        jdbcTemplate.update("UPDATE t_job_inst SET end_time=?, status=? WHERE inst_id=?",new java.sql.Timestamp(Util.getCurrentTime()),status,instId);
    }

    /**
     * 触发事件
     * @param jobId
     * @param instId
     * @param vDate
     */
    private synchronized void triggerEvent(int jobId,long instId, String vDate) {

        try {
            // 触发普通依赖
            jdbcTemplate.update("insert into t_job_event (job_id,inst_id,data_date,ref_job_id,ref_type) " +
                    "select a.job_id,?,?,a.ref_job_id,1 " +
                    "  from t_job_ref a " +
                    "  join t_job_def b on a.job_id=b.job_id " +
                    " where a.ref_job_id=? " +
                    "   AND a.ref_type=1" +
                    "   AND b.job_valid=1", instId, vDate, jobId);

            // 触发2,3,4类型依赖
            logger.info("starting generate job_ref_cnt...");
            List<Map<String,Object>> events = jdbcTemplate.queryForList("select a.job_id,a.ref_type,a.ref_job_id " +
                    "  from t_job_ref a " +
                    "  join t_job_def b on a.job_id=b.job_id " +
                    " where a.ref_job_id=? " +
                    "   AND a.ref_type!=1" +
                    "   AND b.job_valid=1", jobId);
            for (Map<String, Object> event: events) {
                String triggerId = event.get("job_id").toString();         // 被触发的作业ID
                String refJobId = event.get("ref_job_id").toString();     // 触发后续依赖的作业ID
                String refType = event.get("ref_type").toString();
                Cycle trigger = getCycle(Integer.parseInt(triggerId));
                Cycle refJob = getCycle(Integer.parseInt(refJobId));
                if ("2".equals(refType)) {
                    // 日作业依赖小时作业,被依赖的作业必须是日作业，依赖的作业必须是小时作业,并且小时作业的运行周期要能被24整除
                    if (trigger.getCycleUnit() == 1 && refJob.getCycleUnit()== 0 && 24 % refJob.getJobCycle() ==0) {
                        generateRefCnt(triggerId,refJobId,vDate.substring(0,8),refType,24,refJob.getJobCycle());
                    }
                } else if ("3".equals(refType)) {
                    // 月作业依赖日作业，被依赖的作业必须是月作业，依赖的作业必须是日作业，并且依赖的日作业的运行周期必须是1
                    if (trigger.getCycleUnit()== 2 && refJob.getCycleUnit() == 1 && refJob.getJobCycle() ==1) {
                        int totCnt = Util.getDaysOfMonth(vDate);
                        if (totCnt == -1) {
                            logger.warn("vDate={}, getDaysOfMonth error", vDate);
                        } else {
                            generateRefCnt(triggerId, refJobId, vDate.substring(0, 6), refType, totCnt, 1);
                        }
                    }
                } else if ("4".equals(refType)) {
                    // 年作业依赖月作业，被依赖的作业必须是年作业，依赖的作业必须是月作业，并且依赖的月作业运行周期必须是1
                    if (trigger.getCycleUnit() == 3 && refJob.getCycleUnit() == 2 && refJob.getJobCycle() == 1) {
                        generateRefCnt(triggerId, refJobId, vDate.substring(0, 4), refType, 12, 1);
                    }
                }
            }
            logger.info("finished generate job_ref_cnt...");

            // 对完成的计数依赖插入到事件中
            logger.info("insert into t_job_event from t_job_ref_cnt where tot_cnt = succ_cnt");
            jdbcTemplate.update("insert into t_job_event (job_id,inst_id,data_date,ref_job_id,ref_type) " +
                    " select job_id, ?,data_date,ref_job_id,ref_type from t_job_ref_cnt where tot_cnt = succ_cnt",instId);
            logger.info("delete from t_job_ref_cnt where tot_cnt = succ_cnt");
            jdbcTemplate.update("delete from t_job_ref_cnt where tot_cnt = succ_cnt");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成非直接依赖的依赖的计数
     * @param triggerId  作业ID
     * @param refJobId   依赖作业ID
     * @param vDate       数据日期
     * @param refType    依赖类型
     * @param totCnt     依赖需要完成的任务数
     * @param jobCycle   依赖作业的运行周期
     */
    private void generateRefCnt(String triggerId, String refJobId, String vDate, String refType,int totCnt, int jobCycle) {
        int eventCnt = jdbcTemplate.queryForObject
                ("select count(*) from t_job_ref_cnt where job_id=? and data_date=? and ref_job_id =? and ref_type=?",
                        new Object[]{triggerId, vDate,refJobId, refType},Integer.class);
        if (eventCnt == 0 ) {
            // 如果不存在，则新增
            jdbcTemplate.update("insert into t_job_ref_cnt(job_id, ref_job_id, ref_type,data_date, tot_cnt, succ_cnt)" +
                    "values(?,?,2,?,?,?)",triggerId,refJobId,vDate,totCnt,jobCycle);
        } else {
            // 否则更新，对完成的计数+ 依赖作业周期数
            jdbcTemplate.update("update t_job_ref_cnt set succ_cnt=succ_cnt + ? where job_id=?" +
                    " and ref_job_id=? and data_date=? and ref_type=?",jobCycle,triggerId,refJobId,vDate,refType);
        }
    }

    /**
     * 初始化参数
     * @param jobCycle
     * @param cycleUnit
     * @param vDate
     * @return
     */
    public Map<String,String> getParams(int jobCycle, int cycleUnit, String vDate) {
        Map<String,String> params = new HashMap<String, String>();
        params.put("BIPROG_ROOT",((Environment) SpringContextUtil.getBean(Environment.class)).getProperty("prog_root"));
        params.put("vYear", vDate.substring(0,4));
        params.put("vDataDate",vDate);
        try {
            if (cycleUnit==0) {
                // 如果运行周期是小时，则增加小时参数
                params.put("vDataHour", vDate.substring(0, 10));
                params.put("startHr",vDate);

                for(int i=1; i<24; i++) {
                    params.put("vPre" + i + "Hour", Util.dateIns(vDate.substring(0,10),-i,0));
                    params.put("vNext" + i + "Hour", Util.dateIns(vDate.substring(0,10),i,0));
                }

            }
            if (cycleUnit == 0 || cycleUnit == 1) {
                params.put("vDataDate",vDate.substring(0,8));
                params.put("startDt", vDate.substring(0,8));
                for (int i=1;i<10; i++) {
                    params.put("vPre" +i+"Date", Util.dateIns(vDate.substring(0,8),-i,1));
                    params.put("vNext" +i+"Date", Util.dateIns(vDate.substring(0,8),-i,1));
                }
            }

            if (cycleUnit == 0 || cycleUnit ==1 || cycleUnit == 2) {
                params.put("vMonth",vDate.substring(0,6));
                params.put("startMon", vDate.substring(0,6));
                for (int i=1;i<12; i++) {
                    params.put("vPre" +i+"Month", Util.dateIns(vDate.substring(0,6),-i,2));
                    params.put("vNext" +i+"Month", Util.dateIns(vDate.substring(0,6),-i,2));
                }

            }
            String endHr = Util.dateIns(vDate,jobCycle-1,cycleUnit);
            if (cycleUnit == 0 ) {
                params.put("endHr", endHr);
            }
            if (cycleUnit == 0 || cycleUnit == 1) {
                params.put("endDt", endHr.substring(0, 8));
            }
            if (cycleUnit ==0 || cycleUnit == 1 || cycleUnit == 2) {
                params.put("endMon", endHr.substring(0, 6));
            }
        } catch (ParseException e) {
            logger.error("getParams error: {}", e.getMessage());
        }
        return params;
    }
}
