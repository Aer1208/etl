package com.mongohua.etl.schd.job;

/**
 * 作业接口
 * @author xiaohf
 */
public interface Job {

    /**
     * 运行一个作业
     * @param jobId
     * @param vDate
     */
    public void run(final int jobId, final  String vDate) ;
}
