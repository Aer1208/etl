package com.mongohua.etl.model;

/**
 *  非普通依赖作业完成计数
 * @author xiaohaifang
 * @date 2018/9/27 11:52
 */
public class JobRefCnt {
    /**
     * 作业ID
     */
    private int jobId;
    /**
     * 依赖作业ID
     */
    private int refJobId;
    /**
     * 作业依赖类型：
     * 2：天作业依赖小时作业
     * 3：月作业依赖天作业
     * 4：年作业依赖月作业
     */
    private int refType;
    /**
     * 计数日期
     */
    private int dataDate;
    /**
     * 需要完成总计数
     */
    private int totCnt;
    /**
     * 已完成的计数
     */
    private int succCnt;

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getRefJobId() {
        return refJobId;
    }

    public void setRefJobId(int refJobId) {
        this.refJobId = refJobId;
    }

    public int getRefType() {
        return refType;
    }

    public void setRefType(int refType) {
        this.refType = refType;
    }

    public int getDataDate() {
        return dataDate;
    }

    public void setDataDate(int dataDate) {
        this.dataDate = dataDate;
    }

    public int getTotCnt() {
        return totCnt;
    }

    public void setTotCnt(int totCnt) {
        this.totCnt = totCnt;
    }

    public int getSuccCnt() {
        return succCnt;
    }

    public void setSuccCnt(int succCnt) {
        this.succCnt = succCnt;
    }
}
