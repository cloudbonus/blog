package com.github.blog.config;

import com.github.blog.util.impl.DefaultDataSource;
import liquibase.integration.spring.SpringLiquibase;
import lombok.RequiredArgsConstructor;
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

    @Bean
    public DataSource dataSource() {
        return new DefaultDataSource();
    }

    @Bean
    public SpringLiquibase liquibase(@Value("${com.github.blog.changelogFile}") String changelogFile) {
        SpringLiquibase liquibase = new SpringLiquibase();

        liquibase.setChangeLog(changelogFile);
        liquibase.setDataSource(dataSource());
        return liquibase;
    }
}
