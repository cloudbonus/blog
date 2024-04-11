package com.github.blog.service;

import com.github.blog.dto.UserDetailsDto;
import com.github.blog.dto.UserDto;

import java.util.List;


/**
 * @author Raman Haurylau
 */
public interface UserService extends CrudService<UserDto> {
    List<UserDto> findAllByUniversity(UserDetailsDto userDetailsDto);

    List<UserDto> findAllByRole(String role);
}
