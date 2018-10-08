package com.mongohua.etl.service.impl;

import com.mongohua.etl.mapper.JobLockObjMapper;
import com.mongohua.etl.mapper.JobRefMapper;
import com.mongohua.etl.model.JobRef;
import com.mongohua.etl.model.JobRef2;
import com.mongohua.etl.service.JobRefServuce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 作业依赖服务类实现
 * @author xiaohf
 */
@Service
public class JobRefServiceImpl implements JobRefServuce {

    @Autowired
    private JobRefMapper jobRefMapper;

    @Autowired
    private JobLockObjMapper jobLockObjMapper;

    @Override
    public List<JobRef> getList() {
        return jobRefMapper.getList();
    }

    @Override
    public List<JobRef> getById(int jobId) {
        return jobRefMapper.getById(jobId);
    }

    @Override
    public int add(JobRef jobRef) {
        return jobRefMapper.add(jobRef);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String update(JobRef2 jobRef2) {

        try {
            int deletedCnt = 0;
            int insertedCnt = 0;
            // 1、删除
            List<JobRef> deleteRefs = jobRef2.getDeleted();
            if (deleteRefs != null && deleteRefs.size() > 0) {
                int[] deleteJobLocks = new int[jobRef2.getDeleted().size()];
                int jobId = 0;
                for (int i = 0; i < deleteRefs.size(); i++) {
                    jobRefMapper.delete(deleteRefs.get(i));
                    deleteJobLocks[i] = deleteRefs.get(i).getRefJobId();
                    jobId = deleteRefs.get(i).getJobId();
                    deletedCnt++;
                }
                // 删除读锁
                jobLockObjMapper.deleteLock(jobId, deleteJobLocks);
            }
            // 2、新增

            List<JobRef> insertRefs = jobRef2.getInserted();
            if (insertRefs != null && insertRefs.size() > 0) {

                int[] insertJobLocks = new int[insertRefs.size()];
                int jobId = 0;
                for (int i=0; i < insertRefs.size(); i++) {
                    jobRefMapper.add(insertRefs.get(i));
                    insertJobLocks[i] = insertRefs.get(i).getRefJobId();
                    jobId = insertRefs.get(i).getJobId();
                    insertedCnt++;
                }

                // 插入锁
                jobLockObjMapper.insertLock(jobId, insertJobLocks);
            }
            return "{\"deletedCnt\":" + deletedCnt + ",\"insertedCnt\":" + insertedCnt + ",\"status\":0}";
        }catch (Exception e) {
            return "{\"ret\":\""+e.getMessage()+"\",\"status\":-1}";
        }
    }
}
