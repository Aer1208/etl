package com.mongohua.etl.model;

/**
 * 作业定义实体表
 * @author xiaohf
 */
public class JobDef extends Cycle{
    /**
     * 作业编号
     */
    private int jobId;
    /**
     * 作业名称
     */
    private String jobName;
    /**
     * 作业命令名称
     */
    private String cmdName;
    /**
     * 作业命令所在路径
     */
    private String cmdPath;
    /**
     * 命令类型  1：SHELL,2：java,3：perl;4:procedure
     */
    private String cmdType;
    /**
     * 作业状态 0：临时封住，能产生事件，但是不运行，1：正常状态，能产生事件和运行，2：永久封，不产生事件，不运行
     */
    private int jobValid;
    /**
     * 作业组
     */
    private String jobGroup;
    /**
     * 最大运行实例数
     */
    private String maxInstance;
    /**
     * 作业优先级
     */
    private int priorty;

    /**
     * 最后运行状态
     */
    private int lastStatus;



    /**
     * 最后运行数据日期
     */
    private String lastDataDate;

    public int getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(int lastStatus) {
        this.lastStatus = lastStatus;
    }

    public String getLastDataDate() {
        return lastDataDate;
    }

    public void setLastDataDate(String lastDataDate) {
        this.lastDataDate = lastDataDate;
    }

    public int getPriorty() {
        return priorty;
    }

    public void setPriorty(int priorty) {
        this.priorty = priorty;
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

    public String getCmdName() {
        return cmdName;
    }

    public void setCmdName(String cmdName) {
        this.cmdName = cmdName;
    }

    public String getCmdPath() {
        return cmdPath;
    }

    public void setCmdPath(String cmdPath) {
        this.cmdPath = cmdPath;
    }

    public String getCmdType() {
        return cmdType;
    }

    public void setCmdType(String cmdType) {
        this.cmdType = cmdType;
    }

    public int getJobValid() {
        return jobValid;
    }

    public void setJobValid(int jobValid) {
        this.jobValid = jobValid;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getMaxInstance() {
        return maxInstance;
    }

    public void setMaxInstance(String maxInstance) {
        this.maxInstance = maxInstance;
    }

}
