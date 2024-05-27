package com.github.blog.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class OrderRequest {
    @NotNull(message = "Post ID is mandatory")
    private Long postId;

    @NotNull(message = "User ID is mandatory")
    private Long userId;
}
