package com.github.blog.repository.dto.filter;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class CommentReactionFilter {
    private Long commentId;
    private Long reactionId;
    private String username;
}
