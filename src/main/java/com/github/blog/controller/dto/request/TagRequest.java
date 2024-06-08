package com.github.blog.controller.dto.request;

import com.github.blog.controller.annotation.etc.UniqueTag;
import jakarta.validation.constraints.Pattern;

/**
 * @author Raman Haurylau
 */
@UniqueTag
public record TagRequest(@Pattern(message = "Invalid name", regexp = "^[A-Za-z]{2,15}$") String name) {

}
