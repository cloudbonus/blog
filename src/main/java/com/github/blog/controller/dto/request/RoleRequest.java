package com.github.blog.controller.dto.request;

import com.github.blog.controller.annotation.etc.UniqueRole;
import jakarta.validation.constraints.Pattern;

/**
 * @author Raman Haurylau
 */
@UniqueRole
public record RoleRequest(@Pattern(message = "Invalid name", regexp = "^[A-Za-z]{2,15}") String name) {

}
