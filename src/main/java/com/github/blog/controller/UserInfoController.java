package com.github.blog.controller;

import com.github.blog.controller.dto.common.UserInfoDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.UserInfoRequest;
import com.github.blog.controller.dto.request.filter.UserInfoFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.controller.util.marker.UserInfoValidationGroup;
import com.github.blog.service.UserInfoService;
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
import org.springframework.web.bind.annotation.RequestParam;
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
    public UserInfoDto create(@RequestBody @Validated(UserInfoValidationGroup.onCreate.class) UserInfoRequest request) {
        return userInfoService.create(request);
    }

    @GetMapping("{id}/cancel")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public UserInfoDto cancel(@PathVariable("id") @P("id") @Positive(message = "ID must be greater than 0") Long id) {
        return userInfoService.cancel(id);
    }

    @GetMapping("{id}/verify")
    @PreAuthorize("hasRole('ADMIN') and (#roleId == 3 or #roleId == 4)")
    public UserInfoDto verify(@PathVariable("id") @Positive(message = "ID must be greater than 0") Long id, @RequestParam("roleId") @P("roleId") Long roleId) {
        return userInfoService.verify(id, roleId);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public UserInfoDto findById(@PathVariable("id") @P("id") @Positive(message = "ID must be greater than 0") Long id) {
        return userInfoService.findById(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<UserInfoDto> findAll(@Validated UserInfoFilterRequest filterRequest, @Validated PageableRequest pageableRequest) {
        return userInfoService.findAll(filterRequest, pageableRequest);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserInfoDto update(@PathVariable("id") @Positive(message = "ID must be greater than 0") Long id, @RequestBody @Validated UserInfoRequest request) {
        return userInfoService.update(id, request);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserInfoDto delete(@PathVariable("id") @Positive(message = "ID must be greater than 0") Long id) {
        return userInfoService.delete(id);
    }
}
