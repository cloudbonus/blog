package com.github.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dto.RoleDto;
import com.github.blog.service.RoleService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Component
public class RoleController {
    private final RoleService roleService;
    private final ObjectMapper objectMapper;

    @Autowired
    public RoleController(RoleService roleService, ObjectMapper objectMapper) {
        this.roleService = roleService;
        this.objectMapper = objectMapper;
    }

    public int create(RoleDto roleDto) {
        return roleService.create(roleDto);
    }

    public String readById(int id) {
        return convertToJson(roleService.readById(id));
    }

    public String readAll() {
        List<RoleDto> roles = roleService.readAll();
        return convertToJsonArray(roles);
    }

    public RoleDto update(int id, RoleDto roleDto) {
        return roleService.update(id, roleDto);
    }

    public boolean delete(int id) {
        return roleService.delete(id);
    }

    @SneakyThrows
    private String convertToJson(RoleDto roleDto) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(roleDto);
    }

    @SneakyThrows
    private String convertToJsonArray(List<RoleDto> roles) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(roles);
    }
}

