package com.github.blog.dto.common;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * DTO for {@link com.github.blog.model.User}
 */
@Getter
@Setter
public class UserDto {
    private Long id;
    private String login;
    private String password;
    private String email;
    private OffsetDateTime createdAt;
    private OffsetDateTime lastLogin;
}