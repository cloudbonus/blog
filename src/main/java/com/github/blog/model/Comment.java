package com.github.blog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Raman Haurylau
 */
@Data
public class Comment {
    @JsonIgnore
    private int id;
    private String content;
    @JsonIgnore
    private LocalDateTime publishedAt;

    private Post post;
    private User user;
}
