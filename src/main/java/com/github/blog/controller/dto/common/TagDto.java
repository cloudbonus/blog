package com.github.blog.controller.dto.common;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link com.github.blog.model.Tag}
 */
@Getter
@Setter
public class TagDto {
    private Long id;
    private String name;
}