package com.github.blog.controller.dto.common;

import com.github.blog.repository.entity.UserInfo;

/**
 * DTO for {@link UserInfo}
 */
public record UserInfoDto(Long id, String state, String firstname, String surname, String university, String major,
                          String company, String job) {
}