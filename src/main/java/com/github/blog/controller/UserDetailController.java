package com.github.blog.controller;

import com.github.blog.dto.common.UserDetailDto;
import com.github.blog.service.UserDetailService;
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
public class UserDetailController {
    private final UserDetailService userDetailService;

    @PostMapping
    public UserDetailDto create(@RequestBody UserDetailDto userDetails) {
        return userDetailService.create(userDetails);
    }

    @GetMapping("{id}")
    public UserDetailDto findById(@PathVariable("id") Long id) {
        return userDetailService.findById(id);
    }

    @GetMapping
    public List<UserDetailDto> findAll() {
        return userDetailService.findAll();
    }

    @PutMapping("{id}")
    public UserDetailDto update(@PathVariable("id") Long id, @RequestBody UserDetailDto userDetailsDto) {
        return userDetailService.update(id, userDetailsDto);
    }

    @DeleteMapping("{id}")
    public UserDetailDto delete(@PathVariable("id") Long id) {
        return userDetailService.delete(id);
    }
}
