package com.github.blog.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * DTO for {@link com.github.blog.model.User}
 */
@Data
public class UserDto implements Serializable {
    private Long id;
    private String login;
    private String password;
    private String email;
    private OffsetDateTime createdAt;
    private OffsetDateTime lastLogin;
}