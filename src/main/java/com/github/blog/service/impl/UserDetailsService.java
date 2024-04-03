package com.github.blog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dao.Dao;
import com.github.blog.dto.UserDetailsDto;
import com.github.blog.model.UserDetails;
import com.github.blog.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Component
public class UserDetailsService implements Service<Serializable> {
    private final Dao<UserDetails> userDetailsDao;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserDetailsService(Dao<UserDetails> userDetailsDao, ObjectMapper objectMapper) {
        this.userDetailsDao = userDetailsDao;
        this.objectMapper = objectMapper;
    }

    public int create(Serializable userDetailsDto) {
        UserDetails userDetails = convertToObject(userDetailsDto);
        return userDetailsDao.save(userDetails);
    }

    public Serializable readById(int id) {
        Optional<UserDetails> result = userDetailsDao.getById(id);
        if (result.isEmpty()) {
            throw new RuntimeException("User Details not found");
        }
        return convertToDto(result.get());
    }

    public List<Serializable> readAll() {
        List<UserDetails> userDetails = userDetailsDao.getAll();
        if (userDetails.isEmpty()) {
            throw new RuntimeException("Cannot find any user details");
        }
        return userDetails.stream().map(this::convertToDto).toList();
    }

    public boolean update(int id, Serializable userDetailsDto) {
        Optional<UserDetails> result = userDetailsDao.getById(id);

        if (result.isEmpty()) {
            throw new RuntimeException("User Details not found");
        }

        UserDetails userDetails = convertToObject(userDetailsDto);
        userDetails.setUserId(id);
        return userDetailsDao.update(userDetails);
    }

    public boolean delete(int id) {
        return userDetailsDao.deleteById(id);
    }

    private UserDetails convertToObject(Serializable userDetailsDto) {
        return objectMapper.convertValue(userDetailsDto, UserDetails.class);
    }

    private Serializable convertToDto(UserDetails userDetails) {
        return objectMapper.convertValue(userDetails, UserDetailsDto.class);
    }

}
