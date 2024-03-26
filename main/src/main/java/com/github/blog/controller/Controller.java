package com.github.blog.controller;

import com.github.blog.service.ServiceInterface;
import com.github.framework.annotation.Autowired;
import com.github.framework.annotation.Component;

@Component
public class Controller {
    @Autowired
    private ServiceInterface service;

    public void executeService() {
        service.execute();
    }
}