package com.github.blog.service;

import com.github.blog.dto.common.UserDto;
import com.github.blog.dto.request.UserDtoFilter;
import com.github.blog.dto.Page;


/**
 * @author Raman Haurylau
 */
public interface UserService {
    Page<UserDto> findAll(UserDtoFilter requestFilter);

    UserDto create(UserDto t);

    UserDto findById(Long id);

    UserDto update(Long id, UserDto t);

    UserDto delete(Long id);
}