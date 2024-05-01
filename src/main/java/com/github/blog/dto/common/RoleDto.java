package com.github.blog.dto.common;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link com.github.blog.model.Role}
 */
@Getter
@Setter
public class RoleDto {
    private Long id;
    private String roleName;
}