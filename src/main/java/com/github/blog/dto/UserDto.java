package com.github.blog.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * @author Raman Haurylau
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto implements Serializable {
    private String login;
    private String password;
    private String email;

    private Set<RoleDto> roles;
}
