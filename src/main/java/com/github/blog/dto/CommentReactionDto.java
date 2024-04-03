package com.github.blog.dto;

import lombok.Data;

/**
 * @author Raman Haurylau
 */
@Data
public class CommentReactionDto {
    private String reactionType;

    private CommentDto comment;
    private UserDto user;
}
