package com.github.blog.controller.dto.common;

import com.github.blog.repository.entity.CommentReaction;

/**
 * DTO for {@link CommentReaction}
 */
public record CommentReactionDto(Long id, Long commentId, Long userId, Long reactionId) {
}