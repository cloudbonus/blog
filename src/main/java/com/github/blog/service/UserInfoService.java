package com.github.blog.service;

import com.github.blog.controller.dto.common.UserInfoDto;
import com.github.blog.controller.dto.request.UserInfoRequest;
import com.github.blog.controller.dto.request.etc.PageableRequest;
import com.github.blog.controller.dto.request.etc.VerificationRequest;
import com.github.blog.controller.dto.request.filter.UserInfoFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;


/**
 * @author Raman Haurylau
 */
public interface UserInfoService {
    PageResponse<UserInfoDto> findAll(UserInfoFilterRequest filterRequest, PageableRequest pageableRequest);

    UserInfoDto create(UserInfoRequest t);

    UserInfoDto cancel(Long id);

    UserInfoDto verify(Long id, VerificationRequest request);

    UserInfoDto findById(Long id);

    UserInfoDto update(Long id, UserInfoRequest t);

    UserInfoDto delete(Long id);
}
