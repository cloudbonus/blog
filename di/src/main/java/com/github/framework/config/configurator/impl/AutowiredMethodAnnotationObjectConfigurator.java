package com.github.framework.config.configurator.impl;

import com.github.framework.annotation.Autowired;
import com.github.framework.config.configurator.ObjectConfigurator;
import com.github.framework.context.ApplicationContext;
import lombok.SneakyThrows;

import java.lang.reflect.Method;

/**
 * @author Raman Haurylau
 */
public class AutowiredMethodAnnotationObjectConfigurator implements ObjectConfigurator {
    @SneakyThrows
    public void configure(Object t, ApplicationContext context) {
        for (Method method : t.getClass().getMethods()) {
            if (method.isAnnotationPresent(Autowired.class)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                Object[] dependencies = new Object[parameterTypes.length];
                for (int i = 0; i < parameterTypes.length; i++) {
                    dependencies[i] = context.getBean(parameterTypes[i]);
                }
                method.invoke(t, dependencies);
            }
        }
    }
}
