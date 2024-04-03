package com.github.blog.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author Raman Haurylau
 */
@Data
public class Post {
    private int postId;
    private String title;
    private String content;
    private LocalDateTime publishedAt;

    private User user;
    private Set<Tag> tags;
}
