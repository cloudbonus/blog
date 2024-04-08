package com.github.blog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dao.UserDao;
import com.github.blog.dto.UserDto;
import com.github.blog.model.User;
import com.github.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserServiceImpl(UserDao userDao, ObjectMapper objectMapper) {
        this.userDao = userDao;
        this.objectMapper = objectMapper;
    }

    @Override
    public UserDto create(UserDto userDto) {
        User user = convertToObject(userDto);
        enrichUser(user);
        return convertToDto(userDao.create(user));
    }

    @Override
    public UserDto findById(int id) {
        Optional<User> result = userDao.findById(id);
        if (result.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        enrichUser(result.get());
        return convertToDto(result.get());
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = userDao.findAll();
        if (users.isEmpty()) {
            throw new RuntimeException("Cannot find any users");
        }
        users.forEach(this::enrichUser);
        return users.stream().map(this::convertToDto).toList();
    }

    @Override
    public UserDto update(int id, UserDto userDto) {
        Optional<User> result = userDao.findById(id);

        if (result.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User updatedUser = convertToObject(userDto);
        User user = result.get();

        updatedUser.setId(user.getId());
        updatedUser.setCreatedAt(user.getCreatedAt());
        updatedUser.setLastLogin(user.getLastLogin());

        updatedUser = userDao.update(updatedUser);

        return convertToDto(updatedUser);
    }

    @Override
    public int remove(int id) {
        User user = userDao.remove(id);
        if (user == null) {
            return -1;
        } else return user.getId();
    }

    private User convertToObject(UserDto userDto) {
        return objectMapper.convertValue(userDto, User.class);
    }

    private UserDto convertToDto(User user) {
        return objectMapper.convertValue(user, UserDto.class);
    }

    private void enrichUser(User user) {
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }
        user.setCreatedAt(LocalDateTime.now());
    }
}
