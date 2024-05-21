package com.github.blog.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class PostReactionRequest {
    private Long postId;
    private Long userId;
    private Long reactionId;
}
