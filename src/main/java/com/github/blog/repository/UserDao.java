package com.github.blog.repository;

import com.github.blog.controller.dto.response.Page;
import com.github.blog.model.User;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.UserFilter;

import java.util.Optional;

/**
 * @author Raman Haurylau
 */
public interface UserDao extends CrudDao<User, Long> {
    Page<User> findAll(UserFilter filter, Pageable pageable);

    Optional<User> findByUsername(String username);
}
