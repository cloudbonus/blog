package com.github.blog.service.exception.impl;

import com.github.blog.service.exception.UserDetailErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Raman Haurylau
 */
@Getter
@RequiredArgsConstructor
public class UserDetailException extends RuntimeException {
    private final UserDetailErrorResult errorResult;
}
