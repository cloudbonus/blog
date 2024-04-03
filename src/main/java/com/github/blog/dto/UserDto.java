package com.github.blog.dto;

import lombok.Data;

import java.util.Set;

/**
 * @author Raman Haurylau
 */
@Data
public class UserDto {
    private String login;
    private String password;
    private String email;

    private Set<RoleDto> roles;
}
