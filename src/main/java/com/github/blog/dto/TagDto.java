package com.github.blog.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Raman Haurylau
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TagDto implements Serializable {
    private String tagName;
}
