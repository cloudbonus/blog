package com.github.blog.service.exception.impl;

import com.github.blog.service.exception.OrderErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Raman Haurylau
 */
@Getter
@RequiredArgsConstructor
public class OrderException extends RuntimeException {
    private final OrderErrorResult errorResult;
}
