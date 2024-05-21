package com.github.blog.controller.dto.request.filter;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class OrderDtoFilter {
    private String username;
    private Long postId;
    private Long userId;
}
