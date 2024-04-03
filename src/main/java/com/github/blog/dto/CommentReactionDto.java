package com.github.blog.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Raman Haurylau
 */
@Data
public class CommentReactionDto implements Serializable {
    private String reactionType;

    private CommentDto comment;
    private UserDto user;
}
