package com.github.blog.controller.dto.common;


import com.github.blog.repository.entity.Comment;

import java.time.OffsetDateTime;

/**
 * DTO for {@link Comment}
 */
public record CommentDto(Long id, Long postId, Long userId, String content, OffsetDateTime createdAt) {
}