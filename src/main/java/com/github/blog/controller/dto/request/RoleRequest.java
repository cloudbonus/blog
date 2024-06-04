package com.github.blog.controller.dto.request;

import com.github.blog.controller.annotation.etc.UniqueRole;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
@UniqueRole
public class RoleRequest {

    @Pattern(message = "Invalid name", regexp = "^[A-Za-z]{2,15}")
    private String name;
}
