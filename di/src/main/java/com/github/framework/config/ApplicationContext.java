package com.github.framework.config;

import com.github.framework.annotation.Autowired;
import com.github.framework.annotation.Component;
import com.github.framework.annotation.Value;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApplicationContext {
    private InjectorImpl injector;
    private Map<Class<?>, Object> container;
    private Properties properties;
    private String basePackage;
    private final String propertiesFilePath;

    private ApplicationContext(Class<?> configurationClass, String propertiesFilePath) {
        this.propertiesFilePath = propertiesFilePath;
        initializeApplicationContext(configurationClass);
    }

    private ApplicationContext(Class<?> configurationClass) {
        this.propertiesFilePath = "";
        initializeApplicationContext(configurationClass);
    }

    private void initializeApplicationContext(Class<?> configurationClass) {
        this.basePackage = configurationClass.getPackage().getName();
        this.container = new ConcurrentHashMap<>();
        this.injector = new InjectorImpl();
        this.properties = new Properties();

        injectComponents();
    }

    public static ApplicationContext createApplicationContext(Class<?> configurationClass) {
        return new ApplicationContext(configurationClass);
    }

    public static ApplicationContext createApplicationContext(Class<?> configurationClass, String propertiesFilePath) {
        return new ApplicationContext(configurationClass, propertiesFilePath);
    }

    private void loadProperties() {
        String filePath = this.propertiesFilePath.isEmpty() ? "application.properties" : this.propertiesFilePath;
        try (InputStream stream = getInputStream(filePath)) {
            if (stream != null) {
                properties.load(new InputStreamReader(stream, StandardCharsets.UTF_8));
            } else {
                throw new RuntimeException("Could not load properties file.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private InputStream getInputStream(String filePath) throws IOException {
        try {
            return new FileInputStream(filePath);
        } catch (IOException e) {
            return ApplicationContext.class.getClassLoader().getResourceAsStream("application.properties");
        }
    }

    private void injectComponents() {
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .setUrls(ClasspathHelper.forPackage(basePackage))
                        .setScanners(Scanners.TypesAnnotated, Scanners.FieldsAnnotated));

        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(Component.class);
        Set<Field> fieldSet = reflections.getFieldsAnnotatedWith(Value.class);

        if (!fieldSet.isEmpty()) {
            loadProperties();
        }

        registerInjectables(classSet);
        injectComponentsIntoContainer(classSet);
    }

    private void registerInjectables(Set<Class<?>> classSet) {
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

    private void injectComponentsIntoContainer(Set<Class<?>> classSet) {
        for (Class<?> clazz : classSet) {
            try {
                container.put(clazz, inject(clazz));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private <T> T inject(Class<T> classToInjectTo) throws Exception {
        T instance = injectViaConstructor(classToInjectTo);
        injectViaSetters(instance, classToInjectTo);
        injectViaFields(instance, classToInjectTo);

        return instance;
    }

    private <T> T injectViaConstructor(Class<T> classToInjectTo) throws Exception {
        T instance = null;

        for (Constructor<?> constructor : classToInjectTo.getConstructors()) {
            if (constructor.isAnnotationPresent(Autowired.class)) {
                instance = injectConstructor(constructor, classToInjectTo);
            }
        }

        return instance != null ? instance : classToInjectTo.getConstructor().newInstance();
    }

    private <T> T injectConstructor(Constructor<?> constructor, Class<T> classToInjectTo) throws Exception {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] dependencies = new Object[parameterTypes.length];

        int i = 0;

        for (Class<?> parameterType : parameterTypes) {
            dependencies[i] = inject(injector.getInjectable(parameterType));
            i++;
        }

        return classToInjectTo.getConstructor(parameterTypes).newInstance(dependencies);
    }

    private <T> void injectViaSetters(T instance, Class<T> classToInjectTo) throws Exception {
        for (Method method : classToInjectTo.getMethods()) {
            if (method.isAnnotationPresent(Autowired.class)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                Object[] dependencies = new Object[parameterTypes.length];
                for (int i = 0; i < parameterTypes.length; i++) {
                    dependencies[i] = inject(injector.getInjectable(parameterTypes[i]));
                }
                method.invoke(instance, dependencies);
            }
        }
    }

    private <T> void injectViaFields(T instance, Class<T> classToInjectTo) throws Exception {
        for (Field field : classToInjectTo.getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                field.setAccessible(true);
                field.set(instance, inject(injector.getInjectable(field.getType())));
            }
            if (field.isAnnotationPresent(Value.class)) {
                Value valueAnnotation = field.getAnnotation(Value.class);
                String propertyKey = valueAnnotation.value();
                Pattern r = Pattern.compile("\\$\\{(.+)\\}");
                Matcher m = r.matcher(propertyKey);
                if (m.find()) {
                    String propertyValue = properties.getProperty(m.group(1));
                    if (propertyValue != null) {
                        field.setAccessible(true);
                        field.set(instance, propertyValue);
                    } else {
                        throw new IllegalStateException("Property value not found for key " + m.group(1));
                    }
                } else {
                    throw new IllegalStateException("Property key not found in @Value annotation");
                }
            }
        }
    }

    public <T> T getBean(Class<T> type) {
        return type.cast(container.get(type));
    }
}
