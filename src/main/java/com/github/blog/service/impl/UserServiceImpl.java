package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.UserDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.UserRequest;
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
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

/**
 * @author Raman Haurylau
 */
@Log4j2
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
        log.info("Creating a new user with request: {}", request);
        User user = userMapper.toEntity(request);

        Role role = roleDao
                .findByName(RoleEnum.ROLE_USER.name())
                .orElseThrow(() -> {
                    log.error("Role not found: {}", RoleEnum.ROLE_USER.name());
                    return new CustomException(ExceptionEnum.ROLE_NOT_FOUND);
                });

        user.getRoles().add(role);
        userDao.create(user);
        log.info("User created successfully with ID: {}", user.getId());

        return userMapper.toDto(user);
    }

    @Override
    public UserDto findById(Long id) {
        log.info("Finding user by ID: {}", id);
        User user = userDao
                .findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", id);
                    return new CustomException(ExceptionEnum.USER_NOT_FOUND);
                });

        user.setUpdatedAt(OffsetDateTime.now());
        log.info("User found with ID: {}", id);

        return userMapper.toDto(user);
    }

    @Override
    public PageResponse<UserDto> findAll(UserFilterRequest requestFilter, PageableRequest pageableRequest) {
        log.info("Finding all users with filter: {} and pageable: {}", requestFilter, pageableRequest);
        UserFilter filter = userMapper.toEntity(requestFilter);
        Pageable pageable = pageableMapper.toEntity(pageableRequest);
        Page<User> users = userDao.findAll(filter, pageable);

        if (users.isEmpty()) {
            log.error("No users found with the given filter and pageable");
            throw new CustomException(ExceptionEnum.USERS_NOT_FOUND);
        }

        log.info("Found {} users", users.getTotalNumberOfEntities());
        return userMapper.toDto(users);
    }

    @Override
    public UserDto update(Long id, UserRequest request) {
        log.info("Updating user with ID: {} and request: {}", id, request);
        User user = userDao
                .findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", id);
                    return new CustomException(ExceptionEnum.USER_NOT_FOUND);
                });

        user = userMapper.partialUpdate(request, user);
        log.info("User updated successfully with ID: {}", id);

        return userMapper.toDto(user);
    }

    @Override
    public UserDto delete(Long id) {
        log.info("Deleting user with ID: {}", id);
        User user = userDao
                .findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", id);
                    return new CustomException(ExceptionEnum.USER_NOT_FOUND);
                });

        userDao.delete(user);
        log.info("User deleted successfully with ID: {}", id);

        return userMapper.toDto(user);
    }
}