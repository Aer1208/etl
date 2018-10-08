package com.mongohua.etl.service.impl;

import com.mongohua.etl.mapper.JobParamDefMapper;
import com.mongohua.etl.model.JobParamDef;
import com.mongohua.etl.model.JobParamDef2;
import com.mongohua.etl.service.JobParamDefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 参数定义服务类实现
 * @author xiaohf
 */
@Service
public class JobParamDefServiceImpl implements JobParamDefService {

    @Autowired
    private JobParamDefMapper jobParamDefMapper;

    @Override
    public List<JobParamDef> getList() {
        return jobParamDefMapper.getList();
    }

    @Override
    public List<JobParamDef> getById(int jobId) {
        return jobParamDefMapper.getById(jobId);
    }

    @Override
    public int delete(int jobId, int paramSeq) {
        return jobParamDefMapper.delete(jobId,paramSeq);
    }

    @Override
    public int add(JobParamDef jobParamDef) {
        return jobParamDefMapper.add(jobParamDef);
    }

    @Override
    @Transactional(rollbackFor =Exception.class )
    public String update(JobParamDef2 jobParamDef2) {

        try {

            int deletedCnt = 0;
            int updatedCnt = 0;
            int insertedCnt = 0;
            // 1、先提交删除
            if (jobParamDef2.getDeleted() != null && jobParamDef2.getDeleted().size() > 0) {
                for (JobParamDef delParam : jobParamDef2.getDeleted()) {
                    jobParamDefMapper.delete(delParam.getJobId(), delParam.getParamSeq());
                    deletedCnt++;
                }
            }

            // 2、 修改
            if (jobParamDef2.getUpdated() != null && jobParamDef2.getUpdated().size() > 0) {
                for (JobParamDef updParam : jobParamDef2.getUpdated()) {
                    if (jobParamDefMapper.existsParam(updParam.getJobId(), updParam.getParamSeq()) > 0) {
                        jobParamDefMapper.update(updParam);
                    } else {
                        jobParamDefMapper.add(updParam);
                    }
                    updatedCnt++;
                }
            }

            if (jobParamDef2.getInserted() != null && jobParamDef2.getInserted().size() > 0) {
                for (JobParamDef insParam : jobParamDef2.getInserted()) {
                    jobParamDefMapper.add(insParam);
                    insertedCnt++;
                }
            }

            return "{\"deletedCnt\":" + deletedCnt + ",\"updatedCnt\":" + updatedCnt + ",\"insertedCnt\":" + insertedCnt + ",\"status\":0}";
        }catch (Exception e) {
            return "{\"ret\":\"" + e.getCause().getMessage() + "\",\"status\":-1}";
        }
    }
}
