package com.mongohua.etl.model;

import java.util.Date;

/**
 * @author xiaohaifang
 * @date 2018/9/30 9:59
 *        错误实例实体类
 */
public class ErrInst extends Cycle{
    /**
     * 实例ID
     */
    private int instId;

    /**
     * 实例状态：
     *         0：出错作业
     *         1：重调作业，一般不会有状态=1的记录存在
     */
    private int status;

    /**
     * 实例对应的作业类型
     * 1：数据源，2：作业
     */
    private int jobType;

    /**
     * 作业ID
     */
    private int jobId;

    /**
     * 作业名称
     */
    private String jobName;

    /**
     *  数据日期
     */
    private String dataDate;

    /**
     * 实例开始时间
     */
    private Date startTime;

    /**
     * 实例结束时间
     */
    private Date endTime;

    /**
     * 作业运行服务地址
     */
    private String hostName;

    public int getInstId() {
        return instId;
    }

    public void setInstId(int instId) {
        this.instId = instId;
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

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
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

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
}
