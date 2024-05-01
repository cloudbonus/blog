package com.github.blog.repository;

import com.github.blog.controller.dto.response.Page;
import com.github.blog.model.User;
import com.github.blog.repository.dto.filter.UserFilter;

/**
 * @author Raman Haurylau
 */
public interface UserDao extends CrudDao<User, Long> {
    Page<User> findAll(UserFilter filter);
}
