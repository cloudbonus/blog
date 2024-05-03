package com.github.blog.service.exception.impl;

import com.github.blog.service.exception.TagErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Raman Haurylau
 */
@Getter
@RequiredArgsConstructor
public class TagException extends RuntimeException {
    private final TagErrorResult errorResult;
}
