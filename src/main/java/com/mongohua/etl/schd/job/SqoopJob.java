package com.mongohua.etl.schd.job;

import com.mongohua.etl.model.DsDef;
import com.mongohua.etl.schd.common.InitDataBase;
import com.mongohua.etl.schd.common.ParamPaser;
import com.mongohua.etl.utils.Util;
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

        String username = environment.getProperty(servIp+ "." + dbName + ".username");
        String password = environment.getProperty(servIp+ "." + dbName + ".password");
        String port =  String.valueOf(dsDef.getSrcServPort());
        if (port == null || "".equals(port)) {
            port = environment.getProperty(servIp + "." + dbName + ".port", "3306");
        }
        // add by xiaohaifang 账号密码配置改成 ip + db + username/password
        if (StringUtils.isEmpty(username)) {
            username = environment.getProperty(servIp + ".username");
        }

        if(StringUtils.isEmpty(password)) {
            password = environment.getProperty(servIp + ".password");
        }

        String dbType = dsDef.getSrcDbType();
        String jdbcStr = String.format("jdbc:%s://%s:%s/%s?tinyInt1isBit=false",dbType.toLowerCase(),servIp,port,dbName);

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

}
