package com.github.blog.controller;

import com.github.blog.controller.dto.common.RoleDto;
import com.github.blog.service.RoleService;
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
@RequestMapping("roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    public RoleDto create(@RequestBody RoleDto roleDto) {
        return roleService.create(roleDto);
    }

    @GetMapping("{id}")
    public RoleDto findById(@PathVariable("id") Long id) {
        return roleService.findById(id);
    }

    @GetMapping
    public List<RoleDto> findAll() {
        return roleService.findAll();
    }

    @PutMapping("{id}")
    public RoleDto update(@PathVariable("id") Long id, @RequestBody RoleDto roleDto) {
        return roleService.update(id, roleDto);
    }

    @DeleteMapping("{id}")
    public RoleDto delete(@PathVariable("id") Long id) {
        return roleService.delete(id);
    }
}

