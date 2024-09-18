package com.github.blog.controller.advice;

import java.util.List;

/**
 * @author Raman Haurylau
 */
public record ValidationErrorResponse(String status, List<Violation> violations, Long timestamp) {
}
