package com.github.blog.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.github.blog.model.Role}
 */
@Value
public class RoleDto implements Serializable {
    String roleName;
}