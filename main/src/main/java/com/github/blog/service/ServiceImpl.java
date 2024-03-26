package com.github.blog.service;

import com.github.blog.db.DatabaseInterface;
import com.github.framework.annotation.Autowired;
import com.github.framework.annotation.Component;

@Component
public class ServiceImpl implements ServiceInterface {
    private DatabaseInterface database;

    @Autowired
    public void setDatabase(DatabaseInterface database) {
        this.database = database;
    }

    @Override
    public void execute() {
        System.out.println(database.execute());
    }
}
