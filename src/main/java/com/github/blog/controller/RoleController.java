package com.github.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dto.RoleDto;
import com.github.blog.service.RoleService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Controller
public class RoleController {
    private final RoleService roleService;
    private final ObjectMapper objectMapper;

    @Autowired
    public RoleController(RoleService roleService, ObjectMapper objectMapper) {
        this.roleService = roleService;
        this.objectMapper = objectMapper;
    }

    public String create(RoleDto roleDto) {
        return convertToJson(roleService.create(roleDto));
    }

    public String findById(int id) {
        return convertToJson(roleService.findById(id));
    }

    public String findAll() {
        List<RoleDto> roles = roleService.findAll();
        return convertToJsonArray(roles);
    }

    public String update(int id, RoleDto roleDto) {
        return convertToJson(roleService.update(id, roleDto));
    }

    public String remove(int id) {
        int result = roleService.remove(id);
        if (result > 0)
            return String.format("Removed Successfully %d", result);
        else return "Could not remove";
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

