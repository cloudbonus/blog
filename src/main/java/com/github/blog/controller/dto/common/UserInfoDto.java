package com.github.blog.controller.dto.common;

import com.github.blog.model.UserInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link UserInfo}
 */
@Getter
@Setter
public class UserInfoDto {
    private Long id;
    private String firstname;
    private String surname;
    private String universityName;
    private String majorName;
    private String companyName;
    private String jobTitle;
}