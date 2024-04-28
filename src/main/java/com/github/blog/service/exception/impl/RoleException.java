package com.github.blog.service.exception.impl;

import com.github.blog.service.exception.RoleErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Raman Haurylau
 */
@Getter
@RequiredArgsConstructor
public class RoleException extends RuntimeException {
    private final RoleErrorResult errorResult;
}
