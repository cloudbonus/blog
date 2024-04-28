package com.github.blog.service.exception.impl;

import com.github.blog.service.exception.PostReactionErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Raman Haurylau
 */
@Getter
@RequiredArgsConstructor
public class PostReactionException extends RuntimeException {
    private final PostReactionErrorResult errorResult;
}
