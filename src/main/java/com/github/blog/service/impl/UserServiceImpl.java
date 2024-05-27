package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.UserDto;
import com.github.blog.controller.dto.request.UserRequest;
import com.github.blog.controller.dto.request.etc.PageableRequest;
import com.github.blog.controller.dto.request.filter.UserFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.model.Role;
import com.github.blog.model.User;
import com.github.blog.model.util.RoleEnum;
import com.github.blog.repository.RoleDao;
import com.github.blog.repository.UserDao;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.UserFilter;
import com.github.blog.service.UserService;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
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
    private final RoleDao roleDao;

    private final UserMapper userMapper;
    private final PageableMapper pageableMapper;

    @Override
    public UserDto create(UserRequest request) {
        User user = userMapper.toEntity(request);

        Role role = roleDao
                .findByName(RoleEnum.ROLE_USER.name())
                .orElseThrow(() -> new CustomException(ExceptionEnum.ROLE_NOT_FOUND));

        user.getRoles().add(role);

        userDao.create(user);

        return userMapper.toDto(user);
    }

    @Override
    public UserDto findById(Long id) {
        User user = userDao
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_NOT_FOUND));

        user.setLastLogin(OffsetDateTime.now());

        return userMapper.toDto(user);
    }

    @Override
    public PageResponse<UserDto> findAll(UserFilterRequest requestFilter, PageableRequest pageableRequest) {
        UserFilter dtoFilter = userMapper.toDto(requestFilter);

        Pageable pageable = pageableMapper.toEntity(pageableRequest);
        Page<User> users = userDao.findAll(dtoFilter, pageable);

        if (users.isEmpty()) {
            throw new CustomException(ExceptionEnum.USERS_NOT_FOUND);
        }

        return userMapper.toDto(users);
    }

    @Override
    public UserDto update(Long id, UserRequest request) {
        User user = userDao
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_NOT_FOUND));

        user = userMapper.partialUpdate(request, user);

        return userMapper.toDto(user);
    }

    @Override
    public UserDto delete(Long id) {
        User user = userDao
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_NOT_FOUND));

        userDao.delete(user);

        return userMapper.toDto(user);
    }
}
