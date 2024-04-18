package com.github.blog.service;

import com.github.blog.dto.UserDetailDto;
import com.github.blog.dto.UserDto;

import java.util.List;


/**
 * @author Raman Haurylau
 */
public interface UserService extends CrudService<UserDto, Long> {
    List<UserDto> findAllByUniversity(UserDetailDto userDetailsDto);

    List<UserDto> findAllByRole(String role);

    List<UserDto> findAllByJobTitle(UserDetailDto userDetailsDto);

    UserDto findByLogin(UserDto userDto);
}