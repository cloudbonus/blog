package com.github.blog.service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author Raman Haurylau
 */
@Getter
@RequiredArgsConstructor
public enum PostReactionErrorResult {
    POST_REACTION_NOT_FOUND(HttpStatus.NOT_FOUND, "Post reaction not found"),
    POST_REACTIONS_NOT_FOUND(HttpStatus.NOT_FOUND, "No post reactions could be found");

    private final HttpStatus status;
    private final String message;
}
