package com.github.blog.controller;

import com.github.blog.controller.mapper.JsonMapper;
import com.github.blog.dto.UserDetailDto;
import com.github.blog.dto.UserDto;
import com.github.blog.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Controller
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final JsonMapper jsonMapper;

    public String create(UserDto userDto) {
        return jsonMapper.convertToJson(userService.create(userDto));
    }

    public String findById(Long id) {
        return jsonMapper.convertToJson(userService.findById(id));
    }

    public String findAll() {
        List<UserDto> users = userService.findAll();
        return jsonMapper.convertToJson(users);
    }

    public String findAllByUniversity(UserDetailDto userDetailsDto) {
        List<UserDto> users = userService.findAllByUniversity(userDetailsDto);
        return jsonMapper.convertToJson(users);
    }

    public String findAllByRole(String role) {
        List<UserDto> users = userService.findAllByRole(role);
        return jsonMapper.convertToJson(users);
    }

    public String findAllByJobTitle(UserDetailDto userDetailsDto) {
        List<UserDto> users = userService.findAllByJobTitle(userDetailsDto);
        return jsonMapper.convertToJson(users);
    }

    public String findByLogin(UserDto userDto) {
        UserDto user = userService.findByLogin(userDto);
        return jsonMapper.convertToJson(user);
    }

    public String update(Long id, UserDto userDto) {
        return jsonMapper.convertToJson(userService.update(id, userDto));
    }

    public void delete(Long id) {
        userService.delete(id);
    }
}