package com.github.framework.config;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InjectorImpl implements Injector {

    private final ConcurrentMap<Class<?>, Class<?>> injectables = new ConcurrentHashMap<>();

    public void registerInjectable(Class<?> baseClass, Class<?> subClass) {
        if (injectables.containsKey(baseClass)) {
            throw new RuntimeException("Multiple implementations found: " + baseClass.getName());
        }
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
