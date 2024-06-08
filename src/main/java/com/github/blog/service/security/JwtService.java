package com.github.blog.service.security;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Raman Haurylau
 */
public interface JwtService {
    String generateToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);

    String extractUserName(String token);
}
