package com.github.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dto.UserDetailsDto;
import com.github.blog.service.UserDetailsService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Component
public class UserDetailController {
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserDetailController(UserDetailsService userDetailsService, ObjectMapper objectMapper) {
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    public int create(UserDetailsDto userDetails) {
        return userDetailsService.create(userDetails);
    }

    public String readById(int id) {
        return convertToJson(userDetailsService.readById(id));
    }

    public String readAll() {
        List<UserDetailsDto> userDetailsDto = userDetailsService.readAll();
        return convertToJsonArray(userDetailsDto);
    }

    public UserDetailsDto update(int id, UserDetailsDto userDetailsDto) {
        return userDetailsService.update(id, userDetailsDto);
    }

    public boolean delete(int id) {
        return userDetailsService.delete(id);
    }

    @SneakyThrows
    private String convertToJson(UserDetailsDto userDto) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userDto);
    }

    @SneakyThrows
    private String convertToJsonArray(List<UserDetailsDto> users) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(users);
    }
}
