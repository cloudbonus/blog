package com.github.blog.controller;

import com.github.blog.dto.UserDetailsDto;
import com.github.blog.dto.UserDto;
import com.github.blog.service.UserService;
import com.github.blog.util.DefaultMapper;
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
    private final DefaultMapper mapper;

    public String create(UserDto userDto) {
        return mapper.convertToJson(userService.create(userDto));
    }

    public String findById(int id) {
        return mapper.convertToJson(userService.findById(id));
    }

    public String findAll() {
        List<UserDto> users = userService.findAll();
        return mapper.convertToJson(users);
    }

    public String findAllByUniversity(UserDetailsDto userDetailsDto) {
        List<UserDto> users = userService.findAllByUniversity(userDetailsDto);
        return mapper.convertToJson(users);
    }

    public String findAllByRole(String role) {
        List<UserDto> users = userService.findAllByRole(role);
        return mapper.convertToJson(users);
    }

    public String update(int id, UserDto userDto) {
        return mapper.convertToJson(userService.update(id, userDto));
    }

    public String remove(int id) {
        int result = userService.remove(id);
        if (result > 0)
            return String.format("Removed Successfully %d", result);
        else return "Could not remove";
    }
}

