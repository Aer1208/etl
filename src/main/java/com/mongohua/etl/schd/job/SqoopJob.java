package com.mongohua.etl.schd.job;

import com.mongohua.etl.model.DsDef;
import com.mongohua.etl.schd.common.InitDataBase;
import com.mongohua.etl.schd.common.ParamPaser;
import com.mongohua.etl.utils.ConfigUtils;
import com.mongohua.etl.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据源作业运行定义类，主要是运行数据源的导出任务
 * @author xiaohf
 */
@Component
public class SqoopJob extends  AbstractJob{

    @Autowired
    private ConfigUtils configUtils;

    /**
     * 获取sqoop运行的参数
     * @return
     */
    @Override
    public String[] getArgs(int jobId, String vDate) {
        List<String> list = new ArrayList<String>();
        DsDef dsDef = InitDataBase.dsDefMap.get(jobId);
        String servIp = dsDef.getSrcServIp();
        String dbName = dsDef.getSrcDbName();

//        String username = environment.getProperty(servIp+ "." + dbName + ".username");
//        String password = environment.getProperty(servIp+ "." + dbName + ".password");
//        String port =  String.valueOf(dsDef.getSrcServPort());
//        if (port == null || "".equals(port)) {
//            port = environment.getProperty(servIp + "." + dbName + ".port", "3306");
//        }
//        // add by xiaohaifang 账号密码配置改成 ip + db + username/password
//        if (StringUtils.isEmpty(username)) {
//            username = environment.getProperty(servIp + ".username");
//        }
//
//        if(StringUtils.isEmpty(password)) {
//            password = environment.getProperty(servIp + ".password");
//        }

        // modified by xiaohaifang 20191108 修改用户名、密码、端口的获取方式。如果application.properties没有配置，改成从本地文件${prog_root}/conf/program.properties
        String username =  configUtils.getUserName(dsDef);
        String password = configUtils.getPassword(dsDef);
        String port = configUtils.getPort(dsDef);

        String dbType = dsDef.getSrcDbType();
//        String jdbcStr = String.format("jdbc:%s://%s:%s/%s?tinyInt1isBit=false",dbType.toLowerCase(),servIp,port,dbName);
        String jdbcStr = getJdbcStr(dsDef);
        list.add("/usr/bin/sqoop");
        list.add("import");
        list.add("--connect");
        list.add(jdbcStr);
        list.add("--username");
        list.add(username);
        list.add("--password");
        list.add(password);
        list.add("--table");
        list.add(dsDef.getSrcTabName());
        list.add("--target-dir");
        list.add(dsDef.getTargetPath() + "/" + vDate);
        list.add("--fields-terminated-by");
        list.add(dsDef.getFieldDel());
        list.add("--compress");
        list.add("--hive-drop-import-delims");
        // hive空值转换
        list.add("--null-string");
        list.add("\\\\N");
        list.add("--null-non-string");
        list.add("\\\\N");

        if (dsDef.getExportCols() != null && !"".equalsIgnoreCase(dsDef.getExportCols().trim())) {
            list.add("--columns");
            list.add(dsDef.getExportCols());
        }
        list.add("--where");
        if (dsDef.getWhereExp() != null && !"".equalsIgnoreCase(dsDef.getWhereExp().trim())) {
            list.add(ParamPaser.parse(dsDef.getWhereExp(),getParams(dsDef.getJobCycle(), dsDef.getCycleUnit(),vDate)));
        }else {
            list.add("1=1");
        }
        list.add("-m");
        list.add("1");
        return list.toArray(new String[0]);
    }

    /**
     * 根据ds def获取jdbc连接地址
     * @param dsDef
     * @return
     */
    private String getJdbcStr(DsDef dsDef) {
        String dbType = dsDef.getSrcDbType();
        String servIp = dsDef.getSrcServIp();
        int port = dsDef.getSrcServPort();
        String dbName = dsDef.getSrcDbName();
        String jdbcStr = String.format("jdbc:%s://%s:%s/%s",dbType.toLowerCase(),servIp,port,dbName);
        if ("oracle".equalsIgnoreCase(dbType)) {
            jdbcStr=String.format("jdbc:%s:thin:@%s:%s:%s",dbType.toLowerCase(), servIp, port, dbName);
        } else if ("mysql".equalsIgnoreCase(dbType)) {
            jdbcStr = String.format("jdbc:%s://%s:%s/%s?tinyInt1isBit=false",dbType.toLowerCase(),servIp,port,dbName);
        } else if ("db2".equalsIgnoreCase(dbType)) {
            jdbcStr = String.format("jdbc:%s://%s:%s/%s",dbType.toLowerCase(),servIp,port,dbName);
        } else if ("sqlserver".equalsIgnoreCase(dbType)) {
            jdbcStr = String.format("jdbc:microsoft:%s://%s:%s;DatabaseName=%s",dbType.toLowerCase(),servIp,port,dbName);
        } else if ("sybase".equalsIgnoreCase(dbType)) {
            jdbcStr = String.format("jdbc:%s:Tds:%s:%s/%s",dbType.toLowerCase(),servIp,port,dbName);
        }
        return jdbcStr;
    }

}
