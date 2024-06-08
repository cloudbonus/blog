package com.github.blog.controller.advice;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Raman Haurylau
 */
@Getter
@RequiredArgsConstructor
public class Violation {
    private final String name;
    private final String message;
}
