package com.github.blog.controller.dto.request.filter;

import jakarta.validation.constraints.Positive;

/**
 * @author Raman Haurylau
 */
public record UserInfoFilterRequest(@Positive Long userId) {

}
