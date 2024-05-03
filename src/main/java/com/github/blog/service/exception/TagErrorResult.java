package com.github.blog.service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author Raman Haurylau
 */
@Getter
@RequiredArgsConstructor
public enum TagErrorResult {
    TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "Tag not found"),
    TAGS_NOT_FOUND(HttpStatus.NOT_FOUND, "No tags could be found");

    private final HttpStatus status;
    private final String message;
}
