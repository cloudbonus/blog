package com.github.blog.service;

import com.github.blog.controller.dto.common.UserInfoDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.VerificationRequest;
import com.github.blog.controller.dto.request.filter.UserInfoDtoFilter;
import com.github.blog.controller.dto.response.Page;


/**
 * @author Raman Haurylau
 */
public interface UserInfoService {
    Page<UserInfoDto> findAll(UserInfoDtoFilter filterRequest, PageableRequest pageableRequest);

    UserInfoDto create(UserInfoDto t);

    UserInfoDto cancel(Long id);

    UserInfoDto verify(Long id, VerificationRequest request);

    UserInfoDto findById(Long id);

    UserInfoDto update(Long id, UserInfoDto t);

    UserInfoDto delete(Long id);
}
