package com.github.blog.dto;

import lombok.Value;

import java.time.OffsetDateTime;

/**
 * DTO for {@link com.github.blog.model.Comment}
 */
@Value
public class CommentDto {
    Long id;
    PostDto post;
    UserDto user;
    String content;
    OffsetDateTime publishedAt;
}