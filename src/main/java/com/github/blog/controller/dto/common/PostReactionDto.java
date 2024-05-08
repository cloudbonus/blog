package com.github.blog.controller.dto.common;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link com.github.blog.model.PostReaction}
 */
@Getter
@Setter
public class PostReactionDto {
    Long id;
    Long postId;
    Long userId;
    String reactionType;
}