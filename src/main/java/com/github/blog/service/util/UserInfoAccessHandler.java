package com.github.blog.service.util;

import com.github.blog.service.security.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Raman Haurylau
 */
@Component("userInfoAccess")
@RequiredArgsConstructor
public class UserInfoAccessHandler {
    private final AuthenticatedUserService authenticatedUserService;

    public boolean hasRole(String roleName) {
        return authenticatedUserService.getAuthenticatedUser().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(roleName));
    }
}
