package com.github.blog.repository.filter;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class UserFilter {
    private String username;
    private Long roleId;
    private String firstname;
    private String surname;
    private String university;
    private String major;
    private String company;
    private String job;
}
