package com.github.blog.service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author Raman Haurylau
 */
@Getter
@RequiredArgsConstructor
public enum RoleErrorResult {
    ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, "Role not found"),
    ROLES_NOT_FOUND(HttpStatus.NOT_FOUND, "No roles could be found");

    private final HttpStatus status;
    private final String message;
}
