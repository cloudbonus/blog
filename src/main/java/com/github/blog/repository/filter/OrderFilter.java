package com.github.blog.repository.filter;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class OrderFilter {
    private String username;
    private Long postId;
    private Long userId;
}
