package com.github.blog.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class PostRequest {
    private Long userId;
    private String title;
    private String content;
    private List<Long> tagIds;
}
