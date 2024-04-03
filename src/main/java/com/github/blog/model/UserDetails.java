package com.github.blog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author Raman Haurylau
 */
@Data
public class UserDetails {
    private String firstname;
    private String surname;
    private String universityName;
    private String majorName;
    private String companyName;
    private String jobTitle;
    @JsonIgnore
    private int userId;

    private User user;
}
