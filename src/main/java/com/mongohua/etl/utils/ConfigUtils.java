package com.mongohua.etl.utils;

import com.mongohua.etl.model.DsDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 *  数据库账号和密码统一管理
 *
 */
@Component
public class ConfigUtils {
    @Autowired
    private Environment env;

    /**
     * 读取本地配置文件的值
     * @param key
     * @return
     */
    public String getValue(String key) {
        Properties prop = new Properties();
        try {
            prop.load(new FileReader(new File(env.getProperty("prog_root") + "/conf/program.properties")));
            return prop.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取用户名
     * @param dsDef
     * @return
     */
    public String getUserName(DsDef dsDef) {
        String servIp = dsDef.getSrcServIp();
        String dbName = dsDef.getSrcDbName();
        String dbType = dsDef.getSrcDbType().toLowerCase();
        String key1 = String.format("%s.%s.%s.username",dbType, servIp, dbName);
        String key2 = String.format("%s.%s.username", dbType, servIp);
        return getString(key1, key2);
    }

    /**
     * 获取密码
     * @param dsDef
     * @return
     */
    public String getPassword(DsDef dsDef) {
        String servIp = dsDef.getSrcServIp();
        String dbName = dsDef.getSrcDbName();
        String dbType = dsDef.getSrcDbType().toLowerCase();
        String key1 = String.format("%s.%s.%s.password",dbType, servIp, dbName);
        String key2 = String.format("%s.%s.password", dbType, servIp);
        return getString(key1, key2);
    }

    /**
     * 获取端口
     * @param dsDef
     * @return
     */
    public String getPort(DsDef dsDef) {
        String servIp = dsDef.getSrcServIp();
        String dbName = dsDef.getSrcDbName();
        String dbType = dsDef.getSrcDbType().toLowerCase();
        String key1 = String.format("%s.%s.%s.port",dbType, servIp, dbName);
        String key2 = String.format("%s.%s.port", dbType, servIp);
        return getString(key1, key2);
    }

    /**
     * 获取key1或者key2的值
     * @param key1
     * @param key2
     * @return
     */
    private String getString(String key1, String key2) {
        String value = env.getProperty(key1); // 默认从application.properties文件读取
        value = StringUtils.isEmpty(value) ? env.getProperty(key2) :value;
        value = StringUtils.isEmpty(value) ? getValue(key1) : value;  // 从 $port_root/conf/program.properties读取key
        value = StringUtils.isEmpty(value) ? getValue(key2) : value;
        return value;
    }

}
