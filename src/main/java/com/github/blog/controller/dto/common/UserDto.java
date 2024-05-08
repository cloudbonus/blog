package com.github.blog.controller.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    private OffsetDateTime createdAt;
    private OffsetDateTime lastLogin;
}