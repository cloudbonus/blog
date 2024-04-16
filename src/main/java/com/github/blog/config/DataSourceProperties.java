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
    private final int maxPoolSize;
    private final String changelogFile;

    public DataSourceProperties(@Value("${spring.datasource.url}") String url,
                                @Value("${spring.datasource.username}") String username,
                                @Value("${spring.datasource.password}") String password,
                                @Value("${spring.datasource.max-pool-size}") String maxPoolSize,
                                @Value("${com.github.blog.changelogFile}") String changelogFile
                                ) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.maxPoolSize = Integer.parseInt(maxPoolSize);
        this.changelogFile = changelogFile;
    }
}
