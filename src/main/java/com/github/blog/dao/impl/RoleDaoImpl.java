package com.github.blog.dao.impl;

import com.github.blog.dao.RoleDao;
import com.github.blog.model.Role;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Raman Haurylau
 */
@Repository
public class RoleDaoImpl implements RoleDao {
    private static final List<Role> ROLES = new CopyOnWriteArrayList<>();

    @Override
    public Optional<Role> findById(Integer id) {
        return ROLES.stream().filter(r -> r.getId() == id).findAny();
    }

    @Override
    public List<Role> findAll() {
        return ROLES;
    }

    @Override
    public Role create(Role role) {
        ROLES.add(role);
        int index = ROLES.size();
        role.setId(index);
        return role;
    }

    @Override
    public Role update(Role role) {
        for (int i = 0; i < ROLES.size(); i++) {
            if (ROLES.get(i).getId() == role.getId()) {
                ROLES.set(i, role);
                return role;
            }
        }
        return null;
    }

    @Override
    public Role remove(Integer id) {
        Role roleToRemove = null;

        for (Role r : ROLES) {
            if (r.getId() == id) {
                roleToRemove = r;
                break;
            }
        }

        if (roleToRemove != null) {
            ROLES.remove(roleToRemove);
        }

        return roleToRemove;
    }
}

