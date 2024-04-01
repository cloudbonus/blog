package com.github.framework.config;

import lombok.Getter;
import org.reflections.Reflections;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Raman Haurylau
 */
public class JavaConfig implements Config {

    @Getter
    private final Reflections scanner;
    private final Map<Class<?>, Class<?>> ifc2ImplClass;

    public JavaConfig(Class<?> configurationClass) {
        this.scanner = new Reflections(configurationClass.getPackage().getName());
        this.ifc2ImplClass = new HashMap<>();
    }


    @Override
    public <T> Class<? extends T> getImplClass(Class<T> ifc) {
        return ifc2ImplClass.computeIfAbsent(ifc, aClass -> {
            Set<Class<? extends T>> classes = scanner.getSubTypesOf(ifc);

            if (classes.size() != 1) {
                throw new RuntimeException(ifc + " has 0 or more than one impl please update your config");
            }

            return classes.iterator().next();
        }).asSubclass(ifc);
    }

}
