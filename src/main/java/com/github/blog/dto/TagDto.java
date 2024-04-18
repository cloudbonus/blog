package com.github.blog.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.github.blog.model.Tag}
 */
@Value
public class TagDto implements Serializable {
    String tagName;
}