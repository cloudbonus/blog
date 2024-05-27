package com.github.blog.service.exception.impl;

import com.github.blog.service.exception.ExceptionEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Raman Haurylau
 */
@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {
    private final ExceptionEnum exceptionEnum;
}
