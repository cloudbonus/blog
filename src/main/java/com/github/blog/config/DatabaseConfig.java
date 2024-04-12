package com.github.blog.config;

import liquibase.integration.spring.SpringLiquibase;
import lombok.RequiredArgsConstructor;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author Raman Haurylau
 */
@Configuration
@RequiredArgsConstructor
public class DatabaseConfig {

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public DataSource dataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();

        dataSource.setUrl(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public SpringLiquibase liquibase(@Value("${com.github.blog.changelogFile}") String changelogFile) {
        SpringLiquibase liquibase = new SpringLiquibase();

        liquibase.setChangeLog(changelogFile);
        liquibase.setDataSource(dataSource());
        return liquibase;
    }
}
