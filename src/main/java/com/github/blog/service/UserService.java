package com.github.blog.service;

import com.github.blog.dto.common.UserDto;
import com.github.blog.dto.filter.UserFilter;

import java.util.List;


/**
 * @author Raman Haurylau
 */
public interface UserService {
    List<UserDto> findAll(UserFilter filter);

    UserDto create(UserDto t);

    UserDto findById(Long id);

    UserDto update(Long id, UserDto t);

    UserDto delete(Long id);
}