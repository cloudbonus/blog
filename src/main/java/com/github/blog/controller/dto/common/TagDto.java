package com.github.blog.controller.dto.common;

import com.github.blog.repository.entity.Tag;

/**
 * DTO for {@link Tag}
 */
public record TagDto(Long id, String name) {
}