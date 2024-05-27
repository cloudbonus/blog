package com.github.blog.controller.dto.request.filter;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class PostReactionFilterRequest {
    @Positive
    private Long postId;

    @Positive
    private Long reactionId;

    @Pattern(message = "Invalid username", regexp = "^[A-Za-z][A-Za-z0-9._-]{0,15}$")
    private String username;
}
