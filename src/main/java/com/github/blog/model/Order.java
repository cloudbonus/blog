package com.github.blog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Raman Haurylau
 */
@Data
public class Order {
    @JsonIgnore
    private int id;
    private String message;
    private String status;
    @JsonIgnore
    private LocalDateTime orderedAt;

    private User user;
    private Post post;
}
