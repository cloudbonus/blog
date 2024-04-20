package com.github.blog.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.github.blog.model.Role}
 */
@Data
public class RoleDto implements Serializable {
    private Long id;
    private String roleName;
}