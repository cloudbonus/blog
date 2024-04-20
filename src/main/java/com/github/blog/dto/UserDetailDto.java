package com.github.blog.dto;

import lombok.Data;

/**
 * DTO for {@link com.github.blog.model.UserDetail}
 */
@Data
public class UserDetailDto {
    private Long id;
    private UserDto user;
    private String firstname;
    private String surname;
    private String universityName;
    private String majorName;
    private String companyName;
    private String jobTitle;
}