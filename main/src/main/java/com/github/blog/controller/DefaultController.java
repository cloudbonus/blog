package com.github.blog.controller;

import com.github.blog.service.ServiceInterface;
import com.github.framework.annotation.Autowired;
import com.github.framework.annotation.Component;

@Component
public class DefaultController {
    @Autowired
    private ServiceInterface service;

    public void execute() {
        service.execute();
    }
}