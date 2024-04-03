package com.github.blog.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.controller.Controller;
import com.github.blog.service.Service;
import com.github.blog.service.impl.UserDetailsService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * @author Raman Haurylau
 */
@Component
public class UserDetailController implements Controller<Serializable> {
    private final Service<Serializable> userDetailsService;
    private final ObjectMapper objectMapper;
    @Autowired
    public UserDetailController(UserDetailsService userDetailsService, ObjectMapper objectMapper) {
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    public int create(Serializable userDetails) {
        return userDetailsService.create(userDetails);
    }

    public String readById(int id) {
        return convertToJson(userDetailsService.readById(id));
    }

    public String readAll() {
        List<Serializable> userDetailsDto = userDetailsService.readAll();
        return convertToJsonArray(userDetailsDto);
    }

    public boolean update(int id, Serializable userDetailsDto) {
        return userDetailsService.update(id, userDetailsDto);
    }

    public boolean delete(int id) {
        return userDetailsService.delete(id);
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
