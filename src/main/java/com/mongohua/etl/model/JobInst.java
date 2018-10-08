package com.mongohua.etl.model;

import java.util.Date;

/**
 * 作业运行实例
 * @author xiaohf
 */
public class JobInst {

    /**
     * 实例ID
     */
    private int instId;
    /**
     * 作业ID
     */
    private int jobId;
    /**
     * 运行数据日期
     */
    private String dataDate;
    /**
     * 开始日期
     */
    private Date startTime;
    /**
     * 结束日期
     */
    private Date endTime;
    /**
     * 运行状态 0：错误，1：完成，2：正在运行
     */
    private int status =-1;
    /**
     * 作业类型 1：数据源，2：作业
     */
    private int jobType;
    /**
     * 作业名称
     */
    private String jobName;
    /**
     * 作业编码
     */
    private String jobCode;

    public int getInstId() {
        return instId;
    }

    public void setInstId(int instId) {
        this.instId = instId;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getDataDate() {
        return dataDate;
    }

    public void setDataDate(String dataDate) {
        this.dataDate = dataDate;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getJobType() {
        return jobType;
    }

    public void setJobType(int jobType) {
        this.jobType = jobType;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }
}
