package com.github.blog.service;

import com.github.blog.controller.dto.common.UserDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.RegistrationRequest;
import com.github.blog.controller.dto.request.filter.UserDtoFilter;
import com.github.blog.controller.dto.response.Page;


/**
 * @author Raman Haurylau
 */
public interface UserService {
    Page<UserDto> findAll(UserDtoFilter requestFilter, PageableRequest pageableRequest);

    UserDto create(RegistrationRequest t);

    UserDto findById(Long id);

    UserDto update(Long id, RegistrationRequest t);

    UserDto delete(Long id);
}