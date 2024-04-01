package com.github.blog.service.impl;

import com.github.blog.db.DatabaseInterface;
import com.github.blog.service.ServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Component
public class ServiceImpl implements ServiceInterface {
    private static final Logger LOGGER = LogManager.getLogger(ServiceImpl.class);
    private DatabaseInterface database;

    @Autowired
    public void setDatabase(DatabaseInterface database) {
        this.database = database;
    }

    @Override
    public void execute() {
        LOGGER.info(database.execute());
    }
}
