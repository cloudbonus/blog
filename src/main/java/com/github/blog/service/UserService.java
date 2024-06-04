package com.github.blog.service;

import com.github.blog.controller.dto.common.UserDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.UserRequest;
import com.github.blog.controller.dto.request.filter.UserFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;


/**
 * @author Raman Haurylau
 */
public interface UserService {
    PageResponse<UserDto> findAll(UserFilterRequest requestFilter, PageableRequest pageableRequest);

    UserDto create(UserRequest t);

    UserDto findById(Long id);

    UserDto update(Long id, UserRequest t);

    UserDto delete(Long id);
}