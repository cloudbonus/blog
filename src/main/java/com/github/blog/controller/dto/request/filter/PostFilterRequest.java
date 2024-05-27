package com.github.blog.controller.dto.request.filter;

import com.github.blog.service.statemachine.state.OrderState;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class PostFilterRequest {
    @Pattern(message = "Invalid username", regexp = "^[A-Za-z][A-Za-z0-9._-]{0,15}$")
    private String username;

    @Positive
    private Long tagId;

    @Pattern(message = "Invalid state", regexp = "^[A-Za-z][a-zA-Z_]{0,30}$")
    private String state;

    public PostFilterRequest() {
        this.state = OrderState.COMPLETED.name();
    }
}
