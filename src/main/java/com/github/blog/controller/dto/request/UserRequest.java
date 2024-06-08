package com.github.blog.controller.dto.request;

import com.github.blog.controller.annotation.user.UniqueEmail;
import com.github.blog.controller.annotation.user.UniqueUsername;
import com.github.blog.controller.annotation.user.ValidPassword;
import com.github.blog.controller.util.marker.BaseMarker;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * @author Raman Haurylau
 */
public record UserRequest(
        @UniqueUsername(groups = BaseMarker.Third.class) @Pattern(message = "Invalid username", regexp = "^[A-Za-z][A-Za-z0-9._-]{3,20}$") @NotBlank(message = "Username is mandatory", groups = BaseMarker.First.class) String username,
        @ValidPassword @NotBlank(message = "Password is mandatory", groups = BaseMarker.First.class) String password,
        @UniqueEmail(groups = BaseMarker.Third.class) @Pattern(message = "Invalid email", regexp = "^[A-Za-z][a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$") @NotBlank(message = "Email cannot be empty", groups = BaseMarker.Second.class) String email) {

}
