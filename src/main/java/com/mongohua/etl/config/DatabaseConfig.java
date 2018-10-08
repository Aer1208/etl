package com.mongohua.etl.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * 数据库连接配置
 * @author xiaohf
 */
@Configuration
public class DatabaseConfig {
    @Bean(name="mysqlDB")
    @Qualifier("mysqlDB")
    @ConfigurationProperties(prefix = "spring.datasource.mysql.bi")
    public DataSource mysqlDB() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name="mysqlJdbcTemplate")
    public JdbcTemplate mysqlJdbcTemplate(@Qualifier("mysqlDB") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

   /* @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory mybatisSqlSessionFactory(@Qualifier("mysqlDB") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        return bean.getObject();
    }

    @Bean(name="sqlSessionTemplate")
    public SqlSessionTemplate mybatisSqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }*/
}
