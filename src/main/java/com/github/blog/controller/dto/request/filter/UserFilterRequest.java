package com.github.blog.controller.dto.request.filter;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

/**
 * @author Raman Haurylau
 */
public record UserFilterRequest(
        @Pattern(message = "Invalid username", regexp = "^[A-Za-z][A-Za-z0-9._-]{0,15}$") String username,
        @Positive Long roleId,
        @Pattern(message = "Invalid firstname", regexp = "^[A-Za-z][a-zA-Z ,.'-]{0,30}$") String firstname,
        @Pattern(message = "Invalid surname", regexp = "^[A-Za-z][a-zA-Z ,.'-]{0,30}$") String surname,
        @Pattern(message = "Invalid user info", regexp = "^[A-Za-z][a-zA-Z .'-]{0,30}$") String university,
        @Pattern(message = "Invalid user info", regexp = "^[A-Za-z][a-zA-Z .'-]{0,30}$") String major,
        @Pattern(message = "Invalid user info", regexp = "^[A-Za-z][a-zA-Z .'-]{0,30}$") String company,
        @Pattern(message = "Invalid user info", regexp = "^[A-Za-z][a-zA-Z .'-]{0,30}$") String job) {

}
