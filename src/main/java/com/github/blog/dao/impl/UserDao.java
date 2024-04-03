package com.github.blog.dao.impl;

import com.github.blog.dao.Dao;
import com.github.blog.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Component
public class UserDao implements Dao<User> {
    private final List<User> users = new ArrayList<>();

    @Override
    public Optional<User> getById(int id) {
        return users.stream().filter(u -> u.getUserId() == id).findAny();
    }

    @Override
    public List<User> getAll() {
        return users;
    }

    @Override
    public int save(User user) {
        users.add(user);
        int index = users.size();
        user.setUserId(index);
        return index;
    }

    @Override
    public boolean update(User user) {
        if (users.stream().map(u -> u.getUserId() == user.getUserId()).findAny().orElse(false)) {
            users.set(user.getUserId() - 1, user);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteById(int id) {
        return users.removeIf(u -> u.getUserId() == id);
    }
}
