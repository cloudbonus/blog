package com.github.blog.controller.dto.request.filter;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class CommentReactionDtoFilter {
    private Long commentId;
    private Long reactionId;
    private String username;
}
