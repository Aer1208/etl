package com.mongohua.etl.model;

/**
 * 作业依赖实体类
 * @author xiaohf
 */
public class JobRef {
    /**
     * 作业ID
     */
    private int jobId;
    /**
     * 依赖作业ID
     */
    private int refJobId;
    /**
     * 作业依赖类型
     */
    private int refType;

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
}
