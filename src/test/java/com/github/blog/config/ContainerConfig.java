package com.github.blog.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;


/**
 * @author Raman Haurylau
 */
@TestConfiguration(proxyBeanMethods = false)
public class ContainerConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    @ServiceConnection
    public PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>("postgres:16");
    }
}