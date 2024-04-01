package com.github.blog;

import com.github.blog.controller.DefaultController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value="classpath:application.properties", encoding="UTF-8")
@ComponentScan("com.github.blog")
public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
        DefaultController defaultController = context.getBean(DefaultController.class);
        defaultController.execute();
    }
}