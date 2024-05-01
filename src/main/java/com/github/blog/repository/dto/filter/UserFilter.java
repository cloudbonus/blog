package com.github.blog.repository.dto.filter;

import com.github.blog.controller.dto.GenericFilter;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class UserFilter extends GenericFilter {
    private String login;
    private String role;
    private String firstname;
    private String surname;
    private String university;
    private String major;
    private String company;
    private String job;
}
