package com.github.blog.controller;

import com.github.blog.controller.dto.common.UserDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.RegistrationRequest;
import com.github.blog.controller.dto.request.filter.UserDtoFilter;
import com.github.blog.controller.dto.response.Page;
import com.github.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("{id}")
    public UserDto findById(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

    @GetMapping
    public Page<UserDto> findAll(UserDtoFilter requestFilter, PageableRequest pageableRequest) {
        return userService.findAll(requestFilter, pageableRequest);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('Admin') or #id == authentication.principal.id")
    public UserDto update(@PathVariable("id") @P("id") Long id, @RequestBody RegistrationRequest request) {
        return userService.update(id, request);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('Admin') or #id == authentication.principal.id")
    public UserDto delete(@PathVariable("id") @P("id") Long id) {
        return userService.delete(id);
    }
}