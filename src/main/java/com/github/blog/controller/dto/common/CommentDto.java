package com.github.blog.controller.dto.common;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * DTO for {@link com.github.blog.model.Comment}
 */
@Getter
@Setter
public class CommentDto {
    private Long id;
    private Long postId;
    private Long userId;
    private String content;
    private OffsetDateTime publishedAt;
}