package com.github.blog.controller;

import com.github.blog.service.ServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DefaultController {

    @Autowired
    private ServiceInterface service;

    public void execute() {
        service.execute();
    }
}