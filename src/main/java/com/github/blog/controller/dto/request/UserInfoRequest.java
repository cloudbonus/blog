package com.github.blog.controller.dto.request;

import com.github.blog.controller.annotation.user.ValidUserInfo;
import com.github.blog.controller.util.marker.BaseMarker;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * @author Raman Haurylau
 */
@ValidUserInfo
public record UserInfoRequest(
        @Pattern(message = "Invalid firstname", regexp = "^[A-Za-z][a-zA-Z ,.'-]{0,30}$") @NotBlank(message = "Firstname cannot be empty", groups = BaseMarker.First.class) String firstname,
        @Pattern(message = "Invalid surname", regexp = "^[A-Za-z][a-zA-Z ,.'-]{0,30}$") @NotBlank(message = "Surname cannot be empty", groups = BaseMarker.First.class) String surname,
        @Pattern(message = "Invalid university name", regexp = "^[A-Za-z][a-zA-Z .'-]{4,30}$") String university,
        @Pattern(message = "Invalid major name", regexp = "^[A-Za-z][a-zA-Z .'-]{4,30}$") String major,
        @Pattern(message = "Invalid company name", regexp = "^[A-Za-z][a-zA-Z .'-]{4,30}$") String company,
        @Pattern(message = "Invalid job title", regexp = "^[A-Za-z][a-zA-Z .'-]{4,30}$") String job) {

}
