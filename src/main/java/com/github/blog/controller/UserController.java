package com.github.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dto.UserDto;
import com.github.blog.service.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Component
public class UserController {
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserController(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    public int create(UserDto userDto) {
        return userService.create(userDto);
    }

    public String readById(int id) {
        return convertToJson(userService.readById(id));
    }

    public String readAll() {
        List<UserDto> users = userService.readAll();
        return convertToJsonArray(users);
    }

    public UserDto update(int id, UserDto userDto) {
        return userService.update(id, userDto);
    }

    public boolean delete(int id) {
        return userService.delete(id);
    }

    @SneakyThrows
    private String convertToJson(UserDto userDto) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userDto);
    }

    @SneakyThrows
    private String convertToJsonArray(List<UserDto> users) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(users);
    }
}

