package com.mongohua.etl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author xiaohaifang
 * @date 2018/9/29 17:07
 */
@RunWith(SpringRunner.class)
@org.springframework.boot.test.context.SpringBootTest
public class SpringBootTest {

    @Autowired
    public JdbcTemplate jdbcTemplate;
    @Test
    public void test() {
        int eventCnt = jdbcTemplate.queryForObject
                ("select count(*) from t_job_ref_cnt where job_id=? and data_date=? and ref_job_id =? and ref_type=?",
                        new Object[]{"99999999", 20180929,"20000001", 2},Integer.class);
        System.out.println("run here....." + eventCnt);
        try {
            jdbcTemplate.update("update t_job_ref_cnt set succ_cnt=succ_cnt + ? where job_id=?" +
                    " and ref_job_id=? and data_date=? and ref_type=?", 1,"99999999", "20000001", 20180929, "2");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("run here...");
    }
}
