package com.github.blog.dto;

import lombok.Data;

import java.util.Set;

/**
 * @author Raman Haurylau
 */
@Data
public class PostDto {
    private String title;
    private String content;

    private UserDto user;
    private Set<TagDto> tags;
}
