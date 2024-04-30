package com.github.blog.dto.request;

import lombok.Data;

/**
 * @author Raman Haurylau
 */
@Data
public class PostRequestFilter {
    private String login;
    private String tag;
}
