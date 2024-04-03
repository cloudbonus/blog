package com.github.blog.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Raman Haurylau
 */
@Data
public class Order {
    private int orderId;
    private String message;
    private String status;
    private LocalDateTime orderedAt;

    private User user;
    private Post post;
}
