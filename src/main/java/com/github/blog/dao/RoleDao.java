package com.github.blog.dao;

import com.github.blog.model.Role;

import java.util.Optional;

/**
 * @author Raman Haurylau
 */
public interface RoleDao extends CrudDao<Role, Integer> {

    Optional<Role> findByName(String name);
}
