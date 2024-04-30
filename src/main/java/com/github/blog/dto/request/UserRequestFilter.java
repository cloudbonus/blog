package com.github.blog.dto.request;

import lombok.Data;

/**
 * @author Raman Haurylau
 */
@Data
public class UserRequestFilter {
    private String login;
    private String role;
    private String firstname;
    private String surname;
    private String university;
    private String major;
    private String company;
    private String job;
}
