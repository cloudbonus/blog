package com.github.blog.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author Raman Haurylau
 */
@EnableWebMvc
@Configuration
@ComponentScan(basePackages = "com.github.blog")
@PropertySource(value = "classpath:application.properties", encoding = "UTF-8")
public class AppConfig {
}