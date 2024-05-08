package com.github.blog.service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author Raman Haurylau
 */
@Getter
@RequiredArgsConstructor
public enum UserErrorResult {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    USERS_NOT_FOUND(HttpStatus.NOT_FOUND, "No users could be found"),
    UNAUTHORIZED_DELETION_ATTEMPT(HttpStatus.FORBIDDEN, "User can only DELETE themself"),
    UNAUTHORIZED_UPDATE_ATTEMPT(HttpStatus.FORBIDDEN, "User can only UPDATE themself"),
    UNAUTHORIZED_READ_ATTEMPT(HttpStatus.FORBIDDEN, "User can only READ themself"),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "Authentication failed"),
    BAD_CREDENTIALS(HttpStatus.UNAUTHORIZED, "You have entered either the Username and/or Password incorrectly");

    private final HttpStatus status;
    private final String message;
}
