package com.github.blog.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Raman Haurylau
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentDto implements Serializable {
    private String content;

    private PostDto post;
    private UserDto user;
}
