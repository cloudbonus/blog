package com.github.blog.controller;

import com.github.blog.controller.dto.common.UserInfoDto;
import com.github.blog.service.UserInfoService;
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
@RequestMapping("user-details")
@RequiredArgsConstructor
public class UserInfoController {
    private final UserInfoService userInfoService;

    @PostMapping
    public UserInfoDto create(@RequestBody UserInfoDto userDetails) {
        return userInfoService.create(userDetails);
    }

    @GetMapping("{id}")
    public UserInfoDto findById(@PathVariable("id") Long id) {
        return userInfoService.findById(id);
    }

    @GetMapping
    public List<UserInfoDto> findAll() {
        return userInfoService.findAll();
    }

    @PutMapping("{id}")
    public UserInfoDto update(@PathVariable("id") Long id, @RequestBody UserInfoDto userDetailsDto) {
        return userInfoService.update(id, userDetailsDto);
    }

    @DeleteMapping("{id}")
    public UserInfoDto delete(@PathVariable("id") Long id) {
        return userInfoService.delete(id);
    }
}
