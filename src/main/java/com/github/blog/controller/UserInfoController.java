package com.github.blog.controller;

import com.github.blog.controller.dto.common.UserInfoDto;
import com.github.blog.service.UserInfoService;
import com.github.blog.service.exception.UserErrorResult;
import com.github.blog.service.exception.impl.UserException;
import com.github.blog.service.security.impl.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public UserInfoDto create(@RequestBody UserInfoDto request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (!userDetails.getId().equals(request.getId())) {
            throw new UserException(UserErrorResult.UNAUTHORIZED_CREATE_ATTEMPT);
        }
        return userInfoService.create(request);
    }

    @GetMapping("{id}")
    public UserInfoDto findById(@PathVariable("id") Long id, HttpServletRequest HttpRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (!HttpRequest.isUserInRole("ADMIN") && !userDetails.getId().equals(id)) {
            throw new UserException(UserErrorResult.UNAUTHORIZED_READ_ATTEMPT);
        }
        return userInfoService.findById(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserInfoDto> findAll() {
        return userInfoService.findAll();
    }

    @PutMapping("{id}")
    public UserInfoDto update(@PathVariable("id") Long id, @RequestBody UserInfoDto request, HttpServletRequest HttpRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (!HttpRequest.isUserInRole("ADMIN") && !userDetails.getId().equals(id)) {
            throw new UserException(UserErrorResult.UNAUTHORIZED_UPDATE_ATTEMPT);
        }
        return userInfoService.update(id, request);
    }

    @DeleteMapping("{id}")
    public UserInfoDto delete(@PathVariable("id") Long id, HttpServletRequest HttpRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (!HttpRequest.isUserInRole("ADMIN") && !userDetails.getId().equals(id)) {
            throw new UserException(UserErrorResult.UNAUTHORIZED_DELETION_ATTEMPT);
        }
        return userInfoService.delete(id);
    }
}
