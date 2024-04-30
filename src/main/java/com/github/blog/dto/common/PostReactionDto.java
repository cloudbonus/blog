package com.github.blog.dto.common;

import lombok.Value;

/**
 * DTO for {@link com.github.blog.model.PostReaction}
 */
@Value
public class PostReactionDto {
    Long id;
    PostDto post;
    UserDto user;
    String reactionType;
}