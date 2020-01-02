package com.mongohua.etl.model;

/**
 * Host实体类
 * @author  xiaohf
 */
public class Host {
    /**
     * 服务器ip
     */
    private String hostIp;

    /**
     * 服务器名称
     */
    private String hostName;

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
}
