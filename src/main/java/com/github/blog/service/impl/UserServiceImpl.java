package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.UserDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.RegistrationRequest;
import com.github.blog.controller.dto.request.filter.UserDtoFilter;
import com.github.blog.controller.dto.response.Page;
import com.github.blog.model.Role;
import com.github.blog.model.User;
import com.github.blog.model.util.RoleEnum;
import com.github.blog.repository.RoleDao;
import com.github.blog.repository.UserDao;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.UserFilter;
import com.github.blog.service.UserService;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.RoleException;
import com.github.blog.service.exception.impl.UserException;
import com.github.blog.service.mapper.PageableMapper;
import com.github.blog.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

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
    private final PageableMapper pageableMapper;

    @Override
    public UserDto create(RegistrationRequest request) {
        User user = userMapper.toEntity(request);

        Role role = roleDao
                .findByName(RoleEnum.ROLE_USER.name())
                .orElseThrow(() -> new RoleException(ExceptionEnum.ROLE_NOT_FOUND));

        user.getRoles().add(role);

        userDao.create(user);

        return userMapper.toDto(user);
    }

    @Override
    public UserDto findById(Long id) {
        User user = userDao
                .findById(id)
                .orElseThrow(() -> new UserException(ExceptionEnum.USER_NOT_FOUND));

        user.setLastLogin(OffsetDateTime.now());

        return userMapper.toDto(user);
    }

    @Override
    public Page<UserDto> findAll(UserDtoFilter requestFilter, PageableRequest pageableRequest) {
        UserFilter dtoFilter = userMapper.toDto(requestFilter);

        Pageable pageable = pageableMapper.toDto(pageableRequest);
        Page<User> users = userDao.findAll(dtoFilter, pageable);

        if (users.isEmpty()) {
            throw new UserException(ExceptionEnum.USERS_NOT_FOUND);
        }

        return users.map(userMapper::toDto);
    }

    @Override
    public UserDto update(Long id, RegistrationRequest request) {
        User user = userDao
                .findById(id)
                .orElseThrow(() -> new UserException(ExceptionEnum.USER_NOT_FOUND));

        user = userMapper.partialUpdate(request, user);

        return userMapper.toDto(user);
    }

    @Override
    public UserDto delete(Long id) {
        User user = userDao
                .findById(id)
                .orElseThrow(() -> new UserException(ExceptionEnum.USER_NOT_FOUND));

        userDao.delete(user);

        return userMapper.toDto(user);
    }
}
