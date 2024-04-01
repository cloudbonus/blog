package com.github.framework.config.configurator;

import com.github.framework.context.ApplicationContext;

/**
 * @author Raman Haurylau
 */
public interface ObjectConfigurator {
    void configure(Object t, ApplicationContext context);
}
