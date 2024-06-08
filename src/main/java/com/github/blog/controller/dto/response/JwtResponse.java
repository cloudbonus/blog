package com.github.blog.controller.dto.response;

/**
 * @author Raman Haurylau
 */
public record JwtResponse(String token, String username, String type) {
    public JwtResponse(String token, String username) {
        this(token, username, "Bearer");
    }
}
