package com.github.blog.controller;

import com.github.blog.dto.common.UserDto;
import com.github.blog.dto.filter.UserFilter;
import com.github.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto create(@RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @GetMapping("{id}")
    public UserDto findById(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

    @GetMapping
    public List<UserDto> findAll(UserFilter filter) {
        return userService.findAll(filter);
    }

    @PutMapping("{id}")
    public UserDto update(@PathVariable("id") Long id, @RequestBody UserDto userDto) {
        return userService.update(id, userDto);
    }

    @DeleteMapping("{id}")
    public UserDto delete(@PathVariable("id") Long id) {
        return userService.delete(id);
    }
}