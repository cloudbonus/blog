package com.github.blog.dto.common;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * DTO for {@link com.github.blog.model.Comment}
 */
@Getter
@Setter
public class CommentDto {
    Long id;
    Long postId;
    Long userId;
    String content;
    OffsetDateTime publishedAt;
}