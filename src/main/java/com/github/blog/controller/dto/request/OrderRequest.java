package com.github.blog.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class OrderRequest {
    Long id;
    Long postId;
    Long userId;
}
