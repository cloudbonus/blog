package com.github.blog.dto;

import lombok.Value;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * DTO for {@link com.github.blog.model.User}
 */
@Value
public class UserDto implements Serializable {
    String login;
    String email;
    String password;
    OffsetDateTime createdAt;
    OffsetDateTime lastLogin;
}