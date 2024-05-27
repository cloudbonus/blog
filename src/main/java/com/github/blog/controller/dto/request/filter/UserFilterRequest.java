package com.github.blog.controller.dto.request.filter;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class UserFilterRequest {
    @Pattern(message = "Invalid username", regexp = "^[A-Za-z][A-Za-z0-9._-]{0,15}$")
    private String username;

    @Positive
    private Long roleId;

    @Pattern(message = "Invalid firstname", regexp = "^[A-Za-z][a-zA-Z ,.'-]{0,30}$")
    private String firstname;

    @Pattern(message = "Invalid surname", regexp = "^[A-Za-z][a-zA-Z ,.'-]{0,30}$")
    private String surname;

    @Pattern(message = "Invalid user info", regexp = "^[A-Za-z][a-zA-Z .'-]{0,30}$")
    private String university;

    @Pattern(message = "Invalid user info", regexp = "^[A-Za-z][a-zA-Z .'-]{0,30}$")
    private String major;

    @Pattern(message = "Invalid user info", regexp = "^[A-Za-z][a-zA-Z .'-]{0,30}$")
    private String company;

    @Pattern(message = "Invalid user info", regexp = "^[A-Za-z][a-zA-Z .'-]{0,30}$")
    private String job;
}
