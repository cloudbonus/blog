package com.github.blog.service;

import com.github.blog.db.DatabaseInterface;
import com.github.framework.annotation.Autowired;
import com.github.framework.annotation.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Component
public class ServiceImpl implements ServiceInterface {
    private static final Logger logger = LogManager.getLogger(ServiceImpl.class);
    private DatabaseInterface database;

    @Autowired
    public void setDatabase(DatabaseInterface database) {
        this.database = database;
    }

    @Override
    public void execute() {
        logger.info(database.execute());
    }
}
