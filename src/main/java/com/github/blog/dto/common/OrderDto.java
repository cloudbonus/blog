package com.github.blog.dto.common;

import lombok.Value;

import java.time.OffsetDateTime;

/**
 * DTO for {@link com.github.blog.model.Order}
 */
@Value
public class OrderDto {
    Long id;
    PostDto post;
    UserDto user;
    OffsetDateTime orderedAt;
    String message;
    String status;
}