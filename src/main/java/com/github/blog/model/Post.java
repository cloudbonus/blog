package com.github.blog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author Raman Haurylau
 */
@Data
public class Post {
    @JsonIgnore
    private int id;
    private String title;
    private String content;
    @JsonIgnore
    private LocalDateTime publishedAt;

    private User user;
    private Set<Tag> tags;
}
