package com.github.blog.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class RegistrationRequest {
    private String username;
    private String password;
    private String email;
}
