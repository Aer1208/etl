package com.mongohua.etl.schd.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 保存数据库连接信息的类
 * @author xiaohf
 */
public class InitJdbc {

    private final static Logger logger = LoggerFactory.getLogger(InitJdbc.class);

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    /**
     * 激活数据库连接
     * @Scheduled(cron = "0 0 * /6 * * *")
     */
    public void activeJdbc() {
        logger.info("active database connection for job");
        jdbcTemplate.queryForList("select 1");
    }
}
