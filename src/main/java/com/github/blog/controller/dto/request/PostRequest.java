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
    Long userId;
    String title;
    String content;
    List<Long> tagIds;
}
