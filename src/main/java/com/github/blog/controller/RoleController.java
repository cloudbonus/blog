package com.github.blog.controller;

import com.github.blog.controller.dto.common.RoleDto;
import com.github.blog.controller.dto.request.RoleRequest;
import com.github.blog.controller.dto.request.etc.PageableRequest;
import com.github.blog.controller.dto.request.filter.RoleFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.service.RoleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public RoleDto create(@RequestBody @Valid RoleRequest request) {
        return roleService.create(request);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RoleDto findById(@PathVariable("id") @Positive Long id) {
        return roleService.findById(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<RoleDto> findAll(@Valid RoleFilterRequest filterRequest, @Valid PageableRequest pageableRequest) {
        return roleService.findAll(filterRequest, pageableRequest);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RoleDto update(@PathVariable("id") @Positive Long id, @RequestBody @Valid RoleRequest request) {
        return roleService.update(id, request);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RoleDto delete(@PathVariable("id") @Positive Long id) {
        return roleService.delete(id);
    }
}

