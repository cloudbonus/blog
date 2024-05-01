package com.github.blog.dto.request;

import com.github.blog.dto.GenericFilter;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class UserDtoFilter extends GenericFilter {
    private String login;
    private String role;
    private String firstname;
    private String surname;
    private String university;
    private String major;
    private String company;
    private String job;
}
