package com.github.blog;

import com.github.blog.controller.DefaultController;
import com.github.framework.AnnotationConfigApplicationContext;
import com.github.framework.context.ApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = AnnotationConfigApplicationContext.createApplicationContext(Main.class);
        DefaultController defaultController = context.getBean(DefaultController.class);
        defaultController.execute();
    }
}