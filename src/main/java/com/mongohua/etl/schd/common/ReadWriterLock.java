package com.mongohua.etl.schd.common;

/**
 * 实现读写锁接口
 */
public interface ReadWriterLock {

    /**
     * 获取jobId的锁对象
     * @param jobId 作业id
     * @param vDate 作业id所对应的运行数据日期
     */
    public void lock(int jobId, String vDate) throws InterruptedException;

    /**
     * 解除jobId的锁对象
     * @param jobId 作业id
     * @param vDate 作业id锁对应的运行数据日期
     */
    public void  unlock(int jobId, String vDate) throws InterruptedException;
}
