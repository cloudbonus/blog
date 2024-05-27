package com.github.blog.controller.dto.request;

import com.github.blog.controller.annotation.user.ValidUserInfo;
import com.github.blog.controller.util.marker.Marker;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
@ValidUserInfo
public class UserInfoRequest {
    @Positive(message = "User ID should be positive", groups = Marker.First.class)
    @NotNull(message = "User ID is mandatory", groups = Marker.First.class)
    @Null(message = "User ID should be null", groups = Marker.Second.class)
    private Long id;

    @Pattern(message = "Invalid firstname", regexp = "^[A-Za-z][a-zA-Z ,.'-]{0,30}$")
    @NotBlank(message = "Firstname cannot be empty", groups = Marker.First.class)
    private String firstname;

    @Pattern(message = "Invalid surname", regexp = "^[A-Za-z][a-zA-Z ,.'-]{0,30}$")
    @NotBlank(message = "Surname cannot be empty", groups = Marker.First.class)
    private String surname;

    private String universityName;
    private String majorName;
    private String companyName;
    private String jobTitle;
}
