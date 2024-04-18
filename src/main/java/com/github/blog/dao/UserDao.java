package com.github.blog.dao;

import com.github.blog.model.User;

import java.util.List;

/**
 * @author Raman Haurylau
 */
public interface UserDao extends CrudDao<User, Long> {
    List<User> findAllByUniversity(String university);

    List<User> findAllByRole(String roleName);

    List<User> findAllByJobTitle(String jobTitle);

    User findByLogin(String login);
}
