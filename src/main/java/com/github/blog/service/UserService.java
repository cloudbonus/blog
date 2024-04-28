package com.github.blog.service;

import com.github.blog.dto.UserDto;

import java.util.List;


/**
 * @author Raman Haurylau
 */
public interface UserService extends CrudService<UserDto, Long> {
    List<UserDto> findAllByUniversity(String universityName);

    List<UserDto> findAllByRole(String role);

    List<UserDto> findAllByJobTitle(String jobName);

    UserDto findByLogin(String login);
}