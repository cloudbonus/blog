package com.github.blog.dao.impl;

import com.github.blog.dao.UserDetailsDao;
import com.github.blog.model.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserDetailsDaoImpl implements UserDetailsDao {
    private final List<UserDetails> userDetailsList = new ArrayList<>();

    @Override
    public Optional<UserDetails> getById(int id) {
        return userDetailsList.stream().filter(u -> u.getId() == id).findAny();
    }

    @Override
    public List<UserDetails> getAll() {
        return userDetailsList;
    }

    @Override
    public int save(UserDetails userDetails) {
        userDetailsList.add(userDetails);
        int index = userDetailsList.size();
        userDetails.setId(index);
        return index;
    }

    @Override
    public Optional<UserDetails> update(UserDetails userDetails) {
        for (int i = 0; i < userDetailsList.size(); i++) {
            if (userDetailsList.get(i).getId() == userDetails.getId()) {
                userDetailsList.set(i, userDetails);
                return Optional.of(userDetails);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(int id) {
        return userDetailsList.removeIf(u -> u.getId() == id);
    }
}