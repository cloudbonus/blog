package com.github.blog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Raman Haurylau
 */
@Data
public class User {
    @JsonIgnore
    private int id;
    private String login;
    private String password;
    private String email;
    @JsonIgnore
    private LocalDateTime createdAt;
    @JsonIgnore
    private LocalDateTime lastLogin;

    private List<Role> roles;
}
