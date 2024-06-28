package com.github.blog.controller.dto.common;

import com.github.blog.repository.entity.PostReaction;

/**
 * DTO for {@link PostReaction}
 */
public record PostReactionDto(Long id, Long postId, Long userId, Long reactionId) {
}