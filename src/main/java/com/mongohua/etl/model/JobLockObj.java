package com.mongohua.etl.model;

/**
 * 作业锁对象
 * @author xiaohf
 */
public class JobLockObj {

    /**
     * 作业ID
     */
    private int jobId;
    /**
     * 作业锁对象
     */
    private String lockObj;
    /**
     * 作业锁类型 1：写锁 0：读锁
     */
    private int lockType;

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getLockObj() {
        return lockObj;
    }

    public void setLockObj(String lockObj) {
        this.lockObj = lockObj;
    }

    public int getLockType() {
        return lockType;
    }

    public void setLockType(int lockType) {
        this.lockType = lockType;
    }
}
