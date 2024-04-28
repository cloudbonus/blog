package com.github.blog.service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author Raman Haurylau
 */
@Getter
@RequiredArgsConstructor
public enum CommentReactionErrorResult {
    COMMENT_REACTION_NOT_FOUND(HttpStatus.NOT_FOUND, "Comment reaction not found"),
    COMMENT_REACTIONS_NOT_FOUND(HttpStatus.NOT_FOUND, "No comment reactions could be found");

    private final HttpStatus status;
    private final String message;
}
