package com.github.blog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.annotation.Transaction;
import com.github.blog.dao.RoleDao;
import com.github.blog.dao.UserDao;
import com.github.blog.dao.UserRoleDao;
import com.github.blog.dto.UserDto;
import com.github.blog.model.Role;
import com.github.blog.model.User;
import com.github.blog.model.UserRole;
import com.github.blog.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final UserRoleDao userRoleDao;
    private final ObjectMapper objectMapper;

    @Override
    @Transaction
    public UserDto create(UserDto userDto) {
        User user = convertToObject(userDto);

        Optional<Role> result = roleDao.findByName("ROLE_USER");

        if (result.isEmpty()) {
            throw new RuntimeException("Role not found");
        }

        Role role = result.get();
        List<Role> roleForUser = Collections.singletonList(result.get());
        user.setRoles(roleForUser);

        enrichUser(user);

        user = userDao.create(user);

        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(role.getId());

        userRoleDao.create(userRole);

        return convertToDto(user);
    }

    @Override
    public UserDto findById(int id) {
        Optional<User> result = userDao.findById(id);

        if (result.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = result.get();

        enrichUser(user);

        return convertToDto(user);
    }

    @Override
    public List<UserDto> findAllByUniversity(String university) {
        List<User> users = userDao.findAllByUniversity(university);
        return users.stream().map(this::convertToDto).toList();
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = userDao.findAll();

        if (users.isEmpty()) {
            throw new RuntimeException("Cannot find any users");
        }
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
            user.setLastLogin(LocalDateTime.now());
            Role role = new Role();
            role.setName("ROLE_USER");
            List<Role> userRole = Collections.singletonList(role);
            user.setRoles(userRole);
        }
        user.setCreatedAt(LocalDateTime.now());
    }
}
