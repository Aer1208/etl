package com.mongohua.etl.model;

import java.util.ArrayList;

/**
 * 用来保存新增作业的参数实体类
 * @author xiaohf
 */
public class JobDef2 {
    /**
     * 作业的基本信息
     */
    private JobDef jobDef;
    /**
     * 作业的参数定义
     */
    private ArrayList<JobParamDef> jobParamDefs;
    /**
     * 作业依赖关系
     */
    private ArrayList<JobRef> jobRefs;

    /**
     * 获取作业锁列表
     */
    private JobLockObj jobLockObj;


    public JobDef getJobDef() {
        return jobDef;
    }

    public void setJobDef(JobDef jobDef) {
        this.jobDef = jobDef;
    }

    public ArrayList<JobParamDef> getJobParamDefs() {
        return jobParamDefs;
    }

    public void setJobParamDefs(ArrayList<JobParamDef> jobParamDefs) {
        this.jobParamDefs = jobParamDefs;
    }

    public ArrayList<JobRef> getJobRefs() {
        return jobRefs;
    }

    public void setJobRefs(ArrayList<JobRef> jobRefs) {
        this.jobRefs = jobRefs;
    }

    public JobLockObj getJobLockObj() {
        return jobLockObj;
    }

    public void setJobLockObj(JobLockObj jobLockObj) {
        this.jobLockObj = jobLockObj;
    }
}
