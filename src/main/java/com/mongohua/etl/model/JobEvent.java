package com.mongohua.etl.model;

/**
 * 事件实体类
 * @author xiaohf
 */
public class JobEvent {
    /**
     * 事件ID
     */
    private int eventId;
    /**
     * 作业ID
     */
    private int jobId;
    /**
     * 触发事件的实例ID，补事件为默认值
     */
    private int instId;
    /**
     * 触发事件的日期
     */
    private String dataDate;
    /**
     * 依赖作业
     */
    private int refJobId;

    /**
     * 依赖类型
     *  1：普通直接依赖
     *  2：日作业依赖小时作业
     *  3：月作业依赖日作业
     *  4：年作业依赖月作业
     */
    private int refType;

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getInstId() {
        return instId;
    }

    public void setInstId(int instId) {
        this.instId = instId;
    }

    public String getDataDate() {
        return dataDate;
    }

    public void setDataDate(String dataDate) {
        this.dataDate = dataDate;
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
}
