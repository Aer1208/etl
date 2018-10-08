package com.mongohua.etl.model;

/**
 * 系统首页报表展现的数据实体类
 * @author xiaohf
 */
public class Echarts {
    /**
     * 作业ID
     */
    private int jobId;
    /**
     * 数据日期
     */
    private int dataDate;
    /**
     * 运行时长
     */
    private int runTime;
    /**
     * 运行作业数
     */
    private int runJobCnt;
    /**
     * 总作业数
     */
    private int totJobCnt;
    /**
     * 成功作业数
     */
    private int succJobCnt;
    /**
     * 失败作业数
     */
    private int errJobCnt;
    /**
     * 未运行作业数
     */
    private int notRunJobCnt;

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getDataDate() {
        return dataDate;
    }

    public void setDataDate(int dataDate) {
        this.dataDate = dataDate;
    }

    public int getRunTime() {
        return runTime;
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    public int getRunJobCnt() {
        return runJobCnt;
    }

    public void setRunJobCnt(int runJobCnt) {
        this.runJobCnt = runJobCnt;
    }

    public int getTotJobCnt() {
        return totJobCnt;
    }

    public void setTotJobCnt(int totJobCnt) {
        this.totJobCnt = totJobCnt;
    }

    public int getSuccJobCnt() {
        return succJobCnt;
    }

    public void setSuccJobCnt(int succJobCnt) {
        this.succJobCnt = succJobCnt;
    }

    public int getErrJobCnt() {
        return errJobCnt;
    }

    public void setErrJobCnt(int errJobCnt) {
        this.errJobCnt = errJobCnt;
    }

    public int getNotRunJobCnt() {
        return notRunJobCnt;
    }

    public void setNotRunJobCnt(int notRunJobCnt) {
        this.notRunJobCnt = notRunJobCnt;
    }
}
