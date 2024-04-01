package com.github.framework.config.configurator.impl;

import com.github.framework.annotation.Value;
import com.github.framework.config.configurator.ObjectConfigurator;
import com.github.framework.context.ApplicationContext;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Raman Haurylau
 */
public class ValueFieldAnnotationObjectConfigurator implements ObjectConfigurator {
    private final Properties properties;

    public ValueFieldAnnotationObjectConfigurator() {
        this.properties = new Properties();

        try (InputStream stream = ValueFieldAnnotationObjectConfigurator.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (stream != null) {
                properties.load(new InputStreamReader(stream, StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not load properties file.");
        }
    }
    @SneakyThrows
    public void configure(Object t, ApplicationContext context)  {
        for (Field field : t.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Value.class)) {
                Value valueAnnotation = field.getAnnotation(Value.class);
                String propertyKey = valueAnnotation.value();
                Pattern r = Pattern.compile("\\$\\{(.+)\\}");
                Matcher m = r.matcher(propertyKey);
                if (m.find()) {
                    String propertyValue = properties.getProperty(m.group(1));
                    if (propertyValue != null) {
                        field.setAccessible(true);
                        field.set(t, propertyValue);
                    } else {
                        throw new IllegalStateException("Property value not found for key " + m.group(1));
                    }
                } else {
                    throw new IllegalStateException("Property key not found in @Value annotation");
                }
            }
        }
    }

}
