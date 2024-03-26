package com.github.framework.config;

import com.github.framework.annotation.Autowired;
import com.github.framework.annotation.Component;
import com.github.framework.annotation.Value;
import org.reflections.Reflections;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ApplicationContext {
    private static final InjectorImpl injector = new InjectorImpl();
    private static final Map<Class<?>, Object> container = new HashMap<>();
    private static final Properties properties = new Properties();

    static {
        loadProperties();
        injectComponents();
    }

    private ApplicationContext() {
    }

    public static ApplicationContext createApplicationContext() {
        return new ApplicationContext();
    }

    private static void loadProperties() {
        try (var stream = ApplicationContext.class.getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void injectComponents() {
        Reflections reflections = new Reflections("com.github.blog");
        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(Component.class);

        registerInjectables(classSet);
        injectComponentsIntoContainer(classSet);
    }

    private static void registerInjectables(Set<Class<?>> classSet) {
        for (Class<?> clazz : classSet) {
            try {
                Class<?>[] interfaces = clazz.getInterfaces();
                if (interfaces.length == 0) {
                    injector.registerInjectable(clazz, clazz);
                } else {
                    for (Class<?> iface : interfaces) {
                        injector.registerInjectable(iface, clazz);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void injectComponentsIntoContainer(Set<Class<?>> classSet) {
        for (Class<?> clazz : classSet) {
            try {
                container.put(clazz, inject(clazz));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    private static <T> T inject(Class<T> classToInjectTo) throws Exception {

        T instance = injectViaConstructor(classToInjectTo);
        injectViaSetters(instance, classToInjectTo);
        injectViaFields(instance, classToInjectTo);

        return instance;
    }

    private static <T> T injectViaConstructor(Class<T> classToInjectTo) throws Exception {
        T instance = null;

        for (Constructor<?> constructor : classToInjectTo.getConstructors()) {
            if (constructor.isAnnotationPresent(Autowired.class)) {
                instance = injectConstructor(constructor, classToInjectTo);
            }
        }

        return instance != null ? instance : classToInjectTo.getConstructor().newInstance();
    }

    private static <T> T injectConstructor(Constructor<?> constructor, Class<T> classToInjectTo) throws Exception {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] dependencies = new Object[parameterTypes.length];

        int i = 0;

        for (Class<?> parameterType : parameterTypes) {
            dependencies[i] = inject(injector.getInjectable(parameterType));
            i++;
        }

        return classToInjectTo.getConstructor(parameterTypes).newInstance(dependencies);
    }

    private static <T> void injectViaSetters(T instance, Class<T> classToInjectTo) throws Exception {
        for (Method method : classToInjectTo.getMethods()) {
            if (method.isAnnotationPresent(Autowired.class) && method.getName().startsWith("set") && method.getParameterCount() == 1) {
                method.invoke(instance, inject(injector.getInjectable(method.getParameterTypes()[0])));
            }
        }
    }

    private static <T> void injectViaFields(T instance, Class<T> classToInjectTo) throws Exception {
        for (Field field : classToInjectTo.getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                field.setAccessible(true);
                field.set(instance, inject(injector.getInjectable(field.getType())));
            }
            if (field.isAnnotationPresent(Value.class)) {
                Value valueAnnotation = field.getAnnotation(Value.class);
                String propertyKey = valueAnnotation.value();
                String propertyValue = properties.getProperty(propertyKey);
                if (propertyValue != null) {
                    field.setAccessible(true);
                    field.set(instance, propertyValue);
                }
            }
        }
    }

    public <T> T getBean(Class<T> type) {
        return type.cast(container.get(type));
    }
}
