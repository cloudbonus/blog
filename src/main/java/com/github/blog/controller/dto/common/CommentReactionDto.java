package com.github.blog.controller.dto.common;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link com.github.blog.model.CommentReaction}
 */
@Getter
@Setter
public class CommentReactionDto {
    private Long id;
    private Long commentId;
    private Long userId;
    private Long reactionId;
}