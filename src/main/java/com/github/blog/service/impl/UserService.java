package com.github.blog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dao.Dao;
import com.github.blog.dto.UserDto;
import com.github.blog.model.User;
import com.github.blog.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Component
public class UserService implements Service<Serializable> {

    private final Dao<User> userDao;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserService(Dao<User> userDao, ObjectMapper objectMapper) {
        this.userDao = userDao;
        this.objectMapper = objectMapper;
    }

    @Override
    public int create(Serializable userDto) {
        User user = convertToObject(userDto);
        enrichUser(user);
        return userDao.save(user);
    }

    @Override

    public Serializable readById(int id) {
        Optional<User> result = userDao.getById(id);
        if (result.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        enrichUser(result.get());
        return convertToDto(result.get());
    }

    @Override

    public List<Serializable> readAll() {
        List<User> users = userDao.getAll();
        if (users.isEmpty()) {
            throw new RuntimeException("Cannot find any users");
        }
        users.forEach(this::enrichUser);
        return users.stream().map(this::convertToDto).toList();
    }

    @Override
    public boolean update(int id, Serializable userDto) {
        Optional<User> result = userDao.getById(id);

        if (result.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = convertToObject(userDto);
        user.setUserId(id);
        return userDao.update(user);
    }

    @Override
    public boolean delete(int id) {
        return userDao.deleteById(id);
    }

    private User convertToObject(Serializable userDto) {
        return objectMapper.convertValue(userDto, User.class);
    }

    private Serializable convertToDto(User user) {
        return objectMapper.convertValue(user, UserDto.class);
    }

    private void enrichUser(User user) {
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }
        user.setCreatedAt(LocalDateTime.now());
    }
}
