package com.github.blog.controller.dto.request;

import com.github.blog.controller.dto.GenericFilter;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class CommentDtoFilter extends GenericFilter {
    private String login;
}
