package com.github.blog.dto;

import lombok.Data;

/**
 * @author Raman Haurylau
 */
@Data
public class OrderDto {
    private String message;
    private String status;

    private UserDto user;
    private PostDto post;
}
