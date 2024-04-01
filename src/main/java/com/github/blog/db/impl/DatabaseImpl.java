package com.github.blog.db.impl;

import com.github.blog.db.DatabaseInterface;
import com.github.blog.util.ParametersHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseImpl implements DatabaseInterface {
    private final ParametersHolder parametersHolder;

    @Autowired
    public DatabaseImpl(ParametersHolder parametersHolder) {
        this.parametersHolder = parametersHolder;
    }

    @Override
    public String execute() {
        return parametersHolder.getSomeText();
    }
}