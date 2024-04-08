package com.github.blog.dao.impl;

import com.github.blog.dao.UserDao;
import com.github.blog.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Raman Haurylau
 */
@Repository
public class UserDaoImpl implements UserDao {
    private static final List<User> USERS = new CopyOnWriteArrayList<>();

    @Override
    public Optional<User> findById(Integer id) {
        return USERS.stream().filter(u -> u.getId() == id).findAny();
    }

    @Override
    public List<User> findAll() {
        return USERS;
    }

    @Override
    public User create(User user) {
        USERS.add(user);
        int index = USERS.size();
        user.setId(index);
        return user;
    }

    @Override
    public User update(User user) {
        for (int i = 0; i < USERS.size(); i++) {
            if (USERS.get(i).getId() == user.getId()) {
                USERS.set(i, user);
                return user;
            }
        }
        return null;
    }

    @Override
    public User remove(Integer id) {
        User userToRemove = null;

        for (User u : USERS) {
            if (u.getId() == id) {
                userToRemove = u;
                break;
            }
        }

        if (userToRemove != null) {
            USERS.remove(userToRemove);
        }

        return userToRemove;
    }
}
