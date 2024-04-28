package com.github.blog.service.exception.impl;

import com.github.blog.service.exception.CommentErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Raman Haurylau
 */
@Getter
@RequiredArgsConstructor
public class CommentException extends RuntimeException {
    private final CommentErrorResult errorResult;
}
