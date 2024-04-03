package com.github.blog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dao.Dao;
import com.github.blog.dto.RoleDto;
import com.github.blog.model.Role;
import com.github.blog.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Component
public class RoleService implements Service<Serializable> {

    private final Dao<Role> roleDao;
    private final ObjectMapper objectMapper;

    @Autowired
    public RoleService(Dao<Role> roleDao, ObjectMapper objectMapper) {
        this.roleDao = roleDao;
        this.objectMapper = objectMapper;
    }

    @Override
    public int create(Serializable roleDto) {
        Role role = convertToObject(roleDto);
        return roleDao.save(role);
    }

    @Override
    public Serializable readById(int id) {
        Optional<Role> result = roleDao.getById(id);
        if (result.isEmpty()) {
            throw new RuntimeException("Role not found");
        }
        return convertToDto(result.get());
    }

    @Override
    public List<Serializable> readAll() {
        List<Role> roles = roleDao.getAll();
        if (roles.isEmpty()) {
            throw new RuntimeException("Cannot find any roles");
        }
        return roles.stream().map(this::convertToDto).toList();
    }

    @Override
    public boolean update(int id, Serializable roleDto) {
        Optional<Role> result = roleDao.getById(id);

        if (result.isEmpty()) {
            throw new RuntimeException("Role not found");
        }

        Role role = convertToObject(roleDto);
        role.setRoleId(id);
        return roleDao.update(role);
    }

    @Override
    public boolean delete(int id) {
        return roleDao.deleteById(id);
    }

    private Role convertToObject(Serializable roleDto) {
        return objectMapper.convertValue(roleDto, Role.class);
    }

    private Serializable convertToDto(Role role) {
        return objectMapper.convertValue(role, RoleDto.class);
    }
}
