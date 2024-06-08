package com.github.blog.service.security.impl;

import com.github.blog.service.security.AuthenticatedUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @author Raman Haurylau
 */
@Service
public class AuthenticatedUserServiceImpl implements AuthenticatedUserService {
    public UserDetailsImpl getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetailsImpl) authentication.getPrincipal();
    }
}
