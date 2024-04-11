package com.github.blog.service.impl;

import com.github.blog.annotation.Transaction;
import com.github.blog.dao.UserDao;
import com.github.blog.dao.UserDetailsDao;
import com.github.blog.dto.UserDetailsDto;
import com.github.blog.model.User;
import com.github.blog.model.UserDetails;
import com.github.blog.service.UserDetailsService;
import com.github.blog.util.DefaultMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserDetailsDao userDetailsDao;
    private final UserDao userDao;
    private final DefaultMapper mapper;

    @Override
    @Transaction
    public UserDetailsDto create(UserDetailsDto userDetailsDto) {
        UserDetails userDetails = mapper.map(userDetailsDto, UserDetails.class);

        Optional<User> result = userDao.findByLoginAndPassword(userDetails);

        if (result.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User userForUserDetails = result.get();
        userDetails.setId(userForUserDetails.getId());

        userDetails = userDetailsDao.create(userDetails);
        return mapper.map(userDetails, UserDetailsDto.class);
    }

    @Override
    public UserDetailsDto findById(int id) {
        Optional<UserDetails> result = userDetailsDao.findById(id);
        if (result.isEmpty()) {
            throw new RuntimeException("User Details not found");
        }
        return mapper.map(result.get(), UserDetailsDto.class);
    }

    @Override
    public List<UserDetailsDto> findAll() {
        List<UserDetails> userDetails = userDetailsDao.findAll();
        if (userDetails.isEmpty()) {
            throw new RuntimeException("Cannot find any user details");
        }
        return userDetails.stream().map(u -> mapper.map(u, UserDetailsDto.class)).toList();
    }

    @Override
    public UserDetailsDto update(int id, UserDetailsDto userDetailsDto) {
        Optional<UserDetails> result = userDetailsDao.findById(id);

        if (result.isEmpty()) {
            throw new RuntimeException("User Details not found");
        }

        UserDetails updatedUserDetails = mapper.map(userDetailsDto, UserDetails.class);
        UserDetails userDetails = result.get();

        updatedUserDetails.setId(userDetails.getId());

        updatedUserDetails = userDetailsDao.update(updatedUserDetails);

        return mapper.map(updatedUserDetails, UserDetailsDto.class);
    }

    @Override
    public int remove(int id) {
        UserDetails userDetails = userDetailsDao.remove(id);
        if (userDetails == null) {
            return -1;
        } else return userDetails.getId();
    }
}
