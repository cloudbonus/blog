package com.github.blog.service.security;

import com.github.blog.service.security.impl.UserDetailsImpl;

/**
 * @author Raman Haurylau
 */
public interface AuthenticatedUserService {
    UserDetailsImpl getAuthenticatedUser();
}
