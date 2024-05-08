package com.github.blog.controller.dto.common;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * DTO for {@link com.github.blog.model.Order}
 */
@Getter
@Setter
public class OrderDto {
    Long id;
    Long postId;
    Long userId;
    OffsetDateTime orderedAt;
    String message;
    String status;
}