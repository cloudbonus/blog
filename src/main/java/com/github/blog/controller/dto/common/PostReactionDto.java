package com.github.blog.controller.dto.common;

/**
 * DTO for {@link com.github.blog.model.PostReaction}
 */
public record PostReactionDto(Long id, Long postId, Long userId, Long reactionId) {
}