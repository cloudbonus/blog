package com.github.blog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author Raman Haurylau
 */
@Data
public class Tag {
    @JsonIgnore
    private int id;
    private String name;
}
