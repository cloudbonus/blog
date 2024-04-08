package com.github.blog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dao.RoleDao;
import com.github.blog.dto.RoleDto;
import com.github.blog.model.Role;
import com.github.blog.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;
    private final ObjectMapper objectMapper;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao, ObjectMapper objectMapper) {
        this.roleDao = roleDao;
        this.objectMapper = objectMapper;
    }

    @Override
    public RoleDto create(RoleDto roleDto) {
        Role role = convertToObject(roleDto);
        return convertToDto(roleDao.create(role));
    }

    @Override
    public RoleDto findById(int id) {
        Optional<Role> result = roleDao.findById(id);
        if (result.isEmpty()) {
            throw new RuntimeException("Role not found");
        }
        return convertToDto(result.get());
    }

    @Override
    public List<RoleDto> findAll() {
        List<Role> roles = roleDao.findAll();
        if (roles.isEmpty()) {
            throw new RuntimeException("Cannot find any roles");
        }
        return roles.stream().map(this::convertToDto).toList();
    }

    @Override
    public RoleDto update(int id, RoleDto roleDto) {
        Optional<Role> result = roleDao.findById(id);

        if (result.isEmpty()) {
            throw new RuntimeException("Role not found");
        }

        Role updatedRole = convertToObject(roleDto);
        Role role = result.get();

        updatedRole.setId(role.getId());

        updatedRole = roleDao.update(updatedRole);

        return convertToDto(updatedRole);
    }

    @Override
    public int remove(int id) {
        Role role = roleDao.remove(id);
        if (role == null) {
            return -1;
        } else return role.getId();
    }

    private Role convertToObject(RoleDto roleDto) {
        return objectMapper.convertValue(roleDto, Role.class);
    }

    private RoleDto convertToDto(Role role) {
        return objectMapper.convertValue(role, RoleDto.class);
    }
}
