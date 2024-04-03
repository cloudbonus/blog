package com.github.blog.dao.impl;

import com.github.blog.dao.RoleDao;
import com.github.blog.model.Role;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Component
public class RoleDaoImpl implements RoleDao {
    private final List<Role> roles = new ArrayList<>();

    @Override
    public Optional<Role> getById(int id) {
        return roles.stream().filter(r -> r.getId() == id).findAny();
    }

    @Override
    public List<Role> getAll() {
        return roles;
    }

    @Override
    public int save(Role role) {
        roles.add(role);
        int index = roles.size();
        role.setId(index);
        return index;
    }

    @Override
    public Optional<Role> update(Role role) {
        for (int i = 0; i < roles.size(); i++) {
            if (roles.get(i).getId() == role.getId()) {
                roles.set(i, role);
                return Optional.of(role);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(int id) {
        return roles.removeIf(r -> r.getId() == id);
    }
}

