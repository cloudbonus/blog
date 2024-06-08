package com.github.blog.controller.dto.common;

import java.time.OffsetDateTime;

/**
 * DTO for {@link com.github.blog.model.Order}
 */
public record OrderDto(Long id, Long postId, Long userId, OffsetDateTime createdAt, String state) {
}