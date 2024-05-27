package com.github.blog.controller.dto.request.filter;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class TagFilterRequest {
    @Positive
    private Long postId;
}
