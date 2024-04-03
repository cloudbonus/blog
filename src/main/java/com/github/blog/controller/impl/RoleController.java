package com.github.blog.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.controller.Controller;
import com.github.blog.service.Service;
import com.github.blog.service.impl.RoleService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * @author Raman Haurylau
 */
@Component
public class RoleController implements Controller<Serializable> {
    private final Service<Serializable> roleService;
    private final ObjectMapper objectMapper;

    @Autowired
    public RoleController(RoleService roleService, ObjectMapper objectMapper) {
        this.roleService = roleService;
        this.objectMapper = objectMapper;
    }

    public int create(Serializable roleDto) {
        return roleService.create(roleDto);
    }

    public String readById(int id) {
        return convertToJson(roleService.readById(id));
    }

    public String readAll() {
        List<Serializable> roles = roleService.readAll();
        return convertToJsonArray(roles);
    }

    public boolean update(int id, Serializable roleDto) {
        return roleService.update(id, roleDto);
    }

    public boolean delete(int id) {
        return roleService.delete(id);
    }

    @SneakyThrows
    private String convertToJson(Serializable roleDto) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(roleDto);
    }

    @SneakyThrows
    private String convertToJsonArray(List<Serializable> roles) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(roles);
    }
}

