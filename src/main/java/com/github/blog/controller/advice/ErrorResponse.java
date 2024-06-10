package com.github.blog.controller.advice;

/**
 * @author Raman Haurylau
 */
public record ErrorResponse(String status, String message, Long timestamp) {
}
