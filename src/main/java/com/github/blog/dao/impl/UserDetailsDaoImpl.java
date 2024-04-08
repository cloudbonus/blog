package com.github.blog.dao.impl;

import com.github.blog.dao.UserDetailsDao;
import com.github.blog.model.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class UserDetailsDaoImpl implements UserDetailsDao {
    private static final List<UserDetails> USER_DETAILS = new CopyOnWriteArrayList<>();

    @Override
    public Optional<UserDetails> findById(Integer id) {
        return USER_DETAILS.stream().filter(u -> u.getId() == id).findAny();
    }

    @Override
    public List<UserDetails> findAll() {
        return USER_DETAILS;
    }

    @Override
    public UserDetails create(UserDetails userDetails) {
        USER_DETAILS.add(userDetails);
        int index = USER_DETAILS.size();
        userDetails.setId(index);
        return userDetails;
    }

    @Override
    public UserDetails update(UserDetails userDetails) {
        for (int i = 0; i < USER_DETAILS.size(); i++) {
            if (USER_DETAILS.get(i).getId() == userDetails.getId()) {
                USER_DETAILS.set(i, userDetails);
                return userDetails;
            }
        }
        return null;
    }

    @Override
    public UserDetails remove(Integer id) {
        UserDetails userDetailsToRemove = null;

        for (UserDetails u : USER_DETAILS) {
            if (u.getId() == id) {
                userDetailsToRemove = u;
                break;
            }
        }

        if (userDetailsToRemove != null) {
            USER_DETAILS.remove(userDetailsToRemove);
        }

        return userDetailsToRemove;
    }
}