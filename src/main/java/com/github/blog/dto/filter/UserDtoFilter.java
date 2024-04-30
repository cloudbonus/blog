package com.github.blog.dto.filter;

import lombok.Data;

/**
 * @author Raman Haurylau
 */
@Data
public class UserDtoFilter {
    private String login;
    private String role;
    private String firstname;
    private String surname;
    private String university;
    private String major;
    private String company;
    private String job;
}
