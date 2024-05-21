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
    private Long id;
    private Long postId;
    private Long userId;
    private OffsetDateTime orderedAt;
    private String state;
}