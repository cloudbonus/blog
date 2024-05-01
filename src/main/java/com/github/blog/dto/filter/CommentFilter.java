package com.github.blog.dto.filter;

import com.github.blog.dto.GenericFilter;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class CommentFilter extends GenericFilter {
    private String login;
}
