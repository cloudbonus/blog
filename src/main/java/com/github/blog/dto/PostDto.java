package com.github.blog.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * @author Raman Haurylau
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostDto implements Serializable {
    private String title;
    private String content;

    private Set<TagDto> tags;
}
