package com.github.framework.config;

public interface Injector {

    void registerInjectable(Class<?> baseClass, Class<?> subClass);
    <T> Class<? extends T> getInjectable(Class<T> type);

}
