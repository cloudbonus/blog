package com.github.blog.controller.dto.request;

import com.github.blog.controller.annotation.user.UniqueEmail;
import com.github.blog.controller.annotation.user.UniqueUsername;
import com.github.blog.controller.annotation.user.ValidPassword;
import com.github.blog.controller.util.marker.BaseMarker;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Raman Haurylau
 */
public final class UserRequest {
    @UniqueUsername(groups = BaseMarker.Third.class)
    private final @Pattern(message = "Invalid username", regexp = "^[A-Za-z][A-Za-z0-9._-]{3,20}$")
    @NotBlank(message = "Username is mandatory", groups = BaseMarker.First.class) String username;
    @ValidPassword
    private final @NotBlank(message = "Password is mandatory", groups = BaseMarker.First.class) String password;
    @UniqueEmail(groups = BaseMarker.Third.class)
    private final @Pattern(message = "Invalid  email", regexp = "^[A-Za-z][a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotBlank(message  = "Email cannot be empty", groups = BaseMarker.Second.class) String email;

    /**
     *
     */
    public UserRequest(
            List<TagRequest> tags,
            @Pattern(message = "Invalid username", regexp = "^[A-Za-z][A-Za-z0-9._-]{3,20}$") @NotBlank(message = "Username is mandatory", groups = BaseMarker.First.class) String username,
            @NotBlank(message = "Password is mandatory", groups = BaseMarker.First.class) String password,
            @Pattern(message = "Invalid email", regexp = "^[A-Za-z][a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$") @NotBlank(message = "Email cannot be empty", groups = BaseMarker.Second.class) String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
    public @Pattern(message = "Invalid username", regexp = "^[A-Za-z][A-Za-z0-9._-]{3,20}$") @NotBlank(message = "Username is mandatory", groups = BaseMarker.First.class) String username() {
        return username;
    }

    public @NotBlank(message = "Password is mandatory", groups = BaseMarker.First.class) String password() {
        return password;
    }

    public @Pattern(message = "Invalid email", regexp = "^[A-Za-z][a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$") @NotBlank(message = "Email cannot be empty", groups = BaseMarker.Second.class) String email() {
        return email;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (UserRequest) obj;
        return Objects.equals(this.username, that.username) &&
                Objects.equals(this.password, that.password) &&
                Objects.equals(this.email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, email);
    }

    @Override
    public String toString() {
        return "UserRequest[" +
                "username=" + username + ", " +
                "password=" + password + ", " +
                "email=" + email + ']';
    }


}
