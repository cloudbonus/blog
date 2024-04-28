package com.github.blog.service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author Raman Haurylau
 */
@Getter
@RequiredArgsConstructor
public enum UserDetailErrorResult {
    USER_DETAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "User detail not found"),
    USER_DETAILS_NOT_FOUND(HttpStatus.NOT_FOUND, "No user details could be found");

    private final HttpStatus status;
    private final String message;
}
