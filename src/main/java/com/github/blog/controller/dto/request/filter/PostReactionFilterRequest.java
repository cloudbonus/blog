package com.github.blog.controller.dto.request.filter;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

/**
 * @author Raman Haurylau
 */
public record PostReactionFilterRequest(@Positive Long postId, @Positive Long reactionId,
                                        @Pattern(message = "Invalid username", regexp = "^[A-Za-z][A-Za-z0-9._-]{0,15}$") String username) {

}
