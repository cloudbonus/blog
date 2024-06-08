package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.UserDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.filter.UserFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.model.User;
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

    private final UserMapper userMapper;
    private final PageableMapper pageableMapper;

    @Override
    @Transactional(readOnly = true)
    public UserDto findById(Long id) {
        log.debug("Finding user by ID: {}", id);
        User user = userDao
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_NOT_FOUND));

        user.setUpdatedAt(OffsetDateTime.now());
        log.debug("User found with ID: {}", id);

        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserDto> findAll(UserFilterRequest requestFilter, PageableRequest pageableRequest) {
        log.debug("Finding all users with filter: {} and pageable: {}", requestFilter, pageableRequest);
        UserFilter filter = userMapper.toEntity(requestFilter);
        Pageable pageable = pageableMapper.toEntity(pageableRequest);
        Page<User> users = userDao.findAll(filter, pageable);

        if (users.isEmpty()) {
            throw new CustomException(ExceptionEnum.USERS_NOT_FOUND);
        }

        log.debug("Found {} users", users.getTotalNumberOfEntities());
        return userMapper.toDto(users);
    }

    @Override
    public UserDto delete(Long id) {
        log.debug("Deleting user with ID: {}", id);
        User user = userDao
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_NOT_FOUND));

        userDao.delete(user);
        log.debug("User deleted successfully with ID: {}", id);

        return userMapper.toDto(user);
    }
}