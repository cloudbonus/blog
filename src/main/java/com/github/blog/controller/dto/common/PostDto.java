package com.github.blog.controller.dto.common;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * DTO for {@link com.github.blog.model.Post}
 */
@Getter
@Setter
public class PostDto {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private OffsetDateTime publishedAt;
    private List<Long> tagIds;
    private List<Long> commentIds;
}