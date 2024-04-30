package com.github.blog.dto.filter;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Data
public class PostDtoFilter {
    private String login;
    private String tag;
}
