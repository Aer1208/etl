package com.mongohua.etl.schd.common;

import com.mongohua.etl.model.*;
import com.mongohua.etl.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 初始化作业定义表，作业依赖表，作业参数表，数据源定义表，从数据库中将数据保存到内存中
 * 后面获取作业和数据源相关的信息直接从内存中获取，主要基于以下两个因素考虑
 * <li>避免频繁访问数据库，提升性能</li>
 * <li>保证正式运行作业和数据库隔离，数据库变更，如果不刷新内存，不会影响现有的作业，防止未修改完成的作业被读取到</li>
 * @author xiaohf
 */
@Service
public class InitDataBase {
    private final Logger logger = LoggerFactory.getLogger(InitDataBase.class);
    public static Map<Integer, DsDef> dsDefMap = new TreeMap<Integer, DsDef>();
    public static Map<Integer, List<JobRef>> jobRefMap = new TreeMap<Integer, List<JobRef>>();
    public static Map<Integer, List<JobRef>> refJobMap = new TreeMap<Integer, List<JobRef>>();
    public static Map<Integer, JobDef> jobDefMap = new TreeMap<Integer, JobDef>();
    public static Map<Integer, List<JobParamDef>> jobParamDefMap = new TreeMap<Integer, List<JobParamDef>>();

    public static Map<Integer, List<JobLockObj>> jobReadLock = new HashMap<Integer, List<JobLockObj>>();
    public static Map<Integer, List<JobLockObj>> jobWriterLock = new HashMap<Integer, List<JobLockObj>>();

    @Autowired
    private DsDefService dsDefService;

    @Autowired
    private JobRefServuce jobRefServuce;

    @Autowired
    private JobDefService jobDefService;

    @Autowired
    private JobParamDefService jobParamDefService;

    @Autowired
    private JobLockObjService jobLockObjService;

    public void initDsDef() {
        logger.info("刷新数据源表....");
        synchronized (dsDefMap) {
            dsDefMap = new TreeMap<Integer, DsDef>();
            // 初始化DSDEF表
            List<DsDef> dsDefList = dsDefService.getList();
            for (DsDef dsDef : dsDefList) {
                dsDefMap.put(dsDef.getDsId(), dsDef);
            }
        }
        logger.info("刷新数据源表成功");
    }

    public void initJobDef() {
        logger.info("刷新作业定义表....");
        synchronized (jobDefMap) {
            jobDefMap = new TreeMap<Integer, JobDef>();
            List<JobDef> jobDefs = jobDefService.getList();
            for (JobDef jobDef: jobDefs) {
                jobDefMap.put(jobDef.getJobId(), jobDef);
            }
        }
        logger.info("刷新作业定义完成");
    }

    public void initJobParamDef() {
        logger.info("刷新作业参数定义表....");
        synchronized (jobParamDefMap) {
            jobParamDefMap = new TreeMap<Integer, List<JobParamDef>>();
            List<JobParamDef> jobParamDefs = jobParamDefService.getList();
            for (JobParamDef jobParamDef : jobParamDefs) {
                int jobId = jobParamDef.getJobId();
                if (jobParamDefMap.containsKey(jobId)) {
                    jobParamDefMap.get(jobId).add(jobParamDef);
                } else {
                    List<JobParamDef> params = new ArrayList<JobParamDef>();
                    params.add(jobParamDef);
                    jobParamDefMap.put(jobId, params);
                }
            }
        }
        logger.info("刷新作业参数定义表完成");
    }

    public void initJobRef() {
        logger.info("刷新作业依赖表....");
        synchronized (jobRefMap) {
            jobRefMap = new TreeMap<Integer, List<JobRef>>();
            refJobMap = new TreeMap<Integer, List<JobRef>>();
            List<JobRef> jobRefs = jobRefServuce.getList();
            for (JobRef jobRef: jobRefs) {
                // 以依赖作业为主体加载
                int refJobId = jobRef.getRefJobId();
                if (refJobMap.containsKey(refJobId)) {
                    refJobMap.get(refJobId).add(jobRef);
                } else {
                    List<JobRef> refs = new ArrayList<JobRef>();
                    refs.add(jobRef);
                    refJobMap.put(refJobId, refs);
                }

                // 以被依赖作业为主体加载
                int jobId = jobRef.getJobId();
                if (jobRefMap.containsKey(jobId)) {
                    jobRefMap.get(jobId).add(jobRef);
                } else {
                    List<JobRef> refs = new ArrayList<JobRef>();
                    refs.add(jobRef);
                    jobRefMap.put(jobId, refs);
                }
            }
        }
        logger.info("刷新作业依赖表完成");
    }

    public void initJobLockObj() {
        logger.info("刷新作业锁....");
        synchronized (jobWriterLock) {
            jobWriterLock = new HashMap<Integer, List<JobLockObj>>();
            jobReadLock = new HashMap<Integer, List<JobLockObj>>();
            List<JobLockObj> jobLockObjs = jobLockObjService.getAllJobLockObjs();
            for (JobLockObj jobLockObj : jobLockObjs) {
                int jobId = jobLockObj.getJobId();
                if (jobLockObj.getLockType() >= 1) {
                    // 加载写对象锁
                    putLock(jobId, jobLockObj, jobWriterLock);
                } else if (jobLockObj.getLockType() <= 0) {
                    // 加载读对象锁
                    putLock(jobId, jobLockObj, jobReadLock);
                }
            }
        }
        logger.info("刷新作业锁完成");
    }

    /**
     * 将锁对象放入内存中
     * @param jobId
     * @param jobLockObj
     * @param locks
     */
    private void putLock(int jobId, JobLockObj jobLockObj, Map<Integer, List<JobLockObj>> locks) {
        if (locks.containsKey(jobId)) {
            locks.get(jobId).add(jobLockObj);
        } else {
            List<JobLockObj> writerLockObjs = new ArrayList<JobLockObj>();
            writerLockObjs.add(jobLockObj);
            locks.put(jobId, writerLockObjs);
        }
    }

    public void init() {

        // 初始化DSDEF表
        initDsDef();

        // 初始化作业定义表
        initJobDef();

        // 初始化作业参数序
        initJobParamDef();

        // 初始化 JOB_REF表
        initJobRef();

        // 加载读写锁对象的数据
        initJobLockObj();

    }
}
