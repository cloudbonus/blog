package com.github.blog.controller;

import com.github.blog.controller.dto.common.UserDto;
import com.github.blog.controller.dto.request.UserDtoFilter;
import com.github.blog.controller.dto.Page;
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
    public Page<UserDto> findAll(UserDtoFilter requestFilter) {
        return userService.findAll(requestFilter);
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