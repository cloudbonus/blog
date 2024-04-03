package com.github.blog.dao.impl;

import com.github.blog.dao.Dao;
import com.github.blog.model.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserDetailsDao implements Dao<UserDetails> {
    private final List<UserDetails> userDetailsList = new ArrayList<>();

    @Override
    public Optional<UserDetails> getById(int id) {
        return userDetailsList.stream().filter(u -> u.getUserId() == id).findAny();
    }

    @Override
    public List<UserDetails> getAll() {
        return userDetailsList;
    }

    @Override
    public int save(UserDetails userDetails) {
        userDetailsList.add(userDetails);
        int index = userDetailsList.size();
        userDetails.setUserId(index);
        return index;
    }

    @Override
    public boolean update(UserDetails userDetails) {
        if (userDetailsList.stream().map(u -> u.getUserId() == userDetails.getUserId()).findAny().orElse(false)) {
            userDetailsList.set(userDetails.getUserId() - 1, userDetails);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteById(int id) {
        return userDetailsList.removeIf(u -> u.getUserId() == id);
    }
}