package com.mongohua.etl;

import com.mongohua.etl.schd.common.ScheduleThread;
import com.mongohua.etl.utils.SpringContextUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 系统程序入口
 * @author xiaohf
 */
@SpringBootApplication
@MapperScan(value = "com.mongohua.etl.mapper")
@EnableScheduling
@EnableTransactionManagement
public class EtlApplication {

    public final static Logger log = LoggerFactory.getLogger(EtlApplication.class);

    public static void main(String[] args) {
        log.info("Starting application");
        ApplicationContext cxt = SpringApplication.run(EtlApplication.class,args);
        SpringContextUtil.setApplicationContext(cxt);
        Thread schThread = new Thread(cxt.getBean(ScheduleThread.class));
        schThread.setDaemon(true);
        log.info("Starting Schedule Thread");
        schThread.start();
    }

    @Value("${max_run_cnt}")
    private int maxRunCnt;

    @Bean
    public ThreadPoolTaskScheduler getThreadPoolTaskScheduler(){
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(maxRunCnt);
        threadPoolTaskScheduler.initialize();
        return threadPoolTaskScheduler;
    };
}
