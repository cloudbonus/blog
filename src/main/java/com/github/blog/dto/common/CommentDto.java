package com.github.blog.dto.common;

import lombok.Data;

import java.time.OffsetDateTime;

/**
 * DTO for {@link com.github.blog.model.Comment}
 */
@Data
public class CommentDto {
    Long id;
    Long postId;
    Long userId;
    String content;
    OffsetDateTime publishedAt;
}