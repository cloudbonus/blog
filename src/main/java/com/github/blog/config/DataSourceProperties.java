package com.github.blog.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Raman Haurylau
 */

@Getter
@Component
public class DataSourceProperties {
    private final String url;
    private final String username;
    private final String password;
    private final String driverClassName;
    private final String changelogFile;
    private final String hibernateDdlAuto;
    private final String hibernatePhysicalNamingStrategy;

    public DataSourceProperties(@Value("${spring.datasource.url}") String url,
                                @Value("${spring.datasource.username}") String username,
                                @Value("${spring.datasource.password}") String password,
                                @Value("${spring.datasource.driver-class-name}") String driverClassName,
                                @Value("${spring.jpa.hibernate.ddl-auto}") String hibernateDdlAuto,
                                @Value("${spring.jpa.hibernate.hibernate.physical_naming_strategy}") String hibernatePhysicalNamingStrategy,
                                @Value("${com.github.blog.changelogFile}") String changelogFile
    ) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.changelogFile = changelogFile;
        this.driverClassName = driverClassName;
        this.hibernateDdlAuto = hibernateDdlAuto;
        this.hibernatePhysicalNamingStrategy = hibernatePhysicalNamingStrategy;
    }
}
