package com.mongohua.etl.model;

/**
 * 数据源实体类
 * @author xiaohf
 */
public class DsDef extends Cycle{
    /**
     * 数据源编号
     */
    private int dsId;
    /**
     * 数据源名称
     */
    private String dsName;
    /**
     * 数据源的表名
     */
    private String srcTabName;
    /**
     * 数据源数据库名
     */
    private String srcDbName;
    /**
     * 数据库类型 默认为MySql
     */
    private String srcDbType;
    /**
     * 源数据库的服务器IP
     */
    private String srcServIp;
    /**
     * 源数据库端口
     */
    private int srcServPort;
    /**
     * 导出目标路径 hdfs路径
     */
    private String targetPath;
    /**
     * 导出文件字段分隔符
     */
    private String fieldDel;
    /**
     * 导出数据分隔符
     */
    private String exportCols;
    /**
     * 导出数据过滤条件分隔符
     */
    private String whereExp;
    /**
     * 数据源导出优先级
     */
    private int priorty;
    /**
     * 数据源状态 0：无效 1：有效
     */
    private int dsValid;

    /**
     * 最后运行状态
     */
    private int lastStatus;
    /**
     * 最后运行数据日期
     */
    private String lastDataDate;

    /**
     * 数据源调度状态，1：启动，0：未启动
     */
    private int scheduleStatus;

    public int getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(int scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

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

    public int getSrcServPort() {
        return srcServPort;
    }

    public void setSrcServPort(int srcServPort) {
        this.srcServPort = srcServPort;
    }

    public int getDsId() {
        return dsId;
    }

    public void setDsId(int dsId) {
        this.dsId = dsId;
    }

    public String getDsName() {
        return dsName;
    }

    public void setDsName(String dsName) {
        this.dsName = dsName;
    }

    public String getSrcTabName() {
        return srcTabName;
    }

    public void setSrcTabName(String srcTabName) {
        this.srcTabName = srcTabName;
    }

    public String getSrcDbName() {
        return srcDbName;
    }

    public void setSrcDbName(String srcDbName) {
        this.srcDbName = srcDbName;
    }

    public String getSrcDbType() {
        return srcDbType;
    }

    public void setSrcDbType(String srcDbType) {
        this.srcDbType = srcDbType;
    }

    public String getSrcServIp() {
        return srcServIp;
    }

    public void setSrcServIp(String srcServIp) {
        this.srcServIp = srcServIp;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public String getFieldDel() {
        return fieldDel;
    }

    public void setFieldDel(String fieldDel) {
        this.fieldDel = fieldDel;
    }

    public String getExportCols() {
        return exportCols;
    }

    public void setExportCols(String exportCols) {
        this.exportCols = exportCols;
    }

    public String getWhereExp() {
        return whereExp;
    }

    public void setWhereExp(String whereExp) {
        this.whereExp = whereExp;
    }

    public int getPriorty() {
        return priorty;
    }

    public void setPriorty(int priorty) {
        this.priorty = priorty;
    }

    public int getDsValid() {
        return dsValid;
    }

    public void setDsValid(int dsValid) {
        this.dsValid = dsValid;
    }

}
