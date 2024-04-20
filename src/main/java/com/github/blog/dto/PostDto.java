package com.github.blog.dto;

import lombok.Value;

import java.time.OffsetDateTime;

/**
 * DTO for {@link com.github.blog.model.Post}
 */
@Value
public class PostDto {
    Long id;
    UserDto user;
    String title;
    String content;
    OffsetDateTime publishedAt;
}