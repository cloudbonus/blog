package com.github.blog.dao;

import com.github.blog.model.User;
import com.github.blog.model.UserDetails;

import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
public interface UserDao extends CrudDao<User, Integer> {
    List<User> findAllByUniversity(String university);

    List<User> findAllByRole(String roleName);

    Optional<User> findByLoginAndPassword(UserDetails userDetails);
}
