package com.github.blog.controller.dto.common;

/**
 * DTO for {@link com.github.blog.model.UserInfo}
 */
public record UserInfoDto(Long id, String state, String firstname, String surname, String university, String major,
                          String company, String job) {
}