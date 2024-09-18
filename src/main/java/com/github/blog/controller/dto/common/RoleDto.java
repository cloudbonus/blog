package com.github.blog.controller.dto.common;

import com.github.blog.repository.entity.Role;

/**
 * DTO for {@link Role}
 */
public record RoleDto(Long id, String name) {
}