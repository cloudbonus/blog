package com.github.blog.controller.dto.request;

import com.github.blog.controller.dto.GenericFilter;
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
