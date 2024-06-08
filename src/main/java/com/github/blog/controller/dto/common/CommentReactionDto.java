package com.github.blog.controller.dto.common;

/**
 * DTO for {@link com.github.blog.model.CommentReaction}
 */
public record CommentReactionDto(Long id, Long commentId, Long userId, Long reactionId) {
}