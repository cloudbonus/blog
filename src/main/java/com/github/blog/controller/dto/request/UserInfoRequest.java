package com.github.blog.controller.dto.request;

import com.github.blog.controller.annotation.user.ValidUserInfo;
import com.github.blog.controller.util.marker.BaseMarker;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
@ValidUserInfo
public class UserInfoRequest {

    @Pattern(message = "Invalid firstname", regexp = "^[A-Za-z][a-zA-Z ,.'-]{0,30}$")
    @NotBlank(message = "Firstname cannot be empty", groups = BaseMarker.First.class)
    private String firstname;

    @Pattern(message = "Invalid surname", regexp = "^[A-Za-z][a-zA-Z ,.'-]{0,30}$")
    @NotBlank(message = "Surname cannot be empty", groups = BaseMarker.First.class)
    private String surname;

    @Pattern(message = "Invalid university name", regexp = "^[A-Za-z][a-zA-Z .'-]{4,30}$")
    private String university;

    @Pattern(message = "Invalid major name", regexp = "^[A-Za-z][a-zA-Z .'-]{4,30}$")
    private String major;

    @Pattern(message = "Invalid company name", regexp = "^[A-Za-z][a-zA-Z .'-]{4,30}$")
    private String company;

    @Pattern(message = "Invalid job title", regexp = "^[A-Za-z][a-zA-Z .'-]{4,30}$")
    private String job;
}
