package com.github.blog.service.exception.impl;

import com.github.blog.service.exception.PostErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Raman Haurylau
 */
@Getter
@RequiredArgsConstructor
public class PostException extends RuntimeException {
    private final PostErrorResult errorResult;
}
