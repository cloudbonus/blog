package com.github.blog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author Raman Haurylau
 */
@Data
public class CommentReaction {
    private String reactionType;

    @JsonIgnore
    private int commentId;

    private User user;
    private Comment comment;
}
