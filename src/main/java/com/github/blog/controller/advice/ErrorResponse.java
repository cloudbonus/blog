package com.github.blog.controller.advice;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Raman Haurylau
 */
@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private final String status;
    private final String message;
    private final Long timestamp;
}
