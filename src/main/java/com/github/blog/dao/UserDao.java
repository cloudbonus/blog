package com.github.blog.dao;

import com.github.blog.model.User;

import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
public interface UserDao extends CrudDao<User, Long> {
    List<User> findAllByUniversity(String university);

    List<User> findAllByRole(String roleName);

    List<User> findAllByJobTitle(String jobTitle);

    Optional<User> findByLogin(String login);
}
