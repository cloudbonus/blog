package com.github.blog.dto;

import lombok.Value;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * DTO for {@link com.github.blog.model.Order}
 */
@Value
public class OrderDto implements Serializable {
    PostDto post;
    UserDto user;
    OffsetDateTime orderedAt;
    String message;
    String status;
}