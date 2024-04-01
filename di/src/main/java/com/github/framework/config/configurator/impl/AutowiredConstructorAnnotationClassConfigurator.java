package com.github.framework.config.configurator.impl;

import com.github.framework.annotation.Autowired;
import com.github.framework.config.configurator.ClassConfigurator;
import com.github.framework.context.ApplicationContext;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;

/**
 * @author Raman Haurylau
 */
public class AutowiredConstructorAnnotationClassConfigurator implements ClassConfigurator {
    @SneakyThrows
    public <T> T configure(Class<T> implClass, ApplicationContext context) {
        T t = null;
        for (Constructor<?> constructor : implClass.getDeclaredConstructors()) {
            if (constructor.isAnnotationPresent(Autowired.class)) {
                constructor.setAccessible(true);
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                Object[] dependencies = new Object[parameterTypes.length];

                int i = 0;

                for (Class<?> parameterType : parameterTypes) {
                    dependencies[i] = context.getBean(parameterType);
                    i++;
                }

                t = implClass.getConstructor(parameterTypes).newInstance(dependencies);
                break;
            }
        }
        return t != null ? t : implClass.getConstructor().newInstance();
    }
}
