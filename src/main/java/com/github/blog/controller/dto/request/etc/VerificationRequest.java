package com.github.blog.controller.dto.request.etc;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class VerificationRequest {
    @Positive(message = "Role ID should be positive")
    private Long roleId;
}
