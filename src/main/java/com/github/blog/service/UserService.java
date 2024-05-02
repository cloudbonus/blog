package com.github.blog.service;

import com.github.blog.controller.dto.common.UserDto;
import com.github.blog.controller.dto.request.UserDtoFilter;
import com.github.blog.controller.dto.request.UserRequest;
import com.github.blog.controller.dto.response.Page;


/**
 * @author Raman Haurylau
 */
public interface UserService {
    Page<UserDto> findAll(UserDtoFilter requestFilter);

    UserDto create(UserRequest t);

    UserDto findById(Long id);

    UserDto update(Long id, UserRequest t);

    UserDto delete(Long id);
}