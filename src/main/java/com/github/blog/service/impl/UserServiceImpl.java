package com.github.blog.service.impl;

import com.github.blog.dao.RoleDao;
import com.github.blog.dao.UserDao;
import com.github.blog.dto.common.UserDto;
import com.github.blog.dto.filter.UserFilter;
import com.github.blog.model.Role;
import com.github.blog.model.User;
import com.github.blog.service.UserService;
import com.github.blog.service.exception.RoleErrorResult;
import com.github.blog.service.exception.UserErrorResult;
import com.github.blog.service.exception.impl.RoleException;
import com.github.blog.service.exception.impl.UserException;
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
    private final RoleDao roleDao;


    @Override
    public UserDto create(UserDto userDto) {
        User user = userMapper.toEntity(userDto);

        userDao.create(user);

        Role role = roleDao
                .findByName("ROLE_USER")
                .orElseThrow(() -> new RoleException(RoleErrorResult.ROLE_NOT_FOUND));

        user.getRoles().add(role);
        role.getUsers().add(user);

        roleDao.update(role);
        user = userDao.update(user);

        return userMapper.toDto(user);
    }

    @Override
    public UserDto findById(Long id) {
        User user = userDao
                .findById(id)
                .orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));
        enrichUser(user);
        return userMapper.toDto(user);
    }

    @Override
    public List<UserDto> findAll(UserFilter userFilter) {
        List<User> users = userDao.findAll(userFilter);

        if (users.isEmpty()) {
            throw new UserException(UserErrorResult.USERS_NOT_FOUND);
        }

        return users.stream().map(userMapper::toDto).toList();
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {
        User user = userDao
                .findById(id)
                .orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        user = userMapper.partialUpdate(userDto, user);
        user = userDao.update(user);

        return userMapper.toDto(user);
    }

    @Override
    public UserDto delete(Long id) {
        User user = userDao
                .findById(id)
                .orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));
        userDao.delete(user);
        return userMapper.toDto(user);
    }

    private void enrichUser(User user) {
        user.setLastLogin(OffsetDateTime.now());
    }
}
