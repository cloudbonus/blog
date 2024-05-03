package com.github.blog.service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author Raman Haurylau
 */
@Getter
@RequiredArgsConstructor
public enum CommentErrorResult {
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Comment not found"),
    COMMENTS_NOT_FOUND(HttpStatus.NOT_FOUND, "No comments could be found");

    private final HttpStatus status;
    private final String message;
}
