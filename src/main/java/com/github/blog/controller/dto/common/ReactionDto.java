package com.github.blog.controller.dto.common;

import com.github.blog.repository.entity.Reaction;

/**
 * DTO for {@link Reaction}
 */
public record ReactionDto(Long id, String name) {
}
