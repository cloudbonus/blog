package com.github.blog.controller.dto.common;

import com.github.blog.repository.entity.Post;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * DTO for {@link Post}
 */
public record PostDto(Long id, Long userId, String title, String content, OffsetDateTime createdAt, List<Long> tagIds,
                      List<Long> commentIds) {
}