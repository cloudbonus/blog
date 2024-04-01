package com.github.framework.config.configurator;

import com.github.framework.context.ApplicationContext;

/**
 * @author Raman Haurylau
 */
public interface ClassConfigurator {
    <T> T configure(Class<T> implClass, ApplicationContext context);
}
