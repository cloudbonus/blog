package com.github.blog.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */

@Getter
@Setter
public class CommentReactionRequest {
    Long commentId;
    Long userId;
    String reactionType;
}
