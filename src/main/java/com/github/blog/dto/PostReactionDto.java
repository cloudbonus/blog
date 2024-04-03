package com.github.blog.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Raman Haurylau
 */
@Data
public class PostReactionDto implements Serializable {
    private String reactionType;

    private PostDto post;
    private UserDto user;
}
