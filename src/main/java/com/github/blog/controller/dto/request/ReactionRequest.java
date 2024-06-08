package com.github.blog.controller.dto.request;

import com.github.blog.controller.annotation.etc.UniqueReaction;
import jakarta.validation.constraints.Pattern;

/**
 * @author Raman Haurylau
 */
@UniqueReaction
public record ReactionRequest(@Pattern(message = "Invalid name", regexp = "^[A-Za-z]{2,15}$") String name) {

}
