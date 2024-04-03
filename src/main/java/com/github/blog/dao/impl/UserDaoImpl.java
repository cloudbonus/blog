package com.github.blog.dao.impl;

import com.github.blog.dao.UserDao;
import com.github.blog.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Component
public class UserDaoImpl implements UserDao {
    private final List<User> users = new ArrayList<>();

    @Override
    public Optional<User> getById(int id) {
        return users.stream().filter(u -> u.getId() == id).findAny();
    }

    @Override
    public List<User> getAll() {
        return users;
    }

    @Override
    public int save(User user) {
        users.add(user);
        int index = users.size();
        user.setId(index);
        return index;
    }

    @Override
    public Optional<User> update(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == user.getId()) {
                users.set(i, user);
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(int id) {
        return users.removeIf(u -> u.getId() == id);
    }
}
