package com.github.blog.service.impl;

import com.github.blog.annotation.Transaction;
import com.github.blog.dao.RoleDao;
import com.github.blog.dao.UserDao;
import com.github.blog.dao.UserRoleDao;
import com.github.blog.dto.UserDetailsDto;
import com.github.blog.dto.UserDto;
import com.github.blog.mapper.Mapper;
import com.github.blog.model.Role;
import com.github.blog.model.User;
import com.github.blog.model.UserDetails;
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
    private final Mapper mapper;

    @Override
    @Transaction
    public UserDto create(UserDto userDto) {
        User user = mapper.map(userDto, User.class);

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

        return mapper.map(user, UserDto.class);
    }

    @Override
    public UserDto findById(int id) {
        Optional<User> result = userDao.findById(id);

        if (result.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = result.get();

        enrichUser(user);

        return mapper.map(user, UserDto.class);
    }

    @Override
    public List<UserDto> findAllByUniversity(UserDetailsDto userDetailsDto) {
        UserDetails userDetails = mapper.map(userDetailsDto, UserDetails.class);

        List<User> users = userDao.findAllByUniversity(userDetails.getUniversityName());
        return users.stream().map(u -> mapper.map(u, UserDto.class)).toList();
    }

    @Override
    public List<UserDto> findAllByRole(String role) {
        List<User> users = userDao.findAllByRole(role);
        return users.stream().map(u -> mapper.map(u, UserDto.class)).toList();
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = userDao.findAll();

        if (users.isEmpty()) {
            throw new RuntimeException("Cannot find any users");
        }
        return users.stream().map(u -> mapper.map(u, UserDto.class)).toList();
    }

    @Override
    public UserDto update(int id, UserDto userDto) {
        Optional<User> result = userDao.findById(id);

        if (result.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User updatedUser = mapper.map(userDto, User.class);
        User user = result.get();

        updatedUser.setId(user.getId());
        updatedUser.setCreatedAt(user.getCreatedAt());
        updatedUser.setLastLogin(user.getLastLogin());

        updatedUser = userDao.update(updatedUser);

        return mapper.map(updatedUser, UserDto.class);
    }

    @Override
    public int remove(int id) {
        User user = userDao.remove(id);
        if (user == null) {
            return -1;
        } else return user.getId();
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
