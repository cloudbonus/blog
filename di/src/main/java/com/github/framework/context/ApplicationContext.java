package com.github.framework.context;

import com.github.framework.annotation.Component;
import com.github.framework.config.Config;
import com.github.framework.factory.BeanFactory;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Raman Haurylau
 */
public class ApplicationContext {
    @Setter
    private BeanFactory factory;
    private final Map<Class<?>, Object> container;
    @Getter
    private final Config config;

    public ApplicationContext(Config config) {
        this.container = new ConcurrentHashMap<>();
        this.config = config;
    }

    public <T> T getBean(Class<T> type) {
        if (container.containsKey(type)) {
            return type.cast(container.get(type));
        }

        Class<? extends T> implClass = type;

        if (type.isInterface()) {
            implClass = config.getImplClass(type);
        }

        T t = factory.createBean(implClass);

        if (implClass.isAnnotationPresent(Component.class)) {
            container.put(type, t);
        }

        return t;
    }

    public void initComponents() {
        Set<Class<?>> classSet = config.getScanner().getTypesAnnotatedWith(Component.class);

        for (Class<?> aClass : classSet) {

            Class<?>[] interfaces = aClass.getInterfaces();

            try {
                if (interfaces.length == 0) {
                    container.put(aClass, factory.createBean(aClass));
                } else {
                    for (Class<?> iface : interfaces) {
                        container.put(iface, factory.createBean(aClass));
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
