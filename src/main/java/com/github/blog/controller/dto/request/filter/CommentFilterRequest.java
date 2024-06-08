package com.github.blog.controller.dto.request.filter;

import jakarta.validation.constraints.Pattern;

/**
 * @author Raman Haurylau
 */
public record CommentFilterRequest(
        @Pattern(message = "Invalid username", regexp = "^[A-Za-z][A-Za-z0-9._-]{0,15}$") String username) {

}
