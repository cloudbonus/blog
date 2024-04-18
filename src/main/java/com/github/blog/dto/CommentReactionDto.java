package com.github.blog.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.github.blog.model.CommentReaction}
 */
@Value
public class CommentReactionDto implements Serializable {
    CommentDto comment;
    UserDto user;
    String reactionType;
}