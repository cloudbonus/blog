package com.github.blog.dao.impl;

import com.github.blog.dao.Dao;
import com.github.blog.model.Role;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Component
public class RoleDao implements Dao<Role> {
    private final List<Role> roles = new ArrayList<>();

    @Override
    public Optional<Role> getById(int id) {
        return roles.stream().filter(r -> r.getRoleId() == id).findAny();
    }

    @Override
    public List<Role> getAll() {
        return roles;
    }

    @Override
    public int save(Role role) {
        roles.add(role);
        int index = roles.size();
        role.setRoleId(index);
        return index;
    }

    @Override
    public boolean update(Role role) {
        if (roles.stream().map(r -> r.getRoleId() == role.getRoleId()).findAny().orElse(false)) {
            roles.set(role.getRoleId() - 1, role);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteById(int id) {
        return roles.removeIf(r -> r.getRoleId() == id);
    }
}

