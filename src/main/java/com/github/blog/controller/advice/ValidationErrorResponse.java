package com.github.blog.controller.advice;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Getter
@RequiredArgsConstructor
public class ValidationErrorResponse {
    private final String status;
    private final List<Violation> violations;
    private final Long timestamp;
}
