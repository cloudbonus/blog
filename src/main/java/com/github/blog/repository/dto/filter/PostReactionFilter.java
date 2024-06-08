package com.github.blog.repository.dto.filter;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class PostReactionFilter {
    private Long postId;
    private Long reactionId;
    private String username;
}
