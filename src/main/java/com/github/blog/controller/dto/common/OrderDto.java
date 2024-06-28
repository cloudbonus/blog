package com.github.blog.controller.dto.common;

import com.github.blog.repository.entity.Order;

import java.time.OffsetDateTime;

/**
 * DTO for {@link Order}
 */
public record OrderDto(Long id, Long postId, Long userId, OffsetDateTime createdAt, String state) {
}