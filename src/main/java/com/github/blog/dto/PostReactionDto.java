package com.github.blog.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.github.blog.model.PostReaction}
 */
@Value
public class PostReactionDto implements Serializable {
    PostDto post;
    UserDto user;
    String reactionType;
}