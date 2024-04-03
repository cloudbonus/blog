package com.github.blog.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.controller.Controller;
import com.github.blog.service.Service;
import com.github.blog.service.impl.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * @author Raman Haurylau
 */
@Component
public class UserController implements Controller<Serializable> {
    private final Service<Serializable> userService;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserController(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    public int create(Serializable userDto) {
        return userService.create(userDto);
    }

    public String readById(int id) {
        return convertToJson(userService.readById(id));
    }

    public String readAll() {
        List<Serializable> users = userService.readAll();
        return convertToJsonArray(users);
    }

    public boolean update(int id, Serializable userDto) {
        return userService.update(id, userDto);
    }

    public boolean delete(int id) {
        return userService.delete(id);
    }

    @SneakyThrows
    private String convertToJson(Serializable userDto) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userDto);
    }

    @SneakyThrows
    private String convertToJsonArray(List<Serializable> users) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(users);
    }
}

