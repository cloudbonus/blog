package com.github.blog.service.exception.impl;

import com.github.blog.service.exception.CustomEntityException;
import com.github.blog.service.exception.ExceptionEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Raman Haurylau
 */
@Getter
@RequiredArgsConstructor
public class PostException extends RuntimeException implements CustomEntityException {
    private final ExceptionEnum exceptionEnum;
}
