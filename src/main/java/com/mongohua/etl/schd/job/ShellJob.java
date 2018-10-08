package com.mongohua.etl.schd.job;

import com.mongohua.etl.model.JobDef;
import com.mongohua.etl.model.JobParamDef;
import com.mongohua.etl.schd.common.InitDataBase;
import com.mongohua.etl.schd.common.ParamPaser;
import com.mongohua.etl.utils.Constant;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 运行shell脚本的作业,主要是运行作业定义表里面的作业
 * @author xiaohf
 */
@Component
public class ShellJob extends AbstractJob {

    @Override
    public String[] getArgs(int jobId,String vDate) {

        JobDef jobDef = InitDataBase.jobDefMap.get(jobId);
        String cmdName = jobDef.getCmdName();
        String cmdPath = ParamPaser.parse(jobDef.getCmdPath(),getParams(jobDef.getJobCycle(), jobDef.getCycleUnit(), vDate));
        List<JobParamDef> paramDefs = InitDataBase.jobParamDefMap.get(jobId);
        List<String> params = new ArrayList<String> ();
        if (Constant.PATH_SPLIT_STR.equals(cmdPath.substring(cmdPath.length()-1))) {
            params.add(cmdPath + cmdName);
        } else {
            params.add(cmdPath + Constant.PATH_SPLIT_STR + cmdName);
        }
        if(paramDefs != null && paramDefs.size() >0) {
            for (JobParamDef param : paramDefs) {
                params.add(ParamPaser.parse(param.getParamDef(), getParams(jobDef.getJobCycle(), jobDef.getCycleUnit(), vDate)));
            }
        }
        return params.toArray(new String[0]);
    }

    @Override
    public String[] getEnvp() {
        return new String[]{};
    }

}
