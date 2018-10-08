package com.mongohua.etl.model;

/**
 * @author xiaohaifang
 * @date 2018/9/26 15:36
 * 周期性的模型对象
 */
public class Cycle {
    /**
     * 定时任务描述
     */
    private String cronDesc;

    /**
     * 数据源运行周期， 默认等于1
     */
    private int jobCycle;

    /**
     * 运行周期单位，默认 1
     * 0:时 1：天 2：月 3：年
     */
    private int cycleUnit;


    public int getJobCycle() {
        return jobCycle;
    }

    public void setJobCycle(int jobCycle) {
        this.jobCycle = jobCycle;
    }

    public int getCycleUnit() {
        return cycleUnit;
    }

    public void setCycleUnit(int cycleUnit) {
        this.cycleUnit = cycleUnit;
    }
    public String getCronDesc() {
        return cronDesc;
    }

    public void setCronDesc(String cronDesc) {
        this.cronDesc = cronDesc;
    }
}
