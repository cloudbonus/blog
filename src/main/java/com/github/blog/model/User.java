package com.github.blog.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author Raman Haurylau
 */
@Data
public class User {
    private int userId;
    private String login;
    private String password;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    private Set<Role> roles;
}
