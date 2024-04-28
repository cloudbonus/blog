package com.github.blog.dto;

import lombok.Data;

import java.time.OffsetDateTime;

/**
 * DTO for {@link com.github.blog.model.Post}
 */
@Data
public class PostDto {
    Long id;
    Long userId;
    String title;
    String content;
    OffsetDateTime publishedAt;
}