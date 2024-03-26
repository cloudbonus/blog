package com.github.framework.config;

import java.util.HashMap;
import java.util.Map;

public class InjectorImpl implements Injector{

    private Map<Class<?>, Class<?>> injectables = new HashMap<>();

    public  <T> void registerInjectable(Class<?> baseClass, Class<?> subClass) {
        injectables.put(baseClass, subClass.asSubclass(baseClass));
    }

    @Override
    public <T> Class<? extends T> getInjectable(Class<T> type) {
        Class<?> injectable = injectables.get(type);

        if (injectable == null) {
            throw new IllegalArgumentException("No injectable for type " + type);
        }

        return injectable.asSubclass(type);
    }
}
