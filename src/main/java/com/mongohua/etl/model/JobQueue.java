package com.mongohua.etl.model;

/**
 * 队列实体类
 * @author xiaohf
 */
public class JobQueue {

    /**
     * 队列ID，唯一标识
     */
    private int queueId;
    /**
     * 作业ID
     */
    private int jobId;
    /**
     * 数据日期
     */
    private String dataDate;
    /**
     * 作业优先级
     */
    private int priorty;

    /**
     * 队列状态，0表示待调起，1
     */
    private int status;

    /**
     * 作业运行服务地址
     */
    private String hostName;

    public int getQueueId() {
        return queueId;
    }

    public void setQueueId(int queueId) {
        this.queueId = queueId;
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

    public int getPriorty() {
        return priorty;
    }

    public void setPriorty(int priorty) {
        this.priorty = priorty;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
}
