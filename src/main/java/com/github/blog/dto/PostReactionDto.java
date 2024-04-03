package com.github.blog.dto;

import lombok.Data;

/**
 * @author Raman Haurylau
 */
@Data
public class PostReactionDto {
    private String reactionType;

    private PostDto post;
    private UserDto user;
}
