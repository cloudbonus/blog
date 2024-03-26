package com.github.framework.config;

public interface Injector {

    <T> Class<? extends T> getInjectable(Class<T> type);
}
