package com.github.blog.controller.dto.request;

import jakarta.validation.constraints.NotNull;

/**
 * @author Raman Haurylau
 */
public record OrderRequest(@NotNull(message = "Post ID is mandatory") Long postId,
                           @NotNull(message = "User ID is mandatory") Long userId) {

}
