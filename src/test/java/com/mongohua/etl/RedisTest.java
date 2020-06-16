package com.mongohua.etl;

import com.mongohua.etl.schd.common.InitDataBase;
import com.mongohua.etl.schd.common.ReadWriterLock;
import com.mongohua.etl.utils.RedisUtil;
import com.mongohua.etl.utils.SpringContextUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@org.springframework.boot.test.context.SpringBootTest
public class RedisTest {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ReadWriterLock readWriterLock;
    @Autowired
    private InitDataBase initDataBase;

    @Autowired
    private ApplicationContext cxt;

    @Test
    public void testSet() {
        redisUtil.set("test-000","sss");
    }

    @Test
    public void testGet() {
        System.out.println(redisUtil.get("test-000"));
    }

    @Test
    public void testGetAllKeys() {
        System.out.println(redisUtil.getAllKeys());
    }

    @Test
    public void testLock() {
        SpringContextUtil.setApplicationContext(cxt);
        initDataBase.init();
        try {
            readWriterLock.lock(10000001,"20191125");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUnLock() {
        SpringContextUtil.setApplicationContext(cxt);
        initDataBase.init();
        try {
            readWriterLock.unlock(10000001,"20191125");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
