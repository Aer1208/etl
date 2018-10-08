package com.mongohua.etl.schd.common;

import com.mongohua.etl.utils.SpringContextUtil;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ParamPaser {

    public static String parse(String paramDef, String vDate) {
        if(paramDef == null) {
            return "";
        }

        String biprogRoot = ((Environment) SpringContextUtil.getBean(Environment.class)).getProperty("prog_root");


        return paramDef.replaceAll("\\$[\\{]*vDataDate[\\}]*",vDate)
                .replaceAll("\\$[\\{]*vDate[\\}]*",vDate)
                .replaceAll("\\$[\\{]*vMonth[\\}]*",vDate.substring(0,6))
                .replaceAll("\\$[\\{]*BIPROG_ROOT[\\}]*",biprogRoot);
    }

    /**
     * 根据参数Map实例化参数定义
     * @param paramDef
     * @param params
     * @return
     */
    public static String parse(String paramDef, Map<String, String> params) {
        String result = paramDef;
        Iterator<String> keys = params.keySet().iterator();
        while(keys.hasNext()) {
            String key = keys.next();
            result = result.replaceAll("\\$[\\{]*"+key+"[\\}]*", params.get(key));
        }
        return result;
    }

    public static void main(String[] args) {
        String paramDef="date_format(now(),'%Y%m%d%H') between ${startHr} and ${endHr}";
        Map<String,String> params = new HashMap<String, String>();
        params.put("startHr","2018092515".substring(0,10));
        params.put("endHr","2018092515");
        params.put("vDate","20180925");
        System.out.println(parse(paramDef, params));
    }
}
