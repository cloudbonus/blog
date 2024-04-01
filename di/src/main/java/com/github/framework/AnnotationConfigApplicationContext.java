package com.github.framework;

import com.github.framework.config.Config;
import com.github.framework.config.JavaConfig;
import com.github.framework.context.ApplicationContext;
import com.github.framework.factory.BeanFactory;


/**
 * @author Raman Haurylau
 */
public class AnnotationConfigApplicationContext {
    public static ApplicationContext createApplicationContext(Class<?> configurationClass) {
        Config config = new JavaConfig(configurationClass);
        ApplicationContext context = new ApplicationContext(config);
        BeanFactory beanFactory = new BeanFactory(context);
        context.setFactory(beanFactory);
        context.initComponents();
        return context;
    }
}
