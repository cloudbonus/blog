package com.github.blog.repository.dto.filter;

import com.github.blog.controller.dto.GenericFilter;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class PostFilter extends GenericFilter {
    private String login;
    private String tag;
}
