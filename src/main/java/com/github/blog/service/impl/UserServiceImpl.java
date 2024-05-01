package com.github.blog.service.impl;

import com.github.blog.repository.RoleDao;
import com.github.blog.repository.UserDao;
import com.github.blog.controller.dto.common.UserDto;
import com.github.blog.repository.dto.filter.UserFilter;
import com.github.blog.controller.dto.request.UserDtoFilter;
import com.github.blog.controller.dto.Page;
import com.github.blog.model.Role;
import com.github.blog.model.User;
import com.github.blog.service.UserService;
import com.github.blog.service.exception.RoleErrorResult;
import com.github.blog.service.exception.UserErrorResult;
import com.github.blog.service.exception.impl.RoleException;
import com.github.blog.service.exception.impl.UserException;
import com.github.blog.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

/**
 * @author Raman Haurylau
 */
@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
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
        user.setLastLogin(OffsetDateTime.now());
        return userMapper.toDto(user);
    }

    @Override
    public Page<UserDto> findAll(UserDtoFilter requestFilter) {
        UserFilter dtoFilter = userMapper.toDto(requestFilter);

        Page<User> users = userDao.findAll(dtoFilter);

        if (users.isEmpty()) {
            throw new UserException(UserErrorResult.USERS_NOT_FOUND);
        }

        return users.map(userMapper::toDto);
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
}
