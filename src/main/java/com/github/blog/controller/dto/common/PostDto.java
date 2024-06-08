package com.github.blog.controller.dto.common;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * DTO for {@link com.github.blog.model.Post}
 */
public record PostDto(Long id, Long userId, String title, String content, OffsetDateTime createdAt, List<Long> tagIds,
                      List<Long> commentIds) {
}