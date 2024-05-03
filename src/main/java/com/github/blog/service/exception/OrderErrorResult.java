package com.github.blog.service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author Raman Haurylau
 */
@Getter
@RequiredArgsConstructor
public enum OrderErrorResult {
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "Order not found"),
    ORDERS_NOT_FOUND(HttpStatus.NOT_FOUND, "No orders could be found");

    private final HttpStatus status;
    private final String message;
}
