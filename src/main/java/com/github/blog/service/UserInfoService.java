package com.github.blog.service;

import com.github.blog.controller.dto.common.UserInfoDto;

import java.util.List;


/**
 * @author Raman Haurylau
 */
public interface UserInfoService {
    List<UserInfoDto> findAll();

    UserInfoDto create(UserInfoDto t);

    UserInfoDto findById(Long id);

    UserInfoDto update(Long id, UserInfoDto t);

    UserInfoDto delete(Long id);
}
