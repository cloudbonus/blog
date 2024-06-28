package com.github.blog.controller.dto.request.filter;

import com.github.blog.repository.entity.util.OrderState;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

/**
 * @author Raman Haurylau
 */
public record PostFilterRequest(
        @Pattern(message = "Invalid username", regexp = "^[A-Za-z][A-Za-z0-9._-]{0,15}$") String username,
        @Positive Long tagId,
        @Pattern(message = "Invalid state", regexp = "^[A-Za-z][a-zA-Z_]{0,30}$") String state) {

    @Override
    public String state() {
        return state == null ? OrderState.COMMITED.name() : state;
    }
}
