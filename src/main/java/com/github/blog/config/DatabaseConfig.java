package com.github.blog.config;

import liquibase.integration.spring.SpringLiquibase;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author Raman Haurylau
 */
@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource dataSource(DataSourceProperties dataSourceProperties) {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();

        dataSource.setUrl(dataSourceProperties.getUrl());
        dataSource.setUser(dataSourceProperties.getUsername());
        dataSource.setPassword(dataSourceProperties.getPassword());
        return dataSource;
    }

    @Bean
    public SpringLiquibase liquibase(DataSourceProperties dataSourceProperties, DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();

        liquibase.setChangeLog(dataSourceProperties.getChangelogFile());
        liquibase.setDataSource(dataSource);
        return liquibase;
    }
}
