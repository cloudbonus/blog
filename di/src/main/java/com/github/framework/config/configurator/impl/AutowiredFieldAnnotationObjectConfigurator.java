package com.github.framework.config.configurator.impl;

import com.github.framework.annotation.Autowired;
import com.github.framework.config.configurator.ObjectConfigurator;
import com.github.framework.context.ApplicationContext;
import lombok.SneakyThrows;

import java.lang.reflect.Field;

/**
 * @author Raman Haurylau
 */
public class AutowiredFieldAnnotationObjectConfigurator implements ObjectConfigurator {
    @SneakyThrows
    public void configure(Object t, ApplicationContext context){
        for (Field field : t.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                field.setAccessible(true);
                Object object = context.getBean(field.getType());
                field.set(t, object);
            }
        }
    }
}
