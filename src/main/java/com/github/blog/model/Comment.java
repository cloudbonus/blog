package com.github.blog.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Raman Haurylau
 */
@Data
public class Comment {
    private int commentId;
    private String content;
    private LocalDateTime publishedAt;

    private Post post;
    private User user;
}
