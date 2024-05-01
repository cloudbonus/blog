package com.github.blog.dto.request;

import com.github.blog.dto.GenericFilter;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class PostDtoFilter extends GenericFilter {
    private String login;
    private String tag;
}
