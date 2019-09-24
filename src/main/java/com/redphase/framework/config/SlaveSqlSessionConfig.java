package com.redphase.framework.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.redphase.dao.slave", sqlSessionFactoryRef = "slaveSqlSessionFactory")
@Slf4j
public class SlaveSqlSessionConfig {
    @Bean
    public SqlSessionFactory slaveSqlSessionFactory(@Qualifier("slaveDataSource") DataSource dataSource) throws Exception {
        log.info("注入 sqlSessionFactory..slave..");
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        // 设置mybatis的主配置文件
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        bean.setMapperLocations(resolver.getResources("classpath:mybatis/slave/**/*.xml"));
        return bean.getObject();
    }

    @Bean
    public PlatformTransactionManager slaveTransactionManager(@Qualifier("slaveDataSource") DataSource dataSource) {
        log.info("注入 transactionManager..slave..");
        return new DataSourceTransactionManager(dataSource);
    }
}
