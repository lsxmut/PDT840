package com.redphase.framework.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
@Slf4j
@Order(-999)
public class DataSourceConfig implements EnvironmentAware {
    private final String DATA_SOURCE_MASTER = "spring.datasource.master.";
    private final String DATA_SOURCE_SLAVE = "spring.datasource.slave.";
    private Environment dataSourceResolver;

    @Override
    public void setEnvironment(Environment env) {
        this.dataSourceResolver = env;
    }

    @Bean(name = "masterDataSource")
    @Order(1)
    @Primary
    public DataSource masterDataSource() {
        log.info("注入 HikariDataSource..master..");
        HikariDataSource dataSource = new HikariDataSource();
        String val = dataSourceResolver.getProperty(DATA_SOURCE_MASTER + "url");
        if (val != null) {
            dataSource.setJdbcUrl(val);
        }
        val = dataSourceResolver.getProperty(DATA_SOURCE_MASTER + "username");
        if (val != null) {
            dataSource.setUsername(val);//用户名
        }
        val = dataSourceResolver.getProperty(DATA_SOURCE_MASTER + "password");
        if (val != null) {
            dataSource.setPassword(val);//密码
        }
        val = dataSourceResolver.getProperty(DATA_SOURCE_MASTER + "driver-class-name");
        if (val != null) {
            dataSource.setDriverClassName(val);
        }
        return dataSource;
    }

    @Bean(name = "slaveDataSource")
    @Order(1)
    @Qualifier("slaveDataSource")
    public DataSource slaveDataSource() {
        log.info("注入 HikariDataSource..slave..");
        HikariDataSource dataSource = new HikariDataSource();
        String val = dataSourceResolver.getProperty(DATA_SOURCE_SLAVE + "url");
        if (val != null) {
            dataSource.setJdbcUrl(val);
        }
        val = dataSourceResolver.getProperty(DATA_SOURCE_SLAVE + "username");
        if (val != null) {
            dataSource.setUsername(val);//用户名
        }
        val = dataSourceResolver.getProperty(DATA_SOURCE_SLAVE + "password");
        if (val != null) {
            dataSource.setPassword(val);//密码
        }
        val = dataSourceResolver.getProperty(DATA_SOURCE_SLAVE + "driver-class-name");
        if (val != null) {
            dataSource.setDriverClassName(val);
        }
        return dataSource;
    }
}
