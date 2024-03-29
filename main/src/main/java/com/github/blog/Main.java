package com.github.blog;

import com.github.blog.controller.Controller;
import com.github.framework.config.ApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = ApplicationContext.createApplicationContext(Main.class);
        Controller controller = context.getBean(Controller.class);
        controller.execute();
    }
}