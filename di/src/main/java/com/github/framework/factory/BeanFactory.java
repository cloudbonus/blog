package com.github.framework.factory;

import com.github.framework.AnnotationConfigApplicationContext;
import com.github.framework.config.configurator.ClassConfigurator;
import com.github.framework.config.configurator.ObjectConfigurator;
import com.github.framework.context.ApplicationContext;
import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Raman Haurylau
 */
public class BeanFactory {
    private final ApplicationContext context;
    private final List<ObjectConfigurator> objectConfigurators = new ArrayList<>();
    private final ClassConfigurator classConfigurator;

    @SneakyThrows
    public BeanFactory(ApplicationContext context) {
        this.context = context;

        Reflections reflections = new Reflections(AnnotationConfigApplicationContext.class.getPackage().getName());

        for (Class<? extends ObjectConfigurator> aClass : reflections.getSubTypesOf(ObjectConfigurator.class)) {
            objectConfigurators.add(aClass.getDeclaredConstructor().newInstance());
        }

        classConfigurator = reflections.getSubTypesOf(ClassConfigurator.class).iterator().next().getDeclaredConstructor().newInstance();
    }


    public <T> T createBean(Class<T> implClass) {
        T t = create(implClass);

        configure(t);

        return t;
    }

    private <T> void configure(T t) {
        objectConfigurators.forEach(objectConfigurator -> objectConfigurator.configure(t, context));
    }

    @SneakyThrows
    private <T> T create(Class<T> implClass) {
        return classConfigurator.configure(implClass, context);
    }
}
