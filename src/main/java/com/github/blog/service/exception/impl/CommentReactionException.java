package com.github.blog.service.exception.impl;

import com.github.blog.service.exception.CommentReactionErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Raman Haurylau
 */
@Getter
@RequiredArgsConstructor
public class CommentReactionException extends RuntimeException {
    private final CommentReactionErrorResult errorResult;
}
