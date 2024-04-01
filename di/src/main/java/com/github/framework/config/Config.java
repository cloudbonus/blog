package com.github.framework.config;

import org.reflections.Reflections;

/**
 * @author Raman Haurylau
 */
public interface Config {
    <T> Class<? extends T> getImplClass(Class<T> ifc);

    Reflections getScanner();
}
