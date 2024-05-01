package com.github.blog.dto.common;

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
    Long id;
    Long userId;
    String title;
    String content;
    OffsetDateTime publishedAt;
    List<Long> tagIds;
    List<Long> commentIds;
}