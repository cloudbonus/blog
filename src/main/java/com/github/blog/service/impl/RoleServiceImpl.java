package com.github.blog.service.impl;

import com.github.blog.dao.RoleDao;
import com.github.blog.dto.RoleDto;
import com.github.blog.mapper.Mapper;
import com.github.blog.model.Role;
import com.github.blog.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;
    private final Mapper mapper;

    @Override
    public RoleDto create(RoleDto roleDto) {
        Role role = mapper.map(roleDto, Role.class);
        return mapper.map(roleDao.create(role), RoleDto.class);
    }

    @Override
    public RoleDto findById(int id) {
        Optional<Role> result = roleDao.findById(id);
        if (result.isEmpty()) {
            throw new RuntimeException("Role not found");
        }
        return mapper.map(result.get(), RoleDto.class);
    }

    @Override
    public List<RoleDto> findAll() {
        List<Role> roles = roleDao.findAll();
        if (roles.isEmpty()) {
            throw new RuntimeException("Cannot find any roles");
        }
        return roles.stream().map(r -> mapper.map(r, RoleDto.class)).toList();
    }

    @Override
    public RoleDto update(int id, RoleDto roleDto) {
        Optional<Role> result = roleDao.findById(id);

        if (result.isEmpty()) {
            throw new RuntimeException("Role not found");
        }

        Role updatedRole = mapper.map(roleDto, Role.class);
        Role role = result.get();

        updatedRole.setId(role.getId());

        updatedRole = roleDao.update(updatedRole);

        return mapper.map(updatedRole, RoleDto.class);
    }

    @Override
    public int remove(int id) {
        Role role = roleDao.remove(id);
        if (role == null) {
            return -1;
        } else return role.getId();
    }
}
