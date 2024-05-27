package com.github.blog.controller;

import com.github.blog.controller.dto.common.UserInfoDto;
import com.github.blog.controller.dto.request.UserInfoRequest;
import com.github.blog.controller.dto.request.etc.PageableRequest;
import com.github.blog.controller.dto.request.etc.VerificationRequest;
import com.github.blog.controller.dto.request.filter.UserInfoFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.controller.util.marker.UserValidationGroups;
import com.github.blog.service.UserInfoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
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
@Validated
@RestController
@RequestMapping("user-info")
@RequiredArgsConstructor
public class UserInfoController {
    private final UserInfoService userInfoService;

    @PostMapping
    @PreAuthorize("#request.id == authentication.principal.id")
    @Validated(UserValidationGroups.UserInfoCreateValidationGroupSequence.class)
    public UserInfoDto create(@RequestBody @P("request") @Valid UserInfoRequest request) {
        return userInfoService.create(request);
    }

    @GetMapping("{id}/cancel")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public UserInfoDto cancel(@PathVariable("id") @P("id") @Positive Long id) {
        return userInfoService.cancel(id);
    }

    @PostMapping("{id}/verify")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public UserInfoDto verify(@PathVariable("id") @P("id") @Positive Long id, @RequestBody @Valid VerificationRequest request) {
        return userInfoService.verify(id, request);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public UserInfoDto findById(@PathVariable("id") @P("id") @Positive Long id) {
        return userInfoService.findById(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<UserInfoDto> findAll(@Valid UserInfoFilterRequest filterRequest, @Valid PageableRequest pageableRequest) {
        return userInfoService.findAll(filterRequest, pageableRequest);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Validated(UserValidationGroups.UserInfoUpdateValidationGroupSequence.class)
    public UserInfoDto update(@PathVariable("id") @P("id") @Positive Long id, @RequestBody @Valid UserInfoRequest request) {
        return userInfoService.update(id, request);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserInfoDto delete(@PathVariable("id") @P("id") @Positive Long id) {
        return userInfoService.delete(id);
    }
}
