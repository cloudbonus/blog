package com.github.blog.dao;

import com.github.blog.dto.filter.UserFilter;
import com.github.blog.dto.Page;
import com.github.blog.model.User;

/**
 * @author Raman Haurylau
 */
public interface UserDao extends CrudDao<User, Long> {
    Page<User> findAll(UserFilter filter);
}
