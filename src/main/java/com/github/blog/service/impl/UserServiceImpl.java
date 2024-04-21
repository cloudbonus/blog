package com.github.blog.service.impl;

import com.github.blog.dao.RoleDao;
import com.github.blog.dao.UserDao;
import com.github.blog.dto.UserDetailDto;
import com.github.blog.dto.UserDto;
import com.github.blog.model.Role;
import com.github.blog.model.User;
import com.github.blog.model.UserDetail;
import com.github.blog.service.UserService;
import com.github.blog.service.mapper.UserDetailMapper;
import com.github.blog.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author Raman Haurylau
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final UserMapper userMapper;
    private final UserDetailMapper detailMapper;
    private final RoleDao roleDao;


    @Override
    public UserDto create(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        userDao.create(user);
        Role role = roleDao.findByName("ROLE_USER");
        user.getRoles().add(role);
        role.getUsers().add(user);
        roleDao.update(role);
        user = userDao.update(user);
        return userMapper.toDto(user);
    }

    @Override
    public UserDto findById(Long id) {
        User user = userDao.findById(id);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        enrichUser(user);
        return userMapper.toDto(user);
    }

    @Override
    public List<UserDto> findAllByUniversity(UserDetailDto userDetailsDto) {
        UserDetail userDetails = detailMapper.toEntity(userDetailsDto);

        List<User> users = userDao.findAllByUniversity(userDetails.getUniversityName());

        return users.stream().map(userMapper::toDto).toList();
    }

    @Override
    public List<UserDto> findAllByRole(String role) {
        List<User> users = userDao.findAllByRole(role);
        return users.stream().map(userMapper::toDto).toList();
    }

    @Override
    public List<UserDto> findAllByJobTitle(UserDetailDto userDetailsDto) {
        UserDetail userDetails = detailMapper.toEntity(userDetailsDto);

        List<User> users = userDao.findAllByJobTitle(userDetails.getJobTitle());

        return users.stream().map(userMapper::toDto).toList();
    }

    @Override
    public UserDto findByLogin(UserDto userDto) {
        User user = userMapper.toEntity(userDto);

        user = userDao.findByLogin(user.getLogin());

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        return userMapper.toDto(user);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = userDao.findAll();

        if (users.isEmpty()) {
            throw new RuntimeException("Cannot find any users");
        }

        return users.stream().map(userMapper::toDto).toList();
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {
        User user = userDao.findById(id);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        User updatedUser = userMapper.toEntity(userDto);

        updatedUser.setId(user.getId());
        updatedUser.setCreatedAt(user.getCreatedAt());
        updatedUser.setLastLogin(user.getLastLogin());

        updatedUser = userDao.update(updatedUser);

        return userMapper.toDto(updatedUser);
    }

    @Override
    public void delete(Long id) {
        userDao.deleteById(id);
    }

    private void enrichUser(User user) {
        user.setLastLogin(OffsetDateTime.now());
    }
}
