package com.github.blog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author Raman Haurylau
 */
@Data
public class PostReaction {
    private String reactionType;

    @JsonIgnore
    private int id;

    private Post post;
    private User user;
}
