package com.github.blog.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */

@Getter
@Setter
public class CommentReactionRequest {
    private Long commentId;
    private Long userId;
    private Long reactionId;
}
