package com.github.blog.controller.dto.common;


import java.time.OffsetDateTime;

/**
 * DTO for {@link com.github.blog.model.Comment}
 */
public record CommentDto(Long id, Long postId, Long userId, String content, OffsetDateTime createdAt) {
}