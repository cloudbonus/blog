package com.github.blog.dto;

import lombok.Data;

/**
 * @author Raman Haurylau
 */
@Data
public class UserDetailsDto {
    private String firstname;
    private String surname;
    private String universityName;
    private String majorName;
    private String companyName;
    private String jobTitle;

    private UserDto user;
}
