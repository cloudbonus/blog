package com.github.blog.dao;

import com.github.blog.dto.filter.UserFilter;
import com.github.blog.model.User;

import java.util.List;

/**
 * @author Raman Haurylau
 */
public interface UserDao extends CrudDao<User, Long> {
    List<User> findAll(UserFilter filter);
}
