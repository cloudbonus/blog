package com.github.blog.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.github.blog.model.UserDetail}
 */
@Value
public class UserDetailDto implements Serializable {
    UserDto user;
    String firstname;
    String surname;
    String universityName;
    String majorName;
    String companyName;
    String jobTitle;
}