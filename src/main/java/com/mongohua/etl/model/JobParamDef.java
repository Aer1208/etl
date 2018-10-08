package com.mongohua.etl.model;

/**
 * 作业参数定义实例
 * @author xiaohf
 */
public class JobParamDef {
    /**
     * 作业ID
     */
    private int jobId;
    /**
     * 参数序号
     */
    private int paramSeq;
    /**
     * 参数ID
     */
    private String paramId;
    /**
     * 参数名称
     */
    private String paramName;
    /**
     * 参数定义
     */
    private String paramDef;
    /**
     * 参数类型 1：字符串；0：整数
     */
    private int paramType;
    /**
     * 有效标志 0：无效 1：有效
     */
    private int paramValid;

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getParamSeq() {
        return paramSeq;
    }

    public void setParamSeq(int paramSeq) {
        this.paramSeq = paramSeq;
    }

    public String getParamId() {
        return paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamDef() {
        return paramDef;
    }

    public void setParamDef(String paramDef) {
        this.paramDef = paramDef;
    }

    public int getParamType() {
        return paramType;
    }

    public void setParamType(int paramType) {
        this.paramType = paramType;
    }

    public int getParamValid() {
        return paramValid;
    }

    public void setParamValid(int paramValid) {
        this.paramValid = paramValid;
    }
}
