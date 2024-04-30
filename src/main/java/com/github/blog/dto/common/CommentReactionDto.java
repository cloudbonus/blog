package com.github.blog.dto.common;

import lombok.Value;

/**
 * DTO for {@link com.github.blog.model.CommentReaction}
 */
@Value
public class CommentReactionDto {
    Long id;
    CommentDto comment;
    UserDto user;
    String reactionType;
}