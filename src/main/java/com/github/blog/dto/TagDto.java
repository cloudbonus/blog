package com.github.blog.dto;

import lombok.Value;

/**
 * DTO for {@link com.github.blog.model.Tag}
 */
@Value
public class TagDto {
    Long id;
    String tagName;
}