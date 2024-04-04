package com.github.blog.dto;

import lombok.Data;

/**
 * @author Raman Haurylau
 */
@Data
public class CommentDto {
    private String content;

    private PostDto post;
    private UserDto user;
}
