package com.github.blog.service.util;

import com.github.blog.service.security.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Raman Haurylau
 */
@Component("orderAccess")
@RequiredArgsConstructor
public class OrderAccessHandler {
    private final AuthenticatedUserService authenticatedUserService;

    public boolean canFilter(Long id) {
        return id != null && id.equals(authenticatedUserService.getAuthenticatedUser().getId());
    }
}
