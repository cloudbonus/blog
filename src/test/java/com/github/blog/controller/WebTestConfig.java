package com.github.blog.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;


/**
 * @author Raman Haurylau
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.github.blog")
public class WebTestConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>("postgres:latest");
    }

    @Bean
    @Primary
    public DataSource dataSource(final PostgreSQLContainer<?> container) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(container.getDriverClassName());
        dataSource.setUrl(container.getJdbcUrl());
        dataSource.setUsername(container.getUsername());
        dataSource.setPassword(container.getPassword());
        return dataSource;
    }

//    @Bean
//    @Primary
//    UserDetailsService userDetailsService() {
//        UserDetails kvossing0 = User.builder()
//                .username("kvossing0")
//                .password("123")
//                .authorities("ROLE_USER")
//                .build();
//
//        UserDetails gmaccook1 = User.builder()
//                .username("gmaccook1")
//                .password("123")
//                .authorities("ROLE_USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(kvossing0, gmaccook1);
//    }
}