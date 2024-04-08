package com.github.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dto.UserDto;
import com.github.blog.service.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Controller
public class UserController {
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserController(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    public String create(UserDto userDto) {
        return convertToJson(userService.create(userDto));
    }

    public String findById(int id) {
        return convertToJson(userService.findById(id));
    }

    public String findAll() {
        List<UserDto> users = userService.findAll();
        return convertToJsonArray(users);
    }

    public String update(int id, UserDto userDto) {
        return convertToJson(userService.update(id, userDto));
    }

    public String remove(int id) {
        int result = userService.remove(id);
        if (result > 0)
            return String.format("Removed Successfully %d", result);
        else return "Could not remove";
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

