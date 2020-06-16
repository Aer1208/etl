package com.mongohua.etl;

import com.mongohua.etl.mapper.JobLockObjMapper;
import com.mongohua.etl.model.JobLockObj;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@org.springframework.boot.test.context.SpringBootTest
public class JobLockMaperTest {

    @Autowired
    private JobLockObjMapper jobLockObjMapper;

    @Test
    public void testGetJobLockObjByLockObj() {
        JobLockObj jobLockObj = new JobLockObj();
        jobLockObj.setLockObj("test");
        jobLockObj.setLockType(1);
        List<JobLockObj> jobLockObjs = jobLockObjMapper.getJobLockObjByLockObj(jobLockObj);

        System.out.println(jobLockObjs);
    }
}
