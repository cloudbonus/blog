package com.github.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dto.UserDetailsDto;
import com.github.blog.service.UserDetailsService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Controller
public class UserDetailController {
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserDetailController(UserDetailsService userDetailsService, ObjectMapper objectMapper) {
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    public String create(UserDetailsDto userDetails) {
        return convertToJson(userDetailsService.create(userDetails));
    }

    public String findById(int id) {
        return convertToJson(userDetailsService.findById(id));
    }

    public String findAll() {
        List<UserDetailsDto> userDetailsDto = userDetailsService.findAll();
        return convertToJsonArray(userDetailsDto);
    }

    public String update(int id, UserDetailsDto userDetailsDto) {
        return convertToJson(userDetailsService.update(id, userDetailsDto));
    }

    public String remove(int id) {
        int result = userDetailsService.remove(id);
        if (result > 0)
            return String.format("Removed Successfully %d", result);
        else return "Could not remove";
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
