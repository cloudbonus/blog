package com.github.blog.dto.common;

import lombok.Data;

/**
 * DTO for {@link com.github.blog.model.Role}
 */
@Data
public class RoleDto {
    private Long id;
    private String roleName;
}