package com.github.blog.controller.dto.common;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link com.github.blog.model.CommentReaction}
 */
@Getter
@Setter
public class CommentReactionDto {
    Long id;
    Long commentId;
    Long userId;
    String reactionType;
}