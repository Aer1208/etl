package com.mongohua.etl;

import com.mongohua.etl.mapper.JobInstMapper;
import com.mongohua.etl.model.JobInst;
import com.mongohua.etl.service.JobInstService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@org.springframework.boot.test.context.SpringBootTest
public class JobInstServiceTest {

    @Autowired
    private JobInstService jobInstService;

    @Test
    public void testInstList() {
        jobInstService.getListForPage(new JobInst(),1,40);
    }
}
