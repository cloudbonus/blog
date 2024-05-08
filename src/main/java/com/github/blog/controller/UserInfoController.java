package com.github.blog.controller;

import com.github.blog.controller.dto.common.UserInfoDto;
import com.github.blog.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
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
@RequestMapping("user-info")
@RequiredArgsConstructor
public class UserInfoController {
    private final UserInfoService userInfoService;

    @PostMapping
    @PreAuthorize("#r.id == authentication.principal.id")
    public UserInfoDto create(@RequestBody @P("r") UserInfoDto request) {
        return userInfoService.create(request);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('Admin') or #id == authentication.principal.id")
    public UserInfoDto findById(@PathVariable("id") @P("id") Long id) {
        return userInfoService.findById(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserInfoDto> findAll() {
        return userInfoService.findAll();
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('Admin') or #id == authentication.principal.id")
    public UserInfoDto update(@PathVariable("id") @P("id") Long id, @RequestBody UserInfoDto request) {
        return userInfoService.update(id, request);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('Admin') or #id == authentication.principal.id")
    public UserInfoDto delete(@PathVariable("id") @P("id") Long id) {
        return userInfoService.delete(id);
    }
}
