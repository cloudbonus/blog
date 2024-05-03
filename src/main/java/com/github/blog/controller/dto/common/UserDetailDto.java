package com.github.blog.controller.dto.common;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link com.github.blog.model.UserDetail}
 */
@Getter
@Setter
public class UserDetailDto {
    private Long id;
    private String firstname;
    private String surname;
    private String universityName;
    private String majorName;
    private String companyName;
    private String jobTitle;
}