package com.github.blog.controller.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.OffsetDateTime;

/**
 * DTO for {@link com.github.blog.model.User}
 */
public record UserDto(Long id, String username, @JsonIgnore String password, String email, OffsetDateTime createdAt,
                      OffsetDateTime updatedAt) {
}