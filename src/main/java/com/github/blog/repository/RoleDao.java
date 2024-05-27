package com.github.blog.repository;

import com.github.blog.model.Role;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.RoleFilter;

import java.util.Optional;

/**
 * @author Raman Haurylau
 */
public interface RoleDao extends CrudDao<Role, Long> {
    Page<Role> findAll(RoleFilter filter, Pageable pageable);

    Optional<Role> findByName(String name);
}
